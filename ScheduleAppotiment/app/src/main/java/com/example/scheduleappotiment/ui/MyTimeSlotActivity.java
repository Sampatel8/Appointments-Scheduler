package com.example.scheduleappotiment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.adapter.AvailabilityAdapter;
import com.example.scheduleappotiment.databinding.LayoutMyTimeSlotsBinding;
import com.example.scheduleappotiment.model.apimodel.Availability;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyTimeSlotActivity extends BaseActivity {

    private LayoutMyTimeSlotsBinding mBinding;
    private ArrayList<Availability> modelList;
    private AvailabilityAdapter mAdapter;
    private static final String TAG = "MyTimeSlotActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=LayoutMyTimeSlotsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        init();
        addListener();
    }

    private void init(){
        mBinding.appbar.progressMainLoading.setVisibility(View.GONE);
        mBinding.rvMySlots.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        modelList=new ArrayList<>();
        setSupportActionBar(mBinding.appbar.toolbarMain);
        getSupportActionBar().setTitle("My Available Time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void addListener(){
        mBinding.addSlotsBtn.setOnClickListener(v->{
            String[] days=getDayString();
            Intent myTime=new Intent(MyTimeSlotActivity.this, AssignTimeSlotActivity.class);
            myTime.putExtra("days",days);
            startActivityForResult(myTime,1001);
        });
    }

    private String[] getDayString(){
        String[] day=getResources().getStringArray(R.array.time_slots_days);
        List<String> daysList=new ArrayList<>();
        for(String d:day) {
            boolean isFound=false;
            for (Availability slot : modelList) {
                if (d.equalsIgnoreCase(slot.getDayOfWeek())) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound)daysList.add(d);
        }
        return daysList.toArray(new String[]{});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==1001 && requestCode==RESULT_OK){
            if (data!=null){
                Availability model=data.getParcelableExtra("model");
                if (model!=null && model.getDayOfWeek()!=null && model.getStartTime()!=null){
                    modelList.add(model);
                }
                setAdapter();
            }else{
                Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setAdapter(){
        if (modelList!=null && modelList.size()>0) {
            mBinding.rvMySlots.setVisibility(View.VISIBLE);
            mBinding.noDataTv.setVisibility(View.GONE);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter = new AvailabilityAdapter(modelList, new AvailabilityAdapter.AvailabilityItemClick() {
                    @Override
                    public void onItemClick(Availability avail) {
                        ArrayList<String> tmplist=new ArrayList<>(Arrays.asList(getDayString()));
                        tmplist.add(avail.getDayOfWeek());
                        String[] days= tmplist.toArray(new String[]{""});
                        Intent myTime=new Intent(MyTimeSlotActivity.this, AssignTimeSlotActivity.class);
                        myTime.putExtra("days",days);
                        myTime.putExtra("isEdit",true);
                        myTime.putExtra("avail",avail);
                        startActivityForResult(myTime,1001);
                    }
                });
                mBinding.rvMySlots.setAdapter(mAdapter);
            }
        }else{
            mBinding.rvMySlots.setVisibility(View.GONE);
            mBinding.noDataTv.setText(R.string.no_available_slot_text);
            mBinding.noDataTv.setVisibility(View.VISIBLE);
        }
        dismissProgress();
    }

    private void getMyAvailabilitySlots(){
        mBinding.appbar.progressMainLoading.setVisibility(View.VISIBLE);
        ExecutorService executorService=Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            if (MyConstant.mToken==null) CommonUtility.getBearerToken();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MyConstant.MY_URL+"availability?contactId="+ MySharedPref.getInstance(MyTimeSlotActivity.this).getContactId())
                    .get()
                    .addHeader("authorization", "Bearer "+MyConstant.mToken)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    String res=response.body().string();
                    List<Availability> mAvailList=new ObjectMapper().readValue(res, new TypeReference<List<Availability>>() {
                    });
                    if (mAvailList!=null && !mAvailList.isEmpty()){
                        modelList=new ArrayList<>(mAvailList);
                    }
                    runOnUiThread(this::setAdapter);
                }else{
                    Log.d(TAG, "getMyAvailabilitySlots: request"+request.toString());
                    Log.d(TAG, "getMyAvailabilitySlots: failed with code:="+response.code()+"\tmessage:="+response.message()+"\tbody:="+response.body().string());
                    runOnUiThread(()->{
                        Toast.makeText(this, "Server Request is failed", Toast.LENGTH_SHORT).show();
                        dismissProgress();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
                runOnUiThread(()->{
                    dismissProgress();
                    Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyAvailabilitySlots();
    }

    private void dismissProgress(){
        runOnUiThread(()->mBinding.appbar.progressMainLoading.setVisibility(View.GONE));
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
