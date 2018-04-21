package com.sc.semicolon.lombapertamina.Activity.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sc.semicolon.lombapertamina.R;

public class CekPesananUserActivity extends AppCompatActivity {

    private TextView txtNamaBarang;
    private TextView txtJumlahBeli;
    private TextView txtHargaBarang;
    private TextView txtTotalHarga;
    String idAgen,idBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_pesanan_user);
        initView();
        Awal();
    }
    private void Awal(){
        txtNamaBarang.setText(getIntent().getStringExtra("nama"));
        txtHargaBarang.setText(getIntent().getStringExtra("harga"));
        idBarang = getIntent().getStringExtra("id_barang");
        idAgen = getIntent().getStringExtra("id_agen");
        txtTotalHarga.setText(getIntent().getStringExtra("total"));
        txtJumlahBeli.setText(getIntent().getStringExtra("jumlah"));
    }
    private void initView() {
        txtNamaBarang = (TextView) findViewById(R.id.txt_nama_barang);
        txtJumlahBeli = (TextView) findViewById(R.id.txt_jumlah_beli);
        txtHargaBarang = (TextView) findViewById(R.id.txt_harga_barang);
        txtTotalHarga = (TextView) findViewById(R.id.txt_total_harga);
    }
}
