/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laundry.code;

import static com.laundry.code.PasswordFieldToMD5.digest;
import static com.laundry.code.PasswordFieldToMD5.hexStringToByteArray;
import com.laundry.ui.Login;
import static com.laundry.ui.Login.jPasswordField1;
import static com.laundry.ui.Login.jTextField1;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 *
 * @author Jabrikos
 */
public class Laundry {
    
    private static boolean passwordAdminPassed = false;

    
    ConnectionDatabase connectionDatabase = new ConnectionDatabase();
    Connection connection;
    static ResultSet resultSet;
    Statement statement;
    static PreparedStatement preparedStatement;
    String passwordOneDB, passwordTwoDB, passwordUser;
    
    public static void main (String [] args) {
        new Login().setVisible(true);
    }
    
    public static void verifikasiPassword() throws NoSuchAlgorithmException, NoSuchProviderException {
        Laundry laundry = new Laundry();
        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        final JPasswordField passwordAdmin = new JPasswordField(15);
        final JCheckBox checkBoxShowPassword = new JCheckBox();
        checkBoxShowPassword.setToolTipText("Show password");
        panel.add(label);
        panel.add(passwordAdmin);
        panel.add(checkBoxShowPassword);
        
        checkBoxShowPassword.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (checkBoxShowPassword.isSelected()) {
                    passwordAdmin.setEchoChar((char)0);
                } else {
                    passwordAdmin.setEchoChar('*');
                }
            }      
        });
        
        String [] options = new String [] {"    OK    ", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Verifikasi Password Admin", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
        if (option == 0) //Pressing OK
        {
            char [] passwordAdminChar = passwordAdmin.getPassword();
            String usernameAdmin = "IndraSudirman";
            try {
             ConnectionDatabase connectionDatabase = new ConnectionDatabase();
             connectionDatabase.connect();
             connectionDatabase.statement = connectionDatabase.connection.createStatement();
             String sqlQuery = "SELECT `passwordOne`, `passwordTwo` FROM `users` WHERE username = ?";
             preparedStatement = connectionDatabase.connection.prepareStatement(sqlQuery);
             preparedStatement.setString(1, usernameAdmin);
             resultSet = preparedStatement.executeQuery();
             
             if (resultSet.next()) {
                 String passwordOne = resultSet.getString(1);
                 String passwordTwo = resultSet.getString(2);
                 System.out.println("Password One is " + passwordOne);
                 System.out.println("Password Two is " + passwordTwo);
                 
                 byte [] saltKey = hexStringToByteArray(passwordTwo);
                 String passwordReal = digest(passwordAdminChar, saltKey);
                 if (passwordReal.equals(passwordOne)) {
                     JOptionPane.showMessageDialog(null, "Password benar!", "Benar", JOptionPane.INFORMATION_MESSAGE);
                     
                     laundry.setPasswordAdminPassed(true);

                     System.out.println(Laundry.getPasswordAdminPassed() + "password true");
                 } else {
                     JOptionPane.showMessageDialog(null, "Password salah!", "Salah", JOptionPane.INFORMATION_MESSAGE);
                     System.out.println(Laundry.getPasswordAdminPassed());
                 }
             }
             resultSet.close();
             
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error " + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void login() throws NoSuchAlgorithmException {
        String usernameJTextField, usernameToDatabase;
        char [] password;
        usernameJTextField = jTextField1.getText();
        password = jPasswordField1.getPassword();
        
        try {
            connectionDatabase.connect();
            connectionDatabase.statement = connectionDatabase.connection.createStatement();
            String sqlQuery = "SELECT `username`, `passwordOne`, `passwordTwo` FROM `users` WHERE username = ?";
            preparedStatement = connectionDatabase.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, usernameJTextField);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                usernameToDatabase = resultSet.getString(1);
                passwordOneDB = resultSet.getString(2);
                passwordTwoDB = resultSet.getString(3);
                System.out.println("usernameJTextField "+usernameJTextField);
                System.out.println("usernameToDatabase "+usernameToDatabase);
                System.out.println(passwordOneDB);
                System.out.println(passwordTwoDB);
                if (usernameJTextField.equals(usernameToDatabase)) {
                    byte [] salt = hexStringToByteArray(passwordTwoDB);
                    passwordUser = digest(password, salt);
                    if (passwordUser.equals(passwordOneDB)) {
                        JOptionPane.showMessageDialog(null, "Password benar!", "Benar", JOptionPane.INFORMATION_MESSAGE);                    
                        Laundry laundry = new Laundry();
                        System.out.println("Nilai 1 "+Laundry.getPasswordAdminPassed());
                        laundry.setPasswordAdminPassed(true);
                        System.out.println("Nilai 2 " + Laundry.getPasswordAdminPassed());
                    }else {
                        JOptionPane.showMessageDialog(null, "Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
                        Laundry laundry = new Laundry();
                        System.out.println("Password User salah sebelum diset false " + Laundry.getPasswordAdminPassed());
                        laundry.setPasswordAdminPassed(false);
                        System.out.println("Password User salah " + Laundry.getPasswordAdminPassed());
                    }
                
                } else {
                    JOptionPane.showMessageDialog(null, "Username salah!", "Error", JOptionPane.ERROR_MESSAGE);
                }   
                
            } else {
                JOptionPane.showMessageDialog(null, "Username salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            jTextField1.setText(""); 
            Arrays.fill(password, '0');
            System.out.println("Arrays Fill" + Arrays.toString(password));
//            jPasswordField1.selectAll();
//            jPasswordField1.resetKeyboardActions();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e, "Error", JOptionPane.ERROR_MESSAGE);            
        }
    }

   
    
    public static boolean getPasswordAdminPassed() {
        return passwordAdminPassed;
    }

    public void setPasswordAdminPassed(boolean passwordAdminPassed) {
        Laundry.passwordAdminPassed = passwordAdminPassed;
    }
}


























