package com.example.mvvmmeeting.Tools;

import android.app.Application;
import android.util.DisplayMetrics;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    public static int deviceWidth;
    public static int deviceheight;


    @Override
    public void onCreate() {
        super.onCreate();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        deviceWidth = dm.widthPixels;
        deviceheight = dm.heightPixels;

        Realm.init(getApplicationContext());
        RealmConfiguration config =  new RealmConfiguration.Builder()
                .name("myDB.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }
}
