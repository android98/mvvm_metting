package com.example.mvvmmeeting.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mvvmmeeting.Adapters.FullScreenImageAdapter;
import com.example.mvvmmeeting.Models.ImageModel;
import com.example.mvvmmeeting.R;
import com.example.mvvmmeeting.Tools.Utilities;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class FullScreenViewActivity extends AppCompatActivity {


    private Utilities utils;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    int parentId = -1;
    int position = 0;
    ImageView btnRemove;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fullscreen_view);


        Intent intent = getIntent();
        intent.getIntExtra("parentId", -1);
        intent.getIntExtra("position", 0);

        getImageFromRealm();

    }

    private void getImageFromRealm() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ImageModel> results = realm.where(ImageModel.class)
                .equalTo("parentId", parentId)
                .findAll();

        for (int i = 0; i < results.size(); i++) {
            imagePaths.add(results.get(i).getImagePath());
        }

        if (results != null) {
            injectViewPager();
        }

    }

    private void injectViewPager() {

        viewPager = findViewById(R.id.pager);
        utils = new Utilities(getApplicationContext());

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, imagePaths);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(position);
    }


    private void removeImage() {
        btnRemove = findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<ImageModel> rows = realm.where(ImageModel.class)
                                .equalTo("parentId", parentId)
                                .equalTo("imagePath", imagePaths.get(position)).findAll();
                        rows.deleteAllFromRealm();
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        File file = new File(imagePaths.get(position));
                        boolean delete = file.delete();

                        Intent intent = new Intent(FullScreenViewActivity.this
                                , AttachImageActivity.class);
                        intent.putExtra("parentId", parentId);
                        startActivity(intent);
                        finish();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(FullScreenViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                getImageFromRealm();

            }
        });
    }


}
