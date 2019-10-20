package com.sauravchhabra.foodme.repository;

import com.sauravchhabra.foodme.data.remote.ApiService;
import com.sauravchhabra.foodme.model.establisments.EstablismentsResponse;
import com.sauravchhabra.foodme.model.restaurant.search.SearchResponse;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EstablismentsRepository {

    private final ApiService apiService;

    @Inject
    public EstablismentsRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Flowable<EstablismentsResponse> getEstablismentTypes(int cityId, Double lat, Double lon) {
        return apiService.getEstablisments(cityId, lat, lon)
                .onErrorResumeNext(t -> { Timber.e(String.valueOf(t)); })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<SearchResponse> getRestaurants(String establismentId, int entityId, String entityType) {
        return apiService.getSearchDatas(null, establismentId, entityId, entityType, null, null, null)
                .onErrorResumeNext(t -> { Timber.e(String.valueOf(t)); })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
