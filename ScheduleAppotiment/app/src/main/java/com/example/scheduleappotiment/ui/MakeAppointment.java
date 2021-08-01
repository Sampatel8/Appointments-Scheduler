package com.example.scheduleappotiment.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.adapter.SlotAdapter;
import com.example.scheduleappotiment.databinding.ActivityMakeAppointmentBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.model.TimeSlotModel;
import com.example.scheduleappotiment.model.apimodel.AppointmentRequest;
import com.example.scheduleappotiment.model.apimodel.Contact;
import com.example.scheduleappotiment.model.apimodel.SlotRequest;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MakeAppointment extends BaseActivity {

    private ActivityMakeAppointmentBinding mBinding;
    private static final java.lang.String TAG = "MakeAppointment";
    private Handler handler;
    private int spinnerPos = -1;
    private ArrayList<TimeSlotModel> slotList;
    private ArrayList<Contact> contacts;
    private SlotAdapter mSlotAdapter;
    private long appDate;
    private int mSelectedTimeSlotPos = -1;
    private MaterialDatePicker mdp;
    private Appointment mAppointment;
    private boolean isReSchedule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMakeAppointmentBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        handler = new Handler(Looper.getMainLooper());
        init();
        addListener();
    }

    private void init() {
        mdp = MaterialDatePicker.Builder.datePicker().setTitleText("Select Your appointment date").build();
        mBinding.spinner.setVisibility(View.GONE);
        if (getIntent() != null) {
            isReSchedule = getIntent().getBooleanExtra("isReSchedule", false);
            mAppointment = getIntent().getParcelableExtra("appointment");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Make Appointment");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (isReSchedule) {
            ArrayList<Contact> mList = new ArrayList<>();
            Contact contact = mAppointment.getToContact();
            mList.add(contact);
            setContactList(mList);
            if (isReSchedule) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(CommonUtility.getTimeInMilli(mAppointment.getSlot().getDate(), "yyyy-MM-dd"));
                mBinding.appointmentDayEt.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                mBinding.appointmentMonthEt.setText(String.valueOf(cal.get(Calendar.MONTH)+1));
                mBinding.appointmentYearEt.setText(String.valueOf(cal.get(Calendar.YEAR)));
                appDate = cal.getTimeInMillis();
                getTimeSlots();
            }
            mBinding.appointmentTitleEt.setText(mAppointment.getTitle());
            mBinding.appointmentDescEt.setText(mAppointment.getDescription());
        } else getContacts();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getContacts() {
        mBinding.loadPg.setVisibility(View.VISIBLE);
        new Thread(() -> {
            if (MyConstant.mToken == null) {
                CommonUtility.getBearerToken();
            }
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(MyConstant.MY_URL + "getContacts?ispc=" + !MySharedPref.getInstance(MakeAppointment.this).getBoolean(MyConstant.IS_PC))
                    .get()
                    .addHeader("authorization", "Bearer " + MyConstant.mToken)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Log.d(TAG, "getContacts: " + response.toString() + "\t body");
                ObjectMapper mapper = new ObjectMapper();
                java.lang.String body = response.body().string();
                List<Contact> contacts = mapper.readValue(body, new TypeReference<List<Contact>>() {
                });
                Log.d(TAG, "getContacts: " + contacts.get(0).getFirstName());
                setContactList(contacts);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        /*
        ApiClient.createService(ApiPoint.class,"00D5e000002FXvW!AQQAQFjN4g5i._5M8jRmDLkxJUbGG5LH_2i_dHNxacifv5oYkp0BjHUJyBnILZ9PI2kqXvq82NM2ljtkF5DZnWrIm.laXSCD").getContacts()
                .enqueue(new Callback<List<Contact>>() {
                    @Override
                    public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                        List<Contact> list=response.body();
                        //Log.d(TAG, "onResponse: list:="+response.body().toString());
                        Log.d(TAG, "onResponse: "+response.toString()+"\t call:="+call.request().toString());
                        if (list!=null && list.size()>0){
                            setContactList(list);
                        }else{
                            Log.d(TAG, "onResponse: no data");
                            Toast.makeText(MakeAppointment.this,"no list found",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Contact>> call, Throwable t) {
                        t.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(t);
                        Log.d(TAG, "onFailure: reason:="+t.getMessage());
                        Toast.makeText(MakeAppointment.this, "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });*/
    }

    private void setContactList(List<Contact> list) {
        //Log.d(TAG, "setContactList: size:=" + list.size());
        if (list != null && list.size() > 0) {
            contacts = new ArrayList<>(list);
            String[] spinnerArray = new String[list.size() + 1];
            spinnerArray[0] = "Select Teacher";
            for (int i = 1, j = 0; j < list.size(); i++, j++) {
                spinnerArray[i] = (list.get(j).getFirstName() != null ? list.get(j).getFirstName() : "") + " " + (list.get(j).getLastName() != null ? list.get(j).getLastName() : "");
            }
            handler.post(() -> {
                ArrayAdapter<java.lang.String> spinnerArrayAdapter = new ArrayAdapter<java.lang.String>
                        (this, android.R.layout.simple_spinner_item,
                                spinnerArray); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                mBinding.spinner.setAdapter(spinnerArrayAdapter);
                changeLoadPg(false);
                mBinding.spinner.setVisibility(View.VISIBLE);
                if (isReSchedule) {
                    mBinding.spinner.setSelection(spinnerArray.length - 1);
                    mBinding.spinner.setClickable(false);
                } else mBinding.spinner.setClickable(true);
            });
            addListener();
            if (isReSchedule) {
                spinnerPos = (spinnerArray.length - 2);
            }
            if (!isReSchedule) mBinding.spinner.setOnItemSelectedListener(spinnerListener);
        } else {
            mBinding.spinner.setOnItemSelectedListener(null);
            Toast.makeText(this, "can't get list,please try later", Toast.LENGTH_SHORT).show();
        }
        changeLoadPg(false);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListener() {
        mBinding.appointmentBtn.setOnClickListener(v -> {
            makeAppointment();
            //Toast.makeText(this, "Your appointment is processed", Toast.LENGTH_SHORT).show();
        });
        mdp.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(selection);
            appDate = selection;
            mBinding.appointmentDayEt.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
            mBinding.appointmentMonthEt.setText(String.valueOf(cal.get(Calendar.MONTH)+1));
            mBinding.appointmentYearEt.setText(String.valueOf(cal.get(Calendar.YEAR)));
            getTimeSlots();
            mBinding.appointmentDateLl.setClickable(true);
        });
        mdp.addOnNegativeButtonClickListener(v -> {
            appDate = 0;
            mBinding.appointmentDayEt.setText("");
            mBinding.appointmentMonthEt.setText("");
            mBinding.appointmentYearEt.setText("");
            mBinding.appointmentDateLl.setClickable(true);
        });
        mBinding.appointmentDateLl.setOnClickListener(v -> {
            mBinding.appointmentDateLl.setClickable(false);
            mdp.showNow(getSupportFragmentManager(), "myDate");
        });
        mBinding.spinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (contacts != null) {
                    return false;
                } else {
                    Toast.makeText(this, "Please wait we getting date from server", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            return false;
        });
        mBinding.appointmentTitleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0 && mBinding.appointmentTitleTi.isErrorEnabled()) {
                    mBinding.appointmentTitleTi.setError(null);
                    mBinding.appointmentTitleTi.setErrorEnabled(false);
                }
            }
        });
        mBinding.appointmentDateLl.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mBinding.appointmentDateLl.performClick();
                return true;
            }
            return false;
        });

    }

    private final OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                spinnerPos = -1;
                Toast.makeText(MakeAppointment.this, "Please Select the Correct Teacher", Toast.LENGTH_SHORT).show();
            } else {
                spinnerPos = position - 1;
                mBinding.appointmentYearEt.setText("");
                mBinding.appointmentMonthEt.setText("");
                mBinding.appointmentDayEt.setText("");
                mBinding.slotEmptyTv.setText(R.string.select_date_for_slot);
                mBinding.slotEmptyTv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void getTimeSlots() {
        if (spinnerPos != -1 && contacts != null && contacts.size() > 0 && contacts.get(spinnerPos) != null) {
            if (appDate == 0) {
                Toast.makeText(this, "Please Select the date", Toast.LENGTH_SHORT).show();
                return;
            }
            changeLoadPg(true);
            mBinding.slotEmptyTv.setText(R.string.slot_fetch_up);
            mBinding.slotEmptyTv.setVisibility(View.VISIBLE);
            mBinding.appointmentBtn.setEnabled(false);
            new Thread(() -> {
                if (MyConstant.mToken == null) CommonUtility.getBearerToken();

                OkHttpClient client = new OkHttpClient();
                SlotRequest sr = new SlotRequest();
                sr.setContactId(contacts.get(spinnerPos).getId());
                sr.setAppDate(CommonUtility.getMakeAppointmentDate(appDate));
                String req = null;
                try {
                    req = new ObjectMapper().writeValueAsString(sr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(req, mediaType);
                Request request = new Request.Builder()
                        .url(MyConstant.MY_URL + "slots")
                        .post(body)
                        .addHeader("authorization", "Bearer " + MyConstant.mToken)
                        .addHeader("content-type", "application/json")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.d(TAG, "getTimeSlots: " + res);
                        ArrayList<TimeSlotModel> list = new ObjectMapper().readValue(res, new TypeReference<List<TimeSlotModel>>() {
                        });
                        if (list != null) slotList = list;
                        if (isReSchedule) {
                            if (slotList == null) slotList = new ArrayList<>();
                            slotList.add(mAppointment.getSlot());
                        }
                        mSelectedTimeSlotPos = slotList.size() - 1;
                        setTimeSlots();
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                            changeLoadPg(false);
                            mBinding.appointmentBtn.setEnabled(true);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "getTimeSlots: " + e.getMessage());
                    changeLoadPg(false);
                    runOnUiThread(()->mBinding.appointmentBtn.setEnabled(true));
                }
            }).start();
        } else {
            Toast.makeText(this, "Please Select Teacher", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTimeSlots() {
        if (slotList != null && slotList.size() > 0) {
            mSlotAdapter = new SlotAdapter(slotList, pos -> {
                if (pos == mSelectedTimeSlotPos) mSelectedTimeSlotPos = -1;
                else mSelectedTimeSlotPos = pos;
                mSlotAdapter.setSelectedPos(mSelectedTimeSlotPos);
                mSlotAdapter.notifyDataSetChanged();
            }, this);
            if (isReSchedule) mSlotAdapter.setSelectedPos(mSelectedTimeSlotPos);
            runOnUiThread(() -> {
                mBinding.appointmentSlotRv.setLayoutManager(new GridLayoutManager(MakeAppointment.this, 2, RecyclerView.VERTICAL, false));
                mBinding.appointmentSlotRv.setAdapter(mSlotAdapter);
                mBinding.appointmentSlotRv.setVisibility(View.VISIBLE);
                mBinding.slotEmptyTv.setVisibility(View.GONE);
                mBinding.appointmentBtn.setEnabled(true);
            });
        } else {
            runOnUiThread(() -> {
                mBinding.slotEmptyTv.setText(R.string.no_available_slot_text);
                mBinding.slotEmptyTv.setVisibility(View.VISIBLE);
                mBinding.appointmentSlotRv.setVisibility(View.GONE);
                mBinding.appointmentBtn.setEnabled(true);
                //Toast.makeText(this, "No slot available,Please Select different Date", Toast.LENGTH_SHORT).show();
            });
        }
        changeLoadPg(false);
    }

    private void changeLoadPg(boolean show) {
        if (show) {
            runOnUiThread(() -> {
                mBinding.loadPg.setVisibility(View.VISIBLE);
            });
        } else {
            runOnUiThread(() -> {
                mBinding.loadPg.setVisibility(View.GONE);
            });
        }
    }

    private void makeAppointment() {
        if (isAllDetailValid()) {
            showSaveProgress(true);
            if (isReSchedule) {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(() -> {
                    AppointmentRequest appointment = new AppointmentRequest();
                    appointment.setName(mAppointment.getTitle());
                    appointment.setDescriptionC(mAppointment.getDescription());
                    appointment.setSlotC(mAppointment.getSlot().getId());
                    appointment.setFromContactC(mAppointment.getFromContact().getId());
                    appointment.setToContactC(mAppointment.getToContact().getId());
                    appointment.setStatusC(MyConstant.STATUS_REJECTED);
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
                    Log.d(TAG, "makeAppointment: " + req + "\t token" + MyConstant.mToken);
                    Request request = new Request.Builder()
                            .url(MyConstant.MY_URL + "appointment")
                            .post(RequestBody.create(req, MediaType.parse("application/json")))
                            .addHeader("authorization", "Bearer " + MyConstant.mToken)
                            .addHeader("content-type", "application/json")
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            processAppointment();
                        } else {
                            Log.d(TAG, "makeAppointment: req fail" + response.body().string());
                            runOnUiThread(() -> {
                                Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                                showSaveProgress(false);
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                            showSaveProgress(false);
                        });
                    }
                });
            } else {
                processAppointment();
            }
        } else {
            Toast.makeText(this, "Please Fill up the details", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAllDetailValid() {
        boolean valid = true;
        String title = mBinding.appointmentTitleEt.getText().toString();
        if (CommonUtility.isEmpty(title)) {
            mBinding.appointmentTitleTi.setError("Please Enter Title");
            mBinding.appointmentTitleTi.setErrorEnabled(true);
            valid = false;
        }
        if (contacts == null && spinnerPos == -1) {
            Toast.makeText(this, "Please Select Teacher", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (slotList != null && mSelectedTimeSlotPos == -1) {
            Toast.makeText(this, "Please Select time slot", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private void processAppointment() {
        new Thread(() -> {
            slotList.get(mSelectedTimeSlotPos).getAdditionalProperties().clear();
            contacts.get(spinnerPos).getAdditionalProperties().clear();
            AppointmentRequest appointment = new AppointmentRequest();
            appointment.setName(mBinding.appointmentTitleEt.getText().toString());
            appointment.setDescriptionC(mBinding.appointmentDescEt.getText().toString());
            appointment.setSlotC(slotList.get(mSelectedTimeSlotPos).getId());
            appointment.setFromContactC(MySharedPref.getInstance(MakeAppointment.this).getContactId());
            appointment.setToContactC(contacts.get(spinnerPos).getId());
            if (MySharedPref.getInstance(MakeAppointment.this).getBoolean(MyConstant.IS_PC)) {
                if (isReSchedule) appointment.setStatusC(MyConstant.STATUS_RESCHEDULE);
                else appointment.setStatusC(MyConstant.STATUS_APPROVED);
            }
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
            Log.d(TAG, "makeAppointment: " + req + "\t token" + MyConstant.mToken);
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
                        if (isReSchedule) {
                            Toast.makeText(this, "Your appointment is ReSchedule", Toast.LENGTH_SHORT).show();
                            AppointmentDetails.isRescheduleSuccess = true;
                        } else
                            Toast.makeText(this, "Your appointment is processed..", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    Log.d(TAG, "makeAppointment: req fail" + response.body().string());
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Request is failed", Toast.LENGTH_SHORT).show();
                        showSaveProgress(false);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Request is failed..", Toast.LENGTH_SHORT).show();
                    showSaveProgress(false);
                });
            }
        }).start();
    }

    private void showSaveProgress(boolean show){
        runOnUiThread(()->{
            mBinding.saveLoadPg.progressMainLl.setVisibility(show?View.VISIBLE:View.GONE);
            mBinding.transView.setVisibility(show?View.VISIBLE:View.GONE);
        });

    }
}
