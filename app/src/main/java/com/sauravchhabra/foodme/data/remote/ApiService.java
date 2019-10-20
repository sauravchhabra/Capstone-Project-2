package com.sauravchhabra.foodme.data.remote;

import com.sauravchhabra.foodme.model.categories.CategoryResponse;
import com.sauravchhabra.foodme.model.cities.CitiesResponse;
import com.sauravchhabra.foodme.model.establisments.EstablismentsResponse;
import com.sauravchhabra.foodme.model.geocode.GeocodeResponse;
import com.sauravchhabra.foodme.model.locationDetails.LocationDetailsResponse;
import com.sauravchhabra.foodme.model.locations.LocationsResponse;
import com.sauravchhabra.foodme.model.restaurant.detail.RestaurantDetailResponse;
import com.sauravchhabra.foodme.model.restaurant.reviews.ReviewsResponse;
import com.sauravchhabra.foodme.model.restaurant.search.SearchResponse;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // categories
    @GET("categories")
    Single<CategoryResponse> getCategories();

    // cities
    @GET("cities")
    Single<CitiesResponse> getCities(@Query("q") String cityName,
                                     @Query("lat") Double lat,
                                     @Query("lon") Double lon);


    // establisments
    @GET("establishments")
    Flowable<EstablismentsResponse> getEstablisments(@Query("city_id") int cityId,
                                                     @Query("lat") Double lat,
                                                     @Query("lon") Double lon);

    // geocode
    @GET("geocode")
    Flowable<GeocodeResponse> getGeoCode(@Query("lat") Double lat,
                                         @Query("lon") Double lon);

    // location_details
    @GET("location_details")
    Single<LocationDetailsResponse> getLocationDetails(@Query("entity_id") int entityId,
                                                       @Query("entity_type") String entityType);

    // locations
    @GET("locations")
    Flowable<LocationsResponse> getLocations(@Query("query") String query);

    // restaurant details
    @GET("restaurant")
    Single<RestaurantDetailResponse> getRestaurantDetails(@Query("res_id") int restaurantId);

    // reviews
    @GET("reviews")
    Flowable<ReviewsResponse> getReviews(@Query("res_id") int restaurantId);

    // search
    @GET("search")
    Flowable<SearchResponse> getSearchDatas(@Query("q") String query,
                                            @Query("establishment_type") String establishmentType,
                                            @Query("entity_id") Integer entityId,
                                            @Query("entity_type") String entityType,
                                            @Query("lat") Double lat,
                                            @Query("lon") Double lon,
                                            @Query("cuisines") String cuisinesId);
}
