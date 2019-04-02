package com.mobile.kopila.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.kopila.R;
import com.mobile.kopila.adapter.PersonListAdapter;
import com.mobile.kopila.pojo.PhoneDiary;

import org.parceler.Parcels;

import java.util.ArrayList;

public class PersonListActivity extends AppCompatActivity implements PersonListAdapter.OnMessageCallListner {
    private PersonListAdapter adapter;
    private RecyclerView personListRv;
    private String phnum;
    private ImageView back;
    private TextView title;
    ArrayList<PhoneDiary> phoneDiaries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);
        personListRv = findViewById(R.id.person_list_rv);
        phoneDiaries = new ArrayList<>();
        adapter = new PersonListAdapter(this);
        adapter.setOnMessageCallListner(this);
        personListRv.setLayoutManager(new LinearLayoutManager(this));
        personListRv.setAdapter(adapter);

        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        back.setOnClickListener(v -> PersonListActivity.this.finish());
        if (getIntent() != null) {
            phoneDiaries = Parcels.unwrap(getIntent().getParcelableExtra("phoneDiaries"));
            adapter.update(phoneDiaries);

        }
        title.setText(getString(R.string.phone_diary));
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message), Toast.LENGTH_SHORT).show();
                    call_action(String.valueOf(phnum));
                } else if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.message), Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


    public void sendSMS(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "");
        startActivity(intent);

    }


    public void call_action(String phnum) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + String.valueOf(phnum)));
        startActivity(callIntent);
    }

    @Override
    public void onCallClick(String num) {
        this.phnum = num;
        if (isPermissionGranted()) {
            call_action(this.phnum);
        }
    }

    @Override
    public void onMessageClick(String num) {
        if (isPermissionGranted()) {
            sendSMS(String.valueOf(num));
        }

    }
}
