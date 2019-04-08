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

import com.example.mvvmmeeting.databinding.FragmentFragmentMainAllBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_main_all extends Fragment {

    private FragmentFragmentMainAllBinding binding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_fragment_main_all, container, false);
        List<MeetingModel> dataModelList = new ArrayList<>();
        dataModelList.add(new MeetingModel("pooia ", "maleki", "1998/10/00"));
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
        dataModelList.add(new MeetingModel("dsfsdafdsfsdafads ", "sdasdasd","1990/08/08"));
        dataModelList.add(new MeetingModel("erqewtewrhtr ", "u,jikmujghbdfx","1990/08/08"));
        Recycler_Adapter_Show_Meeting myRecyclerViewAdapter = new Recycler_Adapter_Show_Meeting(dataModelList,getActivity());
        binding.setAdapter(myRecyclerViewAdapter);


        Recycler_Adapter_Show_Meeting adapter = binding.getAdapter();
        Log.d("biggg", "onCreateView: "+adapter.models.get(1).meetingName);


        Log.d("biggg", "onCreateView: "+"Salammmmmm");
        return binding.getRoot();

        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
