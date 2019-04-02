package com.mobile.kopila.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobile.kopila.R;
import com.mobile.kopila.pojo.SahayogSamparka;

import java.util.ArrayList;

public class SahyogSamparkaAdapter extends RecyclerView.Adapter<SahyogSamparkaAdapter.SahyogSamparkaViewHolder> {

    private ArrayList<SahayogSamparka> sahayogSamparkas;

    public SahyogSamparkaAdapter() {
        sahayogSamparkas = new ArrayList<>();
    }

    @NonNull
    @Override
    public SahyogSamparkaAdapter.SahyogSamparkaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SahyogSamparkaViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item_sahyog_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SahyogSamparkaAdapter.SahyogSamparkaViewHolder viewHolder, int i) {
        viewHolder.call.setOnClickListener(v -> onMessageCallListner.onCallClick("9840052984"));
        viewHolder.message.setOnClickListener(v -> onMessageCallListner.onMessageClick("9840052984"));

    }

    public void update(ArrayList<SahayogSamparka> sahayogSamparkas) {
        this.sahayogSamparkas = sahayogSamparkas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sahayogSamparkas.size();
    }

    public class SahyogSamparkaViewHolder extends RecyclerView.ViewHolder {
        ImageView call, message;

        public SahyogSamparkaViewHolder(@NonNull View itemView) {
            super(itemView);
            call = itemView.findViewById(R.id.call_iv);
            message = itemView.findViewById(R.id.message_iv);
        }
    }

    OnMessageCallListner onMessageCallListner;

    public void setOnMessageCallListner(OnMessageCallListner onMessageCallListner) {
        this.onMessageCallListner = onMessageCallListner;
    }

    public interface OnMessageCallListner {
        void onCallClick(String num);

        void onMessageClick(String num);
    }
}
