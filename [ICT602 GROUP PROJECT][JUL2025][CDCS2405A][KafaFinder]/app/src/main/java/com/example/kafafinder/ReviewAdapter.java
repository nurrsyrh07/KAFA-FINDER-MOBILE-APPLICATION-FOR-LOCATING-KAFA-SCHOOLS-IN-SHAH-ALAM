package com.example.kafafinder;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewModel> reviewList;

    public ReviewAdapter(List<ReviewModel> reviewList) {
        this.reviewList = reviewList;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView txtFeedback;
        RatingBar ratingBar;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            txtFeedback = itemView.findViewById(R.id.txtFeedback);
            ratingBar = itemView.findViewById(R.id.reviewRatingBar);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewModel model = reviewList.get(position);
        holder.txtFeedback.setText(model.getFeedback());
        holder.ratingBar.setRating(model.getRating());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}


