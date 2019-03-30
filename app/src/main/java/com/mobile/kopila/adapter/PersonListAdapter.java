package com.mobile.kopila.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobile.kopila.R;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.PersonViewHolder> {
    @NonNull
    @Override
    public PersonListAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PersonViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item_person_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PersonListAdapter.PersonViewHolder holder, int i) {
        holder.call.setOnClickListener(v -> onMessageCallListner.onCallClick("9840052984"));
        holder.message.setOnClickListener(v -> onMessageCallListner.onMessageClick("9840052984"));

    }


    @Override
    public int getItemCount() {
        return 20;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView call, message;

        public PersonViewHolder(@NonNull View itemView) {
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
