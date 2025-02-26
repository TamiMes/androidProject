package com.example.androidproject_tamara_hen.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import com.example.androidproject_tamara_hen.R;
import com.example.androidproject_tamara_hen.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Objects;

import Ui.User;


public class UserPersonalInfo extends Fragment {
    UserViewModel viewModel;
    ImageButton btnHome;
    EditText etName, etId, etCardNum, etCardCVV, etPhone, etAdress;
    private DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Button btnUpdate;

    public UserPersonalInfo() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_user_personal_info, container, false);
        btnHome = view.findViewById(R.id.ibnHome);
        etName = view.findViewById(R.id.etUserName);
        etAdress = view.findViewById(R.id.etAdress);
        etPhone = view.findViewById(R.id.etPhone);
        etCardNum = view.findViewById(R.id.etCardNumber);
        etCardCVV = view.findViewById(R.id.etCardCVV);
        etId = view.findViewById(R.id.etID);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        mDatabase.child("users").child(viewModel.getUserEmailLiveData().getValue().replace('.', '_')).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                viewModel.setUser(task.getResult().getValue(User.class));
            }
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                etName.setText(Objects.requireNonNull(viewModel.getUser().getValue()).getName());
                etCardNum.setText(Objects.requireNonNull(viewModel.getUser().getValue()).getCardNumber());
                etCardCVV.setText(Objects.requireNonNull(viewModel.getUser().getValue()).getCvv());
                etId.setText(Objects.requireNonNull(viewModel.getUser().getValue()).getId());
                etPhone.setText(Objects.requireNonNull(viewModel.getUser().getValue()).getPhone());
                etAdress.setText(Objects.requireNonNull(viewModel.getUser().getValue()).getAdress());
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_global_homePage);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users").child(viewModel.getUserEmailLiveData().getValue().replace('.', '_')).setValue(new User(
                        etName.getText().toString(),
                        etPhone.getText().toString(),
                        etCardNum.getText().toString(),
                        etCardCVV.getText().toString(),
                        etId.getText().toString(),
                        etAdress.getText().toString()
                )).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Navigation.findNavController(view).navigate(R.id.action_global_homePage);
                    }
                });
            }
        });
        return view;
    }
}