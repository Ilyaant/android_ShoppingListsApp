package com.example.shoppinglistapp.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity(tableName = "shopping_list")
public class SList implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int ID = 0;

    @ColumnInfo(name = "products")
    List<String> products;

    @ColumnInfo(name = "date")
    String date = "";

    public SList() {
        this.ID = ThreadLocalRandom.current().nextInt();
    }

    public SList(int ID, List<String> products, String date) {
        this.ID = ID;
        this.products = products;
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
