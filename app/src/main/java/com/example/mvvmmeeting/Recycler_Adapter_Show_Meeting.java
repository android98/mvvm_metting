package com.example.mvvmmeeting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvvmmeeting.Activities.MeetingDetailsActivity;
import com.example.mvvmmeeting.databinding.RecyclerShowmeetingsBinding;
import com.orhanobut.hawk.Hawk;

import java.util.List;

public class Recycler_Adapter_Show_Meeting extends RecyclerView.Adapter
        <Recycler_Adapter_Show_Meeting.CustomView>  implements MeetingClickListener{

    private List<MeetingModel> models;
    private Context context;


    public Recycler_Adapter_Show_Meeting(List<MeetingModel> models,Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public Recycler_Adapter_Show_Meeting.CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        RecyclerShowmeetingsBinding showmeetingsBinding = DataBindingUtil.
                inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.recycler_showmeetings, viewGroup, false);
        return new CustomView(showmeetingsBinding);



    }

    @Override
    public void onBindViewHolder(@NonNull final CustomView customView, int i) {

        final MeetingModel meetingModel = models.get(i);
        customView.bind(meetingModel);
        customView.binding.setItemClickListener(this);


    customView.binding.btnMeetingDetails.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent(, MeetingDetailsActivity.class);
            intent.putExtra("meeting_id", meetingModel.getMeetingId());
            context.startActivity(intent);*/
            Log.d("Clicked", "Clicked: ");
            Intent intent = new Intent(context, MeetingDetailsActivity.class);
            intent.putExtra("orderid", meetingModel.getMeetingId());
            context.startActivity(intent);

        }
    });



        //customView.viewDataBinding.setVariable(BR.model, meetingModel);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public void Clicked(MeetingModel model) {
        Log.d("Clicked", "Clicked: "+model.getMeetingId());
        Log.d("Clicked", "Clicked: "+model.meetingName);
        Log.d("Clicked", "Clicked: "+model.meetingInformation);
        Log.d("Clicked", "Clicked: "+model.meetingDate);
        Log.d("Clicked", "Clicked: "+model.meetingTime);

            }

    public class CustomView extends RecyclerView.ViewHolder {
        private RecyclerShowmeetingsBinding binding;
        public CustomView(RecyclerShowmeetingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object object) {
            binding.setVariable(BR.model, object);
            binding.executePendingBindings();
        }
    }
}
