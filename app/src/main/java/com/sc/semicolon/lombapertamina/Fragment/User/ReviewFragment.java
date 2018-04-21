package com.sc.semicolon.lombapertamina.Fragment.User;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.semicolon.lombapertamina.Activity.LoginActivity;
import com.sc.semicolon.lombapertamina.Activity.User.DetailTokoActivity;
import com.sc.semicolon.lombapertamina.Activity.User.MainUserActivity;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;
import com.sc.semicolon.lombapertamina.Retrofit.ApiService;
import com.sc.semicolon.lombapertamina.Retrofit.Client;

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
public class ReviewFragment extends Fragment {


    private LinearLayout div;
    private EditText edtReview;
    private Button btnReview;
    ProgressDialog pd;
    Shared shared;

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        initView(view);
        pd = new ProgressDialog(getActivity());
        shared = new Shared(getActivity());

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shared.getSPSudahLogin()) {
                    insertReview();
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
        getReview();
        return view;
    }

    private void getReview() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getReview().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray == null) {
                            Toast.makeText(getActivity(), "data barang kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);
                                final String id_review = jsonObject.optString("id_review");
                                final String id_user = jsonObject.optString("id_user");
                                final String nama = jsonObject.optString("nama");
                                final String review = jsonObject.optString("review");
                                final String id_agen_barang = jsonObject.optString("id_agen");

                                if (id_agen_barang.equals(shared.getSpBundleIdAgen())) {
                                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View addview = layoutInflater.inflate(R.layout.row_review, null);

                                    final TextView namaReview = addview.findViewById(R.id.txt_nama_review);
                                    final TextView txtReview = addview.findViewById(R.id.txt_review);

                                    txtReview.setText(review);
                                    namaReview.setText(nama);

                                    div.addView(addview);
                                } else {
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
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void insertReview(){
        pd.setTitle("Insert ...");
        pd.setCancelable(false);
        pd.show();
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.insertReview(shared.getSpBundleIdAgen(),
                shared.getSpIdUser(),
                shared.getSpNamaUser(),
                edtReview.getText().toString()).enqueue(new Callback<ResponseBody>() {
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
                }else {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
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
        edtReview = (EditText) view.findViewById(R.id.edt_review);
        btnReview = (Button) view.findViewById(R.id.btn_review);
    }
}
