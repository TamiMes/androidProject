package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.androidproject_tamara_hen.Activities.MainActivity;
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_registration_page, container, false);

        /*Button button1 = view.findViewById(R.id.RegisterButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_registrationFragment_to_mainScreenFragment);
            }
        });*/

        EditText emailRegistrationText = view.findViewById(R.id.etEmailRegistration);
        String registerEmailStr = emailRegistrationText.getText().toString();

        EditText passwordRegistrationText = view.findViewById(R.id.etPasswordRegistration);
        String registerPasswordStr = passwordRegistrationText.getText().toString();

        EditText reEnterPasswordRegistrationText = view.findViewById(R.id.reEnterPasswordTextBox);
        String registerReEnterPasswordStr = reEnterPasswordRegistrationText.getText().toString();

        EditText phoneNumberRegistrationText = view.findViewById(R.id.etPhoneNumber);
        String registerPhoneNumberStr = phoneNumberRegistrationText.getText().toString();

        EditText NameRegistrationText = view.findViewById(R.id.etNameRegistration);
        String nameText = NameRegistrationText.getText().toString();

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