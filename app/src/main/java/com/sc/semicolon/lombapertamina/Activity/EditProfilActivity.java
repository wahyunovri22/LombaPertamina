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

import com.sc.semicolon.lombapertamina.Activity.User.DetailTokoActivity;
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

public class EditProfilActivity extends AppCompatActivity {

    private CircleImageView imgUser;
    private EditText edtNama;
    private EditText edtAlamat;
    private EditText edtTlp;
    private CardView cardEditProfil;
    ProgressDialog pd;
    Shared shared;
    private String imagePath, h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        initView();
        Awal();
        permissionFile();

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooserGalerry();
            }
        });
        cardEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edit();
            }
        });
    }

    private void EditNamaReview(){
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.updateNamaReview(shared.getSpIdUser(),
                edtNama.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String pesan = jsonObject.optString("pesan");
                        Toast.makeText(EditProfilActivity.this, ""+pesan, Toast.LENGTH_SHORT).show();
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
                pd.dismiss();
                Toast.makeText(EditProfilActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Edit(){
        pd.setTitle("Loading ...");
        pd.setCancelable(false);
        pd.show();
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.uppdateProfil(shared.getSpIdUser(),
                edtNama.getText().toString(),
                edtAlamat.getText().toString(),
                edtTlp.getText().toString(),
                h).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String pesan = jsonObject.optString("pesan");

                        shared.saveSPString(shared.SP_NAMA_USER, edtNama.getText().toString());
                        shared.saveSPString(shared.SP_TELEPON_USER, edtTlp.getText().toString());
                        shared.saveSPString(shared.SP_ALAMAT_USER, edtAlamat.getText().toString());
                        shared.saveSPString(shared.SP_FOTO_USER, h);
                        Toast.makeText(EditProfilActivity.this, ""+pesan, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                EditNamaReview();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(EditProfilActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Awal(){
        shared = new Shared(this);
        pd = new ProgressDialog(this);
        h = shared.getSpFotoUser();
        edtNama.setText(shared.getSpNamaUser());
        edtAlamat.setText(shared.getSpAlamatUser());
        edtTlp.setText(shared.getSpTeleponUser());
        Picasso.with(getApplicationContext())
                .load(Config.MY_AVATAR + shared.getSpFotoUser())
                .error(R.drawable.ic_launcher_foreground)
                .into(imgUser);
    }
    private void initView() {
        imgUser = (CircleImageView) findViewById(R.id.img_user);
        edtNama = (EditText) findViewById(R.id.edt_nama);
        edtAlamat = (EditText) findViewById(R.id.edt_alamat);
        edtTlp = (EditText) findViewById(R.id.edt_tlp);
        cardEditProfil = (CardView) findViewById(R.id.card_edit_profil);
    }

    //gallery
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

        Toast.makeText(EditProfilActivity.this, "Gambar " + h, Toast.LENGTH_SHORT).show();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);

        final MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file", f.getName(), requestFile);
        Call<Result> resultCAll = s.postIMmage(part);
        resultCAll.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                p.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(EditProfilActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProfilActivity.this, "Upload Gambar Gagal", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EditProfilActivity.this, "Upload Gambar Gagal", Toast.LENGTH_SHORT).show();
                }

                imagePath = "";

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(EditProfilActivity.this, "Gagal Upload Fail", Toast.LENGTH_SHORT).show();
                p.dismiss();


            }
        });
    }
    private void permissionFile() {
        if (ContextCompat.checkSelfPermission(EditProfilActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfilActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(EditProfilActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Config.MY_PERMISSIONS_REQUEST_FILE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
