package com.example.mvvmmeeting.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mvvmmeeting.Models.MeetingModel;
import com.example.mvvmmeeting.R;

import java.util.List;

public class Recycler_Adapter_Show_Meeting
        extends RecyclerView.Adapter<Recycler_Adapter_Show_Meeting.CustomView> {

    List<MeetingModel> models;


    public Recycler_Adapter_Show_Meeting(List<MeetingModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_showmeetings,
                viewGroup, false);
        return new CustomView(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView customView, int i) {
        MeetingModel meetingModel = models.get(i);
        customView.meetingName.setText(meetingModel.getMeetingName());
        customView.txtTime.setText(meetingModel.getClosingMeetingTime());
        customView.txtDate.setText(meetingModel.getMeetingDate());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustomView extends RecyclerView.ViewHolder {
        TextView meetingName,txtDate,txtTime;


        public CustomView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
