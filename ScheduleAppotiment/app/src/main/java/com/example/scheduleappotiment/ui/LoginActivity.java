package com.example.scheduleappotiment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.scheduleappotiment.BuildConfig;
import com.example.scheduleappotiment.MainActivity;
import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ActivityLoginBinding;
import com.example.scheduleappotiment.model.apimodel.Contact;
import com.example.scheduleappotiment.model.apimodel.NewContact;
import com.example.scheduleappotiment.model.apimodel.LoginApiModel;
import com.example.scheduleappotiment.utility.BaseActivity;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding mBinding;
    private static final String TAG = "LoginActivity";
    private String uid=null;
    private FirebaseUser user=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
        addListener();
    }

    private void initView() {
        mBinding.versionTxt.setText(BuildConfig.VERSION_NAME);
        SpannableString ss=new SpannableString(getString(R.string.forget_pass_text));
        ss.setSpan(new UnderlineSpan(),0,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.forgetTxt.setText(ss);
        mBinding.cpassTip.setVisibility(View.GONE);
        SpannableString ss1=new SpannableString(getString(R.string.sign_up_text));
        ss1.setSpan(new UnderlineSpan(),0,ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.signupTxt.setText(ss1);
        mBinding.signupTxt.setVisibility(View.VISIBLE);
        mBinding.codeTip.setVisibility(View.GONE);
    }

    private void addListener() {
        mBinding.loginBtn.setOnClickListener(v -> {
            if (isCanLogin()) {
                signIn(mBinding.emailEt.getText().toString().trim(),mBinding.passEt.getText().toString().trim());
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
            startActivity(new Intent(LoginActivity.this,ForgetPassActivity.class));
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
                if (!TextUtils.isEmpty(s.toString().trim()) && mBinding.emailTip.isErrorEnabled()){
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
                if (TextUtils.isEmpty(s.toString().trim()) && mBinding.passTip.isErrorEnabled()){
                    removeErrorOnInputLayout(mBinding.passTip);
                }
            }
        });
        mBinding.signupTxt.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            finish();
        });
    }

    private boolean isCanLogin() {
        boolean canLogin = true;
        String email = mBinding.emailEt.getText().toString().trim();
        String pass = mBinding.passEt.getText().toString().trim();

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
//        if (mode == SIGN_UP_MODE) {
//            String cPass = mBinding.cpassEt.getText().toString().trim();
//            if (TextUtils.isEmpty(cPass)) {
//                setErrorOnInputLayout(mBinding.cpassTip, "Please Reenter password");
//                canLogin = false;
//            } else if (!pass.equals(cPass)) {
//                setErrorOnInputLayout(mBinding.passTip, "Your Password & ReEnter Password doesn't matches");
//                canLogin = false;
//            }
//        }
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

    private void signIn(String email, String pass) {
        dismiss(true);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task != null) {
                        if (task.isSuccessful()) {
                            uid=task.getResult().getUser().getUid();
                            user=task.getResult().getUser();
                            //Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            //redirectToMain();
                            //finish();
                            getUserContact(uid);
                        } else if (task.getException() != null) {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Log.d(TAG, "signIn: fail reason may be invalid password:="+task.getException().getMessage());
                                Toast.makeText(this, "The password is invalid", Toast.LENGTH_SHORT).show();
                                dismiss(false);
                                return;
                            }
                            Exception e = task.getException();
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().log("Sign in fail..");
                            FirebaseCrashlytics.getInstance().recordException(e);
                            Log.d(TAG, "signIn: task is unsuccessful,reason:=" + e.getMessage());
                            //Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                            dismiss(false);
                        } else {
                            Log.d(TAG, "signIn: task is complete with failure");
                            Toast.makeText(this, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                            dismiss(false);
                        }
                    } else {
                        Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                        dismiss(false);
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidCredentialsException){
                        Log.d(TAG, "signIn: fail reason may be invalid password:="+e.getMessage());
                        Toast.makeText(this, "The password is invalid", Toast.LENGTH_SHORT).show();
                    }else if (e instanceof FirebaseAuthInvalidUserException){
                        Log.d(TAG, "signIn: fail reason may be invalid email id:="+e.getMessage());
                        Toast.makeText(this, "The email address is not registered with us", Toast.LENGTH_SHORT).show();
                    }else {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        Log.d(TAG, "signIn: Fail,reason:-" + e.getMessage());
                        Toast.makeText(this, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                    }
                    dismiss(false);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.emailEt.setText("");
        mBinding.passEt.setText("");
    }

    private void redirectToMain(){
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("uid",uid);
        //intent.putExtra("code",mBinding.codeEt.getText().toString());
        //intent.putExtra("email",mBinding.emailEt.getText().toString());
        startActivity(intent);
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
                        MySharedPref pref=MySharedPref.getInstance(LoginActivity.this);
                        pref.setContactID(contact.getId());
                        pref.setString(MyConstant.FIRST_NAME,contact.getFirstName());
                        pref.setString(MyConstant.LAST_NAME,contact.getLastName());
                        pref.setBoolean(MyConstant.IS_PC,contact.getIs_PC());
                        runOnUiThread(()->{
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                            redirectToMain();
                            finish();
                        });
                    }else{
                        runOnUiThread(()-> failLoginAPiCall(null));
                    }
                }else{
                    runOnUiThread(()->failLoginAPiCall(null));
                }
                dismiss(false);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "getUserContact: "+e.getMessage());
                runOnUiThread(()->failLoginAPiCall(null));
                dismiss(false);
            }
        });
    }

    private void failLoginAPiCall(String reason)
    {
        Toast.makeText(this, reason!=null?reason:getString(R.string.some_wrong_try_again), Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
    }

    private void dismiss(boolean show){
        runOnUiThread(()->{
            mBinding.loadPg.progressMainLl.setVisibility(show?View.VISIBLE:View.GONE);
            mBinding.transView.setVisibility(show?View.VISIBLE:View.GONE);
        });
    }
}
