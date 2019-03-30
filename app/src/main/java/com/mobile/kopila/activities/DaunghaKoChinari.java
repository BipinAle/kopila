package com.mobile.kopila.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.kopila.R;

public class DaunghaKoChinari extends AppCompatActivity {
    private ImageView back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daungha_ko_chinari);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaunghaKoChinari.this.finish();
            }
        });

        title.setText(getString(R.string.daungha_ko_chinari));
    }
}
