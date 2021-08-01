package com.example.scheduleappotiment.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleappotiment.databinding.ItemTimeSlotTimeBinding;
import com.example.scheduleappotiment.model.apimodel.Availability;
import com.example.scheduleappotiment.utility.CommonUtility;

import java.util.ArrayList;

public class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.MyViewHolder> {
    private ArrayList<Availability> mSlotList;
    private AvailabilityItemClick itemClick;
    public AvailabilityAdapter(ArrayList<Availability> mSlotList,AvailabilityItemClick click) {
        this.mSlotList = mSlotList;
        this.itemClick=click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemTimeSlotTimeBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Availability avail=mSlotList.get(position);
        holder.mBinding.dayTimeSlotTv.setText(avail.getDayOfWeek());
        StringBuilder sb=new StringBuilder();
        sb.append(CommonUtility.getTimeIn12HourMinute(CommonUtility.getDateFrom24HourTime(avail.getStartTime())));
        sb.append(" to ");
        sb.append(CommonUtility.getTimeIn12HourMinute(CommonUtility.getDateFrom24HourTime(avail.getEndTime())));
        holder.mBinding.timeTimeSlotTv.setText(sb.toString());
        holder.mBinding.mainCard.setOnClickListener(v->{
            itemClick.onItemClick(avail);
        });
    }

    @Override
    public int getItemCount() {
        return (mSlotList!=null)?mSlotList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemTimeSlotTimeBinding mBinding;
        public MyViewHolder(@NonNull ItemTimeSlotTimeBinding itemView) {
            super(itemView.getRoot());
            mBinding=itemView;
        }
    }

    public interface AvailabilityItemClick {
        void onItemClick(Availability avail);
    }
}
