/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laundry.code;

import static com.laundry.code.PasswordFieldToMD5.digest;
import static com.laundry.code.PasswordFieldToMD5.getSalt;
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
//        byte [] salt = getSalt();
        
        String [] options = new String [] {"    OK    ", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Verifikasi Password Admin", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
        if (option == 0) //Pressing OK
        {
            char [] passwordAdminChar = passwordAdmin.getPassword();
            String usernameAdmin = "IndraSudirman";
//            String passwordOne = "bfcc83ec6203141323a6e2f392edb73d";
//            String passwordTwo = "9d54178d6411bb0c0f7c787a8304df0b";
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
//                 PasswordFieldToMD5 = passwordFieldToMD5 = new PasswordFieldToMD5();
                 byte [] saltKey = hexStringToByteArray(passwordTwo);
                 String passwordReal = digest(passwordAdminChar, saltKey);
                 if (passwordReal.equals(passwordOne)) {
                     System.out.println("Password matched");
                     System.out.println("Password real is " + passwordReal);
                 } else {
                     System.out.println("Password doesn't match");
                 }
             }
             resultSet.close();
             
             
             
            } catch (SQLException e) {
                System.out.println("Error " + e);
            }
//            Laundry laundry = new Laundry();
//            laundry.verifikasiPasswordAdmin(); 
//            char [] passwordAdminChar = passwordAdmin.getPassword();
//            String passwordAdminInHash = digest(passwordAdminChar, salt);
//            System.out.println("Password in hash MD5 + Salt is : "  + passwordAdminInHash);
        }
    }
    public static void connectToDatabase() {
//        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
//        connectionDatabase.connectDatabase();
    }
    public void verifikasiPasswordAdmin() {
        
        String userNameAdmin, passwordAdmin;
        userNameAdmin = "IndraSudirman";
        passwordAdmin = null;
        
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            connectionDatabase.statement = connectionDatabase.connection.createStatement();
            String sqlQuery = "SELECT * FROM users WHERE username = ?";
//            preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setString(1, userNameAdmin);
            resultSet = statement.executeQuery(sqlQuery);
            
            if (resultSet.next()) {
                System.out.println("Nama username Admin " + userNameAdmin + " terdapat didatabase");
            } else {
                System.out.println("Username salah " );
            }
            
//            connectionDatabase.statement = connectionDatabase.connection.createStatement();
//            String sqlQuery = "SELECT * FROM users WHERE USERNAME = ?";// AND PASSWORD = ?";
//            preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setString(1, userNameAdmin);
////            preparedStatement.setString(2, passwordAdmin);
//            resultSet = preparedStatement.executeQuery();
//            
//            if (resultSet.next()) {
//                System.out.println("Nama username Admin " + userNameAdmin + " terdapat didatabase");
                
            
            
        } catch (SQLException e) {
            System.out.println("Password Admin salah : " + e);
        
        }
        
    }
    
}







