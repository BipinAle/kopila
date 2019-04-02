package com.mobile.kopila.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobile.kopila.R;
import com.mobile.kopila.pojo.PhotoGallery;
import com.mobile.kopila.utils.Utils;

import java.util.ArrayList;


public class SlidingImageAdapter extends PagerAdapter {


    private ArrayList<PhotoGallery> images;
    private LayoutInflater inflater;
    private Context context;

    private Utils utils;

    public SlidingImageAdapter(Context context, ArrayList<PhotoGallery> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        utils = new Utils((AppCompatActivity) context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.image);

        utils.setImage(images.get(position).getImageId(), imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}