package com.example.mvvmmeeting.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.mvvmmeeting.Models.VoiceModel;
import com.example.mvvmmeeting.R;

import java.io.File;
import java.io.IOException;

import io.realm.Realm;

public class RecordVoiceActivity extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private Chronometer chronometer;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
    private SeekBar seekBar;
    private LinearLayout linearLayoutRecorder, linearLayoutPlay;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private String fileTitle = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    private int parentId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        parentId = bundle.getInt("parentId");
        initViews();


    }

    private void initViews() {

        /** setting up the toolbar  **/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

        linearLayoutRecorder = (LinearLayout) findViewById(R.id.linearLayoutRecorder);
        chronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewRecord = (ImageView) findViewById(R.id.imageViewRecord);
        imageViewStop = (ImageView) findViewById(R.id.imageViewStop);
        imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);
        linearLayoutPlay = (LinearLayout) findViewById(R.id.linearLayoutPlay);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        imageViewRecord.setOnClickListener(this);
        imageViewStop.setOnClickListener(this);
        imageViewPlay.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if( view == imageViewRecord ){
            prepareforRecording();
            startRecording();
        }else if( view == imageViewStop ){
            prepareforStop();
            stopRecording();
        }else if( view == imageViewPlay ){
            if( !isPlaying && fileName != null ){
                isPlaying = true;
                startPlaying();
            }else{
                isPlaying = false;
                stopPlaying();
            }
        }

    }



    private void prepareforRecording() {

        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        linearLayoutPlay.setVisibility(View.GONE);
    }

    private void startRecording() {
        //we use the MediaRecorder class to record
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileTitle = String.valueOf(System.currentTimeMillis()) + ".mp3";
        fileName =  root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" + fileTitle;
        Log.d("filename",fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlay.setImageResource(R.drawable.ic_play);
        chronometer.stop();
    }

    private void prepareforStop() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        linearLayoutPlay.setVisibility(View.VISIBLE);
    }

    private void stopRecording() {

        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        //showing the play button

        Realm realm = Realm.getDefaultInstance();
        final int voiceId = getNextKey(realm);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                VoiceModel model = realm.createObject(VoiceModel.class, voiceId);
                model.setParentId(parentId);
                model.setFileName(fileTitle);
                model.setUri(fileName);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(RecordVoiceActivity.this, "صدا با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                Log.i("test123","record saved in realm:");
                Log.i("test123","iD:"+voiceId+" parentId:"+parentId);
                Log.i("test123","fileName:"+fileTitle);
                Log.i("test123","uri:"+fileName);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });


    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
//fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlay.setImageResource(R.drawable.ic_pause);

        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();
        chronometer.start();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometer.stop();
            }
        });

        /** moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    //here the track's progress is being changed as per the progress bar
                    mPlayer.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_list:
                Intent intent = new Intent(this, RecordingListActivity.class);
                intent.putExtra("parentId",parentId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public int getNextKey(Realm realm) {
        try {
            Number number = realm.where(VoiceModel.class).max("voiceId");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

}
