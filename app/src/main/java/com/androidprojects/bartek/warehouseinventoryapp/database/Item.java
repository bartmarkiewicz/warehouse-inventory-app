package com.androidprojects.bartek.warehouseinventoryapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "itemId")
    public int id;
    @ColumnInfo(name = "itemName")
    public String itemName;
    @ColumnInfo(name = "description")
    public String description1;
    @ColumnInfo(name = "cost")
    public String cost;
    @ColumnInfo(name = "quantity")
    public String quantity;
    @ColumnInfo(name = "frozen")
    public boolean frozen;


    public Item(String itemName, String description1, String cost, boolean frozen, String quantity) {
        this.itemName = itemName;
        this.description1 = description1;
        this.cost = cost;
        this.frozen = frozen;
        this.quantity = quantity;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }
}
