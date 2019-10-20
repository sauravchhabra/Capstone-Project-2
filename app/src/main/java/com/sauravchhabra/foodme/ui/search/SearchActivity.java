package com.sauravchhabra.foodme.ui.search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.sauravchhabra.foodme.R;
import com.sauravchhabra.foodme.common.BaseOnlyActivity;
import com.sauravchhabra.foodme.databinding.ActivitySearchBinding;
import com.sauravchhabra.foodme.model.restaurant.search.Restaurant;
import com.sauravchhabra.foodme.ui.detail.RestaurantDetailActivity;
import com.sauravchhabra.foodme.util.ConstantsUtils;
import com.sauravchhabra.foodme.util.LocationUtils;
import com.sauravchhabra.foodme.util.SharedPreferencesUtils;

import javax.inject.Inject;

public class SearchActivity extends BaseOnlyActivity<ActivitySearchBinding, SearchViewModel> implements SearchResultsCallback {

    private SearchResultsAdapter adapter;
    private String query;

    @Inject
    SharedPreferences preferences;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public Class<SearchViewModel> getViewModel() {
        return SearchViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            query = getIntent().getExtras().getString(ConstantsUtils.SEARCH_QUERY_KEY);
        }

        setupToolbar(query);

        adapter = new SearchResultsAdapter(this);
        dataBinding.searchRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.searchRecyclerview.setAdapter(adapter);

        int entityId = SharedPreferencesUtils.getIntegerPreference(preferences, ConstantsUtils.ENTITY_ID, 0);
        String entityType = SharedPreferencesUtils.getStringPreference(preferences, ConstantsUtils.ENTITY_TYPE);

        viewModel.getRestaurants(query, entityId, entityType).observe(this, restaurants -> {
            dataBinding.setListSize(restaurants.size());
            if (restaurants.size() == 0) {
                dataBinding.noResultsLl.setVisibility(View.VISIBLE);
                dataBinding.searchProgressbar.setVisibility(View.GONE);
            } else if (restaurants.size() > 0){
                dataBinding.noResultsLl.setVisibility(View.GONE);
                dataBinding.searchProgressbar.setVisibility(View.GONE);
            }
            adapter.submitList(restaurants);
        });
    }

    @Override
    public void onRestaurantClick(Restaurant restaurant, ImageView sharedElement) {
        Intent intent = new Intent(SearchActivity.this, RestaurantDetailActivity.class);
        intent.putExtra(ConstantsUtils.RESTAURANTS_BUNDLE_KEY, restaurant);
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElement, getString(R.string.shared_element_transition_name)).toBundle();
        startActivity(intent, options);
    }

    @Override
    public void onRestaurantMarkerClick(Restaurant restaurant) {
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

    private void setupToolbar(String title) {
        setSupportActionBar(dataBinding.searchToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(title);
    }

}
