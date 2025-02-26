package com.example.androidproject_tamara_hen.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.androidproject_tamara_hen.R;
import com.google.firebase.functions.FirebaseFunctions;


public class HomePage extends Fragment {

    private Button buttonRegister, buttonLogin;
    private ImageButton supportBtn;
    private FirebaseFunctions mFunctions;
    private LottieAnimationView lottieAnimationView1, lottieAnimationView2;

    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        Log.d("DEBUG", "View Hierarchy: " + view);
        buttonRegister = view.findViewById(R.id.btnRegisterHomePage);
        buttonLogin = view.findViewById(R.id.LoginPage);
        supportBtn = view.findViewById(R.id.customerSupportButton);
        lottieAnimationView1 = view.findViewById(R.id.lottieAnimation);
        lottieAnimationView2 = view.findViewById(R.id.lottieAnimationgold);

        lottieAnimationView1.playAnimation();
        lottieAnimationView2.playAnimation();

        if (buttonRegister != null) {
            buttonRegister.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_homePage_to_registrationPage)
            );
        }

        if (buttonLogin != null) {
            buttonLogin.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_homePage_to_loginPage)
            );
        }

        supportBtn.setOnClickListener(v ->
                        Navigation.findNavController(v).navigate(R.id.action_global_customerSupport)
        );

        return view;
    }


}
