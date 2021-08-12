package com.example.scheduleappotiment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scheduleappotiment.databinding.ActivityMainBinding;
import com.example.scheduleappotiment.ui.NotificationsActivity;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;
    private NavHostFragment mHostFragment;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.appbar.toolbarMain);
        mHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fcv);
        try {
            mNavController = mHostFragment.getNavController();
        } catch (NullPointerException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            mNavController = Navigation.findNavController(this, mBinding.navHostFcv.getId());
        }
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.eventHistoryFragment, R.id.profileFragment, R.id.settingFragment).build();
        //NavigationUI.setupWithNavController(mBinding.toolbarMain,mNavController,appBarConfiguration);
        NavigationUI.setupWithNavController(mBinding.bottomNavMenu, mNavController);
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_fragment_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.notification_menu_item) {
            startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //if (mHostFragment.getNavController().getCurrentDestination().getId()==R.id.homeFragment)finishAffinity();
    }
}