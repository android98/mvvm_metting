package com.example.mvvmmeeting;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class Recycler_Adapter_Show_Meeting extends RecyclerView.Adapter
        <Recycler_Adapter_Show_Meeting.CustomView> {

    List<MeetingModel> models;
    Context context;


    public Recycler_Adapter_Show_Meeting(List<MeetingModel> models,Context context) {
        this.models = models;
        context = context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.recycler_showmeetings, viewGroup, false);
        return new CustomView(dataBinding);



    }

    @Override
    public void onBindViewHolder(@NonNull CustomView customView, int i) {
        MeetingModel meetingModel = models.get(i);
        customView.bind(meetingModel);
        

        //customView.viewDataBinding.setVariable(BR.model, meetingModel);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {

        private ViewDataBinding viewDataBinding;

        public CustomView(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.viewDataBinding = viewDataBinding;
        }

        public void bind(Object object){
            viewDataBinding.setVariable(BR.model, object);
            viewDataBinding.executePendingBindings();
        }

    }


    public void meetingClicked(MeetingModel meetingModel) {
        Log.d("meeting", "meetingClicked: " + meetingModel.meetingName.toString());
        Log.d("meeting", "meetingClicked: " + meetingModel.meetingTime);
        Log.d("meeting", "meetingClicked: " + meetingModel.meetingDate);
        Log.d("meeting", "meetingClicked: " + meetingModel.meetingInformation);
    }

}
