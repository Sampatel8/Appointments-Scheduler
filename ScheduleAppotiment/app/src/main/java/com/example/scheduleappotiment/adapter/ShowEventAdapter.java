package com.example.scheduleappotiment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ItemShowEventBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;

import java.util.ArrayList;

public class ShowEventAdapter extends RecyclerView.Adapter<ShowEventAdapter.MyViewHolder> {

    private ArrayList<Appointment> appointments;
    private Context context;
    private RecycleItemClickListener mClickListener;
    public ShowEventAdapter(ArrayList<Appointment> appointments, Context context,RecycleItemClickListener clickListener) {
        this.appointments = appointments;
        this.context = context;
        this.mClickListener=clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemShowEventBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Appointment item = appointments.get(position);
        holder.mBinding.eventTitleTv.setText(item.getTitle());
        if (!TextUtils.isEmpty(item.getDescription())) {
            holder.mBinding.eventDescTv.setVisibility(View.VISIBLE);
            holder.mBinding.eventDescTv.setText(item.getDescription());
        } else holder.mBinding.eventDescTv.setVisibility(View.GONE);
        try {
            holder.mBinding.eventDateTv.setText(CommonUtility.getDateFromSlotObj(item.getSlot().getDate()));
            holder.mBinding.eventTimeTv.setText(context.getString(R.string.show_event_time,
                    CommonUtility.getTimeFromSlotObj(item.getSlot().getStartTimeC()),
                    CommonUtility.getTimeFromSlotObj(item.getSlot().getEndTimeC())));
            holder.mBinding.eventStatusTv.setText(item.getServerStatus());
            switch (item.getServerStatus()){
                case MyConstant.STATUS_APPROVED:
                case MyConstant.STATUS_RESCHEDULE:
                    holder.mBinding.eventStatusTv.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.green_msg)));
                    break;
                case MyConstant.STATUS_REJECTED:
                    holder.mBinding.eventStatusTv.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
                    break;
                case MyConstant.STATUS_PENDING:
                    holder.mBinding.eventStatusTv.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.yellow_pending)));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.mBinding.getRoot().setOnClickListener(v->{
            mClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return appointments != null ? appointments.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemShowEventBinding mBinding;

        public MyViewHolder(@NonNull ItemShowEventBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
