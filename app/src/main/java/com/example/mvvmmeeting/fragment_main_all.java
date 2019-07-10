package com.example.mvvmmeeting;


import android.content.Context;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvvmmeeting.databinding.FragmentFragmentMainAllBinding;

import io.realm.Realm;
import io.realm.RealmResults;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_main_all extends Fragment {

    private FragmentFragmentMainAllBinding binding;


    MeetingModel archieMeeting = new MeetingModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_fragment_main_all, container, false);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MeetingModel> results = realm.where(MeetingModel.class).findAll();
        Recycler_Adapter_Show_Meeting meeting = new Recycler_Adapter_Show_Meeting(results
                , getActivity());
        binding.setAdapter(meeting);
        Recycler_Adapter_Show_Meeting adapter = binding.getAdapter();
        return binding.getRoot();

    }
}
