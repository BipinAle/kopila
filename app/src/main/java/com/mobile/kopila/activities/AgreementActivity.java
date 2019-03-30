package com.mobile.kopila.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mobile.kopila.R;

public class AgreementActivity extends AppCompatActivity {
    private CheckBox checkBox;
    private TextView goAheadText;
    private boolean isFirstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreement_activity);
        checkBox = findViewById(R.id.checkbox);
        goAheadText = findViewById(R.id.go_ahead_text);
        if (!isFirstRun) {
            startDashboard();
        }


        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                goAheadText.setEnabled(true);
                goAheadText.setClickable(true);
                goAheadText.setTextColor(getResources().getColor(R.color.black));

                goAheadText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDashboard();
                        isFirstRun = false;
                    }
                });
            } else {
                goAheadText.setEnabled(false);
                goAheadText.setClickable(false);
                goAheadText.setTextColor(getResources().getColor(R.color.disable));
            }
        });
    }

    private void startDashboard() {
        Intent intent = new Intent(AgreementActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
