package com.example.scheduleappotiment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scheduleappotiment.databinding.ActivityMainBinding;
import com.example.scheduleappotiment.ui.LoginActivity;
import com.example.scheduleappotiment.ui.NotificationsActivity;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.google.firebase.auth.FirebaseAuth;
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
        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId()==R.id.settingFragment){
                FirebaseAuth.getInstance().signOut();
                try {
                    Toast.makeText(this, "Logout successfully...", Toast.LENGTH_SHORT).show();
                    MySharedPref.getInstance(MainActivity.this).removeAll();
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(login);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Some wrong,please reopen app", Toast.LENGTH_SHORT).show();
                    finishAndRemoveTask();
                }
            }
        });
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