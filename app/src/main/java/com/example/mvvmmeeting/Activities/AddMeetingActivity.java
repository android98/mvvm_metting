package com.example.mvvmmeeting.Activities;


import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.mvvmmeeting.MeetingModel;
import com.example.mvvmmeeting.R;
import com.example.mvvmmeeting.AddMeetingViewModel;
import com.example.mvvmmeeting.Tools.DialogFragmentShowMap;
import com.example.mvvmmeeting.Tools.Utilities;
import com.example.mvvmmeeting.databinding.ActivityAddMeetingBinding;

import java.util.Calendar;
import java.util.Date;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.realm.Realm;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class AddMeetingActivity extends AppCompatActivity {

    public LinearLayout btnGetDate, btnGetTime;
    public TextView txtShowDate, txtShowTime;

    public Date meetingDate = null;
    public String meetingTime = "";
    public PersianCalendar calendar;
    RelativeLayout relativeMap;
    LinearLayout btnGetMembers;



    public static double userLat = 0.0;
    public static double userLng = 0.0;
    public static double meetingLat = 0.0;
    public static double meetingLng = 0.0;
    public static boolean userLocation = false;


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
        getUserLocation();
        getLocation();
        getMembers();
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
                        model.setMeetingDate(meetingDate);
                        model.setMeetingTime(meetingTime);

                        if (AddMeetingActivity.userLocation) {
                            model.setMeetinglat(AddMeetingActivity.meetingLat);
                            model.setMeetingLng(AddMeetingActivity.meetingLng);
                        }
                        Log.d("abdc", "execute: " + "ok");
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("abdc", "execute: " + "onSucceesss");
                        Toast.makeText(AddMeetingActivity.this, "درج شد ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddMeetingActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("abdc", "execute: " + "On Errroooor");
                        Toast.makeText(AddMeetingActivity.this, "مشکلی به وجود امده است ! ", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

            }
        });


        model.OnBtnGetDateClick();


    }

    private void getMembers() {
        btnGetMembers = findViewById(R.id.btnGetMembers);
        btnGetMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMeetingActivity.this, MembersActivity.class);
                Realm realm = Realm.getDefaultInstance();
                int parentId = getNextKey(realm);
                intent.putExtra("parentId", parentId);
                startActivity(intent);
            }
        });
    }

    public void AddMeeting() {

    }

    private int getNextKey(Realm realm) {
        try {

            Number number = realm.where(MeetingModel.class).max("meetingId");
            if (number != null) {
                return number.intValue() + 1;
            } else {
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
                            public void onTimeSet(TimePicker view, int i, int i1) {
                                String min = "";
                                String hou = "";
                                if (i < 10) {
                                    min = "0" + String.valueOf(i);
                                } else {
                                    min = String.valueOf(i);
                                }



                                if (i1 < 10) {
                                    hou = "0" + String.valueOf(i1);
                                }
                                else {
                                    hou = String.valueOf(i1);
                                }
                                meetingTime = min + ":" + hou;
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
                                txtShowDate.setText(persianCalendar.getPersianLongDate());
                                meetingDate = persianCalendar.getTime();
                                Log.d("datesss", "onDateSelected: " + meetingDate);
                            }

                            @Override
                            public void onDismissed() {
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

    public void getLocation() {
        relativeMap = findViewById(R.id.relativeMap);
        relativeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogFragmentShowMap().show(getSupportFragmentManager(), null);
            }
        });
    }

    public void getUserLocation() {
        if (Utilities.checkLocationPermission(this)) {
            Log.i("test123","permission granted");

            SmartLocation.with(this).location()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            userLat = location.getLatitude();
                            userLng = location.getLongitude();
                            Log.i("test123", String.valueOf(userLat));
                            Log.i("test123", String.valueOf(userLng));

                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddMeetingActivity.this,MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

}
