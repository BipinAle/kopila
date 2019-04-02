package com.mobile.kopila.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mobile.kopila.R;

public class Utils {
    private AppCompatActivity activity;

    public Utils(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setImage(String id, ImageView imageView) {
        Glide.with(activity.getApplicationContext()).load("https://docs.google.com/uc?id=" + id)
                .placeholder(activity.getResources().getDrawable(R.drawable.budo_ba))
                .centerCrop()
                .into(imageView);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
