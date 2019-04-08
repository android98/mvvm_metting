package com.example.mvvmmeeting.Activities;


import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.mvvmmeeting.MeetingModel;
import com.example.mvvmmeeting.R;
import com.example.mvvmmeeting.AddMeetingViewModel;
import com.example.mvvmmeeting.Tools.Utilities;
import com.example.mvvmmeeting.databinding.ActivityAddMeetingBinding;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class AddMeetingActivity extends AppCompatActivity {

    public LinearLayout btnGetDate, btnGetTime;
    public TextView txtShowDate, txtShowTime;

    public Date meetingDate = null;
    public String meetingTime = "";
    public PersianCalendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Utilities.getPermission(AddMeetingActivity.this);


        ActivityAddMeetingBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_add_meeting);
        AddMeetingViewModel model = ViewModelProviders.of(this).get(AddMeetingViewModel.class);
        binding.setAddMeeting(model);
        binding.setLifecycleOwner(this);

        getDate();
        getTime();
        AddMeeting();



        model.getMeetingModelLiveData().observe(this, new Observer<MeetingModel>() {
            @Override
            public void onChanged(@Nullable final MeetingModel meetingModel) {


                Toast.makeText(AddMeetingActivity.this, "Start : "
                                + meetingModel.getMeetingName() + "     " +
                                meetingModel.getMeetingInformation() + meetingTime + "    " + meetingDate

                        , Toast.LENGTH_SHORT).show();

                Realm realm = Realm.getDefaultInstance();
                final int meetingId = getNextKey(realm);
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        MeetingModel model = realm.createObject(MeetingModel.class, meetingId);
                        model.setMeetingName(meetingModel.getMeetingName());
                        model.setMeetingInformation(meetingModel.getMeetingInformation());
                        model.setMeetingDate(String.valueOf(meetingDate));
                        model.setMeetingTime(meetingTime);
                        Log.d("abdc", "execute: "+"ok");
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("abdc", "execute: "+"onSucceesss");
                        Toast.makeText(AddMeetingActivity.this, "درج شد ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddMeetingActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("abdc", "execute: "+"On Errroooor");
                        Toast.makeText(AddMeetingActivity.this, "مشکلی ه وجود امده است ! ", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

            }
        });


        model.OnBtnGetDateClick();


    }

    public void AddMeeting(){

    }

    private int getNextKey(Realm realm) {
        try {

            Number number = realm.where(MeetingModel.class).max("meetingId");
            if (number != null) {
                return number.intValue() + 1;
            }else {
                return 0;
            }

        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return 0;
    }

    private void getTime() {
        btnGetTime = findViewById(R.id.btnGetTime);
        txtShowTime = findViewById(R.id.txtShowTime);
        final Calendar currentTime = Calendar.getInstance();
        btnGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddMeetingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String min = "";
                                String hour = "";
                                if (hourOfDay < 10) {
                                    min = "0" + String.valueOf(hourOfDay);
                                } else {
                                    min = String.valueOf(hourOfDay);
                                }
                                if (minute < 10) {
                                    hour = "0" + String.valueOf(minute);
                                }
                                meetingTime = min + ":" + hour;
                                txtShowTime.setText(meetingTime);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });


    }

    public void getDate() {
        btnGetDate = findViewById(R.id.btnGetDate);
        txtShowDate = findViewById(R.id.txtShowDate);
        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianDatePickerDialog pickerDialog = new PersianDatePickerDialog(AddMeetingActivity.this)
                        .setPositiveButtonString(" باشه ")
                        .setNegativeButton(" بی خیال ")
                        .setTodayButton(" امروز ")
                        .setTodayButtonVisible(true)
                        .setActionTextColor(Color.GRAY)
                        .setListener(new Listener() {
                            @Override
                            public void onDateSelected(PersianCalendar persianCalendar) {
                                meetingDate = persianCalendar.getTime();
                                txtShowDate.setText(persianCalendar.getPersianLongDate());
                                Log.d("datesss", "onDateSelected: " + meetingDate);
                            }

                            @Override
                            public void onDismissed() {
                                Toast.makeText(AddMeetingActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        });
                if (meetingDate != null) {
                    calendar = new PersianCalendar();
                    calendar.setTime(meetingDate);
                    pickerDialog.setInitDate(calendar, true);

                }
                pickerDialog.show();
            }
        });
    }
}
