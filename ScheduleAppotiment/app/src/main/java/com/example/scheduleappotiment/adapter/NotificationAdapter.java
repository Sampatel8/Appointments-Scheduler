package com.example.scheduleappotiment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ItemNotificationBinding;
import com.example.scheduleappotiment.model.Appointment;
import com.example.scheduleappotiment.model.Notification;
import com.example.scheduleappotiment.utility.CommonUtility;
import com.example.scheduleappotiment.utility.MyConstant;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private ArrayList<Notification> mList;
    private Context mContext;
    private NotificationItem mClickListener;

    public NotificationAdapter(ArrayList<Notification> mList, Context mContext, NotificationItem mClickListener) {
        this.mList = mList;
        this.mContext = mContext;
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notification.NotificationAppointment item = mList.get(position).getAppointment();
        if (!CommonUtility.isEmpty(item.getFromContactNameC())){holder.mBinding.fromContactTv.setText(item.getFromContactNameC());
        holder.mBinding.fromContactTv.setVisibility(View.VISIBLE);}
        else holder.mBinding.fromContactTv.setVisibility(View.GONE);
        holder.mBinding.msgTv.setText(mContext.getString(R.string.notification_msg,
                item.getFromContactNameC(),
                item.getName(),
                CommonUtility.getDateFromSlotObj(item.getDateC()),
                CommonUtility.getTimeFromSlotObj(item.getStartTimeC())));
        holder.mBinding.statusTv.setText(item.getStatusC());
        switch (item.getStatusC()){
            case MyConstant.STATUS_APPROVED:
            case MyConstant.STATUS_RESCHEDULE:
                holder.mBinding.statusTv.setCompoundDrawableTintList(ColorStateList.valueOf(mContext.getColor(R.color.green_msg)));
                break;
            case MyConstant.STATUS_REJECTED:
                holder.mBinding.statusTv.setCompoundDrawableTintList(ColorStateList.valueOf(mContext.getColor(R.color.red)));
                break;
            case MyConstant.STATUS_PENDING:
                holder.mBinding.statusTv.setCompoundDrawableTintList(ColorStateList.valueOf(mContext.getColor(R.color.yellow_pending)));
                break;
        }
        holder.mBinding.mainCard.setOnClickListener(v->{
            mClickListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemNotificationBinding mBinding;

        public MyViewHolder(@NonNull ItemNotificationBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }
    }

    public interface NotificationItem {

        void onItemClick(int position);
    }
}
