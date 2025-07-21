package com.example.kafafinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import de.hdodenhof.circleimageview.CircleImageView;



import java.io.IOException;

public class UserProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    Button btnSelectImage;
    TextView tvUserName, tvUserEmail;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    ActivityResultLauncher<Intent> imagePickerLauncher;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // View IDs
        profileImage = findViewById(R.id.profileImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);

        Toolbar topToolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(topToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        // Dapatkan nama dari Intent
        String nameFromIntent = getIntent().getStringExtra("full_name");
        if (nameFromIntent != null && !nameFromIntent.isEmpty()) {
            tvUserName.setText(nameFromIntent);
        } else {
            tvUserName.setText("No Name Found");
        }

        // Dapatkan email dari Firebase Auth
        String email = firebaseUser.getEmail();
        tvUserEmail.setText(email);

        // Select Image dari galeri
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            profileImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

//        btnLogout.setOnClickListener(v -> {
//            mAuth.signOut();
//            startActivity(new Intent(UserProfileActivity.this, SignInActivity.class));
//            finish();
//        });
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
            Intent aboutIntent = new Intent(UserProfileActivity.this, UserProfileActivity.class);
            startActivity(aboutIntent);
            return true;
        }else if (id == R.id.menu_logout) {
            mAuth.signOut();
            Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}