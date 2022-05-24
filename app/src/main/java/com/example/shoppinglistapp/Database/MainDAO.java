package com.example.shoppinglistapp.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.shoppinglistapp.Models.SList;

import java.util.List;

@Dao
public interface MainDAO {
    @Insert(onConflict = REPLACE)
    void insert(SList sList);

    @Query("SELECT * FROM shopping_list ORDER BY id DESC")
    List<SList> getAll();

    @Query("UPDATE shopping_list SET products = :products WHERE ID = :id")
    void update(int id, List<String> products);

    @Delete
    void delete(SList sList);
}
