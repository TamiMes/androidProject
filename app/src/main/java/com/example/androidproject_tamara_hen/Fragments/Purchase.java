package com.example.androidproject_tamara_hen.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.example.androidproject_tamara_hen.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import java.util.HashMap;
import java.util.Map;

public class Purchase extends Fragment {

    // UI Components
    private EditText paymentHolderInput, idInput, cardNumberInput, cvvInput, emailInput;
    private Button purchaseButton;
    private LottieAnimationView lottieAnimationView;

    // Firebase Instances
    private FirebaseFirestore db;
    private FirebaseFunctions mFunctions;

    public Purchase() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        // Initialize Firebase Firestore and Functions
        db = FirebaseFirestore.getInstance();
        mFunctions = FirebaseFunctions.getInstance();

        // Initialize UI components
        paymentHolderInput = view.findViewById(R.id.paymentHolderInput);
        idInput = view.findViewById(R.id.idInput);
        cardNumberInput = view.findViewById(R.id.cardNumberInput);
        cvvInput = view.findViewById(R.id.cvvInput);
        emailInput = view.findViewById(R.id.emailInput1);
        purchaseButton = view.findViewById(R.id.purchaseButton);
        lottieAnimationView = view.findViewById(R.id.lottieAnimation1);

        // Hide animation initially
        lottieAnimationView.setVisibility(View.GONE);

        // Handle purchase button click
        purchaseButton.setOnClickListener(v -> {
            String name = paymentHolderInput.getText().toString().trim();
            String id = idInput.getText().toString().trim();
            String cardNumber = cardNumberInput.getText().toString().trim();
            String cvv = cvvInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();

            sendData(name, id, cardNumber, cvv, email);
        });

        return view;
    }

    private void sendData(String name, String id, String cardNumber, String cvv, String email) {
        // Check for empty fields
        if (name.isEmpty() || id.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show animation while processing
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();

        // Create a data map to save in Firestore
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("paymentHolder", name);
        purchaseData.put("id", id);
        purchaseData.put("cardNumber", cardNumber);
        purchaseData.put("cvv", cvv);
        purchaseData.put("email", email);

        // Store in Firestore
        db.collection("purchases")
                .add(purchaseData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Purchase successful!", Toast.LENGTH_SHORT).show();
                    sendReceiptEmail(name, id, cardNumber, cvv, email);
                    stopAnimation();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    stopAnimation();
                });
    }

    private void sendReceiptEmail(String name, String id, String cardNumber, String cvv, String email) {
        // Prepare the data for the Firebase function
        Map<String, Object> data = new HashMap<>();
        data.put("userEmail", email);
        data.put("paymentHolder", name);
        data.put("id", id);
        data.put("cardNumber", cardNumber);
        data.put("cvv", cvv);

        // Call the Firebase function to send the receipt
        mFunctions.getHttpsCallable("sendReceipt").call(data).addOnCompleteListener(task -> {
                    lottieAnimationView.cancelAnimation();
                    lottieAnimationView.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        HttpsCallableResult result = task.getResult();
                        if (result != null) {
                            Map response = (Map) result.getData();
                            if ((Boolean) response.get("success")) {
                                Log.d("Email", "Receipt sent successfully");
                            } else {
                                Log.e("Email", "Error: " + response.get("message"));
                            }
                        }
                    } else {
                        Log.e("Email", "Function call failed", task.getException());
                    }
                });
    }

    private void stopAnimation() {
        lottieAnimationView.cancelAnimation();
        lottieAnimationView.setVisibility(View.GONE);
    }
}
