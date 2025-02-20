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

import com.example.androidproject_tamara_hen.R;

import java.util.concurrent.Executors;

import javax.mail.MessagingException;

import Ui.MailApi;

public class CustomerSupport extends Fragment {

    private EditText emailInput, subjectInput, contentInput;
    private Button submitButton;
    private ImageButton homeBtn;

    private static final String COMPANY_EMAIL = "merchandisingverse@gmail.com";
    private static final String COMPANY_EMAIL_PASSWORD = "87654321@";

    public CustomerSupport() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_support, container, false);

        emailInput = view.findViewById(R.id.emailInput);
        subjectInput = view.findViewById(R.id.subjectInput);
        contentInput = view.findViewById(R.id.contentInput);
        submitButton = view.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> sendEmail());
        homeBtn = view.findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_customerSupport_to_homePage);
            }
        }
        );

        return view;
    }

    private void sendEmail() {
        String userEmail = emailInput.getText().toString().trim();
        String subject = subjectInput.getText().toString().trim();
        String message = contentInput.getText().toString().trim();

        if (userEmail.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format message to include user email at the bottom
        String formattedMessage = message + "\n\n---\nUser Email: " + userEmail;

        // Run email sending in a background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                MailApi sender = new MailApi(COMPANY_EMAIL, COMPANY_EMAIL_PASSWORD, COMPANY_EMAIL, subject, formattedMessage);
                sender.sendEmail();

                // Notify success on UI thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Email sent successfully!", Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (MessagingException e) {
                e.printStackTrace();

                // Notify failure on UI thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to send email", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
