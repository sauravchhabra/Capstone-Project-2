package com.sauravchhabra.foodme.di;


import com.sauravchhabra.foodme.FoodMe;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class})
public interface AppComponent extends AndroidInjector<FoodMe>{
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<FoodMe> {}
}
