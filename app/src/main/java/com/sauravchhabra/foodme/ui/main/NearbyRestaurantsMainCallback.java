package com.sauravchhabra.foodme.ui.main;

import android.widget.ImageView;

import com.sauravchhabra.foodme.model.geocode.NearbyRestaurant;

public interface NearbyRestaurantsMainCallback {

    void onMainNearbyRestaurantsClick(NearbyRestaurant nearbyRestaurant, ImageView sharedElement);

    void onMainNearbyRestaurantMarkerClick(NearbyRestaurant nearbyRestaurant);
}
