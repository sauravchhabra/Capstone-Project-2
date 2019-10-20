package com.sauravchhabra.foodme.ui.establisments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.sauravchhabra.foodme.R;
import com.sauravchhabra.foodme.common.BaseFragment;
import com.sauravchhabra.foodme.databinding.FragmentEstablistmentListBinding;
import com.sauravchhabra.foodme.model.establisments.Establishment;
import com.sauravchhabra.foodme.model.restaurant.search.Restaurant;
import com.sauravchhabra.foodme.ui.detail.RestaurantDetailActivity;
import com.sauravchhabra.foodme.util.ConstantsUtils;
import com.sauravchhabra.foodme.util.LocationUtils;
import com.sauravchhabra.foodme.util.SharedPreferencesUtils;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class EstablistmentListFragment extends BaseFragment<EstablismentsViewModel, FragmentEstablistmentListBinding>
        implements EstablismentCallback.RestaurantCallback {

    private static final String ESTABLISHMENT_ID_KEY = "establishment_id";

    private EstablishmentListAdapter adapter;

    @Inject
    SharedPreferences preferences;

    public static EstablistmentListFragment newInstance(Establishment establistment) {

        Bundle args = new Bundle();

        args.putInt(ESTABLISHMENT_ID_KEY, establistment.getEstablishment().getId());
        EstablistmentListFragment fragment = new EstablistmentListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public EstablistmentListFragment() {
        // Required empty public constructor
    }

    @Override
    public Class<EstablismentsViewModel> getViewModel() {
        return EstablismentsViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_establistment_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        adapter = new EstablishmentListAdapter(this);
        dataBinding.establishmentListRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        dataBinding.establishmentListRecyclerview.setAdapter(adapter);

        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int establishmentId = getArguments().getInt(ESTABLISHMENT_ID_KEY);
        int entityId = SharedPreferencesUtils.getIntegerPreference(preferences, ConstantsUtils.ENTITY_ID, 0);
        String entityType = SharedPreferencesUtils.getStringPreference(preferences, ConstantsUtils.ENTITY_TYPE);

        viewModel.getEstablishmentList(String.valueOf(establishmentId), entityId, entityType).observe(this, restaurants -> {
            dataBinding.setListSize(restaurants.size());
            adapter.submitList(restaurants);
        });
    }

    @Override
    public void onEstablismentClick(Restaurant restaurant, ImageView sharedElement) {
        Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
        intent.putExtra(ConstantsUtils.RESTAURANTS_BUNDLE_KEY, restaurant);
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), sharedElement, getString(R.string.shared_element_transition_name)).toBundle();
        startActivity(intent, options);
    }

    @Override
    public void onEstablismentMarkerClick(Restaurant restaurant) {
        LocationUtils.openGoogleMaps(getActivity(), Double.parseDouble(restaurant.getRestaurant().getLocation().getLatitude()), Double.parseDouble(restaurant.getRestaurant().getLocation().getLongitude()));
    }
}
