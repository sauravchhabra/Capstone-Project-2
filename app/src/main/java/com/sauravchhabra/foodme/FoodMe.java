package com.sauravchhabra.foodme;

import com.sauravchhabra.foodme.di.DaggerAppComponent;
import com.facebook.stetho.Stetho;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

public class FoodMe extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
