package com.example.androidproject_tamara_hen.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject_tamara_hen.R;
import com.example.androidproject_tamara_hen.UserViewModel;
import com.example.androidproject_tamara_hen.data.myData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Ui.Cart;
import Ui.Item;
import Ui.ItemAdapter;

public class Wishlist extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ArrayList<Item> dataSet;
    private LinearLayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private UserViewModel viewModel;
    private myData myData = new myData();
    private Cart cart;
    private ImageButton  ibCostumerSupport,ibHome;

    public Wishlist() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        cart = new Cart();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (userEmail != null) {
            String safeEmailKey = userEmail.replace('.', '_');
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("carts")
                    .child(safeEmailKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        recyclerView = view.findViewById(R.id.resViewWish);
        //clearCartButton = view.findViewById(R.id.clearCartButton);

        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        ibHome = view.findViewById(R.id.ibnHome);
        ibHome.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Navigation.findNavController(v).navigate(R.id.action_global_homePage);
                                       }
                                   }
        );
        ibCostumerSupport = view.findViewById(R.id.customerSupportButton);
        ibCostumerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_global_customerSupport);
//
            }
        }
        );
        dataSet = new ArrayList<>();
        adapter = new ItemAdapter(dataSet, new ItemAdapter.RecyclerViewListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public boolean onLongClick(View view, int position) {
                return false;
            }

            @Override
            public void onAddButtonClick(View view, int position) {
                //Toast.makeText(requireContext(), "Add to number of items", Toast.LENGTH_SHORT).show();
                TextView tvItemCounter = view.findViewById(R.id.tvItemCounter);
                TextView tvItemName = view.findViewById(R.id.tvName);
                int counter = Integer.parseInt(tvItemCounter.getText().toString());
                databaseReference.child("items").child(tvItemName.getText().toString()).setValue(counter + 1);
                tvItemCounter.setText(String.valueOf(counter + 1));
                dataSet.set(position, new Item(dataSet.get(position).getName(), dataSet.get(position).getAmount() + 1, dataSet.get(position).getImage(), 0, dataSet.get(position).getDesc(), dataSet.get(position).getPrice(),dataSet.get(position).getFavorite()));
            }

            @Override
            public void onRemoveButtonClick(View view, int position) {
                //Toast.makeText(requireContext(), "Decrease to number of items", Toast.LENGTH_SHORT).show();
                TextView tvItemCounter = view.findViewById(R.id.tvItemCounter);
                TextView tvItemName = view.findViewById(R.id.tvName);
                int counter = Integer.parseInt(tvItemCounter.getText().toString());
                if (counter > 0) {
                    databaseReference.child("items").child(tvItemName.getText().toString()).setValue(counter - 1);
                    tvItemCounter.setText(String.valueOf(counter - 1));
                    dataSet.set(position, new Item(dataSet.get(position).getName(), dataSet.get(position).getAmount() - 1, dataSet.get(position).getImage(), 0, dataSet.get(position).getDesc(), dataSet.get(position).getPrice(),dataSet.get(position).getFavorite()));
                }
            }

            @Override
            public void onFavoriteButtonClick(View view, int position) {
                dataSet.get(position).setFavorite(!dataSet.get(position).getFavorite());
                databaseReference.child("favorites").child(dataSet.get(position).getName()).setValue(dataSet.get(position).getFavorite());
                dataSet.remove(position);
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onRatingClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        fetchCartData();

        return view;
    }

    private void fetchCartData() {
        if (databaseReference == null) return;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSet.clear();
                DataSnapshot itemsSnapshot = snapshot.child("items");
                DataSnapshot favoritesSnapshot = snapshot.child("favorites");

                for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                    String itemName = itemSnapshot.getKey();
                    Integer quantity = itemSnapshot.getValue(Integer.class);
                    Boolean favorite = favoritesSnapshot.child(itemName).getValue(Boolean.class);
                    if (favorite != null && favorite) {
                        int index = getItemIndexByName(itemName);
                        if (index != -1) {
                            dataSet.add(new Item(
                                    myData.nameArray[index],
                                    quantity,
                                    myData.drawableArray[index],
                                    myData.id_[index],
                                    myData.versionArray[index],
                                    myData.price[index],
                                    favorite
                            ));
                            Log.d("Error",myData.nameArray[index]);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
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
}