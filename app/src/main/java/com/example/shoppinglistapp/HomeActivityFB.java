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
import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ThreadLocalRandom;

public class HomeActivityFB extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    SListsAdapter sListAdapter;
    List<SList> list = new ArrayList<>();
    FloatingActionButton fab_add;
    SList selectedList;
    ProgressDialog loader;

    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String onlineUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_fb);

        recyclerView = findViewById(R.id.recycler_home_fb);
        fab_add = findViewById(R.id.fab_add_fb);

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("lists").child(onlineUserID);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        initialize();

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivityFB.this, SListCreatorActivity.class);
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
                String id = reference.push().getKey();
                reference.child(id).setValue(new_list);
            }
        }
        else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                SList new_list = (SList) data.getSerializableExtra("list");
                Query query = reference.orderByChild("id").equalTo(new_list.getID());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1: snapshot.getChildren()) {
                            snapshot1.getRef().setValue(new_list);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                int index = -1;
                for (int i = 0; i < list.size(); i += 1)
                    if (list.get(i).getID() == new_list.getID()) {
                        index = i;
                    }
                list.remove(index);
                list.add(index, new_list);
                Toast.makeText(HomeActivityFB.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialize() {
        sListAdapter = new SListsAdapter(HomeActivityFB.this, list, listClickListener);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(snapshot.getValue(SList.class));
                sListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                sListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                list.remove(snapshot.getValue(SList.class));
                sListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(sListAdapter);
    }

    private final SListClickListener listClickListener = new SListClickListener() {
        @Override
        public void onClick(SList sList) {
            Intent intent = new Intent(HomeActivityFB.this, SListCreatorActivity.class);
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
                Query query = reference.orderByChild("id").equalTo(selectedList.getID());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1: snapshot.getChildren()) {
                            snapshot1.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                list.remove(selectedList);
                Toast.makeText(HomeActivityFB.this, "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_logged, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(HomeActivityFB.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}