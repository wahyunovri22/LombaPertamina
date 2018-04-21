package com.sc.semicolon.lombapertamina.Fragment.User;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sc.semicolon.lombapertamina.Activity.User.BokingUserActivity;
import com.sc.semicolon.lombapertamina.Activity.User.DetailTokoActivity;
import com.sc.semicolon.lombapertamina.Activity.User.HistoryUserActivity;
import com.sc.semicolon.lombapertamina.Activity.User.PesananUserActivity;
import com.sc.semicolon.lombapertamina.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilUserFragment extends Fragment {


    private CardView pesanan;
    private CardView boking;
    private CardView history;

    public ProfilUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil_user, container, false);
        initView(view);

        pesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PesananUserActivity.class);
                startActivity(intent);
            }
        });
        boking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BokingUserActivity.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryUserActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view) {
        pesanan = (CardView) view.findViewById(R.id.pesanan);
        boking = (CardView) view.findViewById(R.id.boking);
        history = (CardView) view.findViewById(R.id.history);
    }
}
