package com.example.androidproject_tamara_hen;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.Navigation;

//import com.example.androidproject_tamara_hen.data.MyData;
import com.example.androidproject_tamara_hen.data.myData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Ui.Cart;
import Ui.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private NavController navController;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mAuth = FirebaseAuth.getInstance();

        // Initialize NavController properly
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.homePage);

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
                            //navController.navigate(R.id.action_homePage_to_loginPage);
                            userViewModel.setEmail(email);
                            Navigation.findNavController(v).navigate(R.id.action_loginPage_to_userPage);
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
                            addData();
                            userViewModel.setEmail(email);
                            Toast.makeText(MainActivity.this, "Register succeeded", Toast.LENGTH_LONG).show();
                            //navController.navigate(R.id.action_registrationPage_to_homePage);
                            Navigation.findNavController(v).navigate(R.id.action_registrationPage_to_userPage);
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void addData(){
        EditText phone = ((EditText) findViewById(R.id.PhoneNumberRegistration));
        EditText email = ((EditText) findViewById(R.id.EmailRegistration));
        EditText name = ((EditText) findViewById(R.id.NameRegistration));
        EditText password = ((EditText) findViewById(R.id.PasswordRegistration));
        //get data from the layout or register (for now)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(email.getText().toString().replace('.','_'));

        User user = new User(name.getText().toString(),email.getText().toString(),password.getText().toString() ,phone.getText().toString());
        myRef.setValue(user);

        myRef = database.getReference("carts").child(email.getText().toString().replace('.','_'));

        Cart cart = new Cart(email.getText().toString());
        for (int i = 0; i < myData.nameArray.length; i++){
            cart.addItem(myData.nameArray[i],0);
        }
        myRef.setValue(cart);
        Log.d("User and Cart created","User and Cart created");
        Toast.makeText(this,"User and Cart created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
