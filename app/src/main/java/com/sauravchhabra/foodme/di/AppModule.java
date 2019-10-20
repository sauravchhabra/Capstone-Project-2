package com.sauravchhabra.foodme.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.sauravchhabra.foodme.BuildConfig;
import com.sauravchhabra.foodme.FoodMe;
import com.sauravchhabra.foodme.data.local.FoodMeDatabase;
import com.sauravchhabra.foodme.data.local.dao.FavRestaurantDao;
import com.sauravchhabra.foodme.data.remote.ApiService;
import com.sauravchhabra.foodme.data.remote.RequestInterceptor;
import com.sauravchhabra.foodme.util.ConstantsUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {ViewModelModule.class})
class AppModule {

    @Provides
    @Singleton
    Application provideContext(FoodMe application) {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            okHttpClient.addNetworkInterceptor(new StethoInterceptor());
            okHttpClient.addInterceptor(httpLoggingInterceptor);
        }
        okHttpClient.addInterceptor(new RequestInterceptor());
        return okHttpClient.build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    FoodMeDatabase provideFoodMeDatabase(Application application) {
        return Room.databaseBuilder(application, FoodMeDatabase.class, "foodme.db").build();
    }

    @Provides
    @Singleton
    FavRestaurantDao provideFavRestaurantDao(FoodMeDatabase database) {
        return database.favRestaurantDao();
    }
}
