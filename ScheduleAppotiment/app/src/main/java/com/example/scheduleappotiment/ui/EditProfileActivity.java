package com.example.scheduleappotiment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.scheduleappotiment.MainActivity;
import com.example.scheduleappotiment.databinding.ActivityEditProfileBinding;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding mBinding;
    private Intent intent;
    private int genderSelectedRb=-1;
    private final int MALE=1;
    private final int FEMALE=2;
    private String uid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        if (getIntent()!=null)setIntentData();
        addListener();
    }

    private void setIntentData(){
        intent=getIntent();
        if (getIntent().getStringExtra("email")!=null){
            mBinding.emailEt.setText(intent.getStringExtra("email"));
        }
        if (getIntent().getStringExtra("uid")!=null){
            uid = intent.getStringExtra("uid");
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

    }

    private void addListener(){
        mBinding.saveBtn.setOnClickListener(v->{
            if (canSave()){
                redirectToMain();
            }
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
            String[] dobs = dob.split("/");
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
        Intent intent=new Intent(EditProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
