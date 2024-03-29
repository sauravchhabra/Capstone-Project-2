package com.sauravchhabra.foodme.ui.detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sauravchhabra.foodme.R;
import com.sauravchhabra.foodme.common.BaseFragment;
import com.sauravchhabra.foodme.databinding.FragmentRestaurantInfoBinding;
import com.sauravchhabra.foodme.data.local.entity.CommonRestaurant;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantInfoFragment extends BaseFragment<RestaurantDetailViewModel, FragmentRestaurantInfoBinding> {

    private static final String RESTAURANT_KEY = "restaurant_key";


    public RestaurantInfoFragment() {
        // Required empty public constructor
    }

    public static RestaurantInfoFragment newInstance(CommonRestaurant commonRestaurant) {

        Bundle args = new Bundle();
        args.putSerializable(RESTAURANT_KEY, commonRestaurant);

        RestaurantInfoFragment fragment = new RestaurantInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Class<RestaurantDetailViewModel> getViewModel() {
        return RestaurantDetailViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_restaurant_info;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CommonRestaurant restaurant = (CommonRestaurant) getArguments().getSerializable(RESTAURANT_KEY);
        dataBinding.setRestaurant(restaurant);
    }
}
