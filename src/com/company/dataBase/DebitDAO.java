package com.company.db;

import com.company.Account;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DebitDAO extends DatabaseConnection {
    public DebitDAO() {
        createTableDebits();
    }

    public void createTableDebits() {
        String sql = "CREATE TABLE IF NOT EXISTS debits (id integer PRIMARY KEY, " +
                "name text NOT NULL, " +
                "total_amount real, " +
                "date_time text NOT NULL);";
        try {
            getConnect();
            Statement stm = getConnect().createStatement();
            stm.execute(sql);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public boolean checkName(String name) {
        String sql = "SELECT id FROM debits WHERE name=?";
        try {
            PreparedStatement pstm = getConnect().prepareStatement(sql);
            pstm.setString(1, name);
            ResultSet rst = pstm.executeQuery();
            if (rst.next()) {
                return true;
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return false;
    }

    public void insertData(String name, double totalAmount, String datTime) {
        String sql = "INSERT INTO  debits (name, total_amount, date_time) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = getConnect().prepareStatement(sql);
            pstm.setString(1, name);
            pstm.setDouble(2, totalAmount);
            pstm.setString(3, datTime);
            pstm.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public int getDebitId() {
        String sql = "select MAX(id) FROM debits;";
        try {
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


    public Account find(Object object) {
        Account account = new Account();
        String sql = "SELECT * FROM debits WHERE id = ? OR name = ?";
        try (PreparedStatement pstm = getConnect().prepareStatement(sql)) {
            pstm.setObject(1, object);
            pstm.setObject(2, object);
            ResultSet rst = pstm.executeQuery();
            if (rst.next()) {
                account.setID(rst.getInt("id"));
                account.setName(rst.getString("name"));
                account.setTotalDebit(rst.getInt("total_amount"));
                account.setCreationDate(rst.getString("date_time"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return account;
    }

    public void updateAccount(int debitId, double totalAmount, String dateTime) {
        String sql = "UPDATE debits SET total_amount = ?, date_time = ? WHERE id = ?";
        try (PreparedStatement pstm = getConnect().prepareStatement(sql)) {
            pstm.setDouble(1, totalAmount);
            pstm.setString(2, dateTime);
            pstm.setInt(3, debitId);
            pstm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void updateAccountName(int debitId,String name) {
        String sql = "UPDATE debits SET name = ? WHERE id = ?";
        try (PreparedStatement pstm = getConnect().prepareStatement(sql)) {
            pstm.setString(1, name);
            pstm.setInt(2, debitId);
            pstm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public ArrayList<Account> getDebits() {
        ArrayList<Account> results = new ArrayList<>();
        String sql = "SELECT * FROM debits";
        try (Statement stm = getConnect().createStatement()) {
            ResultSet result = stm.executeQuery(sql);
            while (result.next()) {
                Account account = new Account(result.getString("name"),
                        result.getInt("total_amount"),
                        result.getInt("id"),
                        result.getString("date_time"));
                results.add(account);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return results;
    }

    public int returnTotalAmount(Object object) {
        String sql = "SELECT total_amount FROM debits WHERE name = ? OR id = ?";
        try (PreparedStatement pstm = getConnect().prepareStatement(sql)) {
            pstm.setObject(1, object);
            pstm.setObject(2, object);
            ResultSet rst = pstm.executeQuery();
            if (rst.next()) {
                return rst.getInt("total_amount");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    public void updateAccountByNameOrId(Object object, int remain, String dateTime) {
        String sql = "UPDATE debits SET total_amount = ?, date_time = ? WHERE name = ? OR id = ?";
        try (PreparedStatement pstm = getConnect().prepareStatement(sql)) {
            pstm.setDouble(1, returnTotalAmount(object) + remain);
            pstm.setString(2, dateTime);
            pstm.setObject(3, object);
            pstm.setObject(4, object);
            pstm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean checkAccount(Object object) {
        String sql = "SELECT * FROM debits WHERE name = ? OR id = ?";
        try (PreparedStatement pstm = getConnect().prepareStatement(sql)) {
            pstm.setObject(1, object);
            pstm.setObject(2, object);
            ResultSet rst = pstm.executeQuery();
            if (rst.next()) {
               return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }


}
