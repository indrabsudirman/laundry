/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laundry.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Jabrikos
 */
public class ConnectionDatabase {
    
    Connection connection;
    Statement statement;
    public Connection connect(){
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Berhasil koneksi database");
        } catch (ClassNotFoundException e) {
            System.out.println("Gagal koneksi database " + e);
        }
        
        String url = "jdbc:mysql://localhost/laundry";
        try {
            connection = DriverManager.getConnection(url, "root", "");
            System.out.println("Berhasil koneksi database");
        } catch (SQLException e) {
            System.out.println("Gagal koneksi database " + e);
        }
        return connection;
    }
    
}
