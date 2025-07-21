package com.example.kafafinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;

public class AboutUsActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        mAuth = FirebaseAuth.getInstance();

        Toolbar topToolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(topToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(homeIntent);
            return true;
        } else if (id == R.id.menu_about) {
            Intent aboutIntent = new Intent(this, AboutUsActivity.class);
            startActivity(aboutIntent);
            return true;
        } else if (id == R.id.menu_UserProfile) {
            Intent aboutIntent = new Intent(AboutUsActivity.this, UserProfileActivity.class);
            startActivity(aboutIntent);
            return true;
        }else if (id == R.id.menu_logout) {
            mAuth.signOut();
            Intent intent = new Intent(AboutUsActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}