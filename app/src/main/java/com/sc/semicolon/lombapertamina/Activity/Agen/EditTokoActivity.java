package com.sc.semicolon.lombapertamina.Activity.Agen;

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

import com.google.android.gms.common.api.Api;
import com.sc.semicolon.lombapertamina.Activity.BuatTokoActivity;
import com.sc.semicolon.lombapertamina.Activity.User.MainUserActivity;
import com.sc.semicolon.lombapertamina.Helper.Config;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;
import com.sc.semicolon.lombapertamina.Retrofit.ApiService;
import com.sc.semicolon.lombapertamina.Retrofit.Client;
import com.sc.semicolon.lombapertamina.Retrofit.Result;
import com.sc.semicolon.lombapertamina.Retrofit.RetroClient;
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

public class EditTokoActivity extends AppCompatActivity {

    private CircleImageView imgUser;
    private EditText edtNama;
    private EditText edtSlogan;
    private EditText edtAlamat;
    private EditText edtTelponAgen;
    private CardView cardEditToko;
    ProgressDialog pd;
    Shared shared;
    private String imagePath, h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_toko);
        initView();
        Awal();
        permissionFile();


        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooserGalerry();
            }
        });
        cardEditToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditToko();
            }
        });
    }

    private void EditToko(){
        pd.setTitle("Loading ...");
        pd.setCancelable(false);
        pd.dismiss();
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.editAgen(shared.getSpIdUser(),
                edtNama.getText().toString(),
                edtSlogan.getText().toString(),
                edtAlamat.getText().toString(),
                edtTelponAgen.getText().toString(),
                h).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String pesan = jsonObject.optString("pesan");
                        shared.saveSPString(shared.SP_NAMA_AGEN, edtNama.getText().toString());
                        shared.saveSPString(shared.SP_SLOGAN, edtSlogan.getText().toString());
                        shared.saveSPString(shared.SP_ALAMAT_AGEN, edtAlamat.getText().toString());
                        shared.saveSPString(shared.SP_TELEPON_AGEN, edtTelponAgen.getText().toString());
                        shared.saveSPString(shared.SP_AVATAR_AGEN, h);
                        Toast.makeText(EditTokoActivity.this, ""+pesan, Toast.LENGTH_SHORT).show();
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

            }
        });
    }
    private void Awal(){
        pd = new ProgressDialog(this);
        shared = new Shared(this);
        edtNama.setText(shared.getSpNamaAgen());
        edtSlogan.setText(shared.getSpSlogan());
        edtAlamat.setText(shared.getSpAlamatAgen());
        edtTelponAgen.setText(shared.getSpTeleponAgen());
        h = shared.getSpAvatarAgen();
        Picasso.with(getApplicationContext())
                .load(Config.MY_AVATAR + shared.getSpAvatarAgen())
                .error(R.drawable.ic_launcher_foreground)
                .into(imgUser);
    }

    private void initView() {
        imgUser = (CircleImageView) findViewById(R.id.img_user);
        edtNama = (EditText) findViewById(R.id.edt_nama);
        edtSlogan = (EditText) findViewById(R.id.edt_slogan);
        edtAlamat = (EditText) findViewById(R.id.edt_alamat);
        edtTelponAgen = (EditText) findViewById(R.id.edt_telpon_agen);
        cardEditToko = (CardView) findViewById(R.id.card_edit_toko);
    }
    private void permissionFile() {
        if (ContextCompat.checkSelfPermission(EditTokoActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditTokoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(EditTokoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Config.MY_PERMISSIONS_REQUEST_FILE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
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

        Toast.makeText(EditTokoActivity.this, "Gambar " + h, Toast.LENGTH_SHORT).show();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);

        final MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file", f.getName(), requestFile);
        Call<Result> resultCAll = s.postIMmage(part);
        resultCAll.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                p.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(EditTokoActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditTokoActivity.this, "Upload Gambar Gagal", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EditTokoActivity.this, "Upload Gambar Gagal", Toast.LENGTH_SHORT).show();
                }

                imagePath = "";

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(EditTokoActivity.this, "Gagal Upload Fail", Toast.LENGTH_SHORT).show();
                p.dismiss();


            }
        });
    }
}
