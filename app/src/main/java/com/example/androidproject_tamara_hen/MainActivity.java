package com.example.androidproject_tamara_hen;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import Ui.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View v)
    {
        String email = ((EditText) findViewById(R.id.EmailLogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordLogin)).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this , "login succeeded" , Toast.LENGTH_LONG).show();
                            Navigation.findNavController(v).navigate(R.id.action_mainScreenFragment_to_calenderFragment);
                        } else
                        {
                            Toast.makeText(MainActivity.this , "login failed" , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void register(View v)
    {
        String email = ((EditText) findViewById(R.id.EmailRegistration)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordRegistration)).getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            addDataToFirebase();
                            Toast.makeText(MainActivity.this , "register succeeded" , Toast.LENGTH_LONG).show();
                            Navigation.findNavController(v).navigate(R.id.action_registrationFragment_to_mainScreenFragment);

                        } else
                        {
                            Toast.makeText(MainActivity.this , "register failed" , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void addDataToFirebase()
    {
        String email = ((EditText) findViewById(R.id.EmailRegistration)).getText().toString();
        String phone = ((EditText) findViewById(R.id.PasswordRegistration)).getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(phone);

        User u = new User(email, phone);

        myRef.setValue(u);
    }
}