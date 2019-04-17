package com.mobile.kopila.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.kopila.R;

import java.util.ArrayList;

public class HamroBaremaChinariAdapter extends RecyclerView.Adapter<HamroBaremaChinariAdapter.HamroBaremaViewHolder> {
    private ArrayList<String> hamroBaremaList;

    public HamroBaremaChinariAdapter() {
        hamroBaremaList = new ArrayList<>();
    }

    @NonNull
    @Override
    public HamroBaremaChinariAdapter.HamroBaremaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HamroBaremaViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item_harmo_barema, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HamroBaremaChinariAdapter.HamroBaremaViewHolder viewHolder, int i) {
        String singleItem = hamroBaremaList.get(i);
        viewHolder.textView.setText(singleItem.contains("c") ? singleItem.replace("c", ",") : singleItem);

    }

    public void update(ArrayList<String> hamroBaremaList) {
        this.hamroBaremaList = hamroBaremaList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return hamroBaremaList.size();
    }

    public class HamroBaremaViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout container;

        public HamroBaremaViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.hamro_barema_text);
            container = itemView.findViewById(R.id.container);
        }
    }
}
