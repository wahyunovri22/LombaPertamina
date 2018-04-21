package com.sc.semicolon.lombapertamina.Activity.User;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.sc.semicolon.lombapertamina.Fragment.Agen.HistoryAgenFragment;
import com.sc.semicolon.lombapertamina.Fragment.LainnyaFragment;
import com.sc.semicolon.lombapertamina.Fragment.User.HomeUserFragment;
import com.sc.semicolon.lombapertamina.Fragment.User.ProfilUserFragment;
import com.sc.semicolon.lombapertamina.R;

public class MainUserActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.kontenku, new HomeUserFragment())
                            .commit();
                    getSupportActionBar().setTitle("Home");
                    break;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
                    android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
                    fragmentManager2.beginTransaction()
                            .replace(R.id.kontenku, new HistoryAgenFragment())
                            .commit();
                    getSupportActionBar().setTitle("History");
                    break;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
                    android.support.v4.app.FragmentManager fragmentManager3 = getSupportFragmentManager();
                    fragmentManager3.beginTransaction()
                            .replace(R.id.kontenku, new LainnyaFragment())
                            .commit();
                    getSupportActionBar().setTitle("Liannya");
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.kontenku, new HomeUserFragment())
                .commit();
        getSupportActionBar().setTitle("Home");
    }

}
