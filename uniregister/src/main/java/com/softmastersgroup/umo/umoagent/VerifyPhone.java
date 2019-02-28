package com.softmastersgroup.umo.umoagent;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softmastersgroup.umo.umoagent.models.Auth;
import com.softmastersgroup.umo.umoagent.models.AuthResponse;
import com.softmastersgroup.umo.umoagent.rest.RestRepository;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import nouri.in.goodprefslib.GoodPrefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhone extends AppCompatActivity {

    public static EditText etCode;
    Button btnVerify;
    TextView tvResend, tvTimer,tvTextInfo;
    Context ctx;
    public static RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        ctx = VerifyPhone.this;

        init();

        if (GoodPrefs.getInstance().getBoolean("fromLogin",false)){
            startTimerCountDown();

        }else{
            sendVerificationCode();

        }

        GoodPrefs.getInstance().saveBoolean("fromLogin",true);


        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etCode.getText().toString().isEmpty()){
                    return;
                }

                verify(etCode.getText().toString());
            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 6){
                    verify(etCode.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*String txt = s.toString();

                if (txt.length() == 6){
                    verify(etCode.getText().toString());
                }*/

            }
        });



    }

    private void disableResendBtn() {
        tvResend.setEnabled(false);
        tvResend.setTextColor(getResources().getColor(R.color.colorDivider));
        //startTimerCountDown();
    }

    private void enableResendBtn() {
        tvResend.setEnabled(true);
        tvResend.setTextColor(getResources().getColor(R.color.colorAccent));
    }


    private void init() {
        etCode = findViewById(R.id.etCode);
        btnVerify = findViewById(R.id.btnVerify);
        tvResend = findViewById(R.id.tvResendCode);
        tvTimer = findViewById(R.id.tvTimer);
        loader = findViewById(R.id.loader);
        tvTextInfo = findViewById(R.id.tvTextInfo);
        btnVerify.setEnabled(true);
        btnVerify.setBackground(getResources().getDrawable(R.drawable.round_edge_accent_light));

        Auth auth = GoodPrefs.getInstance().getObject("auth",Auth.class);

        if (auth.getPhone() != null && auth.getPhone().isEmpty()){
            Toasty.error(ctx,"Invalid phone number").show();
            return;
        }

        String msge;

        if (GoodPrefs.getInstance().getBoolean("fromLogin",false)){
            msge = "Welcome Back, "+auth.getFirstname()+" ,Enter the 6 digit code sent to "+auth.getPhone() +" in the field below";

        }else{
            msge = "Welcome, "+auth.getFirstname()+" ,Enter the 6 digit code sent to "+auth.getPhone() +" in the field below";

        }

        tvTextInfo.setText(msge);

    }

    private void verify(String code) {

        if (!isValidated()) return;

        btnVerify.setEnabled(false);
        btnVerify.setBackground(getResources().getDrawable(R.drawable.round_edge_accent_light));

        showLoader();

        Map<String,String> map = new HashMap<>();
        map.put("userid",GoodPrefs.getInstance().getObject("auth",Auth.class).getUserid());
        map.put("code",code);

        Log.d("USER_ID",GoodPrefs.getInstance().getObject("auth",Auth.class).getUserid());
        Log.d("VERIFY_CODE",code);

        RestRepository repo = new RestRepository();
        Call<AuthResponse> responseCall = repo.getAppInterface().verify(
              map
        );

        responseCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                hideLoader();

                btnVerify.setEnabled(true);
                btnVerify.setBackground(getResources().getDrawable(R.drawable.ic_round_filled_accent));

                if (response.isSuccessful()){

                    if (response.body() == null) {
                        Toasty.error(ctx, "No response resource received").show();
                        return;
                    }

                    if (response.body().getData() == null) {
                        Toasty.error(ctx, response.body().getMessage()).show();
                        return;
                    }


                    toMain(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                hideLoader();
                Toasty.error(ctx,t.toString()).show();
            }
        });

    }

    private void toMain(Auth data) {

        GoodPrefs.getInstance().deleteValue("fromLogin");

        Auth auth = GoodPrefs.getInstance().getObject("auth",Auth.class);

        auth.setPhone_verified_at(data.getPhone_verified_at());

        GoodPrefs.getInstance().saveObject("auth",auth);

        startActivity(new Intent(ctx,MainApp.class));
    }

    public void toMainFromReceiver(){
        startActivity(new Intent(ctx,MainApp.class));
    }

    private void showLoader(){
        loader.setVisibility(View.VISIBLE);
        etCode.setEnabled(false);
    }

    private void hideLoader(){
        loader.setVisibility(View.GONE);
        etCode.setEnabled(true);
    }

    private boolean isValidated(){

        boolean isValid = true;

        if (etCode.getText().toString().isEmpty()){
            etCode.setError("Pleas Enter Verification Code sent");
            isValid = false;
        }

        return isValid;
    }

    private void startTimerCountDown() {

        disableResendBtn();

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText("00:" + String.valueOf(millisUntilFinished / 1000));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                enableResendBtn();
            }

        }.start();
    }

    private void resendCode() {

        disableResendBtn();
        sendVerificationCode();

    }

    private void sendVerificationCode(){

        Auth user = GoodPrefs.getInstance().getObject("auth",Auth.class);

        RestRepository repository = new RestRepository();
        Call<Map<String,String>> mapCall = repository.getAppInterface().requestCode(
                user.getUserid()
        );

        mapCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()){

                    if (response.body() == null){
                        Toasty.error(ctx,"No response data").show();

                        return;
                    }

                    //Toasty.error(ctx,response.body().get("message")).show();

                    if ("success".equalsIgnoreCase(response.body().get("message"))){
                        startTimerCountDown();
                    }else{
                        Toasty.error(ctx,response.body().get("message")).show();
                    }

                }else{
                    Toasty.error(ctx,"Unexpected error occurred").show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toasty.error(ctx,"Error sending verification code").show();
            }
        });

    }




}
