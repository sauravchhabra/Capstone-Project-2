package com.sauravchhabra.foodme.di;

import com.sauravchhabra.foodme.ui.detail.RestaurantDetailActivity;
import com.sauravchhabra.foodme.ui.establisments.EstablismentsActivity;
import com.sauravchhabra.foodme.ui.favorites.FavoritesActivity;
import com.sauravchhabra.foodme.ui.main.MainActivity;
import com.sauravchhabra.foodme.ui.nearbyrestaurants.NearbyRestaurantsActivity;
import com.sauravchhabra.foodme.ui.search.SearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract NearbyRestaurantsActivity nearbyRestaurantsActivity();

    @ContributesAndroidInjector(modules = FragmentBuilder.class)
    abstract EstablismentsActivity establismentsActivity();

    @ContributesAndroidInjector(modules = FragmentBuilder.class)
    abstract RestaurantDetailActivity restaurantDetailActivity();

    @ContributesAndroidInjector
    abstract FavoritesActivity favoritesActivity();

    @ContributesAndroidInjector
    abstract SearchActivity searchActivity();
}
