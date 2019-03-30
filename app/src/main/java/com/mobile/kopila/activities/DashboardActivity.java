package com.mobile.kopila.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.kopila.R;
import com.mobile.kopila.network.ApiService;
import com.mobile.kopila.network.RetrofitApiClient;
import com.mobile.kopila.pojo.ToleData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private CardView one, two, three, four, five, six, seven;
    private ImageView back;
    private TextView title, update;
    ApiService apiService;
    private File file;
    private ArrayList<ToleData> toleData;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    private Gson gson;
    private ArrayList<ToleData> toleDataPref;

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
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        update = findViewById(R.id.update);
        update.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        apiService = new RetrofitApiClient(this).getAdapter().create(ApiService.class);
        file = new File(getExternalFilesDir(null) + File.separator + "file.txt");
        toleData = new ArrayList<>();
        toleDataPref = new ArrayList<>();
        imageView = findViewById(R.id.image_view);
        progressDialog = new ProgressDialog(this);
        gson = new Gson();
        intClick();
        apiCall();
    }

    private void apiCall() {
        if (isNetworkConnected()) {
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.please_wait));
            getApiData(file);
        } else {
            toleDataPref = getPrefData();
            if (toleDataPref != null && toleDataPref.size() > 0) {
                Toast.makeText(DashboardActivity.this, "success retrieve", Toast.LENGTH_SHORT).show();
                toleData = toleDataPref;
            } else {
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

            }

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void setImage(String id) {
        Glide.with(DashboardActivity.this).load("https://docs.google.com/uc?id=" + id)
                .centerCrop()
                .into(imageView);
    }

    private void getApiData(File file) {
        apiService.downloadFileWithDynamicUrl("spreadsheets/d/11-3J8T8ft8urFsU932-hGKm7PpyHfIBEQPzLbR4ICYk/export?gid=1488719481&format=csv")
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
                                        toleData.add(new ToleData(colums[0], colums[1], colums[2], colums[3], colums[4]));
                                    }
                                    saveData(toleData);//save data in pref

                                } catch (FileNotFoundException e) {
                                    getApiData(file);
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
                        toleDataPref = getPrefData();
                        if (toleDataPref != null && toleDataPref.size() > 0) {
                            Toast.makeText(DashboardActivity.this, "success retrieve", Toast.LENGTH_SHORT).show();
                            toleData = toleDataPref;
                        } else {
                            Toast.makeText(DashboardActivity.this, getString(R.string.sth_went_wrong), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void saveData(ArrayList<ToleData> toleData) {
        String json = gson.toJson(toleData);
        SharedPreferences.Editor editor = getSharedPreferences("Daungha", MODE_PRIVATE).edit();
        editor.putString("main_data", json);
        editor.apply();
    }

    private ArrayList<ToleData> getPrefData() {
        SharedPreferences prefs = getSharedPreferences("Daungha", MODE_PRIVATE);
        String mainDataString = prefs.getString("main_data", null);
        Type type = new TypeToken<ArrayList<ToleData>>() {
        }.getType();
        return gson.fromJson(mainDataString, type);

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
            Intent intent = new Intent(DashboardActivity.this, SahyogSamparkaAtivity.class);
            startActivity(intent);
        });
        seven.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MapActivity.class);
            startActivity(intent);
        });

        update.setOnClickListener(v -> {
            apiCall();
        });


    }
}
