package com.sc.semicolon.lombapertamina.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.semicolon.lombapertamina.Activity.User.CekPesananUserActivity;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BeliBarangActivity extends AppCompatActivity {

    private CardView card;
    private CircleImageView imgBarang;
    private TextView txtNamaBarang;
    private TextView txtHargaBarang;
    private TextView txtStokBarang;
    private EditText edtJumlahPesan;
    private Button btnPesan;
    String idBarang;
    String idAgen;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli_barang);
        initView();
        shared = new Shared(this);
        Awal();
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shared.getSPSudahLogin()) {
                    Hitung();
                }else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
    private void Hitung(){
        if (edtJumlahPesan.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "jumlah pesan harus diisi", Toast.LENGTH_SHORT).show();
        }else {
            int a = Integer.parseInt(edtJumlahPesan.getText().toString());
            int b = Integer.parseInt(txtStokBarang.getText().toString());
            int total = a * Integer.parseInt(txtHargaBarang.getText().toString());
            int hasil = b-a;
            if (hasil < 0){
                Toast.makeText(getApplicationContext(), "maaf jumlah pesanan melebihi stok", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(BeliBarangActivity.this, CekPesananUserActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent1.putExtras(bundle);
                intent.putExtra("nama", txtNamaBarang.getText().toString());
                intent.putExtra("harga", txtHargaBarang.getText().toString());
                intent.putExtra("stok", txtStokBarang.getText().toString());
                intent.putExtra("total", String.valueOf(total));
                intent.putExtra("jumlah", String.valueOf(a));
                intent.putExtra("id_agen", idAgen);
                intent.putExtra("id_barang", idBarang);
                startActivity(intent);
            }
        }

    }

    private void Awal(){
        txtNamaBarang.setText(getIntent().getStringExtra("nama"));
        txtHargaBarang.setText(getIntent().getStringExtra("harga"));
        txtStokBarang.setText(getIntent().getStringExtra("stok"));
        idBarang = getIntent().getStringExtra("id_barang");
        idAgen = getIntent().getStringExtra("id_agen");
        Picasso.with(getApplicationContext())
                .load("http://192.168.43.65/Pertamina/image/" + getIntent().getStringExtra("image"))
                .error(R.drawable.ic_launcher_foreground)
                .into(imgBarang);


    }

    private void initView() {
        card = (CardView) findViewById(R.id.card);
        imgBarang = (CircleImageView) findViewById(R.id.img_barang);
        txtNamaBarang = (TextView) findViewById(R.id.txt_nama_barang);
        txtHargaBarang = (TextView) findViewById(R.id.txt_harga_barang);
        txtStokBarang = (TextView) findViewById(R.id.txt_stok_barang);
        edtJumlahPesan = (EditText) findViewById(R.id.edt_jumlah_pesan);
        btnPesan = (Button) findViewById(R.id.btn_pesan);
    }
}
