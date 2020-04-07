package edu.neu.foodiefriendfinder;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.foodiefriendfinder.models.SelectableRestaurant;
import edu.neu.foodiefriendfinder.models.YelpRestaurant;


//public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> implements RestaurantsViewHolder.OnItemSelectedListener {
public class RestaurantsAdapter extends RecyclerView.Adapter implements RestaurantsViewHolder.OnItemSelectedListener {

    private int restaurantLayout;
    private Context context;
    private List<YelpRestaurant> restaurantList;
    private OnItemClickListener mListener;

    private List<SelectableRestaurant> mValues;
    private boolean isMultiSelectionEnabled;
    RestaurantsViewHolder.OnItemSelectedListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

//    public RestaurantsAdapter(int layoutId, Context context) {
//        this.restaurantLayout = layoutId;
//        this.context = context;
//    }

    public RestaurantsAdapter(int layoutId, Context context,
                              List<YelpRestaurant> restaurants, boolean isMultiSelectionEnabled) {
        this.restaurantLayout = layoutId;
        this.context = context;
//        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (YelpRestaurant restaurant : restaurants) {
            mValues.add(new SelectableRestaurant(restaurant, false));
        }
    }

    public void setRestaurants(List<YelpRestaurant> restaurants) {
        restaurantList = restaurants;
        notifyDataSetChanged();
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(
//                parent.getContext()).inflate(restaurantLayout, parent, false);
//        ViewHolder myViewHolder = new ViewHolder(view, mListener);
//        return myViewHolder;
//    }


    @Override
    public RestaurantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantsViewHolder(view, this);
    }

//    @Override
//    public void onBindViewHolder(RestaurantsAdapter.ViewHolder holder, int position) {
//        YelpRestaurant restaurant = restaurantList.get(position);
//        holder.bind(restaurant, this.context, position);
//    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RestaurantsViewHolder restaurantsViewHolder = (RestaurantsViewHolder) holder;
        SelectableRestaurant selectableRestaurant = mValues.get(position);

        if(isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            restaurantsViewHolder.checkedTextView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple,
                    value, true);
            int checkMarkDrawableResId = value.resourceId;
            restaurantsViewHolder.checkedTextView.setCheckMarkDrawable(checkMarkDrawableResId);
        } else {
            TypedValue value = new TypedValue();
            restaurantsViewHolder.checkedTextView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle,
                    value, true);
            int checkMarkDrawableResId = value.resourceId;
            restaurantsViewHolder.checkedTextView.setCheckMarkDrawable(checkMarkDrawableResId);
        }

        restaurantsViewHolder.selectableRestaurant = selectableRestaurant;
        restaurantsViewHolder.setChecked(restaurantsViewHolder.selectableRestaurant.isSelected());

        YelpRestaurant restaurant = restaurantList.get(position);
        restaurantsViewHolder.bind(restaurant, this.context);
    }

//    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TextView rName;
//        RatingBar rating;
//        TextView reviews;
//        TextView rAddress;
//        TextView foodCategory;
//        TextView distance;
//        TextView price;
//        ImageView imageView;
//        CheckedTextView mCheckTextView;

//        SparseBooleanArray indexisChecked;
//        boolean[] checkedItems;
//        int position = getAdapterPosition();

//        public ViewHolder(View itemView, final OnItemClickListener listener) {
//            super(itemView);
//            rName = itemView.findViewById(R.id.tvName);
//            rating = itemView.findViewById(R.id.ratingBar);
//            reviews = itemView.findViewById(R.id.tvReviews);
//            rAddress = itemView.findViewById(R.id.restaurantAddr);
//            foodCategory = itemView.findViewById(R.id.foodCategory);
//            distance = itemView.findViewById(R.id.restaurantDistan);
//            price = itemView.findViewById(R.id.priceRange);
//            imageView = itemView.findViewById(R.id.imageView);
//            mCheckTextView = itemView.findViewById(R.id.checkedTextView);


//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.onItemClick(position);
//                        }
//                    }
//                }
//            });
//        }

//        void bind(YelpRestaurant restaurant, Context context, int position) {
//            rName.setText(restaurant.name);
//            rating.setRating((float) restaurant.rating);
//            reviews.setText(restaurant.reviewCount + " Reviews");
//            rAddress.setText(restaurant.location.address1);
//            foodCategory.setText(restaurant.categories.get(0).title);
//            distance.setText(restaurant.meterToMile());
//            price.setText(restaurant.price);
//            Glide.with(context).load(restaurant.imageUrl).apply(new RequestOptions().transform(
//                    new CenterCrop(), new RoundedCorners(20))).into(imageView);

//            if (!indexisChecked.get(position, false)) {
//                mCheckTextView.setChecked(false);
//            }
//            else {
//                mCheckTextView.setChecked(true);
//            }
//            mCheckTextView.setText(String.valueOf(restaurantList.get(position)));
//        }

//        @Override
//        public void onClick(View v) {
//            if (!indexisChecked.get(position, false)) {
//                mCheckTextView.setChecked(true);
//                indexisChecked.put(position, true);
//            }
//            else {
//                mCheckTextView.setChecked(false);
//                indexisChecked.put(position, false);
//            }
//        }
//    }

    @Override
    public int getItemCount() {
        return restaurantList == null ? 0 : restaurantList.size();
    }

    // Test
    public List<YelpRestaurant> getSelectedRestaurants() {
        List<YelpRestaurant> selectedRestaurants = new ArrayList<>();
        for (SelectableRestaurant restaurant : mValues) {
            if (restaurant.isSelected()) {
                selectedRestaurants.add(restaurant);
            }
        }
        return selectedRestaurants;
    }

    // Test
    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return RestaurantsViewHolder.MULTI_SELECTION;
        }
        else{
            return RestaurantsViewHolder.SINGLE_SELECTION;
        }
    }

    // Test
    public void onItemSelected(SelectableRestaurant restaurant) {
        if (!isMultiSelectionEnabled) {

            for (SelectableRestaurant selectableRestaurant : mValues) {
                if (!selectableRestaurant.equals(restaurant)
                    && selectableRestaurant.isSelected()) {
                    selectableRestaurant.setSelected(false);
                } else if (selectableRestaurant.equals(restaurant)
                    && restaurant.isSelected()) {
                    selectableRestaurant.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(restaurant);
    }
}
