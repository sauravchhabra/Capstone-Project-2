package com.sauravchhabra.foodme.di;

import com.sauravchhabra.foodme.ui.detail.RestaurantInfoFragment;
import com.sauravchhabra.foodme.ui.detail.RestaurantReviewsFragment;
import com.sauravchhabra.foodme.ui.establisments.EstablismentTypesListFragment;
import com.sauravchhabra.foodme.ui.establisments.EstablistmentListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract EstablismentTypesListFragment establismentTypesListFragment();

    @ContributesAndroidInjector
    abstract EstablistmentListFragment establismentListFragment();

    @ContributesAndroidInjector
    abstract RestaurantInfoFragment restaurantInfoFragment();

    @ContributesAndroidInjector
    abstract RestaurantReviewsFragment restaurantReviewsFragment();

}
