package com.sc.semicolon.lombapertamina.Activity.Agen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.sc.semicolon.lombapertamina.Fragment.Agen.BarangAgenFragment;
import com.sc.semicolon.lombapertamina.Fragment.Agen.InputBarangFragment;
import com.sc.semicolon.lombapertamina.Fragment.Agen.ReviewAgenFragment;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TokoActivity extends AppCompatActivity {

    private CircleImageView imgAgen;
    private TextView txtNamaAgen;
    private TextView txtAlamatAgen;
    private TextView txtTelpAgen;
    Shared shared;
    Handler handler = new Handler();
    private SmartTabLayout viewpagertab;
    private ViewPager viewpager;
    private TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko);
        initView();
        shared = new Shared(this);
        this.handler = new Handler();
        this.handler.postDelayed(runnable, 2000);
        Awal();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
//                .add("FollowUp", FollowUpFragment.class)
                .add("Barang", BarangAgenFragment.class)
                .add("Review", ReviewAgenFragment.class)
                .add("Input", InputBarangFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        imgAgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditTokoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Awal() {
        txtNamaAgen.setText(shared.getSpNamaAgen());
        txtTelpAgen.setText(shared.getSpTeleponAgen());
        txtAlamatAgen.setText(shared.getSpAlamatAgen());
        txtSlogan.setText(shared.getSpSlogan());
        Picasso.with(getApplicationContext())
                .load("http://192.168.43.65/Pertamina/avatar/" + shared.getSpAvatarAgen())
                .error(R.drawable.ic_launcher_foreground)
                .into(imgAgen);
    }

    private void initView() {
        imgAgen = (CircleImageView) findViewById(R.id.img_agen);
        txtNamaAgen = (TextView) findViewById(R.id.txt_nama_agen);
        txtAlamatAgen = (TextView) findViewById(R.id.txt_alamat_agen);
        txtTelpAgen = (TextView) findViewById(R.id.txt_telp_agen);
        viewpagertab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        txtSlogan = (TextView) findViewById(R.id.txt_slogan);
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Awal();
            TokoActivity.this.handler.postDelayed(runnable, 2000);
        }
    };
}
