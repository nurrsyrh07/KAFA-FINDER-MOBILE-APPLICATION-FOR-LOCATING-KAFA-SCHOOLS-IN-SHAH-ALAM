package com.example.kafafinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;

public class DeveloperActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer); // make sure your XML file is named activity_developer.xml
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // hides default title
    }

    // Optional: Inflate top menu (if you use navigation like Home/About Us)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    // Optional: Handle top menu navigation
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
        }else if (id == R.id.menu_UserProfile) {
                Intent aboutIntent = new Intent(DeveloperActivity.this, UserProfileActivity.class);
                startActivity(aboutIntent);
                return true;
        }else if (id == R.id.menu_logout) {
            mAuth.signOut();
            Intent intent = new Intent(DeveloperActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
