package com.company.dataBase;

import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static Connection c = null;
    public static Connection getConnect() {

        try {
            if(c==null){
                c =  DriverManager.getConnection("jdbc:sqlite:salesData.db");
                System.out.println("connect");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return c;
    }
}
