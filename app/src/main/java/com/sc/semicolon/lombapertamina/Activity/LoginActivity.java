package com.sc.semicolon.lombapertamina.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.semicolon.lombapertamina.Activity.User.MainUserActivity;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;
import com.sc.semicolon.lombapertamina.Retrofit.ApiService;
import com.sc.semicolon.lombapertamina.Retrofit.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private CardView cardLogin;
    private TextView txtRegister;
    ProgressDialog pd;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        pd = new ProgressDialog(this);
        shared = new Shared(this);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        cardLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUsername.getText().toString().isEmpty()){
                    edtUsername.setError("username harus diisi");
                }else if (edtPassword.getText().toString().isEmpty()){
                    edtPassword.setError("password harus diisi");
                }else {
                    Login();
                }

            }
        });
    }

    private void Login(){
        pd.setTitle("Loading ...");
        pd.setCancelable(false);
        pd.show();

        ApiService apiService = Client.getInstanceRetrofit();
        apiService.login().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            String id = jsonObject.optString("id_user");
                            String username = jsonObject.optString("username");
                            String password = jsonObject.optString("password");
                            String nama = jsonObject.optString("nama_pembeli");
                            String email = jsonObject.optString("email");
                            String alamat = jsonObject.optString("alamat_pembeli");
                            String telepon = jsonObject.optString("telepon_pembeli");
                            String foto = jsonObject.getString("foto_pembeli");
                            String id_agen = jsonObject.optString("id_agen");
                            String nama_agen = jsonObject.optString("nama_agen");
                            String nomor_agen = jsonObject.optString("nomor_agen");
                            String slogan = jsonObject.optString("slogan_agen");
                            String alamat_agen = jsonObject.optString("alamat_agen");
                            String avatar_agen = jsonObject.optString("avatar_agen");
                            String status = jsonObject.optString("status");
                            String telepon_agen = jsonObject.optString("telepon_agen");
                            String latitude = jsonObject.optString("latitude");
                            String longitude = jsonObject.optString("longitude");


                            if (username.equals(edtUsername.getText().toString()) && password.equals(edtPassword.getText().toString())){
                                shared.saveSPString(shared.SP_ID_USER,id);
                                shared.saveSPString(shared.SP_NAMA_USER,nama);
                                shared.saveSPString(shared.SP_EMAIL,email);
                                shared.saveSPString(shared.SP_ALAMAT_USER,alamat);
                                shared.saveSPString(shared.SP_TELEPON_USER,telepon);
                                shared.saveSPString(shared.SP_FOTO_USER,foto);
                                shared.saveSPString(shared.SP_ID_AGEN,id_agen);
                                shared.saveSPString(shared.SP_NAMA_AGEN,nama_agen);
                                shared.saveSPString(shared.SP_NOMOR_AGEN, nomor_agen);
                                shared.saveSPString(shared.SP_SLOGAN, slogan);
                                shared.saveSPString(shared.SP_ALAMAT_AGEN,alamat_agen);
                                shared.saveSPString(shared.SP_AVATAR_AGEN, avatar_agen);
                                shared.saveSPString(shared.SP_STATUS, status);
                                shared.saveSPString(shared.SP_TELEPON_AGEN, telepon_agen);
                                shared.saveSPString(shared.SP_LATITUDE, latitude);
                                shared.saveSPString(shared.SP_TLONGITUDE, longitude);
                                shared.saveSPBoolean(shared.SP_SUDAH_LOGIN, true);
                                Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                                startActivity(intent);
                            }else {
                                edtPassword.setError("Login gagal Cek akun anda");
                                edtUsername.setError("Login gagal Cek akun anda");
//                                Toast.makeText(LoginActivity.this, "Login gagal Cek akun anda", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        cardLogin = (CardView) findViewById(R.id.card_login);
        txtRegister = (TextView) findViewById(R.id.txt_register);
    }
}
