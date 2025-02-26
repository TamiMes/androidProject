package com.example.androidproject_tamara_hen.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class myCart extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ArrayList<Item> dataSet;
    private Button btnPurchase;
    private TextView totalAmount;
    private LinearLayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private UserViewModel viewModel;

    private myData myData = new myData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        databaseReference = FirebaseDatabase.getInstance()
                .getReference();

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
                TextView tvItemCounter = view.findViewById(R.id.tvItemCounter);
                TextView tvItemName = view.findViewById(R.id.tvName);
                int counter = Integer.parseInt(tvItemCounter.getText().toString());
                databaseReference.child("carts").child(viewModel.getUserEmailLiveData().getValue().replace('.', '_')).child("items").child(tvItemName.getText().toString()).setValue(counter + 1);
                tvItemCounter.setText(String.valueOf(counter + 1));
                dataSet.set(position, new Item(dataSet.get(position).getName(), dataSet.get(position).getAmount() + 1, dataSet.get(position).getImage(), 0, dataSet.get(position).getDesc(), dataSet.get(position).getPrice(), dataSet.get(position).getFavorite(), dataSet.get(position).getRating()));
                updateTotalAmount();
            }

            @Override
            public void onRemoveButtonClick(View view, int position) {
                TextView tvItemCounter = view.findViewById(R.id.tvItemCounter);
                TextView tvItemName = view.findViewById(R.id.tvName);
                int counter = Integer.parseInt(tvItemCounter.getText().toString());
                if (counter > 0) {
                    databaseReference.child("carts").child(viewModel.getUserEmailLiveData().getValue().replace('.', '_')).child("items").child(tvItemName.getText().toString()).setValue(counter - 1);
                    tvItemCounter.setText(String.valueOf(counter - 1));
                    dataSet.set(position, new Item(dataSet.get(position).getName(), dataSet.get(position).getAmount() - 1, dataSet.get(position).getImage(), 0, dataSet.get(position).getDesc(), dataSet.get(position).getPrice(), dataSet.get(position).getFavorite(), dataSet.get(position).getRating()));
                    updateTotalAmount();
                    if (dataSet.get(position).getAmount() == 0) {
                        dataSet.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                }
            }

            @Override
            public void onFavoriteButtonClick(View view, int position) {
                dataSet.get(position).setFavorite(!dataSet.get(position).getFavorite());
                databaseReference.child("favorites").child(dataSet.get(position).getName()).setValue(dataSet.get(position).getFavorite());
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onRatingClick(View view, int position) {

            }


        });
        recyclerView.setAdapter(adapter);
        btnPurchase = view.findViewById(R.id.checkout);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalCost = 0;
                Map<String, Integer> purchasedItems = new HashMap<>();

                for (Item item : dataSet) {
                    int quantity = item.getAmount();
                    if (quantity > 0) {
                        purchasedItems.put(item.getName(), quantity);
                        totalCost += item.getPrice() * quantity;
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putDouble("totalCost", totalCost);
                bundle.putSerializable("purchasedItems", (HashMap<String, Integer>) purchasedItems);

                Navigation.findNavController(v).navigate(R.id.action_myCart_to_purchase, bundle);
            }
        });
        fetchCartData();
        return view;
    }

    private void fetchCartData() {
        if (databaseReference == null) return;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSet.clear();
                DataSnapshot itemsSnapshot = snapshot.child("carts").child(viewModel.getUserEmailLiveData().getValue().replace('.', '_')).child("items");
                DataSnapshot favoritesSnapshot = snapshot.child("carts").child(viewModel.getUserEmailLiveData().getValue().replace('.', '_')).child("favorites");
                //DataSnapshot ratingSnapshot = snapshot.child("ratings");
                DataSnapshot ratingsSnapshot = snapshot.child("Ratings");
                for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                    String itemName = itemSnapshot.getKey();
                    Integer quantity = itemSnapshot.getValue(Integer.class);
                    Boolean favorite = favoritesSnapshot.child(itemName).getValue(Boolean.class);
                    Map<String, Map<String, Float>> ratingsMap = (Map<String, Map<String, Float>>) ratingsSnapshot.getValue();
//                    float rating = ratingSnapshot.child(itemName).getValue(Float.class);
                    if (quantity != null && quantity > 0) {
                        int index = getItemIndexByName(itemName);
                        if (index != -1) {
                            dataSet.add(new Item(
                                    myData.nameArray[index],
                                    quantity,
                                    myData.drawableArray[index],
                                    myData.id_[index],
                                    myData.versionArray[index],
                                    myData.price[index],
                                    favorite,
                                    avrageRating(ratingsMap, myData.nameArray[index])
                            ));
                            Log.d("Error", myData.nameArray[index]);
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
        double total = 0;
        for (Item item : dataSet) {
            total += item.getAmount() * item.getPrice();
        }
        String formattedTotal = String.format("%.2f", total);
        totalAmount.setText("Total: $" + formattedTotal);
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

    private float avrageRating(Map<String, Map<String, Float>> rating, String itemName) {
        float totalRating = 0f;
        int userCount = 0;
        if (rating == null || rating.get(itemName) == null) {
            Log.e("Ratings", "Rating object or ratings map is null");
            return 0f; // No ratings available
        }
        Map<String, Float> itemRating = rating.get(itemName);
        for (Map.Entry<String, Float> entry : itemRating.entrySet()) {
            Log.d("Rating", String.valueOf(entry.getValue()));
            float ratingValue = ((Number) entry.getValue()).floatValue();
            totalRating += ratingValue;
            ;
            userCount++;
        }

        if (userCount > 0) {
            float averageRating = totalRating / userCount;
            return averageRating;
        } else {
            return 0f;
        }

    }
}
