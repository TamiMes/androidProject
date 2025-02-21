package com.example.androidproject_tamara_hen.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.androidproject_tamara_hen.MainActivity;
import com.example.androidproject_tamara_hen.R;

public class HomePage extends Fragment {

    private Button buttonRegister, buttonLogin;
    private ImageButton supportBtn;
    //private EditText email, password;
    //private Bundle bundle;

    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the correct layout
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        Log.d("DEBUG", "View Hierarchy: " + view);
        // Initialize UI elements
        buttonRegister = view.findViewById(R.id.btnRegisterHomePage);
        buttonLogin = view.findViewById(R.id.LoginPage);

        // Set click listener for registration button
        if (buttonRegister != null) {
            //Toast.makeText(requireContext(),"Register NOW!!!!",Toast.LENGTH_SHORT).show();
            buttonRegister.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_homePage_to_registrationPage)
            );
        }

        // Set click listener for login button
        if (buttonLogin != null) {
            buttonLogin.setOnClickListener(v -> {
                //bundle.putString("email", email.getText().toString());
                //if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
//                    if (getActivity() instanceof MainActivity) {
//                        ((MainActivity) getActivity()).login(v);
//                    }
                //}
                Navigation.findNavController(v).navigate(R.id.action_homePage_to_loginPage);
            });
            //Toast.makeText(requireContext(),"Login NOW!!!!",Toast.LENGTH_SHORT).show();
        }
        supportBtn = view.findViewById(R.id.customerSupportButton);
        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Navigation.findNavController(v).navigate(R.id.action_homePage_to_customerSupport);
                                          }
                                      }
        );

        return view;
    }
}
