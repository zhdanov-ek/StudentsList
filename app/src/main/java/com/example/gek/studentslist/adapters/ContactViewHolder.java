package com.example.gek.studentslist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gek.studentslist.R;
import com.example.gek.studentslist.data.CircleTransformation;
import com.example.gek.studentslist.data.User;
import com.squareup.picasso.Picasso;

/**
 * Вью холдер для связывания данных с лаяутом для ContactsAdapter
 */
public class ContactViewHolder extends RecyclerView.ViewHolder {

    private TextView tvName;
    private TextView tvPhone;
    private ImageView ivIcon;

    private final CircleTransformation circleTransformation = new CircleTransformation();

    static ContactViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new ContactViewHolder(inflater.inflate(R.layout.item_contact_phonebook, parent, false));
    }

    public ContactViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvContactName);
        tvPhone = (TextView) itemView.findViewById(R.id.tvContactPhone);
        ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
    }

    void bind(User user) {
        tvName.setText(user.getName());
        tvPhone.setText(user.getPhone());

        Picasso.with(ivIcon.getContext())
                .load(user.getIcon())
                .placeholder(R.drawable.oval)
                .error(R.drawable.oval)
                .transform(circleTransformation)
                .into(ivIcon);

    }
}