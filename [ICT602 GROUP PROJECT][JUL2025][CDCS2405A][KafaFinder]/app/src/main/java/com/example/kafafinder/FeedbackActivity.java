package com.example.kafafinder;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    EditText nameInput, feedbackInput;
    RatingBar ratingBar;
    Button submitButton;
    RecyclerView recyclerView;

    List<Feedback> feedbackList;
    FeedbackAdapter adapter;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize Views
        nameInput = findViewById(R.id.nameInput);
        feedbackInput = findViewById(R.id.feedbackInput);
        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submitButton);
        recyclerView = findViewById(R.id.recyclerView);

        // Firebase Database
        dbRef = FirebaseDatabase.getInstance().getReference("reviews");

        // RecyclerView Setup
        feedbackList = new ArrayList<>();
        adapter = new FeedbackAdapter(this, feedbackList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Submit Button Logic
        submitButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String text = feedbackInput.getText().toString().trim();
            float rating = ratingBar.getRating();

            if (name.isEmpty() || text.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = dbRef.push().getKey();
            Feedback fb = new Feedback(id, name, text, rating);

            dbRef.child(id).setValue(fb)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Feedback submitted", Toast.LENGTH_SHORT).show();

                        // Clear fields
                        nameInput.setText("");
                        feedbackInput.setText("");
                        ratingBar.setRating(0f);

                        // Scroll to latest
                        recyclerView.post(() -> recyclerView.smoothScrollToPosition(feedbackList.size() - 1));
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        // Realtime Data Load
        dbRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedbackList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Feedback f = snap.getValue(Feedback.class);
                    feedbackList.add(f);
                }
                adapter.notifyDataSetChanged();
            }

            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FeedbackActivity.this, "Failed to load feedback", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

