package com.example.mvvmmeeting.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mvvmmeeting.Adapters.Recycler_Adapter_Recording;
import com.example.mvvmmeeting.Models.Recording;
import com.example.mvvmmeeting.Models.VoiceModel;
import com.example.mvvmmeeting.R;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RecordingListActivity extends AppCompatActivity {



    private Toolbar toolbar;
    private RecyclerView recyclerViewRecordings;
    private ArrayList<Recording> recordingArraylist;
    private Recycler_Adapter_Recording recordingAdapter;
    private TextView textViewNoRecordings;
    private int parentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_list);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        parentId = bundle.getInt("parentId");

        initViews();

        fetchRecordings();
    }



    private void initViews() {
        recordingArraylist = new ArrayList<Recording>();
        /** setting up the toolbar  **/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("صداهای ذخیره شده");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

        /** enabling back button ***/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** setting up recyclerView **/
        recyclerViewRecordings = (RecyclerView) findViewById(R.id.recyclerViewRecordings);
        recyclerViewRecordings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewRecordings.setHasFixedSize(true);

        textViewNoRecordings = (TextView) findViewById(R.id.textViewNoRecordings);

    }

    private void fetchRecordings() {

        File root = android.os.Environment.getExternalStorageDirectory();
        String path = root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<VoiceModel> voices = realm.where(VoiceModel.class)
                .equalTo("parentId",parentId)
                .findAll();



        if ( voices != null ){

            for ( int i=0 ; i<voices.size() ; i++ ){
                String fileName = voices.get(i).getFileName();
                String recordingUri = voices.get(i).getUri();

                Recording recording = new Recording(recordingUri,fileName,false);
                recordingArraylist.add(recording);
            }

            textViewNoRecordings.setVisibility(View.GONE);
            recyclerViewRecordings.setVisibility(View.VISIBLE);
            setAdaptertoRecyclerView();

        }else{
            textViewNoRecordings.setVisibility(View.VISIBLE);
            recyclerViewRecordings.setVisibility(View.GONE);
        }

    }
    private void setAdaptertoRecyclerView() {
        recordingAdapter = new Recycler_Adapter_Recording(recordingArraylist,this);
        recyclerViewRecordings.setAdapter(recordingAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
