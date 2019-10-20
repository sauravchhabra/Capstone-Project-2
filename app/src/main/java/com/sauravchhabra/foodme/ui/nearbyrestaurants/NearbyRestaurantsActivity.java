package com.sauravchhabra.foodme.ui.nearbyrestaurants;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.ImageView;
import com.sauravchhabra.foodme.R;
import com.sauravchhabra.foodme.common.BaseOnlyActivity;
import com.sauravchhabra.foodme.databinding.ActivityNearbyRestaurantsBinding;
import com.sauravchhabra.foodme.model.geocode.NearbyRestaurant;
import com.sauravchhabra.foodme.ui.detail.RestaurantDetailActivity;
import com.sauravchhabra.foodme.ui.main.MainViewModel;
import com.sauravchhabra.foodme.util.ConstantsUtils;
import com.sauravchhabra.foodme.util.LocationUtils;
import com.sauravchhabra.foodme.util.SharedPreferencesUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import javax.inject.Inject;

import timber.log.Timber;

public class NearbyRestaurantsActivity extends BaseOnlyActivity<ActivityNearbyRestaurantsBinding, MainViewModel> implements NearbyRestaurantsCallback {

    private NearbyRestaurantsAdapter adapter;
    private InterstitialAd mInterstitialAd;

    @Inject
    SharedPreferences preferences;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_nearby_restaurants;
    }

    @Override
    public Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadAds();
        setupToolbar();

        adapter = new NearbyRestaurantsAdapter(this);
        dataBinding.nearbyRestaurantsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.nearbyRestaurantsRecyclerview.setAdapter(adapter);

        double lat = SharedPreferencesUtils.getDoublePreference(preferences, ConstantsUtils.LATITUDE, 0.0);
        double lon = SharedPreferencesUtils.getDoublePreference(preferences, ConstantsUtils.LONGITUDE, 0.0);

        viewModel.getNearbyRestaurants(lat, lon, null).observe(this, response -> {
            dataBinding.setListSize(response.size());
            adapter.submitList(response);
        });
    }

    @Override
    public void onNearbyRestaurantClick(NearbyRestaurant restaurant, ImageView sharedElement) {
        Intent i = new Intent(NearbyRestaurantsActivity.this, RestaurantDetailActivity.class);
        i.putExtra(ConstantsUtils.RESTAURANTS_BUNDLE_KEY, restaurant);
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElement, getString(R.string.shared_element_transition_name)).toBundle();
        startActivity(i, options);
    }

    @Override
    public void onNearbyRestaurantMarkerClick(NearbyRestaurant restaurant) {
        LocationUtils.openGoogleMaps(this, Double.parseDouble(restaurant.getRestaurant().getLocation().getLatitude()), Double.parseDouble(restaurant.getRestaurant().getLocation().getLongitude()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Timber.d("The interstitial wasn't loaded yet.");
        }
    }

    private void loadAds() {
        MobileAds.initialize(this, getString(R.string.banner_app_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void setupToolbar() {
        setSupportActionBar(dataBinding.nearbyRestaurantsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.nearby_restaurants));
    }
}
