package com.example.androidproject_tamara_hen.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.androidproject_tamara_hen.MainActivity;
import com.example.androidproject_tamara_hen.R;
import com.example.androidproject_tamara_hen.UserViewModel;
import com.example.androidproject_tamara_hen.data.myData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Ui.Cart;
import Ui.Item;
import Ui.Rating;
import Ui.User;

public class ratingLayout extends Fragment {

    ImageView ivItem;
    RatingBar ratingBar;
    TextView tvItemPrice;
    TextView tvItemCounter;
    TextView tvItemDesc;
    TextView tvName;
    private DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    UserViewModel viewModel;
    Button btnUserPage;

    Rating rating;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rating_layout, container, false);
        ratingBar = view.findViewById(R.id.ratingBar);
        ivItem = view.findViewById(R.id.ivItem);
        tvItemPrice = view.findViewById(R.id.tvItemPrice);
        tvItemCounter = view.findViewById(R.id.tvItemCounter);
        tvItemDesc = view.findViewById(R.id.tvItemDesc);
        tvName = view.findViewById(R.id.tvName);
        btnUserPage = view.findViewById(R.id.btnUserPage);
        Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey("selectedItem")) {
            Item selectedItem = (Item) bundle.getSerializable("selectedItem");
            if (selectedItem != null) {
                tvName.setText(selectedItem.getName());
                tvItemPrice.setText(String.valueOf(selectedItem.getPrice()));
                tvItemDesc.setText(String.valueOf(selectedItem.getDesc()));
                tvItemCounter.setText(String.valueOf(selectedItem.getAmount()));
                ivItem.setImageResource(selectedItem.getImage());
                ratingBar.setRating(selectedItem.getRating());
                mDatabase.child("Ratings").child(selectedItem.getName()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            DataSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                float totalRating = 0;
                                int userCount = 0;

                                for (DataSnapshot userRatingSnapshot : snapshot.getChildren()) {
                                    Float ratingValue = userRatingSnapshot.getValue(Float.class); // Rating

                                    if (ratingValue != null) {
                                        totalRating += ratingValue;
                                        userCount++;
                                    }
                                }

                                if (userCount > 0) {
                                    float averageRating = totalRating / userCount;
                                    ratingBar.setRating(averageRating);
                                } else {
                                    ratingBar.setRating(0);
                                }
                            }
                        }
                    }
                });

            }
        }


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Item selectedItem = (Item) bundle.getSerializable("selectedItem");

                if (selectedItem != null && fromUser) {
                    String itemName = selectedItem.getName(); // Item name
                    String userEmail = viewModel.getUserEmailLiveData().getValue(); // Get logged-in user's email

                    mDatabase.child("Ratings").child(itemName).child(userEmail.replace(".", "_")).setValue(rating)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Firebase", "Rating saved successfully!");
                                    } else {
                                        Log.e("Firebase", "Failed to save rating", task.getException());
                                    }
                                }
                            });
                }
            }
        });

        btnUserPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_ratingLayout_to_userPage);
            }
        });
        return view;
    }
}