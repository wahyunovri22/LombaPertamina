package com.sc.semicolon.lombapertamina.Retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cis on 07/04/2018.
 */

public class Client {
    private static String BASE_URL = "http://192.168.43.65/Pertamina/api/";
//    private static String BASE_URL = "http://192.168.88.32/Pertamina/api/";
    private static Retrofit getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public static ApiService getInstanceRetrofit(){
        return getClient().create(ApiService.class);
    }
}
