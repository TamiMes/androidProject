package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.androidproject_tamara_hen.MainActivity;
import com.example.androidproject_tamara_hen.R;

public class HomePage extends Fragment {

    private Button buttonRegister, buttonLogin;
    private EditText email, password;
    private Bundle bundle;

    public HomePage() {
        // Required empty public constructor
    }

    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the correct layout
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Initialize UI elements safely
        buttonRegister = view.findViewById(R.id.RegisterButton);
        buttonLogin = view.findViewById(R.id.LoginButton);
        email = view.findViewById(R.id.EmailLogin);
        password = view.findViewById(R.id.PasswordLogin);
        bundle = new Bundle();

        // Null check to avoid crashes
        if (buttonRegister != null) {
            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(view).navigate(R.id.action_homePage_to_registrationPage);
                }
            });
        }

        if (buttonLogin != null && email != null && password != null) {
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("email", email.getText().toString());
                    if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                        if (getActivity() != null) {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.login(view);
                        }
                    }
                }
            });
        }

        return view;
    }
}
