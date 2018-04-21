package com.sc.semicolon.lombapertamina.Activity.User;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.sc.semicolon.lombapertamina.Activity.BeliBarangActivity;
import com.sc.semicolon.lombapertamina.Fragment.Agen.BarangAgenFragment;
import com.sc.semicolon.lombapertamina.Fragment.Agen.InputBarangFragment;
import com.sc.semicolon.lombapertamina.Fragment.Agen.ReviewAgenFragment;
import com.sc.semicolon.lombapertamina.Fragment.User.BarangFragment;
import com.sc.semicolon.lombapertamina.Fragment.User.ReviewFragment;
import com.sc.semicolon.lombapertamina.Helper.Config;
import com.sc.semicolon.lombapertamina.R;
import com.sc.semicolon.lombapertamina.Retrofit.ApiService;
import com.sc.semicolon.lombapertamina.Retrofit.Client;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTokoActivity extends AppCompatActivity {

    private Button btnPesan;
    private TextView txtNamaAgen;
    private TextView txtAlamatAgen;
    private FloatingActionButton fbMaps;
    private FloatingActionButton fbTelepon;
    private SearchView search;
    private CircleImageView imgAgen;
    private LinearLayout div;
    ProgressDialog pd;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toko);
        initView();

        Awal();
        permissionCall();
        permissionLokasi();
//        getBarang();

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
//                .add("FollowUp", FollowUpFragment.class)
                .add("Barang", BarangFragment.class)
                .add("Review", ReviewFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CekPesananUserActivity.class);
                startActivity(intent);
            }
        });

        fbTelepon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse(getIntent().getStringExtra("telepon")));
//                    startActivity(i);
                String number = getIntent().getStringExtra("telepon");
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        fbMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=+" + getIntent().getStringExtra("alamat")));
                startActivity(intent);
            }
        });
    }

    private void initView() {
        btnPesan = (Button) findViewById(R.id.btn_pesan);
        txtNamaAgen = (TextView) findViewById(R.id.txt_nama_agen);
        txtAlamatAgen = (TextView) findViewById(R.id.txt_alamat_agen);
        fbMaps = (FloatingActionButton) findViewById(R.id.fb_maps);
        fbTelepon = (FloatingActionButton) findViewById(R.id.fb_telepon);
        search = (SearchView) findViewById(R.id.search);
        imgAgen = (CircleImageView) findViewById(R.id.img_agen);
        div = (LinearLayout) findViewById(R.id.div);
    }

    private void Awal() {
        pd = new ProgressDialog(this);
        Picasso.with(getApplicationContext()).load("http://192.168.43.65/Pertamina/avatar/" + getIntent().getStringExtra("image"))
                .error(R.drawable.ic_launcher_foreground).into(imgAgen);
        txtNamaAgen.setText(getIntent().getStringExtra("nama"));
        txtAlamatAgen.setText(getIntent().getStringExtra("alamat"));

    }

    private void getBarang(){
        pd.setTitle("Tunggu ...");
        pd.setCancelable(false);
        pd.show();
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getBarang().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray == null){
                            Toast.makeText(DetailTokoActivity.this, "data barang kosong", Toast.LENGTH_SHORT).show();
                        }else {
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);
                                final String nama_barang = jsonObject.optString("nama_barang");
                                final String harga_barang = jsonObject.optString("harga_barang");
                                final String stok = jsonObject.optString("stok");
                                final String avatar_barang = jsonObject.optString("avatar_barang");
                                final String id_agen_barang = jsonObject.optString("id_agen");
                                final String id_barang = jsonObject.optString("id_barang");

                                if (id_agen_barang.equals(getIntent().getStringExtra("id_agen"))){
                                    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View addview = layoutInflater.inflate(R.layout.row_barang, null);

                                    final TextView namaBarang = addview.findViewById(R.id.txt_nama_barang);
                                    final TextView hargaBarang = addview.findViewById(R.id.txt_harga_barang);
                                    final TextView stokBarang = addview.findViewById(R.id.txt_stok_barang);
                                    final ImageView image = addview.findViewById(R.id.img_barang);
                                    final CardView cardView = addview.findViewById(R.id.card);

                                    namaBarang.setText(nama_barang);
                                    hargaBarang.setText("Rp. " +harga_barang);
                                    stokBarang.setText(stok);
                                    Picasso.with(getApplicationContext()).load("http://192.168.43.65/Pertamina/image/" + avatar_barang)
                                            .error(R.drawable.ic_launcher_foreground)
                                            .into(image);

                                    cardView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getApplicationContext(), BeliBarangActivity.class);
                                            intent.putExtra("nama", nama_barang);
                                            intent.putExtra("harga", harga_barang);
                                            intent.putExtra("stok", stok);
                                            intent.putExtra("image", avatar_barang);
                                            intent.putExtra("id_agen", id_agen_barang);
                                            intent.putExtra("id_barang", id_barang);
                                            startActivity(intent);
                                        }
                                    });

                                    div.addView(addview);
                                }else {
                                    Toast.makeText(DetailTokoActivity.this, "Agen Belum Memasukkan Barang", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailTokoActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void permissionCall() {
        if (ContextCompat.checkSelfPermission(DetailTokoActivity.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DetailTokoActivity.this,
                    Manifest.permission.CALL_PHONE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(DetailTokoActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Config.MY_PERMISSIONS_REQUEST_CALL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void permissionLokasi() {
        if (ContextCompat.checkSelfPermission(DetailTokoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DetailTokoActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(DetailTokoActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Config.MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
