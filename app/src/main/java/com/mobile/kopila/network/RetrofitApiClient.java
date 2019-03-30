package com.mobile.kopila.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
    private static Context context;
    private static Gson gson = new GsonBuilder().setLenient().create();
    private Retrofit retrofit;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public RetrofitApiClient(Context mContext) {
        context = mContext;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = null;
        if (context != null)
            cache = new Cache(context.getCacheDir(), cacheSize);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if (!isNetAvailable(context)) {
                        int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                        request = request
                                .newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .build();
                    }
                    return chain.proceed(request);
                });
        if (cache != null)
            okHttpClientBuilder = okHttpClientBuilder.cache(cache);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit getAdapter() {
        return retrofit;
    }

    public boolean isNetAvailable(Context context) {
        boolean result = false;
        if (context == null)
            return false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        result = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }
}
