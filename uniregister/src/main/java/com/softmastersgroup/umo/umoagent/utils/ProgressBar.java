package com.softmastersgroup.umo.umoagent.utils;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

public class ProgressBar {

    KProgressHUD hud;

    public ProgressBar(Context c){
        hud = KProgressHUD.create(c)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("loading....")
                .setDetailsLabel("Please Wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f);
    }

    public void setCancelable(boolean cancel){
        hud.setCancellable(cancel);
    }


    public void show(){
        hud.show();
    }
    public void done( ){
        if (hud.isShowing()) hud.dismiss();
    }



}