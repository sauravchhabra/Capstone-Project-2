package com.sauravchhabra.foodme.ui.establisments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sauravchhabra.foodme.R;
import com.sauravchhabra.foodme.common.BaseFragment;
import com.sauravchhabra.foodme.databinding.FragmentEstablismentTypesListBinding;
import com.sauravchhabra.foodme.model.establisments.Establishment;
import com.sauravchhabra.foodme.util.ConstantsUtils;
import com.sauravchhabra.foodme.util.SharedPreferencesUtils;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class EstablismentTypesListFragment extends BaseFragment<EstablismentsViewModel, FragmentEstablismentTypesListBinding>
        implements EstablismentCallback.TypesCalback {

    private EstablismentTypesAdapter adapter;

    @Inject
    SharedPreferences preferences;

    public static EstablismentTypesListFragment newInstance() {

        Bundle args = new Bundle();

        EstablismentTypesListFragment fragment = new EstablismentTypesListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public EstablismentTypesListFragment() {
        // Required empty public constructor
    }

    @Override
    public Class<EstablismentsViewModel> getViewModel() {
        return EstablismentsViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_establisment_types_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        adapter = new EstablismentTypesAdapter(new EstablismentTypesAdapter.EstablismentTypesDiffCallback(), this);
        dataBinding.establismentTypesRecyclerview.setAdapter(adapter);

        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int cityId = SharedPreferencesUtils.getIntegerPreference(preferences, ConstantsUtils.CITY_ID, 0);
        viewModel.getEstablismentTypes(cityId, null, null).observe(this, establishments -> {
            dataBinding.setListSize(establishments.size());
            adapter.submitList(establishments);
        });

    }

    @Override
    public void onEstablismentTypesClick(Establishment establishment) {
        ((EstablismentsActivity)getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.establisments_container, EstablistmentListFragment.newInstance(establishment))
                .addToBackStack(null)
                .commit();
    }
}
