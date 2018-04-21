package com.sc.semicolon.lombapertamina.Fragment.Agen;


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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sc.semicolon.lombapertamina.Activity.Agen.EditTokoActivity;
import com.sc.semicolon.lombapertamina.Activity.User.MainUserActivity;
import com.sc.semicolon.lombapertamina.Helper.Config;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;
import com.sc.semicolon.lombapertamina.Retrofit.ApiService;
import com.sc.semicolon.lombapertamina.Retrofit.Client;
import com.sc.semicolon.lombapertamina.Retrofit.Result;
import com.sc.semicolon.lombapertamina.Retrofit.RetroClient;
import com.sc.semicolon.lombapertamina.Retrofit.RetroClientDua;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class InputBarangFragment extends Fragment {


    private CircleImageView imgUser;
    private EditText edtNama;
    private EditText edtStok;
    private EditText edtHarga;
    private CardView cardInputBarang;
    ProgressDialog pd;
    Shared shared;
    private String imagePath, h;

    public InputBarangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input_barang, container, false);
        initView(view);
        Awal();
        permissionFile();

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooserGalerry();
            }
        });
        cardInputBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputBarang();
            }
        });
        return view;
    }

    private void inputBarang(){
        pd.setTitle("Loadig ...");
        pd.setCancelable(false);
        pd.show();
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.inputBarang(shared.getSpIdAgen(),
                edtNama.getText().toString(),
                Integer.parseInt(edtStok.getText().toString()),
                Integer.parseInt(edtHarga.getText().toString()),h).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String pesan = jsonObject.optString("pesan");
                        Toast.makeText(getActivity(), ""+pesan, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainUserActivity.class);
                        startActivity(intent);
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
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Awal(){
        shared = new Shared(getActivity());
        pd = new ProgressDialog(getActivity());
    }

    private void initView(View view) {
        imgUser = (CircleImageView) view.findViewById(R.id.img_user);
        edtNama = (EditText) view.findViewById(R.id.edt_nama);
        edtStok = (EditText) view.findViewById(R.id.edt_stok);
        edtHarga = (EditText) view.findViewById(R.id.edt_harga);
        cardInputBarang = (CardView) view.findViewById(R.id.card_input_barang);
    }

    private void permissionFile() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
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
                Toast.makeText(getActivity(), "Gambar Tidak Ada", Toast.LENGTH_SHORT).show();
                return;

            }

            Uri selectImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor c = getActivity().getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
            if (c != null) {
                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePathColumn[0]);
                imagePath = c.getString(columnIndex);


                Picasso.with(getActivity()).load(new File(imagePath)).into(imgUser);
                h = new File(imagePath).getName();
                uploadImage();

                c.close();


            } else {

                Toast.makeText(getActivity(), "Gambar Tidak Ada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage() {

        final ProgressDialog p;
        p = new ProgressDialog(getActivity());
        p.setMessage("Proses Upload Foto");
        p.show();

        ApiService s = RetroClientDua.getService();


        File f = new File(imagePath);

        Toast.makeText(getActivity(), "Gambar " + h, Toast.LENGTH_SHORT).show();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);

        final MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file", f.getName(), requestFile);
        Call<Result> resultCAll = s.postIMmage(part);
        resultCAll.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                p.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(getActivity(), "Sukses", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Upload Gambar Gagal", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Upload Gambar Gagal", Toast.LENGTH_SHORT).show();
                }

                imagePath = "";

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal Upload Fail", Toast.LENGTH_SHORT).show();
                p.dismiss();


            }
        });
    }
}
