package com.mobile.kopila.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.kopila.R;
import com.mobile.kopila.network.ApiService;
import com.mobile.kopila.network.RetrofitApiClient;

public class DashboardActivity extends AppCompatActivity {

    private CardView one, two, three, four, five, six, seven, eight;

    private TextView title;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        ImageView back = findViewById(R.id.back);
        back.setVisibility(View.INVISIBLE);
        title = findViewById(R.id.title);
        apiService = new RetrofitApiClient(this).getAdapter().create(ApiService.class);

        intClick();

    }

    private void intClick() {
        title.setText(getString(R.string.homepage));
        one.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, HamroBaremaActivity.class);
            startActivity(intent);

        });
        two.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, DaunghaKoChinari.class);
            startActivity(intent);
        });
        three.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PhoneDiaryActivity.class);
            startActivity(intent);
        });
        four.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, JankariSuchanaActivity.class);
            startActivity(intent);
        });
        five.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PhotoGalleryActivity.class);
            startActivity(intent);
        });
        six.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SahyogSamparkaActivity.class);
            startActivity(intent);
        });
        seven.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MapActivity.class);
            startActivity(intent);
        });
        eight.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LekhharuActivity.class);
            startActivity(intent);
        });


    }
}
