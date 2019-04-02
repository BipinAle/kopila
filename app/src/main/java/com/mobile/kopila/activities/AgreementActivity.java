package com.mobile.kopila.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.kopila.R;
import com.mobile.kopila.network.ApiService;
import com.mobile.kopila.network.RetrofitApiClient;
import com.mobile.kopila.utils.Constants;
import com.mobile.kopila.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgreementActivity extends AppCompatActivity {
    private CheckBox checkBox;
    private TextView acceptanceText;
    private boolean isFirstRun = true;
    ApiService apiService;
    private ProgressDialog progressDialog;
    private File file;
    private Utils utils;
    private String acceptanceStringPref;
    private String acceptanceString;
    private Button goAheadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreement_activity);
        checkBox = findViewById(R.id.checkbox);
        goAheadText = findViewById(R.id.go_ahead_text);
        acceptanceText = findViewById(R.id.acceptance_text);
        apiService = new RetrofitApiClient(this).getAdapter().create(ApiService.class);
        file = new File(getExternalFilesDir(null) + File.separator + "agreement.txt");
        progressDialog = new ProgressDialog(this);
        utils = new Utils(this);
        apiCall();
        if (!isFirstRun) {
            startDashboard();
        }


        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                goAheadText.setEnabled(true);
                goAheadText.setClickable(true);
                goAheadText.setTextColor(getResources().getColor(R.color.white));

                goAheadText.setOnClickListener(v -> {
                    startDashboard();
                    isFirstRun = false;
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

    private void apiCall() {
        if (utils.isNetworkConnected()) {
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.please_wait));
            getApiData(file, Constants.AGREEMENT_GID);
        } else {
            acceptanceStringPref = getPrefData();
            if (acceptanceStringPref != null && !acceptanceStringPref.isEmpty()) {
                acceptanceString = acceptanceStringPref;
                acceptanceText.setText(acceptanceString);
            } else {
                acceptanceText.setText(getString(R.string.acceptance_text));
            }

        }
    }


    private void getApiData(File file, String id) {
        apiService.downloadFileWithDynamicUrl("spreadsheets/d/11-3J8T8ft8urFsU932-hGKm7PpyHfIBEQPzLbR4ICYk/export?gid=" + id + "&format=csv")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressDialog.hide();
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            boolean writtenSuccessfully = writeResponseBodyToDisk(response.body());
                            if (writtenSuccessfully) {
                                try {
                                    BufferedReader br = new BufferedReader(new FileReader(file));
                                    String line;
                                    while ((line = br.readLine()) != null) {
                                        String[] colums = line.split(",");
                                        acceptanceString = colums[0];
                                    }
                                    saveData(acceptanceString);//save data in pref
                                    acceptanceText.setText(acceptanceString != null && !TextUtils.isEmpty(acceptanceString) ? acceptanceString : getString(R.string.acceptance_text));

                                } catch (FileNotFoundException e) {
                                    getApiData(file, Constants.AGREEMENT_GID);
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progressDialog.hide();
                        acceptanceStringPref = getPrefData();
                        if (acceptanceStringPref != null && !acceptanceStringPref.isEmpty()) {
                            Toast.makeText(AgreementActivity.this, "success retrieve", Toast.LENGTH_SHORT).show();
                            acceptanceString = acceptanceStringPref;
                        } else {
                            Toast.makeText(AgreementActivity.this, getString(R.string.sth_went_wrong), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void saveData(String hamroBarema) {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE).edit();
        editor.putString(Constants.AGREEMENT, hamroBarema);
        editor.apply();
    }

    private String getPrefData() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        return prefs.getString(Constants.AGREEMENT, null);


    }
}
