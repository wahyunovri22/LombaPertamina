package com.sc.semicolon.lombapertamina.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.MapsInitializer;
import com.sc.semicolon.lombapertamina.Activity.User.DetailTokoActivity;
import com.sc.semicolon.lombapertamina.Activity.User.MainUserActivity;
import com.sc.semicolon.lombapertamina.Helper.Config;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;
import com.sc.semicolon.lombapertamina.Retrofit.ApiService;
import com.sc.semicolon.lombapertamina.Retrofit.Client;
import com.sc.semicolon.lombapertamina.Retrofit.Result;
import com.sc.semicolon.lombapertamina.Retrofit.RetroClient;
import com.sc.semicolon.lombapertamina.Retrofit.RetroClientDua;
import com.sc.semicolon.lombapertamina.Service.GPSTracker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuatTokoActivity extends AppCompatActivity {

    private CircleImageView imgUser;
    private EditText edtNama;
    private EditText edtNomorAgen;
    private EditText edtSlogan;
    private EditText edtAlamat;
    private EditText edtTelponAgen;
    private EditText edtLatitue;
    private EditText edtLongitude;
    private CardView cardEditProfil;
    ProgressDialog pd;
    Shared shared;
    private String imagePath, h, status;
    GPSTracker gpsTracker;
    private double Lat, Long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_toko);
        initView();
        Awal();
        permissionFile();
        permissionLokasi();

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooserGalerry();
            }
        });

        cardEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuatToko();
            }
        });

    }

    private void BuatToko(){
        pd.setTitle("Loading ...");
        pd.setCancelable(false);
        pd.show();
        status = "aktif";
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.daftarAgen(shared.getSpIdUser(),
                edtNama.getText().toString(),
                edtNomorAgen.getText().toString(),
                edtSlogan.getText().toString(),
                edtAlamat.getText().toString(),
                edtTelponAgen.getText().toString(),
                status,
                h,
                Lat,
                Long).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String pesan = jsonObject.optString("pesan");
                        shared.saveSPString(shared.SP_STATUS, status);
                        shared.saveSPString(shared.SP_NAMA_AGEN, edtNama.getText().toString());
                        shared.saveSPString(shared.SP_TELEPON_AGEN, edtTelponAgen.getText().toString());
                        shared.saveSPString(shared.SP_AVATAR_AGEN, edtAlamat.getText().toString());
                        shared.saveSPString(shared.SP_AVATAR_AGEN, h);
                        Toast.makeText(getApplicationContext(), "" +pesan, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
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
                Toast.makeText(getApplicationContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Awal(){
        pd = new ProgressDialog(this);
        shared = new Shared(this);
        h = shared.getSpAvatarAgen();
        edtTelponAgen.setText(shared.getSpTeleponUser());
        edtAlamat.setText(shared.getSpAlamatUser());
        gpsTracker = new GPSTracker(BuatTokoActivity.this);
        if (gpsTracker.canGetLocation()) {
            Lat = gpsTracker.getLatitude();
            Long = gpsTracker.getLongitude();
            MapsInitializer.initialize(BuatTokoActivity.this);
        }
        else {
            gpsTracker.showSettingsAlert();
        }
        edtLatitue.setText(String.valueOf(Lat));
        edtLongitude.setText(String.valueOf(Long));
        edtLatitue.setFocusable(false);
        edtLongitude.setFocusable(false);
    }

    private void initView() {
        imgUser = (CircleImageView) findViewById(R.id.img_user);
        edtNama = (EditText) findViewById(R.id.edt_nama);
        edtNomorAgen = (EditText) findViewById(R.id.edt_nomor_agen);
        edtSlogan = (EditText) findViewById(R.id.edt_slogan);
        edtAlamat = (EditText) findViewById(R.id.edt_alamat);
        edtTelponAgen = (EditText) findViewById(R.id.edt_telpon_agen);
        edtLatitue = (EditText) findViewById(R.id.edt_latitue);
        edtLongitude = (EditText) findViewById(R.id.edt_longitude);
        cardEditProfil = (CardView) findViewById(R.id.card_edit_profil);
    }

    private void showFileChooserGalerry() {
        Intent qq = new Intent(Intent.ACTION_PICK);
        qq.setType("image/*");
        startActivityForResult(Intent.createChooser(qq, "Pilih Foto"), 100);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Gambar Tidak Ada", Toast.LENGTH_SHORT).show();
                return;

            }

            Uri selectImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor c = getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
            if (c != null) {
                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePathColumn[0]);
                imagePath = c.getString(columnIndex);


                Picasso.with(this).load(new File(imagePath)).into(imgUser);
                h = new File(imagePath).getName();
                uploadImage();

                c.close();


            } else {

                Toast.makeText(this, "Gambar Tidak Ada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage() {

        final ProgressDialog p;
        p = new ProgressDialog(this);
        p.setMessage("Proses Upload Foto");
        p.show();

        ApiService s = RetroClient.getService();


        File f = new File(imagePath);

        Toast.makeText(BuatTokoActivity.this, "Gambar " + h, Toast.LENGTH_SHORT).show();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);

        final MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file", f.getName(), requestFile);
        Call<Result> resultCAll = s.postIMmage(part);
        resultCAll.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                p.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(BuatTokoActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BuatTokoActivity.this, "Upload Gambar Gagal", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(BuatTokoActivity.this, "Upload Gambar Gagal", Toast.LENGTH_SHORT).show();
                }

                imagePath = "";

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(BuatTokoActivity.this, "Gagal Upload Fail", Toast.LENGTH_SHORT).show();
                p.dismiss();


            }
        });
    }
    private void permissionLokasi() {
        if (ContextCompat.checkSelfPermission(BuatTokoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BuatTokoActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(BuatTokoActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Config.MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    private void permissionFile() {
        if (ContextCompat.checkSelfPermission(BuatTokoActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BuatTokoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(BuatTokoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Config.MY_PERMISSIONS_REQUEST_FILE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
