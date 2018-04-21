package com.sc.semicolon.lombapertamina.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cia on 08/03/2018.
 */

public class RetroClient {

    //    private static final String ROOT_URL = "http://inssang.can.web.id/images/";
    private static final String ROOT_URL = "http://192.168.43.65/Pertamina/avatar/";

    public RetroClient(){
    }

    private static Retrofit getRetrofitClient(){
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

    }

    public static ApiService getService(){
        return getRetrofitClient().create(ApiService.class);
    }
}
