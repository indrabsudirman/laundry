/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laundry.code;

import static com.laundry.code.PasswordFieldToMD5.byteArrayToHexString;
import static com.laundry.code.PasswordFieldToMD5.digest;
import static com.laundry.code.PasswordFieldToMD5.getSalt;
import static com.laundry.code.PasswordFieldToMD5.hexStringToByteArray;
import com.laundry.ui.Login;
import static com.laundry.ui.Login.jPasswordField1;
import static com.laundry.ui.Login.jTextField1;
import com.laundry.ui.SignUp;
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
                 
                 byte [] saltKey = hexStringToByteArray(passwordTwo);
                 String passwordReal = digest(passwordAdminChar, saltKey);
                 if (passwordReal.equals(passwordOne)) {
                     JOptionPane.showMessageDialog(null, "Password benar!", "Benar", JOptionPane.INFORMATION_MESSAGE);                     
                     laundry.setPasswordAdminPassed(true);
                 } else {
                     JOptionPane.showMessageDialog(null, "Password salah!", "Salah", JOptionPane.INFORMATION_MESSAGE);
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
//            connectionDatabase.statement = connectionDatabase.connection.createStatement();
            String sqlQuery = "SELECT `username`, `passwordOne`, `passwordTwo` FROM `users` WHERE username = ?";
            preparedStatement = connectionDatabase.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, usernameJTextField);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                usernameToDatabase = resultSet.getString(1);
                passwordOneDB = resultSet.getString(2);
                passwordTwoDB = resultSet.getString(3);
                if (usernameJTextField.equals(usernameToDatabase)) {
                    byte [] salt = hexStringToByteArray(passwordTwoDB);
                    passwordUser = digest(password, salt);
                    if (passwordUser.equals(passwordOneDB)) {
                        JOptionPane.showMessageDialog(null, "Password benar!", "Benar", JOptionPane.INFORMATION_MESSAGE);                    
                        Laundry laundry = new Laundry();
                        laundry.setPasswordAdminPassed(true);
                    }else {
                        JOptionPane.showMessageDialog(null, "Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
                        Laundry laundry = new Laundry();
                        laundry.setPasswordAdminPassed(false);
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
            jPasswordField1.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e, "Error", JOptionPane.ERROR_MESSAGE);            
        }
    }
    
    public void signUp() throws NoSuchAlgorithmException, NoSuchProviderException {
        String userNameSignUp;
        char [] passwordTwoSignUp;
        userNameSignUp = SignUp.jTextField1.getText();
        passwordTwoSignUp = SignUp.passwordUser;
        try {
            byte [] salt = getSalt();
            String passwordOne = byteArrayToHexString(salt);
            String passwordTwo = digest(passwordTwoSignUp, salt);
            connectionDatabase.connect();
            String sqlQuery = "INSERT INTO users (username, passwordOne, passwordTwo) VALUES (?, ?, ?)";
            preparedStatement = connectionDatabase.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userNameSignUp);
            preparedStatement.setString(2, passwordTwo);
            preparedStatement.setString(3, passwordOne);
            preparedStatement.execute();
            JOptionPane.showMessageDialog(null, "User baru berhasil dibuat!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);                    
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


























