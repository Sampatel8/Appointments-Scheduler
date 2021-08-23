package com.example.scheduleappotiment.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ActivityAssignTimeSlotsBinding;
import com.example.scheduleappotiment.model.apimodel.Availability;
import com.example.scheduleappotiment.model.apimodel.AvailabilityRequest;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AssignTimeSlotActivity extends BaseActivity {

    private ActivityAssignTimeSlotsBinding mBinding;
    private String[] daysSlot;
    private boolean isFirst = true;
    private long timeInMilli, startTimeMilli, endTimeMilli;
    private int mode = -1;
    private final int DEFAULT_MODE = -1, START_TIME = 1, END_TIME = 2;
    private BottomSheetBehavior mBottomSheet;
    private static final String TAG = "AssignTimeSlotActivity";
    private String dayOfWeek = null;
    private boolean isEdit=false;
    private Availability mAvail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAssignTimeSlotsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        if (getIntent() != null && getIntent().getStringArrayExtra("days") == null) {
            Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
            finish();
        } else if (getIntent() != null && getIntent().getBooleanExtra("isEdit", false)) {
            isEdit = true;
            mAvail = getIntent().getParcelableExtra("avail");
            if (mAvail == null) {
                Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        daysSlot = Objects.requireNonNull(getIntent()).getStringArrayExtra("days");
        init();
        addListener();
    }

    private void init() {
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, daysSlot);
        adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        mBinding.daySpinner.setAdapter(adapter);
        mBottomSheet = BottomSheetBehavior.from(mBinding.bottomBar.getRoot());
        mBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        enableTimeSlot(false);
        changeProgress(false);
        setSupportActionBar(mBinding.appbar.toolbarMain);
        getSupportActionBar().setTitle("Set Up Time Slot");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (isEdit){
            mBinding.daySpinner.setSelection(daysSlot.length-1);
            dayOfWeek=daysSlot[daysSlot.length-1];
            try {
                Calendar startDate = Calendar.getInstance();
                startDate.setTimeInMillis(CommonUtility.getDateFrom24HourTime(mAvail.getStartTime()).getTime());
                startTimeMilli=startDate.getTimeInMillis();
                mBinding.startTimeHour.setText(String.valueOf(startDate.get(Calendar.HOUR_OF_DAY)));
                mBinding.startTimeMinute.setText(String.valueOf(startDate.get(Calendar.MINUTE)));
                if (startDate.get(Calendar.AM_PM) == Calendar.AM) {
                    mBinding.startTimeToggleBtn.check(mBinding.startTimeAmBtn.getId());
                } else mBinding.startTimeToggleBtn.check(mBinding.startTimePmBtn.getId());
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                Calendar endDate = Calendar.getInstance();
                endDate.setTimeInMillis(CommonUtility.getDateFrom24HourTime(mAvail.getEndTime()).getTime());
                endTimeMilli=endDate.getTimeInMillis();
                mBinding.endTimeHour.setText(String.valueOf(endDate.get(Calendar.HOUR_OF_DAY)));
                mBinding.endTimeMinute.setText(String.valueOf(endDate.get(Calendar.MINUTE)));
                if (endDate.get(Calendar.AM_PM) == Calendar.AM) {
                    mBinding.endTimeToggleBtn.check(mBinding.endTimeAmBtn.getId());
                } else mBinding.endTimeToggleBtn.check(mBinding.endTimePmBtn.getId());
            }catch (Exception e){
                e.printStackTrace();
            }
            enableTimeSlot(true);
            if (startTimeMilli != 0 && endTimeMilli != 0) {
                mBinding.saveTimeSlotBtn.setEnabled(true);
                mBinding.saveTimeSlotBtn.setClickable(true);
            }
        }
        else dayOfWeek=daysSlot[0];
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListener() {
        mBinding.daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    dayOfWeek = daysSlot[position];
                    enableTimeSlot(true);
                    resetTimeEdit(mBinding.timeSlotLlStart.getId());
                    resetTimeEdit(mBinding.timeSlotLlEnd.getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dayOfWeek = null;
            }
        });
        mBinding.saveTimeSlotBtn.setOnClickListener(v -> {
//            Intent intent = new Intent();
//            TimeSlotModel model = new TimeSlotModel();
//            model.setDayOfWeek(daysSlot[mBinding.daySpinner.getSelectedItemPosition()]);
//            //model.setTimeSlotDay(String.valueOf(hour)+String.valueOf(minute));
//            intent.putExtra("model", model);
//            setResult(1001, intent);
            if (startTimeMilli != 0 && endTimeMilli != 0 && !CommonUtility.isEmpty(dayOfWeek)) {
                saveTimeSlot();
            } else {
                Toast.makeText(this, "Please fill details", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.bottomBar.dateTimeTimer.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            timeInMilli = calendar.getTimeInMillis();
            mBinding.bottomBar.dateTimeShowTv.setText(CommonUtility.getTimeIn12HourMinute(calendar.getTime()));
        });
        mBinding.bottomBar.dateTimeDoneBtn.setOnClickListener(v -> {
            setTime();
            mBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            mBinding.transView.setVisibility(View.GONE);
        });
        mBinding.bottomBar.dateTimeCancelBtn.setOnClickListener(v -> {
            mBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            mBinding.transView.setVisibility(View.GONE);
        });
        mBinding.timeSlotLlStart.setOnClickListener(v -> {
            mBinding.bottomBar.dateTimeTimer.setVisibility(View.VISIBLE);
            mBinding.transView.setVisibility(View.VISIBLE);
            if (startTimeMilli != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startTimeMilli);
                mBinding.bottomBar.dateTimeTimer.setHour(calendar.get(Calendar.HOUR));
                mBinding.bottomBar.dateTimeTimer.setMinute(calendar.get(Calendar.MINUTE));
            }
            mBottomSheet.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
            mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            mode = START_TIME;
        });
        mBinding.timeSlotLlEnd.setOnClickListener(v -> {
            mBinding.bottomBar.dateTimeTimer.setVisibility(View.VISIBLE);
            mBinding.transView.setVisibility(View.VISIBLE);
            if (endTimeMilli != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endTimeMilli);
                mBinding.bottomBar.dateTimeTimer.setHour(calendar.get(Calendar.HOUR));
                mBinding.bottomBar.dateTimeTimer.setMinute(calendar.get(Calendar.MINUTE));
            }
            mBottomSheet.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
            mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            mode = END_TIME;
        });
        mBinding.timeSlotLlStart.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mBinding.timeSlotLlStart.performClick();
                return true;
            }
            return false;
        });
        mBinding.timeSlotLlEnd.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mBinding.timeSlotLlEnd.performClick();
                return true;
            }
            return false;
        });

    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(timeInMilli);
        switch (mode) {
            case START_TIME:
                startTimeMilli = timeInMilli;
                mBinding.startTimeHour.setText(String.valueOf(calendar.get(Calendar.HOUR)));
                mBinding.startTimeMinute.setText(String.valueOf(calendar.get(Calendar.MINUTE)));
                if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                    mBinding.startTimeToggleBtn.check(mBinding.startTimeAmBtn.getId());
                } else mBinding.startTimeToggleBtn.check(mBinding.startTimePmBtn.getId());
                break;
            case END_TIME:
                endTimeMilli = timeInMilli;
                mBinding.endTimeHour.setText(String.valueOf(calendar.get(Calendar.HOUR)));
                mBinding.endTimeMinute.setText(String.valueOf(calendar.get(Calendar.MINUTE)));
                if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                    mBinding.endTimeToggleBtn.check(mBinding.endTimeAmBtn.getId());
                } else mBinding.endTimeToggleBtn.check(mBinding.endTimePmBtn.getId());
                break;
            default:
                Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                break;
        }
        if (startTimeMilli != 0 && endTimeMilli != 0) {
            mBinding.saveTimeSlotBtn.setEnabled(true);
            mBinding.saveTimeSlotBtn.setClickable(true);
        }
    }

    private void enableTimeSlot(boolean state) {
        if (state) {
            mBinding.startTv.setVisibility(View.VISIBLE);
            mBinding.endTv.setVisibility(View.VISIBLE);
            mBinding.timeSlotLlStart.setVisibility(View.VISIBLE);
            mBinding.timeSlotLlEnd.setVisibility(View.VISIBLE);
            mBinding.saveTimeSlotBtn.setVisibility(View.VISIBLE);
            mBinding.saveTimeSlotBtn.setClickable(false);
        } else {
            mBinding.startTv.setVisibility(View.GONE);
            mBinding.endTv.setVisibility(View.GONE);
            mBinding.timeSlotLlStart.setVisibility(View.GONE);
            mBinding.timeSlotLlEnd.setVisibility(View.GONE);
            mBinding.saveTimeSlotBtn.setVisibility(View.GONE);
        }
    }

    private void saveTimeSlot() {
        changeProgress(true);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Availability availability = new Availability();
            availability.setDayOfWeek(mBinding.daySpinner.getSelectedItem().toString());
            //availability.setContactId(MySharedPref.getInstance(this).getContactId());
            availability.setContactId(MySharedPref.getInstance(AssignTimeSlotActivity.this).getContactId());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startTimeMilli);
            cal.set(Calendar.SECOND, 0);
            availability.setStartTime(CommonUtility.getTimeForAvailabilityIn24Hour(cal.getTime()) + ".000Z");
            cal.setTimeInMillis(endTimeMilli);
            cal.set(Calendar.SECOND, 0);
            availability.setEndTime(CommonUtility.getTimeForAvailabilityIn24Hour(cal.getTime()) + ".000Z");
            if (isEdit)availability.setId(mAvail.getId());
            List<Availability> availabilityList = new ArrayList<>();
            availabilityList.add(availability);

            AvailabilityRequest requestBody = new AvailabilityRequest();
            requestBody.setMlist(availabilityList);

            if (MyConstant.mToken == null) CommonUtility.getBearerToken();

            String myData = null;
            try {
                myData = new ObjectMapper().writeValueAsString(requestBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "saveTimeSlot: myData:=" + myData);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MyConstant.MY_URL + "availability")
                    .post(RequestBody.create(myData, MediaType.parse("application/json")))
                    .addHeader("authorization", "Bearer " + MyConstant.mToken)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.d(TAG, "saveTimeSlot: request:=" + request.toString());
                if (response.isSuccessful()) {
                    Log.d(TAG, "saveTimeSlot: success,code:=" + response.code());
                    Intent intent = new Intent();
                    intent.putExtra("model", availability);
                    runOnUiThread(() -> {
                        setResult(1001, intent);
                        finish();
                    });
                } else {
                    Log.d(TAG, "saveTimeSlot: ,fail,code=" + response.code() + "\tmessage:=" + response.message() + "\tstring=" + response.body().string());
                    Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                }
                changeProgress(false);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "saveTimeSlot: exception:=" + e.getMessage());
                changeProgress(false);
                Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetTimeEdit(int id) {
        if (id == mBinding.timeSlotLlStart.getId()) {
            mBinding.startTimeHour.setText("");
            mBinding.startTimeMinute.setText("");
            mBinding.startTimeToggleBtn.clearChecked();
        } else if (id == mBinding.timeSlotLlEnd.getId()) {
            mBinding.endTimeHour.setText("");
            mBinding.endTimeMinute.setText("");
            mBinding.endTimeToggleBtn.clearChecked();
        }
    }

    private void changeProgress(boolean show) {
        runOnUiThread(() -> {
            mBinding.progressLoading.progressMainLl.setVisibility(show ? View.VISIBLE : View.GONE);
            mBinding.transView.setVisibility(show ? View.VISIBLE : View.GONE);
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
