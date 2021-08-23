package com.example.scheduleappotiment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.adapter.NotificationAdapter;
import com.example.scheduleappotiment.databinding.ActivityNotificationBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.model.Notification;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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


    @Override
    protected void onResume() {
        super.onResume();
        getNotifications();
    }

    private void getNotifications() {
        showProgress(true);
        mBinding.emptyTv.setVisibility(View.VISIBLE);
        mBinding.emptyTv.setText("Fetching your notification,please wait..");
        mBinding.notificationRv.setVisibility(View.GONE);
        if (!CommonUtility.isEmpty(MySharedPref.getInstance(NotificationsActivity.this).getContactId())) {
            new Thread(() -> {
                if (MyConstant.mToken == null) CommonUtility.getBearerToken();
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(MyConstant.MY_URL + "notification?contactId=" + MySharedPref.getInstance(NotificationsActivity.this).getContactId())
                        .get()
                        .addHeader("authorization", "Bearer " + MyConstant.mToken)
                        .addHeader("content-type", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.d(TAG, "getNotifications: "+res);
                        mNotificationList = new ObjectMapper().readValue(res, new TypeReference<List<Notification>>() {
                        });
                        setRecyclerView();
                    } else {
                        Log.d(TAG, "getNotifications: request is failed error," + response.body().string());
                        runOnUiThread(() -> {
                            Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                        });
                        showProgress(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "getAppointmentList: exception occurred" + e.getMessage());
                    runOnUiThread(() -> {
                        Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                    });
                    showProgress(false);
                }
            }).start();
        } else {
            Toast.makeText(NotificationsActivity.this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecyclerView() {
        if (mNotificationList != null && mNotificationList.size() > 0) {
            runOnUiThread(() -> {
                NotificationAdapter mAdapter = new NotificationAdapter(mNotificationList, NotificationsActivity.this, position -> {
                    Intent intent = new Intent(NotificationsActivity.this, AppointmentDetails.class);
                    intent.putExtra("appointmentId", mNotificationList.get(position).getAppointment_c());
                    intent.putExtra("fromNotification", true);
                    startActivity(intent);
                    finish();
                });
                mBinding.notificationRv.setAdapter(mAdapter);
                mBinding.emptyTv.setVisibility(View.GONE);
                mBinding.notificationRv.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this, LinearLayoutManager.VERTICAL, false));
                mBinding.notificationRv.setVisibility(View.VISIBLE);
            });
        } else {
            runOnUiThread(() -> {
                mBinding.notificationRv.setVisibility(View.GONE);
                mBinding.emptyTv.setText("No Notifications is available");
                mBinding.emptyTv.setVisibility(View.VISIBLE);
            });
        }
        showProgress(false);
    }

    private void showProgress(boolean show) {
        runOnUiThread(() -> {
            mBinding.appbar.progressMainLoading.setVisibility(show ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
