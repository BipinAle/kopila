package com.mobile.kopila.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.kopila.R;
import com.mobile.kopila.pojo.PhoneDiary;
import com.mobile.kopila.utils.Utils;

import java.util.ArrayList;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.PersonViewHolder> {
    private ArrayList<PhoneDiary> phoneDiaries;
    private Utils utils;
    private Context context;

    public PersonListAdapter(Context context) {
        phoneDiaries = new ArrayList<>();
        utils = new Utils((AppCompatActivity) context);
    }

    @NonNull
    @Override
    public PersonListAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PersonViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item_person_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PersonListAdapter.PersonViewHolder holder, int i) {
        PhoneDiary phoneDiary = phoneDiaries.get(i);
        holder.phoneTv.setText(phoneDiary.getPhoneNumber());
        holder.nameTv.setText(phoneDiary.getPersonName());

        utils.setImage(phoneDiary.getPhotoUrl(), holder.profileImage);

        holder.call.setOnClickListener(v -> onMessageCallListner.onCallClick(phoneDiary.getPhoneNumber()));
        holder.message.setOnClickListener(v -> onMessageCallListner.onMessageClick(phoneDiary.getPhoneNumber()));

    }

    public void update(ArrayList<PhoneDiary> phoneDiaries) {
        this.phoneDiaries = phoneDiaries;
        notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return phoneDiaries.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView call, message, profileImage;
        TextView nameTv, phoneTv;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            call = itemView.findViewById(R.id.call_iv);
            message = itemView.findViewById(R.id.message_iv);
            profileImage = itemView.findViewById(R.id.profile_image);
            nameTv = itemView.findViewById(R.id.name_tv);
            phoneTv = itemView.findViewById(R.id.phone_number);
        }
    }

    private OnMessageCallListner onMessageCallListner;

    public void setOnMessageCallListner(OnMessageCallListner onMessageCallListner) {
        this.onMessageCallListner = onMessageCallListner;
    }

    public interface OnMessageCallListner {
        void onCallClick(String num);

        void onMessageClick(String num);
    }
}
