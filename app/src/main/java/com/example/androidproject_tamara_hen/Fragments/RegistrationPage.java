package com.example.androidproject_tamara_hen.Fragments;

import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton homeBtn,supportBtn;
    public RegistrationPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationPage newInstance(String param1, String param2) {
        RegistrationPage fragment = new RegistrationPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_registration_page, container, false);

        homeBtn = view.findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_global_homePage);
            }
        }
        );
        supportBtn = view.findViewById(R.id.customerSupportButton);
        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_global_customerSupport);

            }
        }
        );
        EditText emailRegistrationText = view.findViewById(R.id.EmailRegistration);
        String registerEmailStr = emailRegistrationText.getText().toString();

        EditText passwordRegistrationText = view.findViewById(R.id.PasswordRegistration);
        String registerPasswordStr = passwordRegistrationText.getText().toString();

        EditText reEnterPasswordRegistrationText = view.findViewById(R.id.reEnterPasswordRegistration);
        String registerReEnterPasswordStr = reEnterPasswordRegistrationText.getText().toString();

        EditText phoneNumberRegistrationText = view.findViewById(R.id.PhoneNumberRegistration);
        String registerPhoneNumberStr = phoneNumberRegistrationText.getText().toString();

        EditText NameRegistrationText = view.findViewById(R.id.NameRegistration);
        String registernameStr = NameRegistrationText.getText().toString();

        Button button = view.findViewById(R.id.RegisterButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!emailRegistrationText.getText().toString().isEmpty()) &&
                        (!passwordRegistrationText.getText().toString().isEmpty()) &&
                        passwordRegistrationText.getText().toString().equals(reEnterPasswordRegistrationText.getText().toString())) {
                    MainActivity mainAct = (MainActivity) getActivity();
                    mainAct.register(v);
                }else {
                    Toast.makeText(requireContext(), "Please fill all fields according to hints!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }
}