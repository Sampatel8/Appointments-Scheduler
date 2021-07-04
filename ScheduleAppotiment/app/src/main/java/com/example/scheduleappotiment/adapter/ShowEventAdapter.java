package com.example.scheduleappotiment.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleappotiment.databinding.ItemShowEventBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.utility.CommonUtility;

import java.util.ArrayList;

public class ShowEventAdapter extends RecyclerView.Adapter<ShowEventAdapter.MyViewHolder> {

    private ArrayList<Appointment> appointments;

    public ShowEventAdapter(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemShowEventBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Appointment item=appointments.get(position);
        holder.mBinding.eventTitleTv.setText(item.getTitle());
        if (!TextUtils.isEmpty(item.getDescription())){
            holder.mBinding.eventDescTv.setVisibility(View.VISIBLE);
            holder.mBinding.eventDescTv.setText(item.getDescription());
        }else holder.mBinding.eventDescTv.setVisibility(View.GONE);
        try{
            holder.mBinding.eventDateTv.setText(CommonUtility.dateInShowEvent(item.getDateTime()));
            holder.mBinding.eventTimeTv.setText(CommonUtility.timeInShowEvent(item.getDateTime()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return appointments!=null?appointments.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ItemShowEventBinding mBinding;
        public MyViewHolder(@NonNull ItemShowEventBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding=mBinding;
        }
    }
}
