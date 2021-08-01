package com.example.scheduleappotiment.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scheduleappotiment.MainActivity;
import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ActivityEditProfileBinding;
import com.example.scheduleappotiment.model.apimodel.Contact;
import com.example.scheduleappotiment.model.apimodel.LoginApiModel;
import com.example.scheduleappotiment.model.apimodel.NewContact;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding mBinding;
    private Intent intent;
    private int genderSelectedRb=-1;
    private final int MALE=1;
    private final int FEMALE=2;
    private String uid;
    private boolean isEdit=false;
    private Contact mContact;
    private static final String TAG = "EditProfileActivity";
    private MaterialDatePicker mdp;
    private String mContactId;
    private String dePartment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        if (getIntent()!=null)setIntentData();
        if (getIntent().getStringExtra("uid")!=null){
            uid = intent.getStringExtra("uid");
        }
        addListener();
    }

    private void setIntentData(){
        intent=getIntent();
        isEdit=intent.getBooleanExtra("isEdit",false);
        mdp=MaterialDatePicker.Builder.datePicker().setTitleText("Select Your DOB").build();
        if (isEdit){
            mContact=intent.getParcelableExtra("contact");
            if (!CommonUtility.isEmpty(mContact.getEmail())){
                mBinding.emailEt.setText(mContact.getEmail());
            }
            if (!CommonUtility.isEmpty(mContact.getDob())){
                mBinding.dobEt.setText(CommonUtility.changeDateFormat("yyyy-MM-dd","dd-MM-yyyy",mContact.getDob()));
            }
            if (!CommonUtility.isEmpty(mContact.getGender())){
                if (mContact.getGender().equalsIgnoreCase("Male")){
                    mBinding.maleRb.setChecked(true);
                    genderSelectedRb=MALE;
                }else{
                    mBinding.femaleRb.setChecked(true);
                    genderSelectedRb=FEMALE;
                }
            }
            if (!CommonUtility.isEmpty(mContact.getFirstName())){
                mBinding.nameEt.setText(mContact.getFirstName());
            }
        }
        if (getIntent().getStringExtra("email")!=null){
            mBinding.emailEt.setText(intent.getStringExtra("email"));
        }
        if (intent.getStringExtra("name")!=null){
            mBinding.nameEt.setText(intent.getStringExtra("name"));
        }
        if (intent.getStringExtra("dob")!=null){
            mBinding.dobEt.setText(intent.getStringExtra("dob"));
        }
        if (intent.getIntExtra("gender",-1)!=-1){
            if (intent.getIntExtra("gender",-1)==MALE){
                mBinding.maleRb.setChecked(true);
            }else if (intent.getIntExtra("gender",-1)!=FEMALE){
                mBinding.femaleRb.setChecked(true);
            }
        }
        mContactId=intent.getStringExtra("contactId");
        dePartment=intent.getStringExtra("department");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListener(){
        mBinding.saveBtn.setOnClickListener(v->{
            if (canSave()){
                //redirectToMain();
                saveContact();
            }
        });
        mBinding.dobLinear.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP){
                mBinding.dobInp.performClick();
                return true;
            }
            return false;
        });
        mBinding.dobInp.setOnClickListener(v -> {
            mdp.showNow(getSupportFragmentManager(),"DOB");
        });
        mBinding.dobEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim()) && mBinding.dobInp.isErrorEnabled()){
                    removeErrorOnInputLayout(mBinding.dobInp);
                }
            }
        });
        mBinding.nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim()) && mBinding.nameInp.isErrorEnabled()){
                    removeErrorOnInputLayout(mBinding.nameInp);
                }
            }
        });
        mBinding.genderRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId==mBinding.maleRb.getId())genderSelectedRb=MALE;
            else if (checkedId==mBinding.femaleRb.getId())genderSelectedRb=FEMALE;
        });
        mdp.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            mBinding.dobEt.setText(CommonUtility.getDateFromTime(calendar.getTime()));
        });
    }

    private boolean canSave() {
        boolean canSave=true;
        String name=mBinding.nameEt.getText().toString().trim();
        String dob=mBinding.dobEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            setErrorOnInputLayout(mBinding.nameInp,"Please Enter Your name");
            canSave=false;
        }
        if (TextUtils.isEmpty(dob)){
            setErrorOnInputLayout(mBinding.dobInp,"Please Enter Your db");
            canSave=false;
        }else{
            String[] dobs = dob.split("-");
            if (dobs.length==3 && TextUtils.isDigitsOnly(dobs[0]) && TextUtils.isDigitsOnly(dobs[1]) && TextUtils.isDigitsOnly(dobs[2])){
                int day=Integer.parseInt(dobs[0]);
                int month=Integer.parseInt(dobs[1]);
                int year=Integer.parseInt(dobs[2]);
                if (month<=0 || month>12){
                    setErrorOnInputLayout(mBinding.dobInp,"Enter correct month");
                    canSave=false;
                }
                if (day<=0 || day>31){
                    setErrorOnInputLayout(mBinding.dobInp,"Enter correct day");
                    canSave=false;
                }else if (month==4 && month==6 && month==9 && month==11 && day>30){
                    setErrorOnInputLayout(mBinding.dobInp,"Enter correct day");
                    canSave=false;
                }else if (month==2 && day>29){
                    setErrorOnInputLayout(mBinding.dobInp,"Enter correct day");
                    canSave=false;
                }
                if (year>2010){
                    setErrorOnInputLayout(mBinding.dobInp,"Enter dob year less then 2010");
                    canSave=false;
                }
            }else{
                setErrorOnInputLayout(mBinding.dobInp,"Please enter in Correct DOB");
                canSave=false;
            }
        }
        if (genderSelectedRb==-1){
            Toast.makeText(this, "Please Select a gender", Toast.LENGTH_SHORT).show();
            canSave=false;
        }
        return canSave;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Edit Profile");
        if (isEdit){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setErrorOnInputLayout(TextInputLayout inp, String error) {
        inp.setErrorEnabled(true);
        if (error != null && !error.isEmpty()) inp.setError(error);
        else inp.setError("Please Enter Details");
    }

    private void removeErrorOnInputLayout(TextInputLayout inp) {
        inp.setError(null);
        inp.setErrorEnabled(false);
    }

    private void redirectToMain(){
        runOnUiThread(()->{
            if (isEdit){
                onBackPressed();
                return;
            }
            Intent intent=new Intent(EditProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveContact(){
        showProgress(true);
        ExecutorService mService = Executors.newSingleThreadExecutor();
        mService.execute(()->{
            if (MyConstant.mToken==null)CommonUtility.getBearerToken();
            NewContact contactBody = new NewContact();
            Contact newContact = new Contact();
            newContact.setFirstName(mBinding.nameEt.getText().toString());
            newContact.setGender(genderSelectedRb==MALE?"Male":"Female");
            newContact.setDob(CommonUtility.changeDateFormat("dd-MM-YYYY","YYYY-MM-dd",mBinding.dobEt.getText().toString()));
            newContact.setUser_Id__c(uid);
            if (isEdit) {
                newContact.setId(mContact.getId());
                newContact.setDepartment(mContact.getDepartment());
            }else{
                newContact.setId(mContactId);
            }
            contactBody.setContact(newContact);
            OkHttpClient client = new OkHttpClient();
            String reqBody = null;
            try {
                reqBody = new ObjectMapper().writeValueAsString(contactBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "saveOnApi: " + reqBody);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(reqBody, mediaType);
            Request request = new Request.Builder()
                    .url(MyConstant.MY_URL+"getContacts")
                    .method("POST", body)
                    .addHeader("authorization", "Bearer " + MyConstant.mToken)
                    .addHeader("content-type", "application/json")
                    .build();
            //Log.d(TAG, "saveOnApi: request body :="+reqBody.toString()+"\t headers:="+request.headers()+"string:= "+request.toString());
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String res=response.body().string();
                    Log.d(TAG, "saveContact: response:="+res);
                    //redirectToMain();
                    if (!isEdit)getUserContact(uid);
                    else redirectToMain();
                } else {
                    Log.d(TAG, "saveOnApi: error:=" + response.code() + "\t message:=" + response.message()+"\t response:="+response.body().string());
                    runOnUiThread(()-> Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show());
                }
                showProgress(false);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "saveOnApi: exception:=\t"+e.getMessage());
                runOnUiThread(()-> Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show());
                showProgress(false);
            }
        });
    }

    private void showProgress(boolean show){
        runOnUiThread(()->{
            mBinding.loadPg.progressMainLl.setVisibility(show? View.VISIBLE:View.GONE);
            mBinding.transView.setVisibility(show?View.VISIBLE:View.GONE);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserContact(String uid){
        Log.d(TAG, "getUserContact: uid:="+uid);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            OkHttpClient client = new OkHttpClient();
            LoginApiModel model=new LoginApiModel(uid);
            String body=null;
            try {
                body=new ObjectMapper().writeValueAsString(model);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            assert body != null;
            if (MyConstant.mToken==null) CommonUtility.getBearerToken();
            Request request = new Request.Builder()
                    .url(MyConstant.MY_URL+"login")
                    .post(RequestBody.create(body, MediaType.parse("application/json")))
                    .addHeader("authorization", "Bearer "+MyConstant.mToken)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    String res=response.body().string();
                    Contact contact=new ObjectMapper().readValue(res,Contact.class);
                    if (contact!=null){
                        MySharedPref pref=MySharedPref.getInstance(EditProfileActivity.this);
                        pref.setContactID(contact.getId());
                        pref.setString(MyConstant.FIRST_NAME,contact.getFirstName());
                        pref.setString(MyConstant.LAST_NAME,contact.getLastName());
                        pref.setBoolean(MyConstant.IS_PC,contact.getIs_PC());
                        //Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                        //finish();
                        runOnUiThread(this::redirectToMain);
                    }else{
                        runOnUiThread(this::failLoginAPiCall);
                    }
                }else{
                    runOnUiThread(this::failLoginAPiCall);
                }
                showProgress(false);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "getUserContact: "+e.getMessage());
                runOnUiThread(this::failLoginAPiCall);
                showProgress(false);
            }
        });
    }

    private void failLoginAPiCall() {
        showProgress(false);
        Toast.makeText(EditProfileActivity.this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
    }


}
