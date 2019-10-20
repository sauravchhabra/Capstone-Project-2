package com.sauravchhabra.foodme.repository;

import com.sauravchhabra.foodme.data.remote.ApiService;
import com.sauravchhabra.foodme.model.restaurant.search.SearchResponse;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SearchRepository {

    private ApiService apiService;

    @Inject
    public SearchRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Flowable<SearchResponse> getSearchResults(String query, int entityId, String entityType) {
        return apiService.getSearchDatas(query, null, entityId, entityType, null, null, null)
                .onErrorResumeNext(t -> { Timber.e(String.valueOf(t)); })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
