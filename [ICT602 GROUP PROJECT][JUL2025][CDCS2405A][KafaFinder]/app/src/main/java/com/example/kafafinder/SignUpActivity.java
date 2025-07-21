package com.example.kafafinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText nameSignup, emailSignup, passwordSignup;
    Button btnSignup;
    TextView goToLogin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Link UI components
        nameSignup = findViewById(R.id.nameSignup);
        emailSignup = findViewById(R.id.emailSignup);
        passwordSignup = findViewById(R.id.passwordSignup);
        btnSignup = findViewById(R.id.btnSignup);
        goToLogin = findViewById(R.id.goToLogin);
        mAuth = FirebaseAuth.getInstance();

        // Handle sign up
        btnSignup.setOnClickListener(v -> {
            String name = nameSignup.getText().toString().trim();
            String email = emailSignup.getText().toString().trim();
            String password = passwordSignup.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter name, email, and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();

                            // 👉 Pass full name to UserProfileActivity
                            Intent intent = new Intent(SignUpActivity.this, UserProfileActivity.class);
                            intent.putExtra("full_name", name);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Go to login screen
        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });
    }
}