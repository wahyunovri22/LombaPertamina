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
public class ReviewAgenFragment extends Fragment {


    private LinearLayout div;
    ProgressDialog pd;
    Shared shared;
    public ReviewAgenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_agen, container, false);
        initView(view);
        pd = new ProgressDialog(getActivity());
        shared = new Shared(getActivity());
        getReview();
        return view;
    }
    private void getReview(){
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getReview().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray == null){
                            Toast.makeText(getActivity(), "data barang kosong", Toast.LENGTH_SHORT).show();
                        }else {
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);
                                final String id_review = jsonObject.optString("id_review");
                                final String id_user = jsonObject.optString("id_user");
                                final String nama = jsonObject.optString("nama");
                                final String review = jsonObject.optString("review");
                                final String id_agen_barang = jsonObject.optString("id_agen");

                                if (id_agen_barang.equals(shared.getSpIdAgen())){
                                    LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View addview = layoutInflater.inflate(R.layout.row_review, null);

                                    final TextView namaReview = addview.findViewById(R.id.txt_nama_review);
                                    final TextView txtReview= addview.findViewById(R.id.txt_review);

                                    txtReview.setText(review);
                                    namaReview.setText(nama);

                                    div.addView(addview);
                                }else {
//                                    Toast.makeText(DetailTokoActivity.this, "Agen Belum Memasukkan Barang", Toast.LENGTH_SHORT).show();
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
