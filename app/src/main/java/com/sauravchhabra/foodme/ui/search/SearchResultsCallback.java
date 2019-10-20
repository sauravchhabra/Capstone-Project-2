package com.sauravchhabra.foodme.ui.search;

import android.widget.ImageView;

import com.sauravchhabra.foodme.model.restaurant.search.Restaurant;

public interface SearchResultsCallback {
    void onRestaurantClick(Restaurant restaurant, ImageView sharedElement);

    void onRestaurantMarkerClick(Restaurant restaurant);
}
