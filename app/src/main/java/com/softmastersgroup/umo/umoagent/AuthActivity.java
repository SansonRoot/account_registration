package com.softmastersgroup.umo.umoagent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.softmastersgroup.umo.umoagent.models.Auth;
import com.softmastersgroup.umo.umoagent.models.AuthResponse;
import com.softmastersgroup.umo.umoagent.models.IDCardModel;
import com.softmastersgroup.umo.umoagent.models.ImageModel;
import com.softmastersgroup.umo.umoagent.models.LoginData;
import com.softmastersgroup.umo.umoagent.models.PhotoData;
import com.softmastersgroup.umo.umoagent.models.RegisterBundle;
import com.softmastersgroup.umo.umoagent.rest.RestRepository;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.File;

import es.dmoral.toasty.Toasty;
import nouri.in.goodprefslib.GoodPrefs;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    Button btnPrev, btnNewAccount;
    TextInputEditText etUsername, etPassword, etPasswordConfirm;
    MKLoader loader;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ctx = AuthActivity.this;

        init();

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, AddressActivity.class));
                finish();
            }
        });

        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void init() {
        btnPrev = findViewById(R.id.btnPrev);
        btnNewAccount = findViewById(R.id.btnCreateAccount);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        etUsername = findViewById(R.id.etUsername);
        loader = findViewById(R.id.loader);

        bindData();
    }

    private void bindData() {
        RegisterBundle bundle = GoodPrefs.getInstance().getObject("register_bundle", RegisterBundle.class);

        LoginData loginData = bundle.getLogindata();

        if (loginData == null) return;

        etUsername.setText(loginData.getLogin());
        etPassword.setText(loginData.getPassword());

    }

    private void register() {
        RegisterBundle bundle = GoodPrefs.getInstance().getObject("register_bundle", RegisterBundle.class);

        if (bundle == null) {
            //Toasty.error(ctx,);
            return;
        }

        ImageModel model = GoodPrefs.getInstance().getObject("photo",ImageModel.class);
        IDCardModel idCard = GoodPrefs.getInstance().getObject("id_card",IDCardModel.class);
        IDCardModel proof_of_address = GoodPrefs.getInstance().getObject("proof_of_address",IDCardModel.class);

        File file = new File(model.getUrl());
        RequestBody requestBody =  RequestBody.create(MediaType.parse(model.getUrl()),file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("photo",model.getImage_name(),requestBody);

        Gson gson = new Gson();
        String id = idCard.getId_number()+":"+idCard.getId_type()+":"+idCard.getImage_name();

        File idFile = new File(idCard.getImage());
        RequestBody idCardRB =  RequestBody.create(MediaType.parse(idCard.getImage()),idFile);
        MultipartBody.Part idCardImage = MultipartBody.Part.createFormData("id_card",id,idCardRB);

        String proof = proof_of_address.getId_number()+":"+proof_of_address.getId_type()+":"+proof_of_address.getImage_name();

        File res = new File(proof_of_address.getImage());
        RequestBody residentRB =  RequestBody.create(MediaType.parse(proof_of_address.getImage()),res);
        MultipartBody.Part residentImage = MultipartBody.Part.createFormData("proof_of_address",proof,residentRB);



        LoginData loginData = new LoginData();
        loginData.setLogin(etUsername.getText().toString());
        loginData.setPassword(etPassword.getText().toString());

        bundle.setLogindata(loginData);

        GoodPrefs.getInstance().saveObject("register_bundle", bundle);

        //gson = new Gson();
        String bundle1 = gson.toJson(bundle);



        RequestBody name = RequestBody.create(okhttp3.MultipartBody.FORM,bundle1);

        loader.setVisibility(View.VISIBLE);
        RestRepository restRepository = new RestRepository();
        Call<AuthResponse> mapCall = restRepository.getAppInterface().register(
                name,image,idCardImage,residentImage
        );

        mapCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {

                    loader.setVisibility(View.GONE);

                    if (response.body() == null) {
                        Toasty.success(ctx, "No response resource received").show();
                        return;
                    }

                    if (response.body().getData() == null) {
                        Toasty.success(ctx, "No response data received").show();
                        return;
                    }

                    Toasty.success(ctx, response.body().getMessage()).show();


                    toVerification(response.body().getData());

                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toasty.error(ctx, t.toString()).show();

            }
        });
    }

    private void toVerification(Auth auth) {

        GoodPrefs.getInstance().saveBoolean("isLoggedIn", true);
        GoodPrefs.getInstance().saveObject("auth", auth);
        GoodPrefs.getInstance().deleteValue("register_bundle");
        GoodPrefs.getInstance().deleteValue("photo");
        GoodPrefs.getInstance().deleteValue("id_card");
        GoodPrefs.getInstance().deleteValue("proof_of_address");

        startActivity(new Intent(ctx, VerifyPhone.class));
        finish();

    }


}
