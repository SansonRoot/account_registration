package com.softmastersgroup.umo.umoagent.rest;

import com.google.gson.GsonBuilder;
import com.softmastersgroup.umo.umoagent.utils.AndroidUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.softmastersgroup.umo.umoagent.utils.AndroidUtils.BASE_URL;

public class RestRepository {
    private AppInterface appInterface = null;

    public AppInterface getAppInterface(){

        if (appInterface == null){

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(logging);
            client.readTimeout(60,TimeUnit.SECONDS);
            client.writeTimeout(60,TimeUnit.SECONDS);


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .build();

            appInterface = retrofit.create(AppInterface.class);

        }

        return appInterface;
    }
}
