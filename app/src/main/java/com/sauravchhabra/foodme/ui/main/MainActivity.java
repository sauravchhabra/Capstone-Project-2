package com.sauravchhabra.foodme.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import com.sauravchhabra.foodme.R;
import com.sauravchhabra.foodme.common.BaseOnlyActivity;
import com.sauravchhabra.foodme.databinding.ActivityMainBinding;
import com.sauravchhabra.foodme.model.geocode.NearbyRestaurant;
import com.sauravchhabra.foodme.model.locations.LocationSuggestion;
import com.sauravchhabra.foodme.ui.detail.RestaurantDetailActivity;
import com.sauravchhabra.foodme.ui.establisments.EstablismentsActivity;
import com.sauravchhabra.foodme.ui.favorites.FavoritesActivity;
import com.sauravchhabra.foodme.ui.nearbyrestaurants.NearbyRestaurantsActivity;
import com.sauravchhabra.foodme.ui.search.SearchActivity;
import com.sauravchhabra.foodme.util.ConstantsUtils;
import com.sauravchhabra.foodme.util.EntityTypeUtils;
import com.sauravchhabra.foodme.util.GPSUtils;
import com.sauravchhabra.foodme.util.LocationUtils;
import com.sauravchhabra.foodme.util.SharedPreferencesUtils;
import com.sauravchhabra.foodme.widget.AppWidgetHelper;

import java.util.List;

import javax.inject.Inject;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

public class MainActivity extends BaseOnlyActivity<ActivityMainBinding, MainViewModel>
        implements NavigationView.OnNavigationItemSelectedListener, NearbyRestaurantsMainCallback,
        FoodMeLocationCallback, EasyPermissions.PermissionCallbacks, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int TAKEN_NEARBY_RESTAURANTS = 5;
    private static final int LOCATION_PERM_CODE = 101;

    private NearbyRestaurantsMainAdapter nearbyAdapter;

    private ProgressDialog progressDialog;

    @Inject
    SharedPreferences preferences;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar();
        navViewConfig();

        dataBinding.setLifecycleOwner(this);
        AppWidgetHelper.updateAppWidget(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        nearbyAdapter = new NearbyRestaurantsMainAdapter(this);
        dataBinding.nearbyRestaurantsMainRecyclerview.setAdapter(nearbyAdapter);

        // initial loading
        setPrefValuesForInitial();
        int cityId = SharedPreferencesUtils.getIntegerPreference(preferences, ConstantsUtils.CITY_ID, 0);
        double lat = SharedPreferencesUtils.getDoublePreference(preferences, ConstantsUtils.LATITUDE, 0.0);
        double lon = SharedPreferencesUtils.getDoublePreference(preferences, ConstantsUtils.LONGITUDE, 0.0);

        // first loading;
        getNearbyRestaurants(lat, lon);


        dataBinding.viewAllNearbyRestaurants.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NearbyRestaurantsActivity.class)));

        dataBinding.searchView.setOnClickListener(v -> dataBinding.searchView.setIconified(false));
        dataBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra(ConstantsUtils.SEARCH_QUERY_KEY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getNearbyRestaurants(double lat, double lon) {
        viewModel.getNearbyRestaurants(lat, lon, TAKEN_NEARBY_RESTAURANTS).observe(this, response -> {
            dataBinding.setRestaurantSize(response.size());
            nearbyAdapter.submitList(response);
        });
    }


    private void setPrefValuesForInitial() {
        // this initial values for New York, USA
        int cityId = SharedPreferencesUtils.getIntegerPreference(preferences, ConstantsUtils.CITY_ID, 0);
        if (cityId == 0) {
            SharedPreferencesUtils.setStringPreference(preferences, ConstantsUtils.ENTITY_TYPE, EntityTypeUtils.CITY.getType());
            SharedPreferencesUtils.setIntegerPreference(preferences, ConstantsUtils.ENTITY_ID, 280);
            SharedPreferencesUtils.setDoublePreferences(preferences, ConstantsUtils.LATITUDE, 40.71463);
            SharedPreferencesUtils.setDoublePreferences(preferences, ConstantsUtils.LONGITUDE, -74.005806);
            SharedPreferencesUtils.setIntegerPreference(preferences, ConstantsUtils.CITY_ID, 280);
            SharedPreferencesUtils.setIntegerPreference(preferences, ConstantsUtils.COUNTRY_ID, 216);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(ConstantsUtils.CITY_ID)) {
            double lat = SharedPreferencesUtils.getDoublePreference(sharedPreferences, ConstantsUtils.LATITUDE, 0.0);
            double lon = SharedPreferencesUtils.getDoublePreference(sharedPreferences, ConstantsUtils.LONGITUDE, 0.0);
            int cityId = SharedPreferencesUtils.getIntegerPreference(preferences, ConstantsUtils.CITY_ID, 0);

            getNearbyRestaurants(lat, lon);

        }
    }


    @Override
    public void onMainNearbyRestaurantsClick(NearbyRestaurant nearbyRestaurant, ImageView sharedElement) {
        Intent intent = new Intent(MainActivity.this, RestaurantDetailActivity.class);
        intent.putExtra(ConstantsUtils.RESTAURANTS_BUNDLE_KEY, nearbyRestaurant);
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElement, getString(R.string.shared_element_transition_name)).toBundle();
        startActivity(intent, options);
    }

    @Override
    public void onMainNearbyRestaurantMarkerClick(NearbyRestaurant nearbyRestaurant) {
        LocationUtils.openGoogleMaps(this, Double.parseDouble(nearbyRestaurant.getRestaurant().getLocation().getLatitude()), Double.parseDouble(nearbyRestaurant.getRestaurant().getLocation().getLongitude()));
    }

    @Override
    public void onCurrentLocationClick() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            currentLocationConfig();
        } else {
            if (!GPSUtils.isGpsEnabled(this)) {
                gpsSettings();
            } else {
                getLastLocation();
            }
        }
    }

    @Override
    public void onSaveLocationClick(String value) {
        getprogressDialog(getString(R.string.validating));
        viewModel.getLocationDatas(value).observe(this, locationSuggestions -> {

            if (locationSuggestions.size() > 0) {
                LocationSuggestion ls = locationSuggestions.get(0);
                String entitType = ls.getEntityType();
                int entityId = ls.getEntityId();
                double latitude = ls.getLatitude();
                double longitude = ls.getLongitude();
                int cityId = ls.getCityId();
                int countryId = ls.getCountryId();

                SharedPreferencesUtils.setStringPreference(preferences, ConstantsUtils.ENTITY_TYPE, entitType);
                SharedPreferencesUtils.setIntegerPreference(preferences, ConstantsUtils.ENTITY_ID, entityId);
                SharedPreferencesUtils.setDoublePreferences(preferences, ConstantsUtils.LATITUDE, latitude);
                SharedPreferencesUtils.setDoublePreferences(preferences, ConstantsUtils.LONGITUDE, longitude);
                SharedPreferencesUtils.setIntegerPreference(preferences, ConstantsUtils.CITY_ID, cityId);
                SharedPreferencesUtils.setIntegerPreference(preferences, ConstantsUtils.COUNTRY_ID, countryId);

                Toast.makeText(this, getString(R.string.location_saved) + " - " + ls.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.please_enter_valid_city_name), Toast.LENGTH_SHORT).show();
            }
            viewModel.getLocationDatas(value).removeObservers(this);
            progressDialog.dismiss();
        });
    }

    @AfterPermissionGranted(LOCATION_PERM_CODE)
    private void currentLocationConfig() {
        boolean hasLocationPermission = EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasLocationPermission) {
            if (!GPSUtils.isGpsEnabled(this)) {
                gpsSettings();
            } else {
                getLastLocation();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_permission_warning),
                    LOCATION_PERM_CODE, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void gpsSettings() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.enable_gps))
                .setMessage(getString(R.string.require_gps_message))
                .setPositiveButton(getString(android.R.string.yes), (dialog, which) ->
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton(android.R.string.no, (dialog, which) -> {  /* do nothing */ })
                .show();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        getprogressDialog(getString(R.string.finding_your_location)).show();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Timber.e("lat " + location.getLatitude() + "lon " + location.getLongitude());

                SharedPreferencesUtils.setDoublePreferences(preferences, ConstantsUtils.LATITUDE, location.getLatitude());
                SharedPreferencesUtils.setDoublePreferences(preferences, ConstantsUtils.LONGITUDE, location.getLongitude());

                viewModel.getLocationDatasByLatLon(location.getLatitude(), location.getLongitude()).observe(MainActivity.this, locationResponse -> {
                    SharedPreferencesUtils.setStringPreference(preferences, ConstantsUtils.ENTITY_TYPE, locationResponse.getEntityType());
                    SharedPreferencesUtils.setIntegerPreference(preferences, ConstantsUtils.ENTITY_ID, locationResponse.getEntityId());
                    SharedPreferencesUtils.setIntegerPreference(preferences, ConstantsUtils.CITY_ID, locationResponse.getCityId());
                    SharedPreferencesUtils.setIntegerPreference(preferences, ConstantsUtils.COUNTRY_ID, locationResponse.getCountryId());

                    progressDialog.dismiss();
                });
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        }, null);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Timber.e("onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Timber.e("onPermissionsDenied:" + requestCode + ":" + perms.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dataBinding.mainDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_action_my_location:
                showLocationBottomSheet();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_establisments:
                startActivity(new Intent(MainActivity.this, EstablismentsActivity.class));
                break;
            case R.id.nav_nearby_restaurants:
                startActivity(new Intent(MainActivity.this, NearbyRestaurantsActivity.class));
                break;
            case R.id.nav_fav_restaurants:
                startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
        }
        dataBinding.mainDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (dataBinding.mainDrawer.isDrawerOpen(GravityCompat.START)) {
            dataBinding.mainDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private ProgressDialog getprogressDialog(String message) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    private void setupToolbar() {
        setSupportActionBar(dataBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void navViewConfig() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dataBinding.mainDrawer, dataBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dataBinding.mainDrawer.setDrawerListener(toggle);
        toggle.syncState();

        dataBinding.mainNavView.setNavigationItemSelectedListener(this);
    }

    private void showLocationBottomSheet() {
        LocationBottomSheetFragment bottomSheetFragment = new LocationBottomSheetFragment(this);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }
}
