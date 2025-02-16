package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidproject_tamara_hen.ActivitiesAndFragments.Activities.MainActivity;
import com.example.androidproject_tamara_hen.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loginPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loginPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public loginPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static loginPage newInstance(String param1, String param2) {
        loginPage fragment = new loginPage();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
      {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        buttonRegister = view.findViewById(R.id.RegisterButton);
        buttonLogin = view.findViewById(R.id.LoginButton);
        email = view.findViewById(R.id.EmailLogin);
        password = view.findViewById(R.id.PasswordLogin);
        bundle = new Bundle();

        EditText emailLoginText = view.findViewById(R.id.EmailLogin);
        String loginEmailStr = emailLoginText.getText().toString();

        EditText passwordLoginText = view.findViewById(R.id.PasswordLogin);
        String loginPasswordStr = passwordLoginText.getText().toString();

        Button button = view.findViewById(R.id.LoginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainAct = (MainActivity) getActivity();
                mainAct.login(v);
            }
        });

        return view;

    }
}



public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
{
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_home_page, container, false);
    buttonRegister = view.findViewById(R.id.RegisterButton);
    buttonLogin = view.findViewById(R.id.LoginButton);
    email = view.findViewById(R.id.EmailLogin);
    password = view.findViewById(R.id.PasswordLogin);
    bundle = new Bundle();

    buttonRegister.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(view).navigate(R.id.action_homePage_to_registrationPage);
        }
    });

    buttonLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            bundle.putString("email", email.getText().toString());
            if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.login(view);
            }
        }
    });

    return view;
}