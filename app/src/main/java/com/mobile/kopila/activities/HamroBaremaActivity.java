package com.mobile.kopila.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.kopila.R;
import com.mobile.kopila.adapter.CommonAdapter;
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
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HamroBaremaActivity extends AppCompatActivity {
    private ImageView back;
    private TextView title, update;
    private RecyclerView hamroBaremaRv;
    ApiService apiService;
    private ProgressDialog progressDialog;
    private File file;
    private Gson gson;
    private Utils utils;
    private ArrayList<String> hamroBaremaList;
    private ArrayList<String> harmroBaremaPref;
    private CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamro_barema);
        hamroBaremaRv = findViewById(R.id.hamroBaremRv);
        hamroBaremaList = new ArrayList<>();
        gson = new Gson();
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        adapter = new CommonAdapter();
        hamroBaremaRv.setLayoutManager(new LinearLayoutManager(this));
        hamroBaremaRv.setAdapter(adapter);
        back.setOnClickListener(v -> HamroBaremaActivity.this.finish());
        title.setText(getString(R.string.hamro_barema));
        update = findViewById(R.id.update);
        update.setVisibility(View.VISIBLE);
        apiService = new RetrofitApiClient(this).getAdapter().create(ApiService.class);
        file = new File(getExternalFilesDir(null) + File.separator + "hamro_barema.txt");
        progressDialog = new ProgressDialog(this);
        utils = new Utils(this);
        update.setOnClickListener(v -> {
            apiCall();
        });
        apiCall();
    }

    private void apiCall() {
        if (utils.isNetworkConnected()) {
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.please_wait));
            getApiData(file, Constants.HAMROBAREMA_GID);
        } else {
            harmroBaremaPref = getPrefData();
            if (harmroBaremaPref != null && !harmroBaremaPref.isEmpty()) {
                hamroBaremaList.clear();
                hamroBaremaList = harmroBaremaPref;
                adapter.update(hamroBaremaList);
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
                            hamroBaremaList.clear();
                            assert response.body() != null;
                            boolean writtenSuccessfully = writeResponseBodyToDisk(response.body());
                            if (writtenSuccessfully) {
                                try {
                                    BufferedReader br = new BufferedReader(new FileReader(file));
                                    String line;
                                    while ((line = br.readLine()) != null) {
                                        String[] colums = line.split(",");
                                        hamroBaremaList.add(colums[0]);
                                    }
                                    saveData(hamroBaremaList);//save data in pref
                                    adapter.update(hamroBaremaList);

                                } catch (FileNotFoundException e) {
                                    getApiData(file, Constants.HAMROBAREMA_GID);
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
                        harmroBaremaPref.clear();
                        harmroBaremaPref = getPrefData();
                        if (!harmroBaremaPref.isEmpty()) {
                            adapter.update(harmroBaremaPref);
                        } else {
                            Toast.makeText(HamroBaremaActivity.this, getString(R.string.sth_went_wrong), Toast.LENGTH_SHORT).show();

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

    private void saveData(ArrayList<String> hamroBarema) {
        String json = gson.toJson(hamroBarema);
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE).edit();
        editor.putString(Constants.HAMRO_BAREMA, json);
        editor.apply();

    }

    private ArrayList<String> getPrefData() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        String mainDataString = prefs.getString(Constants.HAMRO_BAREMA, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(mainDataString, type);
    }
}
