package com.example.scheduleappotiment.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scheduleappotiment.databinding.ActivityMakeAppointmentBinding;
import com.example.scheduleappotiment.model.apimodel.NewContact;
import com.example.scheduleappotiment.model.apimodel.TokenResponse;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MakeAppointment extends BaseActivity {

    private ActivityMakeAppointmentBinding mBinding;
    private static final String TAG = "MakeAppointment";
    private Handler handler;
    private static String mToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMakeAppointmentBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Make Appointment");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getContacts();
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
            if (mToken == null) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "grant_type=password&client_id=3MVG9cHH2bfKACZY3qTfeMnKcfPOqHVgbOnMLjARLVdGwC4IGLvB3RDc7sUay5KzoFZ547DdWJA7ollnJSgUR&client_secret=4E09E6E0B26783BCF53AD0DC93B6F2C05ED9B159AC7D20A948191DC20B770B94&password=appointment%401235zf8kTbOISywc0dSpmmrsfb8d&username=siddharth9365%40gmail.com.appointment");
                Request request = new Request.Builder()
                        .url("https://login.salesforce.com/services/oauth2/token")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
//                        .addHeader("postman-token", "591102e5-f0a1-45f6-4458-a2ab72100e10")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    TokenResponse response1 = new ObjectMapper().readValue(response.body().string(), TokenResponse.class);
                    mToken = response1.getAccessToken();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://3vision2-dev-ed.my.salesforce.com/services/apexrest/getContacts")
                    .get()
                    .addHeader("authorization", "Bearer " + mToken)
                    .addHeader("cache-control", "no-cache")
//                    .addHeader("postman-token", "82530e76-e782-a0c4-d856-eabed43b9ffd")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Log.d(TAG, "getContacts: " + response.toString() + "\t body");
                ObjectMapper mapper = new ObjectMapper();
                String body = response.body().string();
                List<NewContact> contacts = mapper.readValue(body, new TypeReference<List<NewContact>>() {
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

    private void setContactList(List<NewContact> list) {
        //Log.d(TAG, "setContactList: size:=" + list.size());
        String[] spinnerArray = new String[list.size()+1];
        spinnerArray[0]="Select Teacher";
        for (int i = 1,j=0; j < list.size(); i++,j++) {
            spinnerArray[i] = list.get(j).getFirstName() + " " + list.get(j).getLastName();
        }
        handler.post(() -> {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item,
                            spinnerArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            mBinding.spinner.setAdapter(spinnerArrayAdapter);
            mBinding.loadPg.setVisibility(View.GONE);
        });
        addListener();
    }

    private void addListener(){
        mBinding.appointmentBtn.setOnClickListener(v->{
            Toast.makeText(this, "Your appointment is processed", Toast.LENGTH_SHORT).show();
        });
    }
}
