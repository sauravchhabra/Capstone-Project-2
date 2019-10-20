package com.sauravchhabra.foodme.ui.favorites;

import android.widget.ImageView;

import com.sauravchhabra.foodme.data.local.entity.CommonRestaurant;

public interface FavoritesCallback {
    void onFavoriteRestaurantClick(CommonRestaurant restaurant, ImageView sharedElement);

    void onFavoriteRestaurantMarkerClick(CommonRestaurant restaurant);
}
