/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laundry.code;

import static com.laundry.code.PasswordFieldToMD5.digest;
import static com.laundry.code.PasswordFieldToMD5.hexStringToByteArray;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    
    ConnectionDatabase connectionDatabase;// = new ConnectionDatabase();
    Connection connection;
    static ResultSet resultSet;
    Statement statement;
    static PreparedStatement preparedStatement;
    
    public static void verifikasiPassword() throws NoSuchAlgorithmException, NoSuchProviderException {
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
//                 
                 byte [] saltKey = hexStringToByteArray(passwordTwo);
                 String passwordReal = digest(passwordAdminChar, saltKey);
                 if (passwordReal.equals(passwordOne)) {
                     JOptionPane.showMessageDialog(null, "Password benar!", "Benar", JOptionPane.INFORMATION_MESSAGE);
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
}









