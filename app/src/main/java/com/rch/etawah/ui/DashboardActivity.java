package com.rch.etawah.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rch.etawah.R;
import com.rch.etawah.app.BaseActivity;
import com.rch.etawah.ui.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    @Override
    protected int getResourcesID() {

        return R.layout.activity_dashboard;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openFragment(new HomeFragment());
                    return true;
                case R.id.navigation_orders:
                    openFragment(new HomeFragment());
                    return true;
                case R.id.navigation_profile:
                    openFragment(new HomeFragment());
                    return true;
                case R.id.navigation_news:
                    openFragment(new HomeFragment());
                    return true;
            }
            return false;
        });
        openFragment(new HomeFragment());
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

}
