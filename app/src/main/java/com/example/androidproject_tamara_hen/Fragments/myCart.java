package com.example.androidproject_tamara_hen.Fragments;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidproject_tamara_hen.R;
import com.example.androidproject_tamara_hen.data.myData;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Ui.Cart;
import Ui.Item;
import Ui.ItemAdapter;

public class myCart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ArrayList<Item> dataSet;
    private Button clearCartButton;
    private TextView totalAmount;

    private DatabaseReference databaseReference;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_page);

        recyclerView = findViewById(R.id.resViewCart);
       // clearCartButton = findViewById(R.id.clearCartButton);
       // totalAmount = findViewById(R.id.totalAmount);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataSet = new ArrayList<>();
        adapter = new ItemAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        cart = new Cart();
        databaseReference = FirebaseDatabase.getInstance().getReference("cart");

        // Fetch cart data from Firebase
        fetchCartData();

        clearCartButton.setOnClickListener(v -> clearCart());
    }

    private void fetchCartData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //cart.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String itemName = itemSnapshot.getKey();
                    int quantity = itemSnapshot.getValue(Integer.class);

                    if (quantity > 0) {  // Only add items with quantity > 0
                        cart.addItem(itemName, quantity);
                    }
                }

                updateRecyclerView();
                updateTotalAmount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
            }
        });
    }

    private void updateRecyclerView() {
        dataSet.clear();

        myData myData = new myData(); // Assuming MyData contains item details

        for (int i = 0; i < myData.nameArray.length; i++) {
            int quantity = cart.getQuantity(myData.nameArray[i]);

            if (quantity > 0) {  // Ensuring only items with quantity > 0 are added
                dataSet.add(new Item(
                        myData.nameArray[i],
                        cart.getQuantity((myData.nameArray[i])),
                        myData.drawableArray[i],
                        myData.id_[i],
                        myData.versionArray[i],
                        myData.price[i]
                ));
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void updateTotalAmount() {
        int total = 0;
       // for (Item item : dataSet) {
          //  total += item.getAmount() * item.getPrice(); // Assuming each item has a getPrice() method
       // }
        totalAmount.setText("Total: $" + total);
    }

    private void clearCart() {
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dataSet.clear();
                adapter.notifyDataSetChanged();
                totalAmount.setText("Total: $0");
            }
        });
    }
}
