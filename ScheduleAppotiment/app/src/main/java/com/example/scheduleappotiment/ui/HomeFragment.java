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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.adapter.ShowEventAdapter;
import com.example.scheduleappotiment.databinding.FragmentHomeBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;
import com.example.scheduleappotiment.utility.MySharedPref;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    private FragmentHomeBinding mBinding;
    private ArrayList<Appointment> appointmentList;
    private ShowEventAdapter mAdapter;
    private Handler handler;
    private static final String TAG = "HomeFragment";
    private Context mContext;
    private boolean needToUpdate=false;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

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
        getAppointmentList();
    }

    private void init(){
        handler=new Handler(Looper.getMainLooper());
    }

    private void addListener(){
        mBinding.addFloatBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),MakeAppointment.class));
            needToUpdate=true;
        });
    }

    private void getAppointmentList(){
        if (!CommonUtility.isEmpty(MySharedPref.getInstance(requireActivity()).getContactId())){
            mBinding.loadPg.setVisibility(View.VISIBLE);
            new Thread(()->{
                if (MyConstant.mToken==null)CommonUtility.getBearerToken();
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(MyConstant.MY_URL+"appointment?contactId="+MySharedPref.getInstance(requireContext()).getContactId())
                    .get()
                    .addHeader("authorization", "Bearer "+MyConstant.mToken)
                    .addHeader("content-type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    String res=response.body().string();
                    List<Appointment> tmpList=new ObjectMapper().readValue(res,new TypeReference<List<Appointment>>(){});
                    appointmentList=new ArrayList<>();
                    for (Appointment app:tmpList){
                        if (!app.getServerStatus().equals(MyConstant.STATUS_REJECTED))
                            appointmentList.add(app);
                    }
                    setRecyclerView();
                }else{
                    handler.post(()->{Toast.makeText(mContext, "Request is failed..", Toast.LENGTH_SHORT).show();});
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, "getAppointmentList: exception occurred"+e.getMessage());
                handler.post(()->{
                    mBinding.loadPg.setVisibility(View.GONE);
                    Toast.makeText(mContext, R.string.some_wrong, Toast.LENGTH_SHORT).show();
                });
            }
            }).start();
        }else {
            Toast.makeText(requireActivity(), R.string.some_wrong_try_again, Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecyclerView(){
        handler.post(()->{
        if (appointmentList!=null && appointmentList.size()>0){
            mBinding.noItemTv.setVisibility(View.GONE);
            mBinding.eventRv.setVisibility(View.VISIBLE);
            mBinding.eventRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            mAdapter=new ShowEventAdapter(appointmentList,mContext,position->{
                Intent intent=new Intent(requireActivity(),AppointmentDetails.class);
                intent.putExtra("appointment",appointmentList.get(position));
                startActivity(intent);
            });
            mBinding.eventRv.setAdapter(mAdapter);
        }else{
            mBinding.eventRv.setVisibility(View.GONE);
            mBinding.noItemTv.setVisibility(View.VISIBLE);
        }
        mBinding.loadPg.setVisibility(View.GONE);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appointmentList!=null && AppointmentDetails.isRescheduleSuccess){
            getAppointmentList();
        }else if (needToUpdate){
            getAppointmentList();
        }
        needToUpdate=false;
    }
}
