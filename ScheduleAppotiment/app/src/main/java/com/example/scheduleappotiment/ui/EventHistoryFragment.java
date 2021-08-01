package com.example.scheduleappotiment.ui;

import android.content.Context;
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
import com.example.scheduleappotiment.databinding.FragmentEventHistoryBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;

import java.util.ArrayList;

public class EventHistoryFragment extends Fragment {

   public EventHistoryFragment(){
        super(R.layout.fragment_event_history);
    }

    private Context mContext;
    private FragmentEventHistoryBinding mBinding;
    private ShowEventAdapter mAdapter;
    private ArrayList<Appointment> mainList,showList;
    private boolean cancelAppointment=false;
    private boolean pendingAppointment=false;
    private Handler handler;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding=FragmentEventHistoryBinding.bind(view);
        init();
        addListener();
    }

    private void init(){
        handler=new Handler(Looper.getMainLooper());
        mBinding.loadPg.setVisibility(View.GONE);
        mBinding.filterLinear.setVisibility(View.GONE);
    }

    private void addListener(){
        mBinding.cancelChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cancelAppointment=isChecked;
            updateList();
        });
        mBinding.pendingChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            pendingAppointment=isChecked;
            updateList();
        });
    }

    private void createMainList(){
        mainList=new ArrayList<>();
       }

    private void updateList(){
        if (cancelAppointment){
            for (Appointment app:mainList){
                if (app.getStatus()==MyConstant.CANCEL_APPOINTMENT && !showList.contains(app)){
                    showList.add(app);
                }
            }
        }else{
            for (Appointment app:showList){
                if (app.getStatus()==MyConstant.CANCEL_APPOINTMENT){
                    showList.remove(app);
                }
            }
        }
        if (pendingAppointment){
            for (Appointment app:mainList){
                if (app.getStatus()==MyConstant.PENDING_APPOINTMENT && !showList.contains(app)){
                    showList.add(app);
                }
            }
        }else{
            for (Appointment app:showList){
                if (app.getStatus()==MyConstant.PENDING_APPOINTMENT){
                    showList.remove(app);
                }
            }
        }
        if (showList!=null && showList.size()>0) showRvList();
        else hideList();
    }

    private void showRvList(){
        if (showList != null && showList.size() > 0) {
            handler.post(() -> {
                mBinding.eventRv.setVisibility(View.VISIBLE);
                mBinding.noItemTv.setVisibility(View.GONE);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                mBinding.eventRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                mAdapter = new ShowEventAdapter(showList, mContext, position -> {
                    Intent intent = new Intent(requireActivity(), AppointmentDetails.class);
                    intent.putExtra("appointment", showList.get(position));
                    startActivity(intent);
                });
                mBinding.eventRv.setAdapter(mAdapter);
                mBinding.filterLinear.setVisibility(View.VISIBLE);
            });
        } else hideList();
        showProgress(false);
    }
    private void showProgress(boolean show) {
        handler.post(() -> mBinding.loadPg.setVisibility(show ? View.VISIBLE : View.GONE));
    }


    private void hideList(){
        mBinding.loadPg.setVisibility(View.GONE);
        mBinding.eventRv.setVisibility(View.GONE);
        mBinding.noItemTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.loadPg.setVisibility(View.VISIBLE);
        new Thread(()->{
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            createMainList();
            showList=new ArrayList<>();
            for (Appointment app:mainList){
                if (app.getStatus()==MyConstant.ATTEND_APPOINTMENT)showList.add(app);
            }
            handler.post(this::showRvList);
        }).start();
    }
}
