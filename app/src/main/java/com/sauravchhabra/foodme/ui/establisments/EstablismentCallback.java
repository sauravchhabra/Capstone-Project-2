package com.sauravchhabra.foodme.ui.establisments;

import android.widget.ImageView;

import com.sauravchhabra.foodme.model.establisments.Establishment;
import com.sauravchhabra.foodme.model.restaurant.search.Restaurant;

public interface EstablismentCallback {

    interface TypesCalback {
        void onEstablismentTypesClick(Establishment establishment);
    }

    interface RestaurantCallback {
        void onEstablismentClick(Restaurant restaurant, ImageView sharedElement);

        void onEstablismentMarkerClick(Restaurant restaurant);
    }
}
