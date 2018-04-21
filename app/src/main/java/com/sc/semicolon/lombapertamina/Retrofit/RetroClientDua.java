package com.sc.semicolon.lombapertamina.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cis on 12/04/2018.
 */

public class RetroClientDua {
    //    private static final String ROOT_URL = "http://inssang.can.web.id/images/";
    private static final String ROOT_URL = "http://192.168.43.65/Pertamina/image/";

    public RetroClientDua(){
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
