package com.example.scheduleappotiment;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scheduleappotiment.databinding.ActivityMainBinding;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;
    private NavHostFragment mHostFragment;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.appbar.toolbarMain);
        mHostFragment= (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fcv);
        try {
            mNavController = mHostFragment.getNavController();
        }catch (NullPointerException e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            mNavController=Navigation.findNavController(this,mBinding.navHostFcv.getId());
        }
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(R.id.homeFragment,R.id.eventHistoryFragment,R.id.profileFragment,R.id.settingFragment).build();
        //NavigationUI.setupWithNavController(mBinding.toolbarMain,mNavController,appBarConfiguration);
        NavigationUI.setupWithNavController(mBinding.bottomNavMenu,mNavController);
        NavigationUI.setupActionBarWithNavController(this,mNavController,appBarConfiguration);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}