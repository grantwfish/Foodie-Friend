package edu.neu.foodiefriendfinder.models;

public class SelectableRestaurant extends YelpRestaurant {
    private boolean isSelected = false;

    public SelectableRestaurant(YelpRestaurant yelpRestaurant, boolean isSelected) {
        super();
        this.isSelected = isSelected;
    }

    public boolean isSelected() { return isSelected; }

    public void setSelected(boolean selected) { isSelected = selected; }
}
