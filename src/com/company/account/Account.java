package com.company.account;

public class Account {
    private String name;
    private int totalDebit;
    private int ID;
    private String creationDate;

    public Account() {

    }

    public Account(String name, int totalDebit, int ID, String creationDate) {
        this.name = name;
        this.totalDebit = totalDebit;
        this.ID = ID;
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(int totalDebit) {
        this.totalDebit = totalDebit;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}


