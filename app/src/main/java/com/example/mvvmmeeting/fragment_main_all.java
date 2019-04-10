package com.example.mvvmmeeting;


import android.content.Context;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvvmmeeting.databinding.FragmentFragmentMainAllBinding;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_main_all extends Fragment {

    private FragmentFragmentMainAllBinding binding;
    private Context context;


    MeetingModel archieMeeting = new MeetingModel();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_fragment_main_all, container, false);
       /* List<MeetingModel> dataModelList = new ArrayList<>();
        dataModelList.add(new MeetingModel("pooia ", "maleki", "1980/10/8"));
        dataModelList.add(new MeetingModel("asdasfdsf ", "sdasdasd","1990/08/08"));
        dataModelList.add(new MeetingModel("asdasd ", "sdasdasd","1990/08/08"));
        dataModelList.add(new MeetingModel("asdasfdsf ", "tgfbv","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("asfasdfsdf ", "ewteryhrty","1990/08/08"));
        dataModelList.add(new MeetingModel("dsfsdafdsfsdafads ", "sdasdasd","1990/08/08"));*/
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MeetingModel> results = realm.where(MeetingModel.class).findAll();
        /*PersianCalendar calendar = new PersianCalendar();
        for (int i = 0; i < results.size(); i++) {

            MeetingModel meetingModel = results.get(i);
            MeetingInfoModel model = new MeetingInfoModel();
            model.setMeetingName(meetingModel.getMeetingName());
            model.setClosingMeetingTime(meetingModel.getMeetingTime());
            model.setMeetingDate(meetingModel.getMeetingDate());
            model.setMeetingInformation(meetingModel.getMeetingInformation());

            Log.d("realam", "onCreateView: " + model.getMeetingName());
            Log.d("realam", "onCreateView: " + model.getClosingMeetingTime());
            Log.d("realam", "onCreateView: " + model.getMeetingDate());
            Log.d("realam", "onCreateView: " + model.getMeetingInformation());

        }*/
            //archieMeeting.add(model);
            //Log.d("realm", "onCreateView: " + archieMeeting.get(2).getMeetingName());

            Recycler_Adapter_Show_Meeting meeting = new Recycler_Adapter_Show_Meeting(results
                    ,getActivity());
            binding.setAdapter(meeting);



        Recycler_Adapter_Show_Meeting adapter = binding.getAdapter();
        //Recycler_Adapter_Show_Meeting adapter = binding.getAdapter();
        //Log.d("biggg", "onCreateView: "+adapter.models.get(1).meetingName);


        Log.d("biggg", "onCreateView: " + "Salammmmmm");

        return binding.getRoot();

        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
