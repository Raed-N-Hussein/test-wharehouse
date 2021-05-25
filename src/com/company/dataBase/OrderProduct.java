package com.company.db;
import java.sql.*;

public class OrderProduct extends DatabaseConnection {
    public OrderProduct() {
        createOrderProductTable();
    }

    public void createOrderProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS orders_products (order_id int NOT NULL, " +
                "product_id int NOT NULL," +
                "quantity REAL NOT NULL);";
        try {
            getConnect();
            Statement stm = getConnect().createStatement();
            stm.execute(sql);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public void insertDataOrderProduct(int orderId, int productId,float quantity) {
        String sql = "INSERT INTO  orders_products (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try {
            getConnect();
            PreparedStatement pstm = getConnect().prepareStatement(sql);
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
