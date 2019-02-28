package com.softmastersgroup.umo.umoagent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.softmastersgroup.umo.umoagent.models.Auth;
import com.softmastersgroup.umo.umoagent.models.BioData;
import com.softmastersgroup.umo.umoagent.models.RegisterBundle;

import nouri.in.goodprefslib.GoodPrefs;

public class LaunchActivity extends AppCompatActivity {

    Button btnIndividual,btnBusiness,btnLogin;
    Context ctx;
    RegisterBundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = getBaseContext();

        boolean isLoggedIn = GoodPrefs.getInstance().getBoolean("isLoggedIn",false);

        if (isLoggedIn){

            Auth auth = GoodPrefs.getInstance().getObject("auth",Auth.class);

            if (auth.getPhone_verified_at() == null || auth.getPhone_verified_at().isEmpty()){

                GoodPrefs.getInstance().saveBoolean("fromLogin",true);

                startActivity(new Intent(ctx,VerifyPhone.class));
                finish();

            }else{
                startActivity(new Intent(ctx,MainApp.class));
                finish();
            }
        }

        setContentView(R.layout.activity_launch);

        init();

        btnIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BioData bioData =  bundle.getBiodata();

                if (bioData == null){
                    bioData = new BioData();
                }

                bioData.setType("individual");

                bundle.setBiodata(bioData);

                startActivity(new Intent(ctx,DemographicData.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,LoginActivity.class));
            }
        });

    }

    private void init(){
        btnBusiness = findViewById(R.id.btnBusiness);
        btnIndividual = findViewById(R.id.btnIndividual);
        btnLogin = findViewById(R.id.btnLogin);

        loadData();
    }

    private void loadData(){
        bundle = GoodPrefs.getInstance().getObject("register_bundle",RegisterBundle.class);

        if (bundle == null){

            bundle = new RegisterBundle();

        }
    }

    private boolean isSmsPermissionGranted(){
        return ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }


    public void toMain(Activity tClass){
        startActivity(new Intent(ctx,tClass.getClass()));
    }
}
