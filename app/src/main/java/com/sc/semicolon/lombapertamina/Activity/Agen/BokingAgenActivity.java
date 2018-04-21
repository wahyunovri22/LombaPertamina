package com.sc.semicolon.lombapertamina.Activity.Agen;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.semicolon.lombapertamina.Activity.HistoryActivity;
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

public class BokingAgenActivity extends AppCompatActivity {

    private LinearLayout div;
    ProgressDialog pd;
    Shared shared;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boking_agen);
        initView();
        pd = new ProgressDialog(this);
        shared = new Shared(this);
        permissionFile();
        getData();
    }

    private void getData(){
        pd.setTitle("Loading ...");
        pd.setCancelable(false);
        pd.show();
        final ApiService apiService = Client.getInstanceRetrofit();
        apiService.getTransaksi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray == null){
                            Toast.makeText(BokingAgenActivity.this, "data kosong", Toast.LENGTH_SHORT).show();
                        }else {
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);
                                String id_user = jsonObject.optString("id_user");
                                String id_transaksi = jsonObject.optString("id_transaksi");
                                String id_agen = jsonObject.optString("id_agen");
                                String nama_pembeli = jsonObject.optString("nama_pembeli");
                                String nama_barang = jsonObject.optString("nama_barang");
                                String jumlah = jsonObject.getString("jumlah");
                                String harga = jsonObject.optString("harga");
                                String tgl_beli = jsonObject.optString("tgl_beli");
                                String status_pesan = jsonObject.optString("status_pesan");
                                String tlp = jsonObject.optString("telp_pembeli");
                                String pesan = jsonObject.optString("status");

                                if (shared.getSpIdAgen().equals(id_agen) && status_pesan.equals("BOKING")){
                                    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View addview = layoutInflater.inflate(R.layout.row_history_agen, null);

                                    final TextView namaAgen = addview.findViewById(R.id.txt_nama_agen);
                                    final TextView idTrans = addview.findViewById(R.id.txt_id_transaksi);
                                    final TextView txtTelp = addview.findViewById(R.id.txt_telp);
                                    final TextView namaBarang = addview.findViewById(R.id.txt_barang_pesanan);
                                    final TextView tanggalPesan = addview.findViewById(R.id.txt_tgl_pesan);
                                    final TextView JumlahPesan = addview.findViewById(R.id.txt_jumlah_beli);
                                    final TextView HargaTotal = addview.findViewById(R.id.txt_total_harga);
                                    final TextView Status = addview.findViewById(R.id.txt_status);
                                    final Button button = addview.findViewById(R.id.btn_barang_sampai);
                                    final Button btnBatal = addview.findViewById(R.id.btn_batal);
                                    final Button btnTolak = addview.findViewById(R.id.btn_tolak);

                                    namaAgen.setText(nama_pembeli);
                                    idTrans.setText(id_transaksi);
                                    txtTelp.setText(tlp);
                                    namaBarang.setText(nama_barang);
                                    JumlahPesan.setText(jumlah);
                                    tanggalPesan.setText(tgl_beli);
                                    Status.setText(pesan);
                                    HargaTotal.setText(harga);
                                    if (pesan.equals("DISETUJUI")){
                                        button.setVisibility(View.GONE);
                                        btnBatal.setVisibility(View.VISIBLE);
                                        btnTolak.setVisibility(View.GONE);
                                    }else if (pesan.equals("DITOLAK")){
                                        button.setVisibility(View.GONE);
                                        btnBatal.setVisibility(View.GONE);
                                        btnTolak.setVisibility(View.GONE);
                                    }else if (pesan.equals("SAMPAI")){
                                        button.setVisibility(View.GONE);
                                        btnBatal.setVisibility(View.GONE);
                                        btnTolak.setVisibility(View.GONE);
                                    }
                                    else {
                                        btnTolak.setVisibility(View.VISIBLE);
                                        button.setVisibility(View.VISIBLE);
                                        btnBatal.setVisibility(View.GONE);
                                        button.setText("Terima Boking");
                                    }
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ApiService apiService1 = Client.getInstanceRetrofit();
                                            apiService1.updateStatus(idTrans.getText().toString(),"DISETUJUI").enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    pd.dismiss();
                                                    if (response.isSuccessful()){
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                            String pesan = jsonObject1.optString("pesan");
                                                            Toast.makeText(BokingAgenActivity.this, ""+pesan, Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    pd.dismiss();
                                                    Toast.makeText(BokingAgenActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                                    btnBatal.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ApiService apiService2 = Client.getInstanceRetrofit();
                                            apiService2.updateStatus(idTrans.getText().toString(),"DIBATALKAN").enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    pd.dismiss();
                                                    if (response.isSuccessful()){
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                            String pesan = jsonObject1.optString("pesan");
                                                            Toast.makeText(BokingAgenActivity.this, ""+pesan, Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    dialog = new AlertDialog.Builder(BokingAgenActivity.this);
                                                    inflater = getLayoutInflater();
                                                    dialogView = inflater.inflate(R.layout.custom_pesan, null);
                                                    dialog.setView(dialogView);
                                                    dialog.setCancelable(true);
                                                    dialog.setIcon(R.mipmap.ic_launcher);
                                                    dialog.setTitle("Alasan Membatalkan Pesanan");

                                                    final EditText edtUcapan    = (EditText) dialogView.findViewById(R.id.edt_ucapan);

                                                    edtUcapan.setText(null);

                                                    dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            edtUcapan.getText().toString();

                                                            try {
                                                                SmsManager smsManager = SmsManager.getDefault();
                                                                smsManager.sendTextMessage(txtTelp.getText().toString(), null,
                                                                        ""+ namaAgen.getText().toString() + " " + edtUcapan.getText().toString(), null, null);
                                                                Toast.makeText(getApplicationContext(), "sms berhasil", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }catch (Exception e){
                                                                Toast.makeText(getApplicationContext(), "sms gagal dikirim cek pulsa atau perizinan aplikasi anda", Toast.LENGTH_SHORT).show();
                                                                e.printStackTrace();
                                                            }
//                Toast.makeText(RegisterActivity.this, ""+edtUcapan.getText().toString(), Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    });

                                                    dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });

                                                    dialog.show();
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    pd.dismiss();
                                                    Toast.makeText(BokingAgenActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                    div.addView(addview);
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
                pd.dismiss();
                Toast.makeText(BokingAgenActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        div = (LinearLayout) findViewById(R.id.div);
    }
    private void permissionFile() {
        if (ContextCompat.checkSelfPermission(BokingAgenActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BokingAgenActivity.this,
                    Manifest.permission.SEND_SMS)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(BokingAgenActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    // untuk menampilkan dialog
    private void DialogForm() {

    }
}
