package com.example.androidproject_tamara_hen.Fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.androidproject_tamara_hen.data.myData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

import Ui.Cart;
import Ui.Item;
import Ui.ItemAdapter;
import Ui.Rating;
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
    private Rating rating;
    private ImageButton btnMyCart, btnCustumerSupport, btnPersonal, ibFavoritePage, homeBtn;
    private ArrayList<Item> dataSet;
    EditText editText;
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
        editText = view.findViewById(R.id.editText);
        btnMyCart = view.findViewById(R.id.ibMyCart);
        ibFavoritePage = view.findViewById(R.id.ibFavorites);
        btnMyCart.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             Navigation.findNavController(v).navigate(R.id.action_userPage_to_myCart);
                                         }
                                     }
        );
        btnCustumerSupport = view.findViewById(R.id.customerSupportButton);
        btnCustumerSupport.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Navigation.findNavController(v).navigate(R.id.action_global_customerSupport);
//
                                                  }
                                              }
        );
        homeBtn = view.findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Navigation.findNavController(v).navigate(R.id.action_global_homePage);
                                       }
                                   }
        );
        btnPersonal = view.findViewById(R.id.ibToPersonalInfo);
        btnPersonal.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Navigation.findNavController(v).navigate(R.id.action_userPage_to_userPersonalInfo);
                                           }
                                       }
        );

        ibFavoritePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_userPage_to_wishlist);
            }
        });

        dataSet = new ArrayList<>();
        recyclerView = view.findViewById(R.id.resView);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        TextView tvName = view.findViewById(R.id.tvUserName);

        viewModel.getUserEmailLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String email) {
                if (email != null) {
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

                mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            cart = task.getResult().child("carts").child(email.replace('.', '_')).getValue(Cart.class);

                            DataSnapshot ratingsSnapshot = task.getResult().child("Ratings");
                            Map<String, Map<String, Float>> ratingsMap = (Map<String, Map<String, Float>>) ratingsSnapshot.getValue();

                            for (int i = 0; i < myData.nameArray.length; i++) {
                                dataSet.add(new Item(
                                        myData.nameArray[i],
                                        cart.getQuantity((myData.nameArray[i])),
                                        myData.drawableArray[i],
                                        myData.id_[i],
                                        myData.versionArray[i],
                                        myData.price[i],
                                        cart.getFavorite(myData.nameArray[i]),
                                        avrageRating(ratingsMap, myData.nameArray[i])
                                ));
                            }
                        }
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });

        // Set up the adapter
        adapter = new ItemAdapter(dataSet, new ItemAdapter.RecyclerViewListener() {
            @Override
            public void onClick(View view, int position) {
                Item clickedItem = dataSet.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedItem", clickedItem);

                Navigation.findNavController(view).navigate(R.id.action_userPage_to_ratingLayout, bundle);

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
                mDatabase.child("carts").child(viewModel.getUserEmailLiveData().getValue().replace('.', '_')).child("items").child(tvItemName.getText().toString()).setValue(counter + 1);
                tvItemCounter.setText(String.valueOf(counter + 1));
            }

            @Override
            public void onRemoveButtonClick(View view, int position) {
                TextView tvItemCounter = view.findViewById(R.id.tvItemCounter);
                TextView tvItemName = view.findViewById(R.id.tvName);
                int counter = Integer.parseInt(tvItemCounter.getText().toString());
                if (counter > 0) {
                    mDatabase.child("carts").child(viewModel.getUserEmailLiveData().getValue().replace('.', '_')).child("items").child(tvItemName.getText().toString()).setValue(counter - 1);
                    tvItemCounter.setText(String.valueOf(counter - 1));
                }
            }

            @Override
            public void onFavoriteButtonClick(View view, int position) {
                dataSet.get(position).setFavorite(!dataSet.get(position).getFavorite());
                mDatabase.child("carts").child(viewModel.getUserEmailLiveData().getValue().replace('.', '_')).child("favorites").child(dataSet.get(position).getName()).setValue(dataSet.get(position).getFavorite());
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onRatingClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.toString().isEmpty())) {
                    filter(charSequence.toString());
                } else {
                    adapter.filterList(dataSet);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return view;
    }

    private void filter(String text) {

        ArrayList<Item> filteredList = new ArrayList<>();


        for (Item item : dataSet) {

            if (item.getName().toLowerCase().contains(text.toLowerCase())) {

                filteredList.add(item);
            }
        }


        if (filteredList.isEmpty()) {

            Toast.makeText(requireContext(), "No character with the entered letters found..", Toast.LENGTH_SHORT).show();
            adapter.filterList(filteredList);
        } else {
            adapter.filterList(filteredList);
        }
    }

    private float avrageRating(Map<String, Map<String, Float>> rating, String itemName) {
        float totalRating = 0f;
        int userCount = 0;
        if (rating == null || rating.get(itemName) == null) {
            return 0f;
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