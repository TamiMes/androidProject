package com.example.androidproject_tamara_hen.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidproject_tamara_hen.R;
import com.example.androidproject_tamara_hen.data.myData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Ui.Cart;
import Ui.Item;
import Ui.ItemAdapter;

public class myCart extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ArrayList<Item> dataSet;
    private Button btnClearCart,btnPurchase;
    private TextView totalAmount;
    private LinearLayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private Cart cart;

    private myData myData = new myData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cart = new Cart();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (userEmail != null) {
            String safeEmailKey = userEmail.replace('.', '_');
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("carts")
                    .child(safeEmailKey)
                    .child("items");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_cart, container, false);

        recyclerView = view.findViewById(R.id.resViewCart);
        totalAmount = view.findViewById(R.id.totalAmount);
        //clearCartButton = view.findViewById(R.id.clearCartButton);

        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        dataSet = new ArrayList<>();
        adapter = new ItemAdapter(dataSet);
        recyclerView.setAdapter(adapter);
        btnPurchase = view.findViewById(R.id.checkout);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_myCart_to_purchase);
            }
        }
        );

        fetchCartData();

        //clearCartButton.setOnClickListener(v -> clearCart());

        return view;
    }

    private void fetchCartData() {
        if (databaseReference == null) return;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSet.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String itemName = itemSnapshot.getKey();
                    Integer quantity = itemSnapshot.getValue(Integer.class);

                    if (quantity != null && quantity > 0) {
                        int index = getItemIndexByName(itemName);
                        if (index != -1) {
                            dataSet.add(new Item(
                                    myData.nameArray[index],
                                    quantity,
                                    myData.drawableArray[index],
                                    myData.id_[index],
                                    myData.versionArray[index],
                                    (int) myData.price[index]
                            ));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                updateTotalAmount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    private int getItemIndexByName(String itemName) {
        for (int i = 0; i < myData.nameArray.length; i++) {
            if (myData.nameArray[i].equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    private void updateTotalAmount() {
        int total = 0;
        for (Item item : dataSet) {
            total += item.getAmount() * item.getPrice();
        }
        totalAmount.setText("Total: $" + total);
    }

    private void clearCart() {
        if (databaseReference == null) return;

        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dataSet.clear();
                adapter.notifyDataSetChanged();
                totalAmount.setText("Total: $0");


            }
        });
    }
}
