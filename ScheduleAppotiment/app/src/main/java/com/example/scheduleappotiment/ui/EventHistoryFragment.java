package com.example.scheduleappotiment.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.adapter.ShowEventAdapter;
import com.example.scheduleappotiment.databinding.FragmentEventHistoryBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventHistoryFragment extends Fragment {

    public EventHistoryFragment() {
        super(R.layout.fragment_event_history);
    }

    private Context mContext;
    private FragmentEventHistoryBinding mBinding;
    private ShowEventAdapter mAdapter;
    private ArrayList<Appointment> mainList, showList;
    private boolean cancelAppointment = false;
    private boolean pendingAppointment = false;
    private Handler handler;
    private static final String TAG = "EventHistoryFragment";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentEventHistoryBinding.bind(view);
        init();
        addListener();
    }

    private void init() {
        handler = new Handler(Looper.getMainLooper());
        mBinding.loadPg.setVisibility(View.GONE);
        mBinding.filterLinear.setVisibility(View.GONE);
    }

    private void addListener() {
        /*mBinding.cancelChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cancelAppointment = isChecked;
            updateList();
        });
        mBinding.pendingChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            pendingAppointment = isChecked;
            updateList();
        });*/
        mBinding.cancelChip.setOnClickListener(v->{
            cancelAppointment=(!cancelAppointment);
            updateList();
            changeChipState(cancelAppointment,mBinding.cancelChip);
        });
        mBinding.pendingChip.setOnClickListener(v->{
            pendingAppointment=(!pendingAppointment);
            updateList();
            changeChipState(pendingAppointment,mBinding.pendingChip);
        });
    }

    private void createMainList() {
        mainList = new ArrayList<>();
        mainList.add(new Appointment("For Team setup", "to finalize team member", CommonUtility.getTimeInMilli(25, 8, 2021, 11, 0), null, MyConstant.ATTEND_APPOINTMENT, null, null, null));
        mainList.add(new Appointment("To lean app resource", CommonUtility.getTimeInMilli(7, 7, 2021, 14, 15), null, MyConstant.PENDING_APPOINTMENT, null));
        mainList.add(new Appointment("Discuss meetUp", CommonUtility.getTimeInMilli(1, 7, 2021, 11, 35), null, MyConstant.CANCEL_APPOINTMENT, null));
        mainList.add(new Appointment("show first Prototype", CommonUtility.getTimeInMilli(3, 7, 2021, 13, 5), null, MyConstant.REJECTED_APPOINTMENT, null));
    }

    private void updateList() {
        showProgress(true);
        if (cancelAppointment) {
            for (Appointment app : mainList) {
                if (app.getServerStatus().equals(MyConstant.STATUS_REJECTED) && !showList.contains(app)) {
                    showList.add(app);
                }
            }
        } else {
            ArrayList<Appointment> tmpList=new ArrayList<>();
            for (int i=0;i<showList.size();i++) {
                Appointment app=showList.get(i);
                if (app.getServerStatus().equals(MyConstant.STATUS_REJECTED)) {
                    tmpList.add(app);
                }
            }
            showList.removeAll(tmpList);
        }
        if (pendingAppointment) {
            for (Appointment app : mainList) {
                if (app.getServerStatus().equals(MyConstant.STATUS_PENDING) && !showList.contains(app)) {
                    showList.add(app);
                }
            }
        } else {
            ArrayList<Appointment> tmpList=new ArrayList<>();
            for (int i=0;i<showList.size();i++) {
                Appointment app=showList.get(i);
                if (app.getServerStatus().equals(MyConstant.STATUS_PENDING)) {
                    tmpList.add(app);
                }
            }
            showList.removeAll(tmpList);
        }
        if (showList != null && showList.size() > 0) showRvList();
        else hideList();
        showProgress(false);
    }

    private void showRvList() {
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

    private void hideList() {
        handler.post(() -> {
            mBinding.loadPg.setVisibility(View.GONE);
            mBinding.eventRv.setVisibility(View.GONE);
            mBinding.noItemTv.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainList == null) getAppointmentList();
        /*new Thread(()->{
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
        }).start();*/
    }

    private void getAppointmentList() {
        if (!CommonUtility.isEmpty(MySharedPref.getInstance(mContext).getContactId())) {
            showProgress(true);
            new Thread(() -> {
                if (MyConstant.mToken == null) CommonUtility.getBearerToken();
                OkHttpClient client = new OkHttpClient();
                Request request;
                try {
                    request = new Request.Builder()
                            .url(MyConstant.MY_URL + "appointment?contactId=" + MySharedPref.getInstance(requireContext()).getContactId())
                            .get()
                            .addHeader("authorization", "Bearer " + MyConstant.mToken)
                            .addHeader("content-type", "application/json")
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "getAppointmentList: can't build request,message" + e.getMessage());
                    return;
                }
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        mainList = new ObjectMapper().readValue(res, new TypeReference<List<Appointment>>() {
                        });
                        showList = new ArrayList<>();
                        for (Appointment app : mainList) {
                            if (app.getServerStatus().equals(MyConstant.STATUS_APPROVED))
                                showList.add(app);
                        }
                        if (showList != null && showList.size() > 0) showRvList();
                        else hideList();
                    } else {
                        handler.post(() -> Toast.makeText(mContext, "Request is failed..", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "getAppointmentList: exception occurred" + e.getMessage());
                    handler.post(() -> {
                        mBinding.loadPg.setVisibility(View.GONE);
                        Toast.makeText(mContext, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        }
    }

    private void showProgress(boolean show) {
        handler.post(() -> mBinding.loadPg.setVisibility(show ? View.VISIBLE : View.GONE));
    }

    private void changeChipState(boolean isSelected, Chip chip){
        if (isSelected){
            chip.setChipStrokeWidth(getResources().getDimension(R.dimen._2sdp));
            chip.setElevation(getResources().getDimensionPixelSize(R.dimen._5sdp));
        }else{
            chip.setChipStrokeWidth(0);
            chip.setElevation(0);
        }
    }
}
