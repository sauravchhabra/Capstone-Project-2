package com.sauravchhabra.foodme.repository;

import com.sauravchhabra.foodme.data.remote.ApiService;
import com.sauravchhabra.foodme.model.geocode.GeocodeResponse;
import com.sauravchhabra.foodme.model.locations.LocationsResponse;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivityRepository {

    private final ApiService apiService;

    @Inject
    public MainActivityRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Flowable<GeocodeResponse> getNearbyRestaurants(Double lat, Double lon) {
        return apiService.getGeoCode(lat, lon)
                .onErrorResumeNext(t -> { Timber.e(String.valueOf(t)); })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<LocationsResponse> getLocationDatas(String query) {
        return apiService.getLocations(query)
                .onErrorResumeNext(t -> { Timber.e(String.valueOf(t)); })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<GeocodeResponse> getLocationDatasByLatLon(Double lat, Double lon) {
        return apiService.getGeoCode(lat, lon)
                .onErrorResumeNext(t -> { Timber.e(String.valueOf(t)); })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
