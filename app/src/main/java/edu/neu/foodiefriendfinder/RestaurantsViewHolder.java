package edu.neu.foodiefriendfinder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import edu.neu.foodiefriendfinder.models.SelectableRestaurant;
import edu.neu.foodiefriendfinder.models.YelpRestaurant;

public class RestaurantsViewHolder extends RecyclerView.ViewHolder {


    OnItemSelectedListener itemSelectedListener;
    CheckedTextView checkedTextView;
    SelectableRestaurant selectableRestaurant;
    static final int MULTI_SELECTION = 2;
    static final int SINGLE_SELECTION = 1;

    TextView rName;
    RatingBar rating;
    TextView reviews;
    TextView rAddress;
    TextView foodCategory;
    TextView distance;
    TextView price;
    ImageView imageView;
    int position = getAdapterPosition();


    public RestaurantsViewHolder(View itemView, OnItemSelectedListener listener) {
        super(itemView);
        itemSelectedListener = listener;

        checkedTextView = itemView.findViewById(R.id.checkedTextView);
        rName = itemView.findViewById(R.id.tvName);
        rating = itemView.findViewById(R.id.ratingBar);
        reviews = itemView.findViewById(R.id.tvReviews);
        rAddress = itemView.findViewById(R.id.restaurantAddr);
        foodCategory = itemView.findViewById(R.id.foodCategory);
        distance = itemView.findViewById(R.id.restaurantDistan);
        price = itemView.findViewById(R.id.priceRange);
        imageView = itemView.findViewById(R.id.imageView);

        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectableRestaurant.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(selectableRestaurant);
            }
        });

    }

    public void setChecked(boolean value) {
        if (value) {
            checkedTextView.setBackgroundColor(Color.LTGRAY);
        } else {
            checkedTextView.setBackground(null);
        }
        selectableRestaurant.setSelected(value);
        checkedTextView.setChecked(value);
    }

    public void bind(YelpRestaurant restaurant, Context context) {
        rName.setText(restaurant.name);
        rating.setRating((float) restaurant.rating);
        reviews.setText(restaurant.reviewCount + " Reviews");
        rAddress.setText(restaurant.location.address1);
        foodCategory.setText(restaurant.categories.get(0).title);
        distance.setText(restaurant.meterToMile());
        price.setText(restaurant.price);
        Glide.with(context).load(restaurant.imageUrl).apply(new RequestOptions().transform(
                new CenterCrop(), new RoundedCorners(20))).into(imageView);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(SelectableRestaurant restaurant);
    }
}
