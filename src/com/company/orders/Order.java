package com.company.orders;

import com.company.Product;
import com.company.dataBase.DebitDAO;

import java.util.ArrayList;

public class Order {
    private ArrayList<Product> items;
    private int total, paid, remain, totalDebit;;
    private String date;
    private long id;
    private String clientName;
    private String sellerName = "";
    public Order() {

    }

    public Order(ArrayList<Product> items, String date, String clientName, String sellerName, int paid) {
        this.items = items;
        this.date = date;
        this.clientName = clientName;
        this.sellerName = sellerName;
        this.total = this.calculateTotal();
        this.paid = paid;
        this.remain = this.total - this.paid;
        this.totalDebit = new DebitDAO().returnTotalAmount(clientName);

    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public int getPaid() {
        return paid;
    }

    public int getRemain() {
        return remain;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public void setTotalDebit(int totalDebit) {
        this.totalDebit = totalDebit;
    }

    public int getTotalDebit() {
        return totalDebit ;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    private int calculateTotal() {
        int total = 0;
        for (Product item : this.items) {
            total += item.getPrice();
        }
        return total;
    }
}
