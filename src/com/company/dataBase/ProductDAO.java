package com.company.dataBase;

import com.company.Product;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {

    public ProductDAO() {
        create();
    }


    public void create() {
        String sql = "create table if not exists product (id integer primary key," +
                "name text not null," +
                "expireDate text not null," +
                "cumulativePrice int," +
                "singlePrice int," +
                "cumulativeQuantity int," +
                "singleQuantity int," +
                "remainQuantity int," +
                "barcode text not null)";
        try (PreparedStatement preparedStatement = Connect.getConnect().prepareStatement(sql)) {
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public void insert(Product product) {
        String sql = "insert into product(name, " +
                "expireDate," +
                "cumulativePrice, " +
                "singlePrice," +
                " cumulativeQuantity," +
                " singleQuantity, " +
                "remainQuantity, " +
                "barcode)values(?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = Connect.getConnect().prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getExpireDate());
            preparedStatement.setInt(3, product.getCumulativePrice());
            preparedStatement.setInt(4, product.getSinglePrice());
            preparedStatement.setInt(5, product.getCumulativeQuantity());
            preparedStatement.setInt(6, product.getSingleQuantity());
            preparedStatement.setInt(7, product.getRemainQuantity());
            preparedStatement.setString(8, product.getBarcode());
            preparedStatement.execute();
            System.out.println("insert");
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public static ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "select * from product";
        try (PreparedStatement preparedStatement = Connect.getConnect().prepareStatement(sql)) {
            ResultSet rst = preparedStatement.executeQuery();
            while (rst.next()) {
                Product product = new Product(rst.getInt(1),rst.getString(2), rst.getString(3),
                        rst.getInt(4), rst.getInt(5), rst.getInt(6),
                        rst.getInt(7), rst.getInt(8), rst.getString(9));

                products.add(product);

            }


        } catch (SQLException e) {
            //TODO
        }
        return products;
    }

    public static boolean checkBarcode(String barcode) {
        String sql = "select id from product where barcode = ?";
        try (PreparedStatement preparedStatement = Connect.getConnect().prepareStatement(sql)) {
            preparedStatement.setString(1, barcode);
            ResultSet rst = preparedStatement.executeQuery();
            if (rst.next()) {
                return true;
            }
        } catch (SQLException e) {
            //TODO
        }
        return false;
    }


    public static void updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, " +
                "expireDate = ?," +
                "cumulativePrice = ?," +
                "singlePrice = ?," +
                "cumulativeQuantity = ?," +
                "singleQuantity = ?," +
                "remainQuantity = ?" +
                "WHERE barcode = ?";

        try (PreparedStatement pstmt = Connect.getConnect().prepareStatement(sql)) {

            // set the corresponding param

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getExpireDate());
            pstmt.setInt(3, product.getCumulativePrice());
            pstmt.setInt(4, product.getSinglePrice());
            pstmt.setInt(5, product.getCumulativeQuantity());
            pstmt.setInt(6, product.getSingleQuantity());
            pstmt.setInt(7, product.getRemainQuantity());
            pstmt.setString(8, product.getBarcode());

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ohhhhh");
            System.out.println(e.getMessage());
        }
    }


}