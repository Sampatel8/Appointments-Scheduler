package com.example.scheduleappotiment.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.scheduleappotiment.BuildConfig;
import com.example.scheduleappotiment.MainActivity;
import com.example.scheduleappotiment.databinding.ActivitySplashBinding;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding mBinding;
    private boolean needLogin=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.versionTxt.setText(BuildConfig.VERSION_NAME);
        init();
    }

    private void init(){
        needLogin= FirebaseAuth.getInstance().getCurrentUser() == null || CommonUtility.isEmpty(MySharedPref.getInstance(this).getContactId());
    }
    @Override
    protected void onResume() {
        super.onResume();
        new Thread(()->{
            Intent intent;
            if (needLogin)intent=new Intent(SplashActivity.this,LoginActivity.class);
            else{
                intent=new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("uid",FirebaseAuth.getInstance().getUid());
            }
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
