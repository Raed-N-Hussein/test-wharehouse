package com.company.db;

import com.company.Order;
import com.company.Product;

import java.sql.*;
import java.util.ArrayList;

public class OrderDAO extends DatabaseConnection {
    public OrderDAO() {
        createTableOrder();
    }

    public void createTableOrder() {
        String sql = "CREATE TABLE IF NOT EXISTS orders (id integer PRIMARY KEY, " +
                "total_price real, " +
                "paid_amount real, " +
                "remain_amount real, " +
                "total_debit real, " +
                "created_at text NOT NULL, " +
                "client_name text NOT NULL);";
        try {
            getConnect();
            Statement stm = getConnect().createStatement();
            stm.execute(sql);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public void insertData(int totalPrice, int paidAmount, int remainAmount, int totalDebit, String dateTime, String clientName, ArrayList<Product> products) {
        String sql = "INSERT INTO  orders (total_price, paid_amount, remain_amount, total_debit, created_at, client_name) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            getConnect();
            PreparedStatement pstm = getConnect().prepareStatement(sql);
            pstm.setInt(1, totalPrice);
            pstm.setInt(2, paidAmount);
            pstm.setInt(3, remainAmount);
            pstm.setInt(4, totalDebit);
            pstm.setString(5, dateTime);
            pstm.setString(6, clientName);
            pstm.executeUpdate();
            this.insertDataToOrderProductTable(products);

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public int getOrderId() {
        String sql = "select MAX(id) FROM orders;";
        try {
            getConnect();
            PreparedStatement pstm = getConnect().prepareStatement(sql);
            ResultSet rst = pstm.executeQuery();
            if (rst.next()) {
                return rst.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    public void insertDataToOrderProductTable(ArrayList<Product> products) {
        OrderProduct OrderProduct = new OrderProduct();
        for (Product prod : products) {
            OrderProduct.insertDataOrderProduct(getOrderId(), prod.getId(),prod.getQuantity());
        }
    }

    public Order find(int orderId) throws Exception {
        Order order = new Order();
        String sql = "SELECT * FROM orders WHERE id = ?";
        PreparedStatement pstm = getConnect().prepareStatement(sql);
        pstm.setInt(1, orderId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            order.setId(orderId);
            order.setTotal(rst.getInt("total_price"));
            order.setPaid(rst.getInt("paid_amount"));
            order.setRemain(rst.getInt("remain_amount"));
            order.setTotalDebit(rst.getInt("total_debit"));
            order.setDate(rst.getString("created_at"));
            order.setClientName(rst.getString("client_name"));
        } else {
            throw new Exception("هذا الوصل غير موجود...!");
        }
        order.setItems(getOrderProducts(orderId));

        return order;

    }

    public ArrayList<Product> getOrderProducts(int orderId) {
        ArrayList<Product> results = new ArrayList<>();
        String sql = "SELECT id, name, cumulative_price, single_price, quantity, barcode " +
                "FROM orders_products" +
                " LEFT JOIN products ON " +
                "orders_products.product_id = products.id " +
                "WHERE orders_products.order_id = ?;";
        try (PreparedStatement pstm = getConnect().prepareStatement(sql)) {
            pstm.setInt(1, orderId);
            ResultSet rst = pstm.executeQuery();
            while (rst.next()) {
                Product prod = new Product(rst.getInt("id"),
                        rst.getString("name"),
                        rst.getInt("cumulative_price"),
                        rst.getInt("single_price"),
                        rst.getInt("quantity"),
                        rst.getString("barcode"));
                prod.setQuantity(rst.getFloat("quantity"));
                results.add(prod);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }


    public ArrayList<Order> getOrdersToClient(Object object) {
        ArrayList<Order> results = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE client_name = ? OR id = ?";
        try (PreparedStatement pstm = getConnect().prepareStatement(sql)) {
            pstm.setObject(1, object);
            pstm.setObject(2, object);
            ResultSet result = pstm.executeQuery();
            while (result.next()) {
                Order order = new Order();
                order.setId(result.getInt("id"));
                order.setTotal(result.getInt("total_price"));
                order.setPaid(result.getInt("paid_amount"));
                order.setRemain(result.getInt("remain_amount"));
                order.setTotalDebit(result.getInt("total_debit"));
                order.setDate(result.getString("created_at"));
                order.setClientName(result.getString("client_name"));
                results.add(order);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return results;
    }
}
