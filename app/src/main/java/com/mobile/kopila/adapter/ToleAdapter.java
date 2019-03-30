package com.mobile.kopila.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.kopila.R;
import com.mobile.kopila.activities.PersonListActivity;

public class ToleAdapter extends RecyclerView.Adapter<ToleAdapter.ToleViewHolder> {
    private String[] toles;

    public ToleAdapter(String[] toles) {
        this.toles = toles;

    }

    @NonNull
    @Override
    public ToleAdapter.ToleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ToleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item_tole_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ToleAdapter.ToleViewHolder viewHolder, final int i) {
        viewHolder.toleName.setText(toles[i]);
        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(viewHolder.itemView.getContext(), PersonListActivity.class);
            viewHolder.itemView.getContext().startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return toles.length;
    }

    public class ToleViewHolder extends RecyclerView.ViewHolder {
        TextView toleName;

        public ToleViewHolder(@NonNull View itemView) {
            super(itemView);
            toleName = itemView.findViewById(R.id.tole_name);
        }

    }
}
