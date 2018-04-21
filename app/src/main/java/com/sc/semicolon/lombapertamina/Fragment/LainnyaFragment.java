package com.sc.semicolon.lombapertamina.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sc.semicolon.lombapertamina.Activity.Agen.TokoActivity;
import com.sc.semicolon.lombapertamina.Activity.BuatTokoActivity;
import com.sc.semicolon.lombapertamina.Activity.LoginActivity;
import com.sc.semicolon.lombapertamina.Activity.ProfilActivity;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LainnyaFragment extends Fragment {


    private CardView cardProfil;
    private CardView cardToko;
    private CardView cardLogin;
    private CardView cardBuatToko;
    Shared shared;
    Handler handler = new Handler();

    public LainnyaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lainnya, container, false);
        initView(view);
        shared = new Shared(getActivity());
        this.handler = new Handler();
        this.handler.postDelayed(runnable, 2000);
        Awal();
        
        cardLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        cardProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfilActivity.class);
                startActivity(intent);
            }
        });

        cardToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TokoActivity.class);
                startActivity(intent);
            }
        });
        cardBuatToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BuatTokoActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view) {
        cardProfil = (CardView) view.findViewById(R.id.card_profil);
        cardToko = (CardView) view.findViewById(R.id.card_toko);
        cardLogin = (CardView) view.findViewById(R.id.card_login);
        cardBuatToko = (CardView) view.findViewById(R.id.card_buat_toko);
    }
    private void Awal(){
        if (shared.getSPSudahLogin() && shared.getSpStatus().equals("aktif")) {
            cardProfil.setVisibility(View.VISIBLE);
            cardLogin.setVisibility(View.GONE);
            cardToko.setVisibility(View.VISIBLE);
            cardBuatToko.setVisibility(View.GONE);
        } else if (shared.getSPSudahLogin() && shared.getSpStatus().equals("belum")){
            cardProfil.setVisibility(View.VISIBLE);
            cardLogin.setVisibility(View.GONE);
            cardToko.setVisibility(View.GONE);
            cardBuatToko.setVisibility(View.VISIBLE);
        }else {
            cardProfil.setVisibility(View.GONE);
            cardLogin.setVisibility(View.VISIBLE);
            cardToko.setVisibility(View.GONE);
            cardBuatToko.setVisibility(View.GONE);
        }
    }

    public final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Awal();
            LainnyaFragment.this.handler.postDelayed(runnable, 2000);
        }
    };
}
