package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.also_food_court_userApp.Models.ModelReviewProduct;
import com.lgt.also_food_court_userApp.R;

import java.util.List;

/**
 * Created by Ranjan on 6/13/2020.
 */
public class AdapterProductReview extends RecyclerView.Adapter<AdapterProductReview.ReviewProductHolder> {
    private List<ModelReviewProduct>reviewList;
    private Context context;

    public AdapterProductReview(List<ModelReviewProduct> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_review_layout,parent,false);
      return new ReviewProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductReview.ReviewProductHolder holder, int position) {


        holder.tv_userName.setText(reviewList.get(position).getUserName());
        holder.tv_reviewTime.setText(reviewList.get(position).getReviewTime());
        holder.tv_customerReview.setText(reviewList.get(position).getCustomerReview());
        holder.RatingBar.setRating(Float.parseFloat(reviewList.get(position).getRatingBar()));

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewProductHolder extends RecyclerView.ViewHolder {
        private TextView tv_userName,tv_reviewTime,tv_customerReview;
        private RatingBar RatingBar;
        public ReviewProductHolder(@NonNull View itemView) {
            super(itemView);
            tv_userName=itemView.findViewById(R.id.tv_userName);
            tv_reviewTime=itemView.findViewById(R.id.tv_reviewTime);
            tv_customerReview=itemView.findViewById(R.id.tv_customerReview);
            RatingBar=itemView.findViewById(R.id.RatingBar);
        }
    }
}
