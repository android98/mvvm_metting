package com.example.mvvmmeeting;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mvvmmeeting.databinding.ActivityTestBinding;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity {


    private ActivityTestBinding activityTestBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTestBinding = DataBindingUtil.setContentView(this, R.layout.activity_test);

        List<MeetingModel> models = new ArrayList<>();
        models.add(new MeetingModel("pooria", "name od boy", "1990/10/10"));
        models.add(new MeetingModel("pooria", "name od boy", "1990/10/10"));
        models.add(new MeetingModel("pooria", "name od boy", "1990/10/10"));
        models.add(new MeetingModel("pooria", "name od boy", "1885/80/85"));

        Recycler_Adapter_Show_Meeting adapter_show_meeting = new Recycler_Adapter_Show_Meeting(
                models, this
        );
        activityTestBinding.setMyAdapter(adapter_show_meeting);



    }
}
