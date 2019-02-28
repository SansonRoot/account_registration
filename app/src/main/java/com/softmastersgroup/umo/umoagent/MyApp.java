package com.softmastersgroup.umo.umoagent;

import android.app.Application;

import nouri.in.goodprefslib.GoodPrefs;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        GoodPrefs.init(getApplicationContext());
    }
}
