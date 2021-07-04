package com.example.scheduleappotiment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.adapter.ShowEventAdapter;
import com.example.scheduleappotiment.databinding.FragmentHomeBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    private FragmentHomeBinding mBinding;
    private ArrayList<Appointment> appointmentList;
    private ShowEventAdapter mAdapter;
    private Handler handler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding=FragmentHomeBinding.bind(view);
        mBinding.loadPg.setVisibility(View.VISIBLE);
        init();
        addListener();
    }

    private void init(){
        handler=new Handler(Looper.getMainLooper());
    }

    private void addListener(){
        mBinding.addFloatBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),MakeAppointment.class));
        });
    }
    private void getAppointmentList(){
        appointmentList=new ArrayList<>();
        appointmentList.add(new Appointment("First Appointment","To check the correct functionality", CommonUtility.getTimeInMilli(27,7,2021,11,30),null, MyConstant.UPCOMING_APPOINTMENT));
        appointmentList.add(new Appointment("Take for project discussion",CommonUtility.getTimeInMilli(13,8,2021,13,40),null,MyConstant.UPCOMING_APPOINTMENT));
        appointmentList.add(new Appointment("To meet up","To discuses the future project guidance",CommonUtility.getTimeInMilli(11,7,2021,10,45),null,MyConstant.UPCOMING_APPOINTMENT));
    }

    private void setRecyclerView(){
        mBinding.loadPg.setVisibility(View.GONE);
        if (appointmentList!=null && appointmentList.size()>0){
            mBinding.noItemTv.setVisibility(View.GONE);
            mBinding.eventRv.setVisibility(View.VISIBLE);
            mBinding.eventRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            mAdapter=new ShowEventAdapter(appointmentList);
            mBinding.eventRv.setAdapter(mAdapter);
        }else{
            mBinding.eventRv.setVisibility(View.GONE);
            mBinding.noItemTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.loadPg.setVisibility(View.VISIBLE);
        new Thread(()->{
            try{
                Thread.sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }
            getAppointmentList();
            handler.post(this::setRecyclerView);
        }).start();
    }
}
