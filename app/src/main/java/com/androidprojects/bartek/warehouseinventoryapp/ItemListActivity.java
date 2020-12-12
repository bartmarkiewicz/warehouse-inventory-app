package com.androidprojects.bartek.warehouseinventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.androidprojects.bartek.warehouseinventoryapp.database.Item;
import com.androidprojects.bartek.warehouseinventoryapp.database.ItemViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyRecyclerAdapter adapter;
    ItemViewModel itemViewModel;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceCondition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerAdapter();

        recyclerView.setAdapter(adapter);

        itemViewModel = MainActivity.itemViewModel;

        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() { // this updates the adapter data based on the data found in the database
            @Override
            public void onChanged(List<Item> newData) {
                adapter.setItems(newData);
                adapter.notifyDataSetChanged();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseReferenceCondition = databaseReference.getRoot();
        databaseReferenceCondition.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                itemViewModel.getAllItems().getValue().add(dataSnapshot.getValue(Item.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                itemViewModel.getAllItems().getValue().remove(dataSnapshot.getValue(Item.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
