package com.sc.semicolon.lombapertamina.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cia on 08/03/2018.
 */

public class Shared {
    public static final String SP_MAHASISWA_APP = "spAsuransiAgen";

    public static final String SP_ID_USER = "spId";
    public static final String SP_NAMA_USER= "spNama";
    public static final String SP_RULE = "spRule";
    public static final String SP_ALAMAT_USER = "spStatus";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_TELEPON_USER = "sEmail";
    public static final String SP_FOTO_USER = "sFullname";
    public static final String SP_ID_AGEN = "sKtp";
    public static final String SP_NAMA_AGEN = "sTlp";
    public static final String SP_NOMOR_AGEN = "sAvatar";
    public static final String SP_SLOGAN = "sToken";
    public static final String SP_ALAMAT_AGEN = "sAsuransi";
    public static final String SP_AVATAR_AGEN = "sStatusApp";
    public static final String SP_STATUS = "sSMS";
    public static final String SP_TELEPON_AGEN= "sList";
    public static final String SP_LATITUDE= "sLtd";
    public static final String SP_TLONGITUDE= "sLong";
    public static final String SP_BUNDLE_ID_AGEN = "sid_bundle";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public Shared(Context context){
        sp = context.getSharedPreferences(SP_MAHASISWA_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpIdUser() {
        return sp.getString(SP_ID_USER, "");
    }

    public String getSpNamaUser() {
        return sp.getString(SP_NAMA_USER, "");
    }

    public String getSpRule() {
        return sp.getString(SP_RULE, "");
    }

    public String getSpAlamatUser() {
        return sp.getString(SP_ALAMAT_USER, "");
    }

    public String getSpEmail() {
        return sp.getString(SP_EMAIL, "");
    }

    public String getSpTeleponUser() {
        return sp.getString(SP_TELEPON_USER, "");
    }

    public String getSpFotoUser() {
        return sp.getString(SP_FOTO_USER, "");
    }

    public String getSpIdAgen() {
        return sp.getString(SP_ID_AGEN, "");
    }

    public String getSpNamaAgen() {
        return sp.getString(SP_NAMA_AGEN, "");
    }

    public String getSpNomorAgen() {
        return sp.getString(SP_NOMOR_AGEN, "");
    }

    public String getSpSlogan() {
        return sp.getString(SP_SLOGAN, "");
    }

    public String getSpAlamatAgen() {
        return sp.getString(SP_ALAMAT_AGEN, "");
    }

    public String getSpAvatarAgen() {
        return sp.getString(SP_AVATAR_AGEN, "");
    }

    public String getSpStatus() {
        return sp.getString(SP_STATUS, "");
    }

    public String getSpTeleponAgen() {
        return sp.getString(SP_TELEPON_AGEN, "");
    }

    public String getSpBundleIdAgen() {
        return sp.getString(SP_BUNDLE_ID_AGEN, "");
    }

    public String getSpLatitude() {
        return sp.getString(SP_LATITUDE, "");
    }

    public String getSpTlongitude() {
        return sp.getString(SP_TLONGITUDE, "");
    }

    public Boolean getSPSudahLogin() {
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}
