package com.sauravchhabra.foodme.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import com.sauravchhabra.foodme.common.BaseViewModel;
import com.sauravchhabra.foodme.model.geocode.GeocodeResponse;
import com.sauravchhabra.foodme.model.geocode.Location;
import com.sauravchhabra.foodme.model.geocode.NearbyRestaurant;
import com.sauravchhabra.foodme.model.locations.LocationSuggestion;
import com.sauravchhabra.foodme.model.locations.LocationsResponse;
import com.sauravchhabra.foodme.repository.MainActivityRepository;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {

    private MainActivityRepository repository;

    private MutableLiveData<List<NearbyRestaurant>> nearbyRestaurantsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<LocationSuggestion>> locationsLiveData = new MutableLiveData<>();
    private MutableLiveData<Location> locationByLatLonLive = new MutableLiveData<>();

    @Inject
    public MainViewModel(MainActivityRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<NearbyRestaurant>> getNearbyRestaurants(Double lat, Double lon, Integer takeCount) {
        if (takeCount != null) {
            disposable.add(repository.getNearbyRestaurants(lat, lon)
                    .map(GeocodeResponse::getNearbyRestaurants)
                    .flatMapIterable(response -> response)
                    .take(takeCount)
                    .toList()
                    .subscribe(response -> nearbyRestaurantsLiveData.setValue(response)));
        } else {
            disposable.add(repository.getNearbyRestaurants(lat, lon)
                    .map(GeocodeResponse::getNearbyRestaurants)
                    .subscribe(response -> nearbyRestaurantsLiveData.setValue(response)));
        }

        return nearbyRestaurantsLiveData;
    }

    public LiveData<List<LocationSuggestion>> getLocationDatas(String query) {
        disposable.add(repository.getLocationDatas(query)
                .map(LocationsResponse::getLocationSuggestions)
                .flatMapIterable(response -> response)
                .toList()
                .subscribe(response -> locationsLiveData.setValue(response)));
        return locationsLiveData;
    }

    public LiveData<Location> getLocationDatasByLatLon(Double lat, Double lon) {
        disposable.add(repository.getLocationDatasByLatLon(lat, lon)
                .map(GeocodeResponse::getLocation)
                .subscribe(location -> locationByLatLonLive.setValue(location)));

        return locationByLatLonLive;
    }
}
