package com.sauravchhabra.foodme.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.sauravchhabra.foodme.ui.detail.RestaurantDetailViewModel;
import com.sauravchhabra.foodme.ui.establisments.EstablismentsViewModel;
import com.sauravchhabra.foodme.ui.main.MainViewModel;
import com.sauravchhabra.foodme.ui.search.SearchViewModel;
import com.sauravchhabra.foodme.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindsCuisinesViewModel(MainViewModel cuisinesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EstablismentsViewModel.class)
    abstract ViewModel bindsEstablismentsViewModel(EstablismentsViewModel establismentsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantDetailViewModel.class)
    abstract ViewModel bindsRestaurantDetailViewModel(RestaurantDetailViewModel restaurantDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindsSearchViewModel(SearchViewModel searchViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);
}
