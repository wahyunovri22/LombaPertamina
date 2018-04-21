package com.sc.semicolon.lombapertamina.Activity.Agen;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.semicolon.lombapertamina.Activity.User.HistoryUserActivity;
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

public class HistoryAgenActivity extends AppCompatActivity {

    private LinearLayout div;
    ProgressDialog pd;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_agen);
        initView();

        shared = new Shared(this);
        getHistory();
    }

    private void getHistory(){
        pd = new ProgressDialog(this);
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
                            Toast.makeText(HistoryAgenActivity.this, "data Transaksi kosong", Toast.LENGTH_SHORT).show();
                        }else {
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);
                                String id_user = jsonObject.optString("id_user");
                                String id_agen = jsonObject.optString("id_agen");
                                String nama_pembeli = jsonObject.optString("nama_pembeli");
                                String nama_agen = jsonObject.optString("nama_agen");
                                String nama_barang = jsonObject.optString("nama_barang");
                                String jumlah = jsonObject.getString("jumlah");
                                String harga = jsonObject.optString("harga");
                                String tgl_beli = jsonObject.optString("tgl_beli");
                                String tlp = jsonObject.optString("telp_pembeli");
                                String status_pesan = jsonObject.optString("status_pesan");
                                String pesan = jsonObject.optString("status");

                                if (shared.getSpIdAgen().equals(id_agen)){
                                    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View addview = layoutInflater.inflate(R.layout.row_history, null);

                                    final TextView namaAgen = addview.findViewById(R.id.txt_nama_agen);
                                    final TextView namaBarang = addview.findViewById(R.id.txt_barang_pesanan);
                                    final TextView tanggalPesan = addview.findViewById(R.id.txt_tgl_pesan);
                                    final TextView JumlahPesan = addview.findViewById(R.id.txt_jumlah_beli);
                                    final TextView HargaTotal = addview.findViewById(R.id.txt_total_harga);
                                    final TextView Status = addview.findViewById(R.id.txt_status);
                                    final Button button = addview.findViewById(R.id.btn_barang_sampai);

                                    namaAgen.setText(nama_pembeli);
                                    namaBarang.setText(nama_barang);
                                    JumlahPesan.setText(jumlah);
                                    tanggalPesan.setText(tgl_beli);
                                    Status.setText(pesan);
                                    HargaTotal.setText(harga);

                                    button.setVisibility(View.GONE);

                                    div.addView(addview);
                                }else {
//                                    Toast.makeText(HistoryUserActivity.this, "History kosong login dahulu", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HistoryAgenActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initView() {
        div = (LinearLayout) findViewById(R.id.div);
    }
}
