package com.company.dataBase;

import com.company.orders.Order;
import com.company.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderDAO {
    public OrderDAO() {
        createTableOrder();
    }

    public void createTableOrder() {
        String sql = "create table if not exists orders (id integer PRIMARY KEY, " +
                "total_price real, " +
                "paid_amount real, " +
                "remain_amount real, " +
                "total_debit real, " +
                "created_at text NOT NULL, " +
                "client_name text NOT NULL);";
        try {
            PreparedStatement stm = Connect.getConnect().prepareStatement(sql);
            stm.execute();

        } catch (SQLException se) {
            System.out.println(se.getMessage() + " error..");
        }
    }

    public static void insertData(Order order) {
        String sql = "INSERT INTO  orders (total_price," +
                " paid_amount, " +
                "remain_amount, " +
                "total_debit, " +
                "created_at," +
                " client_name)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        try {

            PreparedStatement pstm = Connect.getConnect().prepareStatement(sql);
            pstm.setInt(1, order.getTotal());
            pstm.setInt(2, order.getPaid());
            pstm.setInt(3, order.getRemain());
            pstm.setInt(4, order.getTotalDebit());
            pstm.setString(5, order.getDate());
            pstm.setString(6, order.getClientName());
            pstm.executeUpdate();
            insertDataToOrderProductTable(order.getItems());

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public static int getOrderId() {
        String sql = "select MAX(id) FROM orders;";
        try {

            PreparedStatement pstm = Connect.getConnect().prepareStatement(sql);
            ResultSet rst = pstm.executeQuery();
            if (rst.next()) {
                return rst.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    public static void insertDataToOrderProductTable(ArrayList<Product> products) {
        OrderProduct OrderProduct = new OrderProduct();
        for (Product prod : products) {
            OrderProduct.insertDataOrderProduct(getOrderId(), prod.getId(), prod.getQuantity());
        }
    }

    public static Order find(int orderId) throws Exception {
        Order order = new Order();
        String sql = "SELECT * FROM orders WHERE id = ?";
        PreparedStatement pstm = Connect.getConnect().prepareStatement(sql);
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

    public static ArrayList<Product> getOrderProducts(int orderId) {
        ArrayList<Product> results = new ArrayList<>();
        String sql = "SELECT id, name, " +
                "cumulativePrice, " +
                "singlePrice," +
                "quantity, barcode " +
                "FROM product" +
                " LEFT JOIN orders_products ON " +
                "orders_products.product_id = product.id " +
                "WHERE orders_products.order_id = ?;";
        try {
            PreparedStatement pstm = Connect.getConnect().prepareStatement(sql);
            pstm.setInt(1, orderId);

            ResultSet rst = pstm.executeQuery();
            while (rst.next()) {
                Product product = new Product(rst.getInt("id"), rst.getString("name"),
                        rst.getInt("cumulativePrice"), rst.getInt("singlePrice"),
                        rst.getInt("quantity"), rst.getString("barcode"));
                results.add(product);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }


    public ArrayList<Order> getOrdersToClient(Object object) {
        ArrayList<Order> results = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE client_name = ? OR id = ?";
        try (PreparedStatement pstm = Connect.getConnect().prepareStatement(sql)) {
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
