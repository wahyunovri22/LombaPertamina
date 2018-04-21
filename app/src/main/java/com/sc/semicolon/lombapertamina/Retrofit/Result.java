package com.sc.semicolon.lombapertamina.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cia on 08/03/2018.
 */

public class Result {
    @SerializedName("result")
    @Expose
    private String result;

    public String getResult(){
        return  result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
