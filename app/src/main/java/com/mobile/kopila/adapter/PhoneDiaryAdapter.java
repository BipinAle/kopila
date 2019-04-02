package com.mobile.kopila.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.kopila.R;
import com.mobile.kopila.activities.PersonListActivity;
import com.mobile.kopila.pojo.PhoneDiary;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PhoneDiaryAdapter extends RecyclerView.Adapter<PhoneDiaryAdapter.ToleViewHolder> {
    private final int[] colors;
    private Context context;
    private ArrayList<List<PhoneDiary>> phoneDiariesList;
    private ArrayList<String> toleNamelist;

    public PhoneDiaryAdapter(Context context) {
        this.context = context;
        toleNamelist = new ArrayList<>();
        phoneDiariesList = new ArrayList<>();
        colors = context.getResources().getIntArray(R.array.cardcolor);
    }

    @NonNull
    @Override
    public PhoneDiaryAdapter.ToleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ToleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item_tole_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PhoneDiaryAdapter.ToleViewHolder viewHolder, final int i) {
        viewHolder.toleName.setText(toleNamelist.get(i));

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(viewHolder.itemView.getContext(), PersonListActivity.class);
            intent.putExtra("phoneDiaries", Parcels.wrap(phoneDiariesList.get(i)));
            viewHolder.itemView.getContext().startActivity(intent);
        });
        viewHolder.cardView.setCardBackgroundColor(colors[(int) Math.floor((Math.random() * ((1000 - 10) + 1) + 10) % colors.length)]);


    }


    @Override
    public int getItemCount() {
        return toleNamelist.size();
    }

    public void updateData(ArrayList<String> toleNamelist, ArrayList<List<PhoneDiary>> phoneDiariesList) {
        this.phoneDiariesList = phoneDiariesList;
        this.toleNamelist = toleNamelist;
        notifyDataSetChanged();
    }

    public class ToleViewHolder extends RecyclerView.ViewHolder {
        TextView toleName;
        CardView cardView;

        public ToleViewHolder(@NonNull View itemView) {
            super(itemView);
            toleName = itemView.findViewById(R.id.tole_name);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}
