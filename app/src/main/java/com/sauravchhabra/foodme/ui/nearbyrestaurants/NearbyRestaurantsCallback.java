package com.sauravchhabra.foodme.ui.nearbyrestaurants;

import android.widget.ImageView;

import com.sauravchhabra.foodme.model.geocode.NearbyRestaurant;

public interface NearbyRestaurantsCallback {
    void onNearbyRestaurantClick(NearbyRestaurant restaurant, ImageView sharedElement);

    void onNearbyRestaurantMarkerClick(NearbyRestaurant restaurant);
}
