package com.example.scheduleappotiment.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.scheduleappotiment.BuildConfig;
import com.example.scheduleappotiment.databinding.ActivitySplashBinding;
import com.example.scheduleappotiment.utility.BaseActivity;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.versionTxt.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(()->{
            Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
            try{
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                startActivity(intent);
                finish();
            });
        }).start();
    }
}
