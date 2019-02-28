package com.softmastersgroup.umo.umoagent.rest;

import com.softmastersgroup.umo.umoagent.models.AuthResponse;
import com.softmastersgroup.umo.umoagent.models.LoginData;
import com.softmastersgroup.umo.umoagent.models.LoginModel;
import com.softmastersgroup.umo.umoagent.models.RegisterBundle;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AppInterface {

    @Multipart
    @POST("umoservices/nregister.php")
    Call<AuthResponse> register(
            @Part("register_data") RequestBody data,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part id,
            @Part MultipartBody.Part proof_of_residence
    );

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("umoservices/verifysms.php")
    Call<AuthResponse> verify(
            @QueryMap Map<String,String> uuid_code
    );

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("umoservices/requestcode.php")
    Call<Map<String,String>> requestCode(
            @Query("userid") String userid
    );


    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("umoservices/nlogin.php")
    Call<AuthResponse> login(
            @Body LoginModel logindata
    );



}
