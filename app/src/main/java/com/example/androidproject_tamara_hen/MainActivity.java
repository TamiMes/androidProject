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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Ui.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        // Initialize NavController properly
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager() .findFragmentById(R.id.homePage);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController);
        }
    }

    public void login(View v) {
        String email = ((EditText) findViewById(R.id.EmailLogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordLogin)).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login succeeded", Toast.LENGTH_LONG).show();
                            navController.navigate(R.id.action_homePage_to_loginPage);
                        } else {
                            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void register(View v) {
        String email = ((EditText) findViewById(R.id.EmailRegistration)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordRegistration)).getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addDataToFirebase();
                            Toast.makeText(MainActivity.this, "Register succeeded", Toast.LENGTH_LONG).show();
                            navController.navigate(R.id.action_registrationPage_to_homePage);
                        } else {
                            Toast.makeText(MainActivity.this, "Register failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void addDataToFirebase() {
        String email = ((EditText) findViewById(R.id.EmailRegistration)).getText().toString();
        String phone = ((EditText) findViewById(R.id.PasswordRegistration)).getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(phone);

        User u = new User(email, phone);
        myRef.setValue(u);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
