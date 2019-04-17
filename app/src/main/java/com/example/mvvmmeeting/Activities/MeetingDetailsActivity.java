package com.example.mvvmmeeting.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mvvmmeeting.MeetingModel;
import com.example.mvvmmeeting.R;
import com.example.mvvmmeeting.Tools.DialogFragmentShowMap;
import com.example.mvvmmeeting.Tools.Utilities;
import com.orhanobut.hawk.Hawk;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import okhttp3.internal.Util;

public class MeetingDetailsActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView txtTopToolbar;
    TextView txtShowDate;
    TextView txtShowClosingDate;
    TextView txtShowClosingTime;
    TextView txtShowTime;

    LinearLayout btnGetDate;
    LinearLayout btnGetClosingDate;
    LinearLayout btnGetTime;
    LinearLayout btnGetClossingTime;
    LinearLayout btnGetMembers;



    RelativeLayout relativeMap;

    EditText edtMeetingInfo;
    EditText edtMeetingName;

    String meetingTime = "";

    public static double meetingLat = 0.0;
    public static double meetingLng = 0.0;

    String meetingDate = "";
    Date meetingClosingDate = null;

    private int orderID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.orderID = bundle.getInt("orderid");
        Log.d("orderid", "onCreate: " + this.orderID);
        GetMeetingInfoFromRealm(orderID);
        Utilities.getPermission(MeetingDetailsActivity.this);

    }



    private void InjectTopToolbar(String toolbar) {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtTopToolbar = findViewById(R.id.txtTopToolbar);
        txtTopToolbar.setText(toolbar);

    }

    private void GetMeetingInfoFromRealm(int orderId) {

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<MeetingModel> result = realm.where(MeetingModel.class)
                .equalTo("meetingId", orderID).findAll();

        if (result != null) {


            InjectTopToolbar(result.get(0).getMeetingName());
            getDate();
            /*meetingDate = result.get(0).getMeetingDate();
            PersianCalendar calendar = new PersianCalendar();
            calendar.setTime(result.get(0).getMeetingDate());*/
            txtShowDate.setText(result.get(0).getMeetingDate());

            getClosingDate();

            if (result.get(0).getMeetingClosingDat() != null) {
                meetingClosingDate = result.get(0).getMeetingClosingDat();
                PersianCalendar calendarClosing = new PersianCalendar();
                calendarClosing.setTime(result.get(0).getMeetingClosingDat());
                txtShowClosingDate.setText(calendarClosing.getPersianLongDate());

            }
            getTime();
            txtShowTime.setText(result.get(0).getMeetingTime());
            meetingLat = result.get(0).getMeetinglat();
            meetingLng = result.get(0).getMeetingLng();
            edtMeetingInfo = findViewById(R.id.edtMeetingInfo);
            edtMeetingInfo.setText(result.get(0).getMeetingInformation());
            edtMeetingName = findViewById(R.id.edtMeetingName);
            edtMeetingName.setText(result.get(0).getMeetingName());


            getLocation();
            getClosingTime();
            txtShowClosingTime.setText((CharSequence) result.get(0).getClosingMeetingTime());


            Log.d("memebr", "GetMeetingInfoFromRealm: " + result.get(0).getMeetingId());
            Log.d("memebr", "GetMeetingInfoFromRealm: " + result.get(0).getMeetingName());
            Log.d("memebr", "GetMeetingInfoFromRealm: " + result.get(0).getMeetingTime());
            Log.d("memebr", "GetMeetingInfoFromRealm: " + result.get(0).getMeetinglat());
            Log.d("memebr", "GetMeetingInfoFromRealm: " + result.get(0).getMeetingLng());
            Log.d("memebr", "GetMeetingInfoFromRealm: " + result.get(0).getMeetingInformation());
            Log.d("memebr", "GetMeetingInfoFromRealm: " + result.get(0).getMeetingDate());





        }
    }

    private void getLocation() {


        relativeMap = findViewById(R.id.relativeMap);
        relativeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DialogFragmentShowMap().show(getSupportFragmentManager(),null);

            }
        });
    }

    private void getTime() {
        btnGetTime = findViewById(R.id.btnGetTime);
        txtShowTime = findViewById(R.id.txtShowTime);
        final Calendar currentTime = Calendar.getInstance();


        btnGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MeetingDetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                                String min = "";
                                String hou = "";


                                if ( i<10 ){
                                    min = "0"+String.valueOf(i);
                                }else {
                                    min = String.valueOf(i);
                                }


                                if ( i1<10 ){
                                    hou = "0"+String.valueOf(i1);
                                }else{
                                    hou = String.valueOf(i1);
                                }
                                meetingTime = min + ":" + hou;
                                txtShowTime.setText(meetingTime);

                                Realm realm = Realm.getDefaultInstance();

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute (Realm realm) {
                                        MeetingModel obj = realm.where(MeetingModel.class).equalTo(
                                                "meetingId", orderID).findFirst();
                                        if(obj != null) {
                                            obj.setMeetingTime(meetingTime);
                                        }
                                    }
                                });

                            }
                        },hour,minute,true);

                timePickerDialog.show();
            }

        });
    }

    private void getClosingDate(){

        btnGetClosingDate = findViewById(R.id.btnGetClosingDate);
        txtShowClosingDate = findViewById(R.id.txtShowClosingDate);

        btnGetClosingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PersianDatePickerDialog perDialog = new PersianDatePickerDialog(MeetingDetailsActivity.this)
                        .setPositiveButtonString("باشه")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("امروز")
                        .setTodayButtonVisible(true)
                        .setActionTextColor(Color.GRAY)
                        .setListener(new Listener() {
                            @Override
                            public void onDateSelected(final PersianCalendar persianCalendar) {
                                txtShowClosingDate.setText(persianCalendar.getPersianLongDate());

                                meetingClosingDate = persianCalendar.getTime();

                                Realm realm = Realm.getDefaultInstance();

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute (Realm realm) {
                                        MeetingModel obj = realm.where(MeetingModel.class).equalTo("meetingId", orderID).findFirst();
                                        if(obj != null) {
                                            obj.setMeetingClosingDat(meetingClosingDate);
                                        }

                                    }
                                });

                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                if ( meetingClosingDate != null ){
                    PersianCalendar calendar = new PersianCalendar();
                    calendar.setTime(meetingClosingDate);
                    perDialog.setInitDate(calendar,true);
                }

                perDialog.show();

            }
        });

    }


    private void getDate() {

        btnGetDate = findViewById(R.id.btnGetDate);
        txtShowDate = findViewById(R.id.txtShowDate);

        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PersianDatePickerDialog perDialog = new PersianDatePickerDialog(MeetingDetailsActivity.this)
                        .setPositiveButtonString("باشه")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("امروز")
                        .setTodayButtonVisible(true)
                        .setActionTextColor(Color.GRAY)
                        .setListener(new Listener() {
                            @Override
                            public void onDateSelected(final PersianCalendar persianCalendar) {
                                txtShowDate.setText(persianCalendar.getPersianLongDate());
                                //meetingDate = persianCalendar.getTime();

                                Realm realm = Realm.getDefaultInstance();

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        MeetingModel obj = realm.where(MeetingModel.class)
                                                .equalTo("meetingId", orderID).findFirst();
                                        if (obj != null) {
                                            obj.setMeetingDate(String.valueOf(persianCalendar.getTime()));
                                        }

                                    }
                                });

                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

               /* if (meetingDate != null) {
                    PersianCalendar calendar = new PersianCalendar();
                    calendar.setTime(meetingDate);
                    perDialog.setInitDate(calendar, true);
                }*/

                perDialog.show();

            }
        });

    }


    private void getClosingTime(){

        btnGetClossingTime = findViewById(R.id.btnGetClossingTime);
        txtShowClosingTime = findViewById(R.id.txtShowClosingTime);
        final Calendar currentTime = Calendar.getInstance();


        btnGetClossingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MeetingDetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                String min = "";
                                String hou = "";
                                String closingMeetingTime = "";

                                if ( i<10 ){
                                    min = "0"+String.valueOf(i);
                                }else {
                                    min = String.valueOf(i);
                                }

                                if ( i1<10 ){
                                    hou = "0"+String.valueOf(i1);
                                }else{
                                    hou = String.valueOf(i1);
                                }
                                closingMeetingTime = min + ":" + hou;
                                txtShowClosingTime.setText(closingMeetingTime);

                                Realm realm = Realm.getDefaultInstance();

                                final String finalClosingMeetingTime = closingMeetingTime;
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute (Realm realm) {
                                        MeetingModel obj = realm.where(MeetingModel.class).equalTo(
                                                "meetingId", orderID).findFirst();
                                        if(obj != null) {
                                            obj.setClosingMeetingTime(finalClosingMeetingTime);
                                        }
                                    }
                                });

                            }
                        },hour,minute,true);

                timePickerDialog.show();
            }

        });
    }



    private void getMembers(){

        btnGetMembers = findViewById(R.id.btnGetMembers);
        btnGetMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MeetingDetailsActivity.this,PresentMembersActivity.class);
                Realm realm = Realm.getDefaultInstance();

                if ( orderID != -1 ){

                    intent.putExtra("parentId",orderID);
                    startActivity(intent);
                }
            }
        });

    }


}
