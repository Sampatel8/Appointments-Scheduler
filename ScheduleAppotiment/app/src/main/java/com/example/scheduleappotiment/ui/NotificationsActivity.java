package com.example.scheduleappotiment.ui;

import android.os.Bundle;



import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.adapter.NotificationAdapter;
import com.example.scheduleappotiment.databinding.ActivityNotificationBinding;
import com.example.scheduleappotiment.model.Notification;


import java.util.ArrayList;
import java.util.List;



public class NotificationsActivity extends BaseActivity {

    private ActivityNotificationBinding mBinding;
    private static final String TAG = "NotificationsActivity";
    private ArrayList<Notification> mNotificationList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        init();
    }

    private void init() {
        mBinding.appbar.toolbarMain.setTitle("Your Notifications");
        setSupportActionBar(mBinding.appbar.toolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    
}
