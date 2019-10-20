package com.sauravchhabra.foodme.ui.establisments;

import android.os.Bundle;
import android.view.MenuItem;
import com.sauravchhabra.foodme.R;
import com.sauravchhabra.foodme.common.BaseActivity;
import com.sauravchhabra.foodme.databinding.ActivityEstablismentsBinding;

public class EstablismentsActivity extends BaseActivity<ActivityEstablismentsBinding> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_establisments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar();

        if (dataBinding.establismentsContainer != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.establisments_container, EstablismentTypesListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(dataBinding.establistmentsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.establisments));
    }
}
