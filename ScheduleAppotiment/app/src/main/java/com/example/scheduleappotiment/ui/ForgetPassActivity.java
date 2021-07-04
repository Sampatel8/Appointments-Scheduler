package com.example.scheduleappotiment.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scheduleappotiment.BuildConfig;
import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ActivityForgetPassBinding;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class ForgetPassActivity extends BaseActivity {

    private ActivityForgetPassBinding mBinding;
    private static final String TAG = "ForgetPassActivity";
    private int mode=-1;
    private final int MODE_RESET=101;
    private final int MODE_GO_LOGIN=102;
    private final int MODE_DEFAULT=-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityForgetPassBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        init();
        addListener();
    }

    private void init(){
        mBinding.versionTxt.setText(BuildConfig.VERSION_NAME);
        mode=MODE_RESET;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void addListener(){
        mBinding.resetPassBtn.setOnClickListener(v -> {
            if (mode==MODE_RESET){
                if (isEmailValid()){
                    mBinding.loadPg.setVisibility(View.VISIBLE);
                    resetPass(mBinding.emailEt.getText().toString().trim());
                }
            }else if (mode==MODE_GO_LOGIN){
                onBackPressed();
            }
        });
        mBinding.emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) && mBinding.emailInp.isErrorEnabled()){
                    mBinding.emailInp.setError("");
                    mBinding.emailInp.setErrorEnabled(false);
                }
            }
        });
    }

    private boolean isEmailValid(){
        boolean can=true;
        String email=mBinding.emailEt.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            mBinding.emailInp.setErrorEnabled(true);
            mBinding.emailInp.setError("Please Enter Email id");
            can=false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mBinding.emailInp.setErrorEnabled(true);
            mBinding.emailInp.setError("Please Enter valid Email id");
            can=false;
        }
        return can;
    }

    private void resetPass(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task!=null){
                        if (task.isSuccessful()){
                            mBinding.emailInp.setEnabled(false);
                            mBinding.emailEt.setEnabled(false);
                            mBinding.resetPassBtn.setEnabled(true);
                            mBinding.msgTv.setVisibility(View.VISIBLE);
                            mBinding.resetPassBtn.setText("Back To Login");
                            mode=MODE_GO_LOGIN;
                        }else if (task.getException()!=null){
                            Exception e=task.getException();
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);
                            Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Log.d(TAG, "resetPass: task is null");
                        FirebaseCrashlytics.getInstance().log("Reset Pass, task is null,email ID:="+email);
                        Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                    }
                    mBinding.loadPg.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().log("Reset Password >>> Email id:="+email+"\t reason:="+e.getMessage());
                    FirebaseCrashlytics.getInstance().recordException(e);
                    Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                    mBinding.loadPg.setVisibility(View.GONE);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Forget Password");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
