package com.example.scheduleappotiment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.scheduleappotiment.BuildConfig;
import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ActivityLoginBinding;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;


public class SignUpActivity extends BaseActivity {

    private ActivityLoginBinding mBinding;
    private static final String TAG = "SignUpActivity";
    private String uid = null;
    private FirebaseUser user = null;
    private Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
        addListener();
    }

    private void initView() {
        handler=new Handler(Looper.getMainLooper());
        mBinding.versionTxt.setText(BuildConfig.VERSION_NAME);
        SpannableString ss = new SpannableString(getString(R.string.have_account_text));
        ss.setSpan(new UnderlineSpan(), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.forgetTxt.setText(ss);
        mBinding.signupTxt.setVisibility(View.GONE);
        mBinding.nameInp.setVisibility(View.VISIBLE);
        mBinding.cpassTip.setVisibility(View.VISIBLE);
        mBinding.loginBtn.setText("Sign Up");
        ViewGroup.LayoutParams oldParams=mBinding.cardLinear.getLayoutParams();
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(oldParams.width,oldParams.height);
        params.setMargins(0,getResources().getDimensionPixelOffset(R.dimen._5sdp),0,getResources().getDimensionPixelSize(R.dimen._10sdp));
        mBinding.cardLinear.setLayoutParams(params);
        mBinding.cardLinear.requestLayout();
    }

    private void addListener() {
        mBinding.loginBtn.setOnClickListener(v -> {
            if (isCanLogin()) {
                signUp(mBinding.emailEt.getText().toString().trim(), mBinding.passEt.getText().toString().trim());
            }
        });
        mBinding.emailEt.setOnFocusChangeListener((v, isFocus) -> {
            if (isFocus) removeErrorOnInputLayout(mBinding.emailTip);
        });
        mBinding.passEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) removeErrorOnInputLayout(mBinding.passTip);
        });
        mBinding.cpassEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) removeErrorOnInputLayout(mBinding.cpassTip);
        });
        mBinding.forgetTxt.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
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
                if (!TextUtils.isEmpty(s.toString().trim()) && mBinding.emailTip.isErrorEnabled()) {
                    removeErrorOnInputLayout(mBinding.emailTip);
                }
            }
        });
        mBinding.passEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) && mBinding.passTip.isErrorEnabled()) {
                    removeErrorOnInputLayout(mBinding.passTip);
                }
            }
        });
        mBinding.cpassEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim()) && mBinding.cpassTip.isErrorEnabled()) {
                    removeErrorOnInputLayout(mBinding.cpassTip);
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
                if (!TextUtils.isEmpty(s.toString().trim()) && mBinding.nameInp.isErrorEnabled()) {
                    removeErrorOnInputLayout(mBinding.nameInp);
                }
            }
        });
    }

    private boolean isCanLogin() {
        boolean canLogin = true;
        String email = mBinding.emailEt.getText().toString().trim();
        String pass = mBinding.passEt.getText().toString().trim();
        String code = mBinding.codeEt.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            setErrorOnInputLayout(mBinding.emailTip, "Email id is required");
            canLogin = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setErrorOnInputLayout(mBinding.emailTip, "Enter Valid Email id");
            canLogin = false;
        }
        if (TextUtils.isEmpty(pass)) {
            setErrorOnInputLayout(mBinding.passTip, "Password is required");
            canLogin = false;
        }
        String cPass = mBinding.cpassEt.getText().toString().trim();
        if (TextUtils.isEmpty(cPass)) {
            setErrorOnInputLayout(mBinding.cpassTip, "Please Reenter password");
            canLogin = false;
        } else if (!pass.equals(cPass)) {
            setErrorOnInputLayout(mBinding.passTip, "Your Password & ReEnter Password doesn't matches");
            canLogin = false;
        }

        if (TextUtils.isEmpty(code)) {
            setErrorOnInputLayout(mBinding.codeTip, "Code is required");
            canLogin = false;
        }
        if (TextUtils.isEmpty(mBinding.nameEt.getText().toString().trim())){
            setErrorOnInputLayout(mBinding.nameInp,"Name is require");
            canLogin=false;
        }
        return canLogin;
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

    private void signUp(String email, String pass) {
        mBinding.loadPg.setVisibility(View.VISIBLE);
        setEnable(false);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task != null) {
                        if (task.isSuccessful()) {
                            uid = task.getResult().getUser().getUid();
                            user = task.getResult().getUser();
                            Toast.makeText(this, "SignUp Successful", Toast.LENGTH_LONG).show();
                            editProfile();
                            //finish();
                           // saveOnApi();
                        } else if (task.getException() != null) {
                            Exception e = task.getException();
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().log(e.getMessage());
                            FirebaseCrashlytics.getInstance().recordException(e);
                            Log.d(TAG, "signUp: exception caught,reason:=" + e.getMessage());
                            Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_LONG).show();
                            mBinding.loadPg.setVisibility(View.GONE);
                            setEnable(true);
                        } else {
                            Log.d(TAG, "signUp: failed...");
                            Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                            mBinding.loadPg.setVisibility(View.GONE);
                            setEnable(true);
                        }
                    } else {
                        Log.d(TAG, "signUp: failed task is null");
                        FirebaseCrashlytics.getInstance().log("SignUp failed is null");
                        Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                        mBinding.loadPg.setVisibility(View.GONE);
                        setEnable(true);
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(e);
                    Log.d(TAG, "signUp: failed:=" + e.getMessage());
                    Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                    mBinding.loadPg.setVisibility(View.GONE);
                    setEnable(true);
                });
    }

    private void editProfile() {
        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();

    }


    private void setEnable(boolean enable){
        mBinding.emailTip.setEnabled(enable);
        mBinding.nameInp.setEnabled(enable);
        mBinding.codeTip.setEnabled(enable);
        mBinding.passTip.setEnabled(enable);
        mBinding.cpassTip.setEnabled(enable);
        mBinding.loginBtn.setEnabled(enable);
    }

}
