package com.example.scheduleappotiment.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scheduleappotiment.BuildConfig;
import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.FragmentSettingBinding;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {

    public SettingFragment(){
        super(R.layout.fragment_setting);
    }

    private FragmentSettingBinding mBinding;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding=FragmentSettingBinding.bind(view);
        mBinding.versionTxt.setText(BuildConfig.VERSION_NAME);
        mBinding.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            try {
                MySharedPref.getInstance(mContext).removeAll();
                Intent login = new Intent(requireActivity(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
                getActivity().finish();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(mContext, "Some wrong,please reopen app", Toast.LENGTH_SHORT).show();
                getActivity().finishAndRemoveTask();
            }
        });
    }


}
