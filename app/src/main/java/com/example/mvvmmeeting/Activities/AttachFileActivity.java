package com.example.mvvmmeeting.Activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mvvmmeeting.Adapters.Recycler_Adapter_Attach_File;
import com.example.mvvmmeeting.Models.FileModel;
import com.example.mvvmmeeting.R;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import io.realm.Realm;
import io.realm.RealmResults;

public class AttachFileActivity extends AppCompatActivity {


    ImageView btnAdd;
    ImageView btnBack;
    RecyclerView recyclerFile;
    Recycler_Adapter_Attach_File recyclerAdapter;
    LinearLayoutManager recyclerManager;
    int GET_FILE_REQUEST_CODE = 1;
    private int parentId = -1;
    TextView txtNoFile;
    RealmResults<FileModel> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_file);

        Intent intent = getIntent();
        parentId = intent.getIntExtra("parentId", -1);

        injectTopToolbar();
        getFilesFromRealm();
    }


    private void injectTopToolbar() {

        // btn add
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withActivity(AttachFileActivity.this)
                        .withRequestCode(GET_FILE_REQUEST_CODE)
                        .start();
            }
        });

        // btn back
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void addFileToRealm(final String fileName, final String filePath) {

        Realm realm = Realm.getDefaultInstance();
        final int fileId = getNextKey(realm);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                FileModel model = realm.createObject(FileModel.class, fileId);
                model.setParentId(parentId);
                model.setFileName(fileName);
                model.setFilePath(filePath);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i("test123", "file added to realmDB");
                getFilesFromRealm();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("test123", "cant add this file to realmDB");
            }
        });
    }

    private void injectRecyclerView() {

        recyclerFile = findViewById(R.id.recyclerFile);
        recyclerAdapter = new Recycler_Adapter_Attach_File(AttachFileActivity.this, files);
        recyclerManager = new LinearLayoutManager(AttachFileActivity.this, LinearLayoutManager.VERTICAL, false);

        recyclerFile.setHasFixedSize(true);
        recyclerFile.setLayoutManager(recyclerManager);
        recyclerFile.setAdapter(recyclerAdapter);

    }

    private void getFilesFromRealm() {

        txtNoFile = findViewById(R.id.txtNoFile);
        recyclerFile = findViewById(R.id.recyclerFile);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<FileModel> results = realm.where(FileModel.class)
                .equalTo("parentId", parentId)
                .findAll();

        if (results != null) {
            files = results;
            if (files.size() >= 1) {
                txtNoFile.setVisibility(View.GONE);
                recyclerFile.setVisibility(View.VISIBLE);

                injectRecyclerView();
            } else {
                txtNoFile.setVisibility(View.VISIBLE);
                recyclerFile.setVisibility(View.GONE);
            }
        } else {
            Log.i("test123", "files == null ");
        }

    }

    public int getNextKey(Realm realm) {
        try {
            Number number = realm.where(FileModel.class).max("fileId");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == GET_FILE_REQUEST_CODE) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

                addFileToRealm(fileName, filePath);
            }
        }
    }

}
