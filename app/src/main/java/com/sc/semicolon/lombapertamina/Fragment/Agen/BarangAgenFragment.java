package com.sc.semicolon.lombapertamina.Fragment.Agen;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.semicolon.lombapertamina.Activity.BeliBarangActivity;
import com.sc.semicolon.lombapertamina.Activity.User.DetailTokoActivity;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;
import com.sc.semicolon.lombapertamina.Retrofit.ApiService;
import com.sc.semicolon.lombapertamina.Retrofit.Client;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarangAgenFragment extends Fragment {


    private LinearLayout div;
    ProgressDialog pd;
    Shared shared;
    public BarangAgenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_barang_agen, container, false);
        initView(view);
        pd = new ProgressDialog(getActivity());
        shared = new Shared(getActivity());
        getBarang();
        return view;
    }

    private void getBarang(){
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getBarang().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray == null){
                            Toast.makeText(getActivity(), "data barang kosong", Toast.LENGTH_SHORT).show();
                        }else {
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);
                                final String nama_barang = jsonObject.optString("nama_barang");
                                final String harga_barang = jsonObject.optString("harga_barang");
                                final String stok = jsonObject.optString("stok");
                                final String avatar_barang = jsonObject.optString("avatar_barang");
                                final String id_agen_barang = jsonObject.optString("id_agen");
                                final String id_barang = jsonObject.optString("id_barang");

                                if (id_agen_barang.equals(shared.getSpIdAgen())){
                                    LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View addview = layoutInflater.inflate(R.layout.row_barang, null);

                                    final TextView namaBarang = addview.findViewById(R.id.txt_nama_barang);
                                    final TextView hargaBarang = addview.findViewById(R.id.txt_harga_barang);
                                    final TextView stokBarang = addview.findViewById(R.id.txt_stok_barang);
                                    final ImageView image = addview.findViewById(R.id.img_barang);
                                    final CardView cardView = addview.findViewById(R.id.card);

                                    namaBarang.setText(nama_barang);
                                    hargaBarang.setText("Rp. " +harga_barang);
                                    stokBarang.setText(stok);
                                    Picasso.with(getActivity()).load("http://192.168.43.65/Pertamina/image/" + avatar_barang)
                                            .error(R.drawable.ic_launcher_foreground)
                                            .into(image);

//                                    cardView.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Intent intent = new Intent(getActivity(), BeliBarangActivity.class);
//                                            intent.putExtra("nama", nama_barang);
//                                            intent.putExtra("harga", harga_barang);
//                                            intent.putExtra("stok", stok);
//                                            intent.putExtra("image", avatar_barang);
//                                            intent.putExtra("id_agen", id_agen_barang);
//                                            intent.putExtra("id_barang", id_barang);
//                                            startActivity(intent);
//                                        }
//                                    });

                                    div.addView(addview);
                                }else {
//                                    Toast.makeText(getActivity(), "Agen Belum Memasukkan Barang", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initView(View view) {
        div = (LinearLayout) view.findViewById(R.id.div);
    }
}
