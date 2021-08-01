package com.example.scheduleappotiment.adapter;

import android.content.Context;
import android.os.IInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleappotiment.R;
import com.example.scheduleappotiment.databinding.ItemSlotBinding;
import com.example.scheduleappotiment.model.TimeSlotModel;

import java.util.ArrayList;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.MyViewHolder> {

    private ArrayList<TimeSlotModel> slotModels = null;
    private SlotItemClickListener itemClickListener = null;
    private int selectedPos = -1;
    private Context context;

    public SlotAdapter(ArrayList<TimeSlotModel> slotModels, SlotItemClickListener itemClickListener, Context context) {
        this.slotModels = slotModels;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemSlotBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TimeSlotModel model = slotModels.get(position);
        model.setStartTimeC(model.getStartTimeC().replace(".000Z", ""));
        model.setEndTimeC(model.getEndTimeC().replace(".000Z", ""));
        holder.mBinding.slotStartTv.setText(model.getStartTimeC());
        holder.mBinding.slotEndTv.setText(model.getEndTimeC());
        holder.mBinding.mainCard.setOnClickListener(v -> {
            itemClickListener.onItemClick(position);
        });
        if (selectedPos != -1 && selectedPos == position) {
            holder.mBinding.mainCard.setStrokeColor(context.getColor(R.color.purple_200));
        } else {
            holder.mBinding.mainCard.setStrokeColor(context.getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return slotModels != null ? slotModels.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemSlotBinding mBinding;

        public MyViewHolder(@NonNull ItemSlotBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }
    }

    public interface SlotItemClickListener {

        void onItemClick(int pos);
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }
}
