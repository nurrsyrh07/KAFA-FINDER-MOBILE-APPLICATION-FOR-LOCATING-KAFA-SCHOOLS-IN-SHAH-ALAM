package com.example.kafafinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private Context context;
    private List<Feedback> feedbackList;

    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, feedbackView;
        RatingBar ratingDisplay;

        EditText editName, editFeedback;
        RatingBar editRating;

        Button editButton, saveButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Display views
            nameView = itemView.findViewById(R.id.nameView);
            feedbackView = itemView.findViewById(R.id.feedbackView);
            ratingDisplay = itemView.findViewById(R.id.ratingDisplay);

            // Editable views
            editName = itemView.findViewById(R.id.editNameField);
            editFeedback = itemView.findViewById(R.id.editFeedbackField);
            editRating = itemView.findViewById(R.id.editRatingBar);

            // Buttons
            editButton = itemView.findViewById(R.id.editButton);
            saveButton = itemView.findViewById(R.id.saveButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);

        // Set display values
        holder.nameView.setText(feedback.name);
        holder.feedbackView.setText(feedback.text);
        holder.ratingDisplay.setRating(feedback.rating);

        // Set editable values
        holder.editName.setText(feedback.name);
        holder.editFeedback.setText(feedback.text);
        holder.editRating.setRating(feedback.rating);

        // Hide edit fields initially
        holder.editName.setVisibility(View.GONE);
        holder.editFeedback.setVisibility(View.GONE);
        holder.editRating.setVisibility(View.GONE);
        holder.saveButton.setVisibility(View.GONE);

        // Handle Edit button
        holder.editButton.setOnClickListener(v -> {
            // Hide view mode
            holder.nameView.setVisibility(View.GONE);
            holder.feedbackView.setVisibility(View.GONE);
            holder.ratingDisplay.setVisibility(View.GONE);

            // Show edit mode
            holder.editName.setVisibility(View.VISIBLE);
            holder.editFeedback.setVisibility(View.VISIBLE);
            holder.editRating.setVisibility(View.VISIBLE);
            holder.saveButton.setVisibility(View.VISIBLE);
            holder.editButton.setVisibility(View.GONE);
        });

        // Handle Save button
        holder.saveButton.setOnClickListener(v -> {
            String newName = holder.editName.getText().toString();
            String newText = holder.editFeedback.getText().toString();
            float newRating = holder.editRating.getRating();

            Feedback updated = new Feedback(feedback.id, newName, newText, newRating);

            FirebaseDatabase.getInstance().getReference("Feedbacks")
                    .child(feedback.id)
                    .setValue(updated)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Feedback updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
                    });

            // Return to display mode
            holder.nameView.setText(newName);
            holder.feedbackView.setText(newText);
            holder.ratingDisplay.setRating(newRating);

            holder.nameView.setVisibility(View.VISIBLE);
            holder.feedbackView.setVisibility(View.VISIBLE);
            holder.ratingDisplay.setVisibility(View.VISIBLE);

            holder.editName.setVisibility(View.GONE);
            holder.editFeedback.setVisibility(View.GONE);
            holder.editRating.setVisibility(View.GONE);
            holder.saveButton.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.VISIBLE);
        });

        // Handle Delete button
        holder.deleteButton.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("Feedbacks")
                    .child(feedback.id)
                    .removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Feedback deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }
}



