package com.example.shoppinglistapp;

import androidx.cardview.widget.CardView;

import com.example.shoppinglistapp.Models.SList;

public interface SListClickListener {
    void onClick(SList sList);
    void onLongClick(SList sList, CardView cardView);
}
