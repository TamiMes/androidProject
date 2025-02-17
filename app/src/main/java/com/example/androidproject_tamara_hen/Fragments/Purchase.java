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
import com.example.androidproject_tamara_hen.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Purchase extends Fragment {

    private EditText paymentHolderInput, idInput, cardNumberInput, cvvInput;
    private Button purchaseButton;
    private FirebaseFirestore db;

    public Purchase() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        paymentHolderInput = view.findViewById(R.id.paymentHolderInput);
        idInput = view.findViewById(R.id.idInput);
        cardNumberInput = view.findViewById(R.id.cardNumberInput);
        cvvInput = view.findViewById(R.id.cvvInput);
        purchaseButton = view.findViewById(R.id.purchaseButton);

        // Handle purchase button click
        purchaseButton.setOnClickListener(v -> sendDataToFirebase());

        return view;
    }

    private void sendDataToFirebase() {
        String name = paymentHolderInput.getText().toString().trim();
        String id = idInput.getText().toString().trim();
        String cardNumber = cardNumberInput.getText().toString().trim();
        String cvv = cvvInput.getText().toString().trim();

        if (name.isEmpty() || id.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a data map
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("paymentHolder", name);
        purchaseData.put("id", id);
        purchaseData.put("cardNumber", cardNumber);
        purchaseData.put("cvv", cvv);

        // Store in Firestore
        db.collection("purchases")
                .add(purchaseData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(getContext(), "Purchase successful!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
