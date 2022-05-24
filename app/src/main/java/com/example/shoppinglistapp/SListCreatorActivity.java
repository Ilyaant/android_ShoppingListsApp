package com.example.shoppinglistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shoppinglistapp.Adapters.ListViewAdapter;
import com.example.shoppinglistapp.Models.SList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SListCreatorActivity extends AppCompatActivity {

    static ListView listView;
    static List<String> items;
    static ListViewAdapter adapter;
    Toast t;
    EditText input;
    ImageView enter;
    SList list;
    boolean isOldList = false;
    FloatingActionButton fab_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slist_creator);

        listView = findViewById(R.id.listView);
        input = findViewById(R.id.input);
        enter = findViewById(R.id.add);

        items = new ArrayList<>();
        list = new SList();
        try {
            list = (SList) getIntent().getSerializableExtra("old_list");
            items = list.getProducts();
            isOldList = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        fab_save = findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                if (!isOldList) {
                    list = new SList();
                }
                list.setProducts(items);
                list.setDate(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("list", list);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = items.get(i);
                makeToast(name);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                makeToast("Removed: " + items.get(i));
                removeItem(i);
                return false;
            }
        });

        adapter = new ListViewAdapter(getApplicationContext(), items);
        listView.setAdapter(adapter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString();
                if (text == null || text.length() == 0) {
                    makeToast("Enter an item.");
                } else {
                    addItem(text);
                    input.setText("");
                    makeToast("Added: " + text);
                }
            }
        });
    }

    public static void addItem(String item) {
        items.add(item);
        listView.setAdapter(adapter);
    }

    public static void removeItem(int remove) {
        items.remove(remove);
        listView.setAdapter(adapter);
    }

    private void makeToast(String s) {
        if (t != null)
            t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
}