package com.sc.semicolon.lombapertamina.Fragment.Agen;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.semicolon.lombapertamina.Activity.Agen.BokingAgenActivity;
import com.sc.semicolon.lombapertamina.Activity.Agen.HistoryAgenActivity;
import com.sc.semicolon.lombapertamina.Activity.Agen.PesananAgenActivity;
import com.sc.semicolon.lombapertamina.Helper.Shared;
import com.sc.semicolon.lombapertamina.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryAgenFragment extends Fragment {


    private TextView txt;
    private LinearLayout layout;
    private CardView cardBoking;
    private CardView cardPesan;
    private CardView cardHistory;
    Shared shared;

    public HistoryAgenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_agen, container, false);
        initView(view);
        shared = new Shared(getActivity());
        Awal();

        cardBoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BokingAgenActivity.class);
                startActivity(intent);
            }
        });

        cardPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PesananAgenActivity.class);
                startActivity(intent);
            }
        });

        cardHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryAgenActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void Awal(){
        if (shared.getSpStatus().equals("aktif")){
            layout.setVisibility(View.VISIBLE);
            txt.setVisibility(View.GONE);
        }else {
            layout.setVisibility(View.GONE);
            txt.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View view) {
        txt = (TextView) view.findViewById(R.id.txt);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        cardBoking = (CardView) view.findViewById(R.id.card_boking);
        cardPesan = (CardView) view.findViewById(R.id.card_pesan);
        cardHistory = (CardView) view.findViewById(R.id.card_history);
    }
}
