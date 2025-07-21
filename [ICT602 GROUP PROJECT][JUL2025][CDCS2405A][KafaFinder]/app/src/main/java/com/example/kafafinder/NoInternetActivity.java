package com.example.kafafinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NoInternetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        // Letak dalam sini
        Button retryButton = findViewById(R.id.btnRetry);

        retryButton.setOnClickListener(v -> {
            if (ConnectivityUtil.isConnected(NoInternetActivity.this)) {
                Intent intent = new Intent(NoInternetActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Still no connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}