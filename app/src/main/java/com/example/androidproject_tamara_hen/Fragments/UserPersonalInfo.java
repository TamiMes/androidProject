package com.example.androidproject_tamara_hen.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject_tamara_hen.R;
import com.example.androidproject_tamara_hen.UserViewModel;

import java.util.Objects;

import Ui.User;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link UserPersonalInfo#newInstance} factory method to
// * create an instance of this fragment.
// */
public class UserPersonalInfo extends Fragment {
    UserViewModel viewModel;
    ImageButton btnHome;
    TextView tvName;


//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserPersonalInfo() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment UserPersonalInfo.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static UserPersonalInfo newInstance(String param1, String param2) {
//        UserPersonalInfo fragment = new UserPersonalInfo();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Setting the pointers to all elements and inflating the fragment
         View view = inflater.inflate(R.layout.fragment_user_personal_info, container, false);
         btnHome = view.findViewById(R.id.ibnHome);
         tvName = view.findViewById(R.id.tvUserNamePersonalInfoPage);

         viewModel.getUser().observe(getViewLifecycleOwner(),new Observer<User>(){

             @Override
             public void onChanged(User user) {
                 tvName.setText(Objects.requireNonNull(viewModel.getUser().getValue()).getName());
             }
         });
         btnHome.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Navigation.findNavController(view).navigate(R.id.action_userPersonalInfo_to_homePage);
             }
         });


        return view;
    }
}