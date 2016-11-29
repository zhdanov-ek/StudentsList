package com.example.gek.studentslist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gek.studentslist.R;
import com.example.gek.studentslist.data.User;

/**
 * Created by gek on 29.11.2016.
 */
public class ContactViewHolder extends RecyclerView.ViewHolder {

    private TextView tvName;
    private TextView tvPhone;
    private ImageView ivIcon;

    static ContactViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new ContactViewHolder(inflater.inflate(R.layout.item_contact_phonebook, parent, false));
    }

    public ContactViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvContactName);
        tvPhone = (TextView) itemView.findViewById(R.id.tvContactPhone);
        ivIcon = (ImageView) itemView.findViewById(R.id.ivAvatar);
    }

    void bind(User user) {
        tvName.setText(user.getName());
        tvPhone.setText(user.getPhone());

    }
}