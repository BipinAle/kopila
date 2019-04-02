package com.mobile.kopila.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.kopila.R;
import com.mobile.kopila.adapter.PhoneDiaryAdapter;
import com.mobile.kopila.network.ApiService;
import com.mobile.kopila.network.RetrofitApiClient;
import com.mobile.kopila.pojo.PhoneDiary;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneDiaryActivity extends AppCompatActivity {
    private ImageView back;
    private RecyclerView toleRv;
    private PhoneDiaryAdapter adapter;
    private TextView title, update;
    ApiService apiService;
    private ArrayList<PhoneDiary> phoneDiaries, phoneDiaryPref;

    private ProgressDialog progressDialog;
    private Gson gson;
    private File file;
    private Utils utils;
    HashMap<String, List<PhoneDiary>> phoneDiariesHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_diary);
        toleRv = findViewById(R.id.tole_rv);

        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        back.setOnClickListener(v -> PhoneDiaryActivity.this.finish());

        title.setText(getString(R.string.phone_diary));
        update = findViewById(R.id.update);
        update.setVisibility(View.VISIBLE);
        apiService = new RetrofitApiClient(this).getAdapter().create(ApiService.class);
        file = new File(getExternalFilesDir(null) + File.separator + "phone_diary.txt");
        progressDialog = new ProgressDialog(this);
        gson = new Gson();
        utils = new Utils(this);
        adapter = new PhoneDiaryAdapter();
        toleRv.setLayoutManager(new GridLayoutManager(this, 2));
        toleRv.setAdapter(adapter);
        phoneDiaries = new ArrayList<>();
        phoneDiaryPref = new ArrayList<>();
        apiCall();


    }

    private void apiCall() {
        if (utils.isNetworkConnected()) {
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.please_wait));
            getApiData(file, Constants.PHONE_DIARY_GID);
        } else {
            phoneDiaryPref = getPrefData();
            if (phoneDiaryPref != null && phoneDiaryPref.size() > 0) {
                Toast.makeText(PhoneDiaryActivity.this, "success retrieve", Toast.LENGTH_SHORT).show();
                phoneDiaries = phoneDiaryPref;
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
                                        phoneDiaries.add(new PhoneDiary(colums[0], colums[1], colums[2], colums[3], colums[4]));
                                    }
                                    saveData(phoneDiaries);//save data in pref
                                    HashMap<String, List<PhoneDiary>> groupByNaeHashmap = groupByName(phoneDiaries);
                                    if (phoneDiaries.size() > 0) {
                                        //Getting keys
                                        Set<String> keyProductSet = groupByNaeHashmap.keySet();
                                        //Creating an ArrayList of keys
                                        ArrayList<String> toleNamelist = new ArrayList<>(keyProductSet);

                                        //Getting values
                                        Collection<List<PhoneDiary>> productListValues = groupByNaeHashmap.values();
                                        //Creating an ArrayList of values
                                        ArrayList<List<PhoneDiary>> phoneDiariesList = new ArrayList<>(productListValues);


                                        adapter.updateData(toleNamelist,phoneDiariesList);

                                    }

                                } catch (FileNotFoundException e) {
                                    getApiData(file, Constants.PHONE_DIARY_GID);
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
                        phoneDiaryPref = getPrefData();
                        if (phoneDiaryPref != null && phoneDiaryPref.size() > 0) {
                            Toast.makeText(PhoneDiaryActivity.this, "success retrieve", Toast.LENGTH_SHORT).show();
                            phoneDiaries = phoneDiaryPref;
                        } else {
                            Toast.makeText(PhoneDiaryActivity.this, getString(R.string.sth_went_wrong), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void saveData(ArrayList<PhoneDiary> toleData) {
        String json = gson.toJson(toleData);
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE).edit();
        editor.putString(Constants.PHONE_DIARY, json);
        editor.apply();
    }

    private ArrayList<PhoneDiary> getPrefData() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        String mainDataString = prefs.getString(Constants.PHONE_DIARY, null);
        Type type = new TypeToken<ArrayList<PhoneDiary>>() {
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

    private HashMap<String, List<PhoneDiary>> groupByName(ArrayList<PhoneDiary> phoneDiaries) {

        for (PhoneDiary phoneDiary : phoneDiaries) {
            String key = phoneDiary.getWardName();
            if (phoneDiariesHashMap.containsKey(key)) {
                List<PhoneDiary> list = phoneDiariesHashMap.get(key);
                assert list != null;
                list.add(phoneDiary);

            } else {
                List<PhoneDiary> list = new ArrayList<>();
                list.add(phoneDiary);
                phoneDiariesHashMap.put(key, list);
            }

        }

        return phoneDiariesHashMap;
    }

}
