package com.example.mvvmmeeting.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvvmmeeting.Adapters.Recycler_Adapter_Show_Meeting;
import com.example.mvvmmeeting.Models.MeetingModel;
import com.example.mvvmmeeting.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_main_all extends Fragment {

    public RecyclerView recyclerView;
    public Recycler_Adapter_Show_Meeting adapter;
    public List<MeetingModel> models;


    public fragment_main_all() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //

        recyclerView = container.findViewById(R.id.mainRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        models = new ArrayList<>();
        adapter = new Recycler_Adapter_Show_Meeting(models);
        recyclerView.setAdapter(adapter);
        return inflater.inflate(R.layout.fragment_fragment_main_all, container, false);

    }


}
