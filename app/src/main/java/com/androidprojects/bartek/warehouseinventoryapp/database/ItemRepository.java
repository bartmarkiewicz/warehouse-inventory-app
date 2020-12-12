package com.androidprojects.bartek.warehouseinventoryapp.database;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;

    public ItemRepository(Application application){
        ItemDatabase db = ItemDatabase.getDatabase(application);
        itemDao = db.itemDao();
        allItems = itemDao.getAllItem();
    }

    public LiveData<List<Item>> getAllItems(){
        return allItems;
    }

    public void insert(final Item item){
        ItemDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                itemDao.addItem(item);
            }
        });
    }

    public void deleteAll(){
        ItemDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                itemDao.deleteAllItems();
            }
        });
    }
}
