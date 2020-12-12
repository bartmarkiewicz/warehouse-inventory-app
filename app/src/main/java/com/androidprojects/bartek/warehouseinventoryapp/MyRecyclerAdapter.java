package com.androidprojects.bartek.warehouseinventoryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidprojects.bartek.warehouseinventoryapp.database.Item;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    List<Item> data;

    public MyRecyclerAdapter() {
        super();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //data = MainActivity.listOfItems;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerv_card_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < getItemCount()) {
            holder.name1.setText(data.get(position).itemName);
            holder.desc1.setText(data.get(position).description1);
            holder.cost.setText(data.get(position).cost);
            holder.quantity.setText(data.get(position).quantity);
            holder.freeze.setText(Boolean.toString(data.get(position).frozen));
            holder.id.setText(Integer.toString(data.get(position).id));
        }

    }
    public void setItems(List<Item> newData) {
        data=newData;
    }
    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        } else return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name1, desc1, cost, quantity, freeze, id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name1 = itemView.findViewById(R.id.nameField);
            desc1 = itemView.findViewById(R.id.descriptionField);
            cost = itemView.findViewById(R.id.costField);
            quantity = itemView.findViewById(R.id.quantityField);
            freeze = itemView.findViewById(R.id.freezeField);
            id = itemView.findViewById(R.id.idField);
        }
    }
}
