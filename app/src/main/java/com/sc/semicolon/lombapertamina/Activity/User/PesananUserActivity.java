package com.sc.semicolon.lombapertamina.Activity.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class PesananUserActivity extends AppCompatActivity {


    private LinearLayout div;
    ProgressDialog pd;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_user);
        initView();
        pd = new ProgressDialog(this);
        shared = new Shared(this);
        getHistory();
    }

    private void getHistory(){
        pd.setTitle("Loading ...");
        pd.setCancelable(false);
        pd.show();

        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getTransaksi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray == null){
                            Toast.makeText(PesananUserActivity.this, "data Transaksi kosong", Toast.LENGTH_SHORT).show();
                        }else {
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);
                                String id_user = jsonObject.optString("id_user");
                                String id_transaksi = jsonObject.optString("id_transaksi");
                                String nama_agen = jsonObject.optString("nama_agen");
                                String nama_barang = jsonObject.optString("nama_barang");
                                String jumlah = jsonObject.getString("jumlah");
                                String harga = jsonObject.optString("harga");
                                String tgl_beli = jsonObject.optString("tgl_beli");
                                String status_pesan = jsonObject.optString("status_pesan");
                                String pesan = jsonObject.optString("status");

                                if (shared.getSpIdUser().equals(id_user) && status_pesan.equals("KIRIM")){
                                    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View addview = layoutInflater.inflate(R.layout.row_history, null);

                                    final TextView namaAgen = addview.findViewById(R.id.txt_nama_agen);
                                    final TextView idTransaksi = addview.findViewById(R.id.txt_nama_agen);
                                    final TextView namaBarang = addview.findViewById(R.id.txt_barang_pesanan);
                                    final TextView tanggalPesan = addview.findViewById(R.id.txt_tgl_pesan);
                                    final TextView JumlahPesan = addview.findViewById(R.id.txt_jumlah_beli);
                                    final TextView HargaTotal = addview.findViewById(R.id.txt_total_harga);
                                    final TextView Status = addview.findViewById(R.id.txt_status);
                                    final Button button = addview.findViewById(R.id.btn_barang_sampai);

                                    idTransaksi.setText(id_transaksi);
                                    namaAgen.setText(nama_agen);
                                    namaBarang.setText(nama_barang);
                                    JumlahPesan.setText(jumlah);
                                    tanggalPesan.setText(tgl_beli);
                                    Status.setText(pesan);
                                    HargaTotal.setText(harga);
                                    if (pesan.equals("DISETUJUI")){
                                        button.setVisibility(View.VISIBLE);
                                    }else {
                                        button.setVisibility(View.GONE);
                                    }

                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ApiService apiService1 = Client.getInstanceRetrofit();
                                            apiService1.updateStatus(idTransaksi.getText().toString(),"SAMPAI").enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()){
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                            String pesan = jsonObject1.optString("pesan");
                                                            Toast.makeText(PesananUserActivity.this, ""+pesan, Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(PesananUserActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                    div.addView(addview);
                                }else {
//                                    Toast.makeText(PesananUserActivity.this, "data pesan kosong login dahulu", Toast.LENGTH_SHORT).show();
                                }
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
                Toast.makeText(PesananUserActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        div = (LinearLayout) findViewById(R.id.div);
    }
}
