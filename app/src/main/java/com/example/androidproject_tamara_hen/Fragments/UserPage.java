package com.example.androidproject_tamara_hen.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject_tamara_hen.R;

import com.example.androidproject_tamara_hen.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import Ui.Cart;
import Ui.Item;
import Ui.ItemAdapter;
import Ui.User;


public class UserPage extends Fragment {

    UserViewModel viewModel;
    private DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String taskResult;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ItemAdapter adapter;
    private Cart cart;
    private ImageButton btnPurchase,btnCustumerSupport;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserPage() {
        // Required empty public constructor
    }


    public static UserPage newInstance(String param1, String param2) {
        UserPage fragment = new UserPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_page, container, false);
        btnPurchase = view.findViewById(R.id.ibAddItem);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
         Navigation.findNavController(v).navigate(R.id.action_userPage_to_purchase);
           }
          }
        );
        btnCustumerSupport=view.findViewById(R.id.customerSupportButton);
        btnCustumerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_userPage_to_customerSupport);
            }
        }
        );

        ArrayList<Item> dataSet = new ArrayList<>();
        recyclerView = view.findViewById(R.id.resView);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        TextView tvName = view.findViewById(R.id.tvUserName);

        viewModel.getUserEmailLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String email) {
                if (email != null) {
                    // Fetch user name from Firebase when email is available
                    mDatabase.child("users").child(email.replace('.', '_')).get()
                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    } else {
                                        viewModel.setUser(task.getResult().getValue(User.class));
                                        tvName.setText(viewModel.getUser().getValue().getName() != null ? viewModel.getUser().getValue().getName() : "No name found");
                                    }
                                }
                            });
                } else {
                    tvName.setText("No email found");
                }
            }
        });

        if (null != getArguments())
            mDatabase.child("carts").child(getArguments().getString("email").replace('.', '_')).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        cart = task.getResult().getValue(Cart.class);
                        //        // Populate the dataSet
                        for (int i = 0; i < cart.getItems().size(); i++) {
                            dataSet.add(new Item(
                                    myData.nameArray[i],
                                    cart.getQuantity((myData.nameArray[i])),
                                    myData.drawableArray[i],
                                    myData.id_[i]
                            ));
                        }
                    }
                    recyclerView.setAdapter(adapter);
                }
            });

        // Set up the adapter
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
                mDatabase.child("carts").child(getArguments().getString("email").replace('.', '_')).child("items").child(tvItemName.getText().toString()).setValue(counter + 1);
                tvItemCounter.setText(String.valueOf(counter + 1));
            }

            @Override
            public void onRemoveButtonClick(View view, int position) {
                //Toast.makeText(requireContext(), "Decrease to number of items", Toast.LENGTH_SHORT).show();
                TextView tvItemCounter = view.findViewById(R.id.tvItemCounter);
                TextView tvItemName = view.findViewById(R.id.tvName);
                int counter = Integer.parseInt(tvItemCounter.getText().toString());
                if (counter > 0) {
                    mDatabase.child("carts").child(getArguments().getString("email").replace('.', '_')).child("items").child(tvItemName.getText().toString()).setValue(counter - 1);
                    tvItemCounter.setText(String.valueOf(counter - 1));
                }
            }
        });
//        recyclerView.setAdapter(adapter);

        // Return the modified view
        return view;
    }

    private void dbListener() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("loadPost:onCancelled", databaseError.toException().toString());
            }
        };
        DatabaseReference myRef = database.getReference("users").child(getArguments().getString("email").replace('.', '_'));
        myRef.addValueEventListener(userListener);
    }
}