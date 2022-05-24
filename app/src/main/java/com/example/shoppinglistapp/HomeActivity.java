package com.example.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shoppinglistapp.Adapters.SListsAdapter;
import com.example.shoppinglistapp.Database.RoomDB;
import com.example.shoppinglistapp.Models.SList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    SListsAdapter sListAdapter;
    List<SList> list = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SList selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);

        database = RoomDB.getInstance(this);
        list = database.mainDAO().getAll();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        updateRecycler(list);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SListCreatorActivity.class);
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                SList new_list = (SList) data.getSerializableExtra("list");
                database.mainDAO().insert(new_list);
                list.clear();
                list.addAll(database.mainDAO().getAll());
                sListAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                SList new_list = (SList) data.getSerializableExtra("list");
                database.mainDAO().update(new_list.getID(), new_list.getProducts());
                list.clear();
                list.addAll(database.mainDAO().getAll());
                sListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<SList> list) {
        sListAdapter = new SListsAdapter(HomeActivity.this, list, listClickListener);
        recyclerView.setAdapter(sListAdapter);
    }

    private final SListClickListener listClickListener = new SListClickListener() {
        @Override
        public void onClick(SList sList) {
            Intent intent = new Intent(HomeActivity.this, SListCreatorActivity.class);
            intent.putExtra("old_list", sList);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(SList sList, CardView cardView) {
            selectedList = new SList();
            selectedList = sList;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                database.mainDAO().delete(selectedList);
                list.remove(selectedList);
                sListAdapter.notifyDataSetChanged();
                Toast.makeText(HomeActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_guest, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.returnToLogin:
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}