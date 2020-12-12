package com.androidprojects.bartek.warehouseinventoryapp.database;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private LiveData<List<Item>> allItems;
    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
        allItems = itemRepository.getAllItems();
    }
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void insert(Item item){
        itemRepository.insert(item);
    }

    public void deleteAll(){
        itemRepository.deleteAll();
    }


}
