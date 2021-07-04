package com.example.scheduleappotiment.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {

    public SettingFragment(){
        super(R.layout.fragment_setting);
    }

    private FragmentSettingBinding mBinding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding=FragmentSettingBinding.bind(view);
    }


}
