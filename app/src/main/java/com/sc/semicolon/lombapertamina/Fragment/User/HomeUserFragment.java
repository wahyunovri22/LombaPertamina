package com.sc.semicolon.lombapertamina.Fragment.User;


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

import com.sc.semicolon.lombapertamina.Activity.Agen.TokoActivity;
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
public class HomeUserFragment extends Fragment {


    private LinearLayout div;
    ProgressDialog pd;
    Shared shared;

    public HomeUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);
        initView(view);
        pd = new ProgressDialog(getActivity());
        shared = new Shared(getActivity());
        getAgen();
        return view;
    }

    private void getAgen(){
        pd.setTitle("Loading ...");
        pd.setCancelable(false);
        pd.show();
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getAgen().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray == null){
                            Toast.makeText(getActivity(), "data kosong", Toast.LENGTH_SHORT).show();
                        }else{
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);
                                final String nama_agen = jsonObject.optString("nama_agen");
                                final String id_agen = jsonObject.optString("id_agen");
                                String nomor_agen = jsonObject.optString("nomor_agen");
                                String slogan = jsonObject.optString("slogan_agen");
                                final String alamat_agen = jsonObject.optString("alamat_agen");
                                final String image = jsonObject.optString("avatar_agen");
                                String status = jsonObject.optString("status");
                                final String telepon = jsonObject.optString("telepon_agen");
                                final String latitude = jsonObject.optString("latitude");
                                final String longitude = jsonObject.optString("longitude");

                                if (status.equals("aktif")){
                                    LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View addview = layoutInflater.inflate(R.layout.row_home,null);
                                    final TextView namaAgen = addview.findViewById(R.id.txt_nama_agen);
                                    final TextView nomorAgen = addview.findViewById(R.id.txt_nomor_agen);
                                    final TextView sloganAgen = addview.findViewById(R.id.txt_slogan_agen);
                                    final TextView alamatAgen = addview.findViewById(R.id.txt_alamat_agen);
                                    final CardView cardView = addview.findViewById(R.id.card);
                                    ImageView imgAgen = addview.findViewById(R.id.img_agen);

                                    namaAgen.setText(nama_agen);
                                    nomorAgen.setText(nomor_agen);
                                    sloganAgen.setText(slogan);
                                    alamatAgen.setText(alamat_agen);
                                    Picasso.with(getActivity()).load("http://192.168.43.65/Pertamina/avatar/" + image)
                                            .error(R.drawable.ic_launcher_foreground).into(imgAgen);

                                    cardView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (id_agen.equals(shared.getSpIdAgen())){
                                                Intent intent2 = new Intent(getActivity(), TokoActivity.class);
                                                startActivity(intent2);
                                            }else {
                                                shared.saveSPString(shared.SP_BUNDLE_ID_AGEN, id_agen);
                                                Intent intent = new Intent(getActivity(), DetailTokoActivity.class);
                                                intent.putExtra("id_agen", id_agen);
                                                intent.putExtra("nama",nama_agen);
                                                intent.putExtra("telepon",telepon);
                                                intent.putExtra("alamat",alamat_agen);
                                                intent.putExtra("latitude", latitude);
                                                intent.putExtra("longitude", longitude);
                                                intent.putExtra("image", image);
                                                startActivity(intent);
                                            }

                                        }
                                    });

                                    div.addView(addview);
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
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {

        div = (LinearLayout) view.findViewById(R.id.div);
    }
}
