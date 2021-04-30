package com.company;

public class Product {
    private int id;
    private String name;
    private int singlePrice;
    private int cumulativePrice;
    private int cumulativeQuantity;
    private int singleQuantity;
    private int remainQuantity;

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    private int quantity;
    private boolean isCumulative;
    private String barcode;
    private String expireDate;

    public Product() {

    }

    public int getCumulativeQuantity() {
        return cumulativeQuantity;
    }

    public void setCumulativeQuantity(int cumulativeQuantity) {
        this.cumulativeQuantity = cumulativeQuantity;
    }

    public int getSingleQuantity() {
        return singleQuantity;
    }

    public void setSingleQuantity(int singleQuantity) {
        this.singleQuantity = singleQuantity;
    }

    public int getRemainQuantity() {
        return remainQuantity;
    }

    public void setRemainQuantity(int remainQuantity) {
        this.remainQuantity = remainQuantity;
    }

    public Product(String name, int cumulativePrice, int singlePrice) {
        this.name = name;
        this.cumulativePrice = cumulativePrice;
        this.singlePrice = singlePrice;
        this.quantity = 1;
        this.isCumulative = false;
    }

    public Product(String name, String expireDate,int cumulativePrice, int singlePrice, int cumulativeQuantity,
                   int singleQuantity, int remainQuantity, String barcode){
        this.name = name;
        this.expireDate = expireDate;
        this.cumulativePrice = cumulativePrice;
        this.singlePrice = singlePrice;
        this.cumulativeQuantity = cumulativeQuantity;
        this.singleQuantity = singleQuantity;
        this.remainQuantity = remainQuantity;
        this.barcode = barcode;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(int singlePrice) {
        this.singlePrice = singlePrice;
    }

    public int getCumulativePrice() {
        return cumulativePrice;
    }


    public void setCumulativePrice(int cumulativePrice) {
        this.cumulativePrice = cumulativePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isCumulative() {
        return isCumulative;
    }

    public void setCumulative(boolean cumulative) {
        isCumulative = cumulative;
    }
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getPrice() {
        // if isCumulative true return cumulative * quantity .. else return singlePrice * quantity
        return isCumulative ? cumulativePrice * quantity : singlePrice * quantity;
    }
}
