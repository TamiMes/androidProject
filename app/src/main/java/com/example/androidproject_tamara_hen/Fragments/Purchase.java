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

import com.airbnb.lottie.LottieAnimationView;
import com.example.androidproject_tamara_hen.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class Purchase extends Fragment {

    private EditText paymentHolderInput, idInput, cardNumberInput, cvvInput, emailInput;
    private Button purchaseButton;
    private ImageButton homeBtn, supportBtn;
    private double totalCost;
    private HashMap<String, Integer> items;
    private LottieAnimationView lottieAnimationView1, lottieAnimationView2;
    private FirebaseFirestore db;
    private FirebaseFunctions mFunctions;

    public Purchase() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        db = FirebaseFirestore.getInstance();
        mFunctions = FirebaseFunctions.getInstance();
        // Initialize UI components
        paymentHolderInput = view.findViewById(R.id.paymentHolderInput);
        idInput = view.findViewById(R.id.idInput);
        cardNumberInput = view.findViewById(R.id.cardNumberInput);
        cvvInput = view.findViewById(R.id.cvvInput);
        emailInput = view.findViewById(R.id.emailInput1);
        purchaseButton = view.findViewById(R.id.purchaseButton);
        lottieAnimationView1 = view.findViewById(R.id.lottieAnimation1);
        lottieAnimationView2 = view.findViewById(R.id.lottieAnimationemail);
        supportBtn = view.findViewById(R.id.customerSupportButton);
        homeBtn = view.findViewById(R.id.ibhome);
        stopAnimation1();
        stopAnimation2();


        Bundle bundle = getArguments();
        if (bundle != null) {
            totalCost = bundle.getDouble("totalCost", 0);
            items = (HashMap<String, Integer>) bundle.getSerializable("purchasedItems");
        }

        Button btnConfirmPurchase = view.findViewById(R.id.purchaseButton);
        btnConfirmPurchase.setOnClickListener(v -> processPurchase());
        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_global_customerSupport);
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_global_homePage);
            }
        });
        return view;
    }

    private void processPurchase() {
        String name = paymentHolderInput.getText().toString().trim();
        String id = idInput.getText().toString().trim();
        String cardNumber = cardNumberInput.getText().toString().trim();
        String cvv = cvvInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        sendData(name, id, cardNumber, cvv, email, totalCost, items);
    }

    private void sendData(String name, String id, String cardNumber, String cvv, String email, double totalCost, Map<String, Integer> items) {
        if (name.isEmpty() || id.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        lottieAnimationView1.setVisibility(View.VISIBLE);
        lottieAnimationView1.playAnimation();

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("userEmail", email);
        purchaseData.put("paymentHolder", name);
        purchaseData.put("id", id);
        purchaseData.put("cardNumber", cardNumber);
        purchaseData.put("cvv", cvv);
        purchaseData.put("totalCost", totalCost);
        purchaseData.put("items", items);

        db.collection("purchases")
                .add(purchaseData)
                .addOnSuccessListener(documentReference -> {
                    sendReceiptEmail(name, id, cardNumber, cvv, email, totalCost, items);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    stopAnimation1();
                });
    }

    private void sendReceiptEmail(String name, String id, String cardNumber, String cvv, String email, double totalCost, Map<String, Integer> items) {
        Map<String, Object> data = new HashMap<>();
        data.put("userEmail", email);
        data.put("paymentHolder", name);
        data.put("id", id);
        data.put("cardNumber", cardNumber);
        data.put("cvv", cvv);
        data.put("totalCost", totalCost);
        data.put("items", items);

        mFunctions.getHttpsCallable("sendReceipt").call(data).addOnCompleteListener(task -> {
            lottieAnimationView1.cancelAnimation();
            lottieAnimationView1.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                HttpsCallableResult result = task.getResult();
                if (result != null) {
                    Map response = (Map) result.getData();
                    if ((Boolean) response.get("success")) {
                        lottieAnimationView2.setVisibility(View.VISIBLE);
                        lottieAnimationView2.playAnimation();
                    } else {
                        Log.e("Email", "Error: " + response.get("message"));
                    }
                }
            } else {
                Log.e("Email", "Function call failed", task.getException());
            }
        });
    }


    private void stopAnimation1() {
        lottieAnimationView1.cancelAnimation();
        lottieAnimationView1.setVisibility(View.GONE);
    }

    private void stopAnimation2() {
        lottieAnimationView2.cancelAnimation();
        lottieAnimationView2.setVisibility(View.GONE);
    }
}
