package com.mobile.kopila.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.kopila.R;

public class SahyogSamparkaAtivity extends AppCompatActivity {
    private ImageView back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sahyog_samparka_ativity);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SahyogSamparkaAtivity.this.finish();
            }
        });

        title.setText(getString(R.string.sahayog_samparka));
    }
}
