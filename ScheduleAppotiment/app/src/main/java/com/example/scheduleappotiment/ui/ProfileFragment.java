package com.example.scheduleappotiment.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    public ProfileFragment(){
        super(R.layout.fragment_profile);
    }

    private FragmentProfileBinding mBinding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding=FragmentProfileBinding.bind(view);
    }

}
