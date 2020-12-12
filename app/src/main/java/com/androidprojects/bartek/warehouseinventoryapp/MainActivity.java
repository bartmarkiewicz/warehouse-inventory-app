package com.androidprojects.bartek.warehouseinventoryapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidprojects.bartek.warehouseinventoryapp.database.Item;
import com.androidprojects.bartek.warehouseinventoryapp.database.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    static ArrayList<Item> listOfItems = new ArrayList<>();
    static ItemViewModel itemViewModel;
    int xLoc, yLoc;

    public void addItem(){
        EditText itemName = findViewById(R.id.editText);
        String toastString = itemName.getText().toString() + " has been added.";
        Toast.makeText(MainActivity.this, toastString, Toast.LENGTH_SHORT).show();
        EditText field1 = findViewById(R.id.editText);
        EditText field2 = findViewById(R.id.editText2);
        EditText field3 = findViewById(R.id.editText3);
        EditText field4 = findViewById(R.id.editText4);
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        String itemNameS = field1.getText().toString();
        String quantity = field2.getText().toString();
        String cost = field3.getText().toString();
        String desc1 = field4.getText().toString();
        boolean frozen = toggleButton.isPressed();
        Item newItem = new Item(itemNameS, desc1,cost,frozen,quantity);
        //listOfItems.add(newItem);
        itemViewModel.insert(newItem);
    }

    public void clearItems(){
        EditText field1 = findViewById(R.id.editText);
        EditText field2 = findViewById(R.id.editText2);
        EditText field3 = findViewById(R.id.editText3);
        EditText field4 = findViewById(R.id.editText4);
        field1.setText("");
        field2.setText("");
        field3.setText("");
        field4.setText("");
        sharedPreferences.edit().clear().apply();
        //listOfItems.clear();
        itemViewModel.deleteAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("warehousePrefs", MODE_PRIVATE);

        setContentView(R.layout.activity_main);




        Button addItemBt = findViewById(R.id.button);
        addItemBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        final Button clearBt = findViewById(R.id.button2);
        clearBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearItems();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        BroadCastReceiverSM myBroadCastReceiver = new BroadCastReceiverSM();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));



        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.clear_itemsN){
                    clearItems();
                }
                if(item.getItemId() == R.id.add_itemN){
                    addItem();
                }

                if(item.getItemId() == R.id.listAllItems){
                    Intent goToItemListAct = new Intent(MainActivity.this, ItemListActivity.class);
                    MainActivity.this.startActivity(goToItemListAct);
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Item has been added successfully.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addItem(v);

            }
        });
        fab.bringToFront();

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);


        View activity = findViewById(R.id.mainactivity);

        activity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventType = event.getActionMasked();
                int dx =0, dy=0;
                if (eventType == MotionEvent.ACTION_DOWN){ // when finger touches the screen
                    xLoc = (int)event.getX();
                    yLoc = (int)event.getY();
                } else if (eventType == MotionEvent.ACTION_UP){ // when finger stops touching the screen
                    dx = (int)event.getX() - xLoc;//diff between 2 values
                    dy = (int)event.getY() - yLoc;
                }
                if ( (dy >= -50 && dy <= 50) && dx <= -200){ //swipe left
                    clearItems();
                } else if ((dy >= -50 && dy <= 50) && dx >= 200){ // swipe right
                    addItem();
                }

                return true;
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu,menu);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.bringToFront();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_item){
            addItem();
        }
        if (id == R.id.clear_items){
            clearItems();
        }
        return super.onOptionsItemSelected(item);
    }



    protected void onPause() {

        super.onPause();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        EditText field1 = findViewById(R.id.editText);
        EditText field2 = findViewById(R.id.editText2);
        EditText field3 = findViewById(R.id.editText3);
        EditText field4 = findViewById(R.id.editText4);
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        edit.putString("itemName", field1.getText().toString());
        edit.putString("quantity",field2.getText().toString());
        edit.putString("cost", field3.getText().toString());
        edit.putString("description", field4.getText().toString());
        edit.putBoolean("frozen", toggleButton.isPressed());
        edit.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("warehousePrefs", MODE_PRIVATE);

        String itemname = sharedPreferences.getString("itemName", "");
        String quantity = sharedPreferences.getString("quantity", "");
        String cost = sharedPreferences.getString("cost", "");
        String description = sharedPreferences.getString("description", "");
        Boolean frozenItem = sharedPreferences.getBoolean("frozen", false);

        EditText field1 = findViewById(R.id.editText);
        EditText field2 = findViewById(R.id.editText2);
        EditText field3 = findViewById(R.id.editText3);
        EditText field4 = findViewById(R.id.editText4);
        field1.setText(itemname);
        field2.setText(quantity);
        field3.setText(cost);

        field4.setText(description);
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setChecked(frozenItem);

    }

    public void addItem(View view) {
        addItem();
    }


    class BroadCastReceiverSM extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //this receives the intent from the sms receiver
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            StringTokenizer sT = new StringTokenizer(msg, ";");
            String itemName = sT.nextToken();
            String itemQuantity = sT.nextToken();
            String itemCost = sT.nextToken();
            String itemDescription = sT.nextToken();
            String buttonToggle = sT.nextToken();

            //fields updated here
            EditText field1 = findViewById(R.id.editText);
            EditText field2 = findViewById(R.id.editText2);
            EditText field3 = findViewById(R.id.editText3);
            EditText field4 = findViewById(R.id.editText4);
            field1.setText(itemName);
            field2.setText(itemQuantity);
            field3.setText(itemCost);
            field4.setText(itemDescription);
            ToggleButton toggleButton = findViewById(R.id.toggleButton);
            toggleButton.setChecked(Boolean.parseBoolean(buttonToggle));
        }
    }
}

