package com.softmastersgroup.umo.umoagent;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softmastersgroup.umo.umoagent.models.Auth;
import com.softmastersgroup.umo.umoagent.models.AuthResponse;
import com.softmastersgroup.umo.umoagent.models.BioData;
import com.softmastersgroup.umo.umoagent.models.LoginData;
import com.softmastersgroup.umo.umoagent.models.LoginModel;
import com.softmastersgroup.umo.umoagent.models.RegisterBundle;
import com.softmastersgroup.umo.umoagent.rest.RestRepository;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.Map;

import es.dmoral.toasty.Toasty;
import nouri.in.goodprefslib.GoodPrefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnIndividual,btnBusiness,btnLogin;
    TextInputEditText etUsername,etPassword;
    MKLoader loader;
    Context ctx;
    RegisterBundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = LoginActivity.this;

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

        setContentView(R.layout.activity_login);

        init();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(etUsername,etPassword)){
                    login(etUsername.getText().toString(),etPassword.getText().toString());
                }
            }
        });

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
    }

    private void init(){
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        loader = findViewById(R.id.loader);
        btnBusiness = findViewById(R.id.btnBusiness);
        btnIndividual = findViewById(R.id.btnIndividual);

        loadData();
    }

    private void loadData(){
        bundle = GoodPrefs.getInstance().getObject("register_bundle",RegisterBundle.class);

        if (bundle == null){

            bundle = new RegisterBundle();

        }
    }

    private boolean isValid(EditText etUsername,EditText etPassword){
        boolean valid = true;

        if (etUsername.getText().toString().isEmpty()){
            valid = false;
            etUsername.setError("Username required");
        }

        if (etPassword.getText().toString().isEmpty()){
            valid = false;
            etUsername.setError("Password required");
        }

        return valid;

    }

    private void login(String username,String password){
        loader.setVisibility(View.VISIBLE);

        LoginData loginData = new LoginData(username,password);

        RestRepository repository = new RestRepository();
        Call<AuthResponse> mapCall = repository.getAppInterface().login(new LoginModel(loginData));

        mapCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                loader.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body() == null) {
                        Toasty.success(ctx, "No response resource received").show();
                        return;
                    }

                    if (response.body().getData() == null) {
                        Toasty.success(ctx, "No response data received").show();
                        return;
                    }

                    toMain(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toasty.error(ctx,t.toString()).show();
            }
        });
    }

    private void toMain(Auth auth){

        GoodPrefs.getInstance().saveObject("auth",auth);
        //Toasty.success(ctx,auth.getPhone_verified_at()).show();

        if (auth.getPhone_verified_at() == null || auth.getPhone_verified_at().isEmpty()){

            GoodPrefs.getInstance().saveBoolean("fromLogin",true);

            startActivity(new Intent(ctx,VerifyPhone.class));
            finish();

        }else{
            startActivity(new Intent(ctx,MainApp.class));
            finish();
        }
    }
}
