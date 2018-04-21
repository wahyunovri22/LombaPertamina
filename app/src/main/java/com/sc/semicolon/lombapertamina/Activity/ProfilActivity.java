package com.sc.semicolon.lombapertamina.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sc.semicolon.lombapertamina.Activity.User.BokingUserActivity;
import com.sc.semicolon.lombapertamina.Activity.User.HistoryUserActivity;
import com.sc.semicolon.lombapertamina.Activity.User.MainUserActivity;
import com.sc.semicolon.lombapertamina.Activity.User.PesananUserActivity;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {

    private CircleImageView imgUser;
    private TextView txtNamaUser;
    private TextView txtAlamatUser;
    private CardView pesanan;
    private CardView boking;
    private CardView history;
    Shared shared;
    Handler handler = new Handler();
    private Button btnLogout;
    private TextView txtTelpUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        initView();
        shared = new Shared(this);
        this.handler = new Handler();
        this.handler.postDelayed(runnable, 2000);
        Awal();

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfilActivity.class);
                startActivity(intent);
            }
        });
        pesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PesananUserActivity.class);
                startActivity(intent);
            }
        });

        boking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BokingUserActivity.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryUserActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.saveSPBoolean(shared.SP_SUDAH_LOGIN, false);
                shared.saveSPString(shared.SP_ID_AGEN, "");
                shared.saveSPString(shared.SP_ID_USER,"");
                startActivity(new Intent(getApplicationContext(), MainUserActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    private void initView() {
        imgUser = (CircleImageView) findViewById(R.id.img_user);
        txtNamaUser = (TextView) findViewById(R.id.txt_nama_user);
        txtAlamatUser = (TextView) findViewById(R.id.txt_alamat_user);
        pesanan = (CardView) findViewById(R.id.pesanan);
        boking = (CardView) findViewById(R.id.boking);
        history = (CardView) findViewById(R.id.history);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        txtTelpUser = (TextView) findViewById(R.id.txt_telp_user);
    }

    private void Awal() {
        if (shared.getSPSudahLogin()) {
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogout.setVisibility(View.GONE);
        }
        Picasso.with(getApplicationContext())
                .load("http://192.168.43.65/Pertamina/avatar/" + shared.getSpFotoUser())
                .error(R.drawable.ic_launcher_foreground)
                .into(imgUser);
        txtNamaUser.setText(shared.getSpNamaUser());
        txtAlamatUser.setText(shared.getSpAlamatUser());
        txtTelpUser.setText(shared.getSpTeleponUser());
    }

    public final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Awal();
            ProfilActivity.this.handler.postDelayed(runnable, 2000);
        }
    };
}
