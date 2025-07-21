package com.example.kafafinder;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // User already signed in
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // User not signed in
            startActivity(new Intent(this, SignInActivity.class));
        }

        finish(); // close this activity so user can't return to it
    }
}
