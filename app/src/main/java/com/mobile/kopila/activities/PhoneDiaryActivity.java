package com.mobile.kopila.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.kopila.R;
import com.mobile.kopila.adapter.ToleAdapter;

public class PhoneDiaryActivity extends AppCompatActivity {
    private ImageView back;
    private TextView title;
    private RecyclerView toleRv;
    private ToleAdapter adapter;
    private String[] toles = {"गौरिखुट्टा ", "दौघी ", "चुदरीथुम ", "नौधारा ", "मोहरे ", "जहेरिपोखरी"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_diary);
        toleRv = findViewById(R.id.tole_rv);

        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        back.setOnClickListener(v -> PhoneDiaryActivity.this.finish());

        title.setText(getString(R.string.phone_diary));

        adapter = new ToleAdapter(toles);
        toleRv.setLayoutManager(new GridLayoutManager(this, 2));
        toleRv.setAdapter(adapter);


    }


}
