package com.company.dataBase;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderProduct {
    public OrderProduct() {
        createOrderProductTable();
    }

    public void createOrderProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS orders_products (order_id int NOT NULL, " +
                "product_id int NOT NULL," +
                "quantity REAL NOT NULL);";
        try {
            PreparedStatement stm = Connect.getConnect().prepareStatement(sql);
            stm.execute();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public void insertDataOrderProduct(int orderId, int productId,float quantity) {
        String sql = "INSERT INTO  orders_products (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = Connect.getConnect().prepareStatement(sql);
            pstm.setInt(1, orderId);
            pstm.setInt(2, productId);
            pstm.setFloat(3, quantity);
            pstm.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println(se.getMessage());
        }
    }
}
