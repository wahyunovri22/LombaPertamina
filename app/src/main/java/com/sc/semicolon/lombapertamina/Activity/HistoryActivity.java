package com.sc.semicolon.lombapertamina.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sc.semicolon.lombapertamina.Helper.Config;
import com.sc.semicolon.lombapertamina.R;

public class HistoryActivity extends AppCompatActivity {

    private EditText edtText;
    private EditText edtNo;
    private Button btnKirimLangsung;
    private Button btnKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();

        permissionFile();
    }

    private void initView() {
        edtText = (EditText) findViewById(R.id.edt_text);
        edtNo = (EditText) findViewById(R.id.edt_no);
        btnKirimLangsung = (Button) findViewById(R.id.btn_kirim_langsung);
        btnKirim = (Button) findViewById(R.id.btn_kirim);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(edtNo.getText().toString(), null,
                            edtText.getText().toString(), null, null);
                    Toast.makeText(getApplicationContext(), "sms berhasil", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "sms gagal dikirim cek pulsa atau perizinan aplikasi anda", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        btnKirimLangsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("0895349549759", null,
                            "Selamat UlangTahun", null, null);
                    Toast.makeText(getApplicationContext(), "sms berhasil", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "sms gagal dikirim cek pulsa atau perizinan aplikasi anda", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void permissionFile() {
        if (ContextCompat.checkSelfPermission(HistoryActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(HistoryActivity.this,
                    Manifest.permission.SEND_SMS)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(HistoryActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
