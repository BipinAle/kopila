package com.mobile.kopila.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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

public class DaunghaKoChinari extends AppCompatActivity {
    private ImageView back;
    private TextView title, update, daunghaKoChinariText;
    ApiService apiService;
    private ProgressDialog progressDialog;
    private File file;
    private Utils utils;
    private String daunghaKoChinariPref;
    private String daunghaKoChinari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daungha_ko_chinari);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        back.setOnClickListener(v -> DaunghaKoChinari.this.finish());
        daunghaKoChinariText = findViewById(R.id.daungha_ko_chinari_text);
        update = findViewById(R.id.update);
        update.setVisibility(View.VISIBLE);
        apiService = new RetrofitApiClient(this).getAdapter().create(ApiService.class);
        file = new File(getExternalFilesDir(null) + File.separator + "DaunghaKoChinari.txt");
        progressDialog = new ProgressDialog(this);
        utils = new Utils(this);
        title.setText(getString(R.string.daungha_ko_chinari));
        apiCall();

    }


    private void apiCall() {
        if (utils.isNetworkConnected()) {
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.please_wait));
            getApiData(file, Constants.DAUNGHA_KO_CHINARI_GID);
        } else {
            daunghaKoChinariPref = getPrefData();
            if (daunghaKoChinariPref != null && !daunghaKoChinariPref.isEmpty()) {
                Toast.makeText(DaunghaKoChinari.this, "success retrieve", Toast.LENGTH_SHORT).show();
                daunghaKoChinari = daunghaKoChinariPref;
            } else {
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

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
                                        daunghaKoChinari = colums[0];
                                    }
                                    saveData(daunghaKoChinari);//save data in pref
                                    daunghaKoChinariText.setText(daunghaKoChinari);


                                } catch (FileNotFoundException e) {
                                    getApiData(file, Constants.DAUNGHA_KO_CHINARI_GID);
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
                        daunghaKoChinariPref = getPrefData();
                        if (daunghaKoChinariPref != null && !daunghaKoChinariPref.isEmpty()) {
                            Toast.makeText(DaunghaKoChinari.this, "success retrieve", Toast.LENGTH_SHORT).show();
                            daunghaKoChinari = daunghaKoChinariPref;
                        } else {
                            Toast.makeText(DaunghaKoChinari.this, getString(R.string.sth_went_wrong), Toast.LENGTH_SHORT).show();

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
        editor.putString(Constants.DAUNGHA_KO_CHINARI, hamroBarema);
        editor.apply();
    }

    private String getPrefData() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        return prefs.getString(Constants.DAUNGHA_KO_CHINARI, null);


    }
}
