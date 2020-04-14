package edu.neu.foodiefriend;

import android.content.Context;
import android.view.LayoutInflater;
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

import java.util.List;

import edu.neu.foodiefriend.models.YelpRestaurant;


public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {
    private int restaurantLayout;
    private Context context;
    private List<YelpRestaurant> restaurantList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    RestaurantsAdapter(int layoutId, Context context) {
        this.restaurantLayout = layoutId;
        this.context = context;

    }

    public void setRestaurants(List<YelpRestaurant> restaurants) {
        restaurantList = restaurants;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(restaurantLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view, mListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantsAdapter.ViewHolder holder, int position) {
        YelpRestaurant restaurant = restaurantList.get(position);
        holder.bind(restaurant, this.context);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rName;
        RatingBar rating;
        TextView reviews;
        TextView rAddress;
        TextView foodCategory;
        TextView distance;
        TextView price;
        ImageView imageView;
        CheckedTextView checkedTextView;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            rName = itemView.findViewById(R.id.tvName);
            rating = itemView.findViewById(R.id.ratingBar);
            reviews = itemView.findViewById(R.id.tvReviews);
            rAddress = itemView.findViewById(R.id.restaurantAddr);
            foodCategory = itemView.findViewById(R.id.foodCategory);
            distance = itemView.findViewById(R.id.restaurantDistan);
            price = itemView.findViewById(R.id.priceRange);
            imageView = itemView.findViewById(R.id.imageView);
            checkedTextView = itemView.findViewById(R.id.checkedTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                            if (!checkedTextView.isChecked()) {
                                checkedTextView.setChecked(true);
                            } else {
                                checkedTextView.setChecked(false);
                            }
                        }
                    }
                }
            });
        }

        void bind(YelpRestaurant restaurant, Context context) {
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
    }

    @Override
    public int getItemCount() {
        return restaurantList == null ? 0 : restaurantList.size();
    }

    List<YelpRestaurant> getRestaurantList() {
        return this.restaurantList;
    }
}