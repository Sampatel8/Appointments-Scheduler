package com.example.scheduleappotiment.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ActivityEditProfileBinding;
import com.example.scheduleappotiment.databinding.FragmentProfileBinding;
import com.example.scheduleappotiment.model.apimodel.Contact;
import com.example.scheduleappotiment.model.apimodel.LoginApiModel;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    private FragmentProfileBinding mBinding;
    private Handler mHandler;
    private Context mContext;
    private Contact mContact;
    private static final String TAG = "ProfileFragment";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentProfileBinding.bind(view);
        init();
        addListener();
        setHasOptionsMenu(true);
    }

    private void init() {
        mHandler = new Handler(Looper.getMainLooper());
        if (MySharedPref.getInstance(getContext()).getBoolean(MyConstant.IS_PC)) {
            mBinding.myTimeSlotBtn.setVisibility(View.VISIBLE);
        } else mBinding.myTimeSlotBtn.setVisibility(View.GONE);
    }

    private void addListener() {
        mBinding.myTimeSlotBtn.setOnClickListener(v -> {

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mContact == null) getContact();
    }

    private void getContact() {
        changePg(true);
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            LoginApiModel model = new LoginApiModel(FirebaseAuth.getInstance().getCurrentUser().getUid());
            String body = null;
            try {
                body = new ObjectMapper().writeValueAsString(model);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            assert body != null;
            if (MyConstant.mToken == null) CommonUtility.getBearerToken();
            Request request = new Request.Builder()
                    .url(MyConstant.MY_URL + "login")
                    .post(RequestBody.create(body, MediaType.parse("application/json")))
                    .addHeader("authorization", "Bearer " + MyConstant.mToken)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Contact contact = new ObjectMapper().readValue(res, Contact.class);
                    if (contact != null) {
                        mContact = contact;
                        setProfile();
                    } else {
                        Log.d(TAG, "getContact: Contact is null");
                        mHandler.post(()->{
                            Toast.makeText(mContext, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Log.d(TAG, "getContact: response is failed,code:=" + response.code());
                    Log.d(TAG, "getContact: fail message:=" + response.message());
                    mHandler.post(()->{
                        Toast.makeText(mContext, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                    });
                }
                changePg(false);
            } catch (IOException e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
                changePg(false);
                mHandler.post(()->{
                    Toast.makeText(mContext, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void changePg(boolean show) {
        mHandler.post(() -> {
            mBinding.loadPg.setVisibility(show ? View.VISIBLE : View.GONE);
        });
    }

    private void setProfile() {
        if (mContact != null) {
            mHandler.post(() -> {
                if (!CommonUtility.isEmpty(mContact.getLastName()))
                    mBinding.namePtv.setText(mContact.getFirstName() + " " + mContact.getLastName());
                else mBinding.namePtv.setText(mContact.getFirstName());

                if (!CommonUtility.isEmpty(mContact.getDob()))
                    mBinding.dobPtv.setText(mContact.getDob());
                else mBinding.dobPtv.setText("Please Provide Your DOB");

                if (!CommonUtility.isEmpty(mContact.getGender()))
                    mBinding.genderPtv.setText(mContact.getGender());
                else mBinding.genderPtv.setText("Please Provide Your gender");

                if (!CommonUtility.isEmpty(mContact.getEmail())) {
                    mBinding.emailPtv.setText(mContact.getEmail());
                    mBinding.emailCard.setVisibility(View.VISIBLE);
                } else mBinding.emailCard.setVisibility(View.GONE);


                mBinding.nameCard.setVisibility(View.VISIBLE);
                mBinding.genderCard.setVisibility(View.VISIBLE);
                mBinding.emailCard.setVisibility(View.VISIBLE);
                mBinding.dobCard.setVisibility(View.VISIBLE);
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment_menu, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ed_profile) {
            if (mContact != null) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("contact", mContact);
                intent.putExtra("isEdit",true);
                intent.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
            } else {
                Toast.makeText(mContext, R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
