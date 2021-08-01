package com.example.scheduleappotiment.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ActivityAppointmentDetailsBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.model.apimodel.AppointmentRequest;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentDetails extends BaseActivity {

    private ActivityAppointmentDetailsBinding mBinding;
    private Appointment mAppointment;
    private static final String TAG = "AppointmentDetails";
    public static boolean isRescheduleSuccess = false;
    private boolean isRescheduleRequest = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAppointmentDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        if (getIntent() != null && getIntent().getParcelableExtra("appointment") != null) {
            mAppointment = getIntent().getParcelableExtra("appointment");
        } else {
            Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
            finish();
        }
        setSupportActionBar(mBinding.appbar.toolbarMain);
        getSupportActionBar().setTitle("Appointment Details");
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @SuppressLint({"SetTextI18n", "UseCompatTextViewDrawableApis"})
    private void init() {
        String FName = mAppointment.getToContact().getFirstName();
        String LName = mAppointment.getToContact().getLastName() != null ? mAppointment.getToContact().getLastName() : "";
        if (!CommonUtility.isEmpty(FName))
            mBinding.toContactTv.setText(getString(R.string.appointment_schedule_with, FName +" "+ LName));
        else mBinding.toContactTv.setText(getString(R.string.appointment_schedule_on));
        mBinding.titleTv.setText(mAppointment.getTitle());
        mBinding.dateTv.setText(mAppointment.getSlot().getDate());
        mBinding.timeTv.setText(getString(R.string.show_event_time,
                CommonUtility.getTimeFromSlotObj(mAppointment.getSlot().getStartTimeC()),
                CommonUtility.getTimeFromSlotObj(mAppointment.getSlot().getEndTimeC())));
        setStatusTv(mAppointment.getServerStatus());
        mBinding.descTv.setText(mAppointment.getDescription());
        mBinding.cancelBtn.setOnClickListener(v -> changeStatusAppointment(MyConstant.STATUS_REJECTED));
        if (mAppointment.getServerStatus().equals(MyConstant.STATUS_REJECTED)){
            mBinding.changeStatusBtn.setVisibility(View.GONE);
            mBinding.reScheduleBtn.setVisibility(View.GONE);
            mBinding.cancelBtn.setVisibility(View.GONE);
        }else {
            if (mAppointment.getToContact().getId().equals(MySharedPref.getInstance(this).getContactId())) {
                if (mAppointment.getServerStatus().equals(MyConstant.STATUS_PENDING)) {
                    mBinding.changeStatusBtn.setText("APPROVE");
                    mBinding.changeStatusBtn.setOnClickListener(v -> {
                        changeStatusAppointment(MyConstant.STATUS_APPROVED);
                    });
                } else if (mAppointment.getServerStatus().equals(MyConstant.STATUS_APPROVED)) {
                    mBinding.changeStatusBtn.setVisibility(View.GONE);
                    mBinding.cancelBtn.setVisibility(View.GONE);
                    mBinding.reScheduleBtn.setVisibility(View.GONE);
                }
                if (mAppointment.getServerStatus().equals(MyConstant.STATUS_RESCHEDULE)) {
                    mBinding.changeStatusBtn.setVisibility(View.GONE);
                    mBinding.cancelBtn.setVisibility(View.GONE);
                }
            } else if (mAppointment.getFromContact().getId().equals(MySharedPref.getInstance(this).getContactId())) {
                switch (mAppointment.getServerStatus()) {
                    case MyConstant.STATUS_PENDING:
                        mBinding.changeStatusBtn.setVisibility(View.GONE);
                        mBinding.cancelBtn.setVisibility(View.VISIBLE);
                        break;
                    case MyConstant.STATUS_APPROVED:
                        mBinding.changeStatusBtn.setVisibility(View.GONE);
                        mBinding.cancelBtn.setVisibility(View.GONE);
                        break;
                    case MyConstant.STATUS_RESCHEDULE:
                        mBinding.cancelBtn.setVisibility(View.GONE);
                        mBinding.reScheduleBtn.setVisibility(View.GONE);
                        mBinding.changeStatusBtn.setVisibility(View.GONE);
                        break;
                }

            }
            if (MySharedPref.getInstance(this).getBoolean(MyConstant.IS_PC)) {
                if (mAppointment.getServerStatus().equals(MyConstant.STATUS_PENDING)) {
                    mBinding.reScheduleBtn.setVisibility(View.VISIBLE);
                    mBinding.reScheduleBtn.setOnClickListener(v -> {
                        Intent intent = new Intent(AppointmentDetails.this, MakeAppointment.class);
                        intent.putExtra("isReSchedule", true);
                        intent.putExtra("appointment", mAppointment);
                        startActivity(intent);
                        isRescheduleRequest = true;
                    });

                }
                else{
                    mBinding.reScheduleBtn.setVisibility(View.GONE);
                }
            } else{
                mBinding.reScheduleBtn.setVisibility(View.GONE);
            }
        }
        Log.d(TAG, "init: changeBtn "+mBinding.changeStatusBtn.getVisibility()+"\t cancelBtn:="+mBinding.cancelBtn.getVisibility());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRescheduleRequest && isRescheduleSuccess) {
            setStatusTv(MyConstant.STATUS_RESCHEDULE);
            isRescheduleSuccess = false;
        }
        isRescheduleRequest = false;
    }

    private void changeStatusAppointment(String status) {
        showProgress(true);
        new Thread(() -> {
            mAppointment.setServerStatus(status);
            AppointmentRequest appointment = new AppointmentRequest();
            appointment.setName(mAppointment.getTitle());
            appointment.setDescriptionC(mAppointment.getDescription());
            appointment.setSlotC(mAppointment.getSlot().getId());
            appointment.setFromContactC(mAppointment.getFromContact().getId());
            appointment.setToContactC(mAppointment.getToContact().getId());
            appointment.setStatusC(mAppointment.getServerStatus());
            appointment.setId(mAppointment.getId());
            AppointmentRequest.Request AppointmentReq = new AppointmentRequest.Request();
            AppointmentReq.setAppointment(appointment);

            if (MyConstant.mToken == null) CommonUtility.getBearerToken();
            OkHttpClient client = new OkHttpClient();
            String req = null;
            try {
                req = new ObjectMapper().writeValueAsString(AppointmentReq);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "makeAppointment: " + req + "\t token " + MyConstant.mToken);
            Request request = new Request.Builder()
                    .url(MyConstant.MY_URL + "appointment")
                    .post(RequestBody.create(req, MediaType.parse("application/json")))
                    .addHeader("authorization", "Bearer " + MyConstant.mToken)
                    .addHeader("content-type", "application/json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Your appointment Status Changed", Toast.LENGTH_SHORT).show();
                        mBinding.statusTv.setText(mAppointment.getServerStatus());
                        mBinding.cancelBtn.setVisibility(View.GONE);
                        mBinding.changeStatusBtn.setVisibility(View.GONE);
                        showProgress(false);
                    });
                } else {
                    Log.d(TAG, "makeAppointment: req fail" + response.body().string());
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Request is failed", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Request is failed..", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                });
            }
        }).start();
    }

    private void showProgress(boolean show) {
        runOnUiThread(() -> {
            mBinding.loadPg.progressMainLl.setVisibility(show ? View.VISIBLE : View.GONE);
            mBinding.transView.setVisibility(show ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    private void setStatusTv(String status){
        mBinding.statusTv.setText(status);
        switch (status){
            case MyConstant.STATUS_APPROVED:
            case MyConstant.STATUS_RESCHEDULE:
                mBinding.statusTv.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.green_msg)));
                break;
            case MyConstant.STATUS_REJECTED:
                mBinding.statusTv.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.red)));
                break;
            case MyConstant.STATUS_PENDING:
                mBinding.statusTv.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.yellow_pending)));
                break;
        }
    }

}
