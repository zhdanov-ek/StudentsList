package com.example.gek.studentslist.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.gek.studentslist.data.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gek on 29.11.2016.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder>{

    private final List<User> users = new ArrayList<>();
    private LayoutInflater inflater;

    public ContactsAdapter(@Nullable List<User> usersList) {
        updateAdapter(usersList);
    }

    public void updateAdapter(@Nullable List<User> usersList) {
        users.clear();
        if (usersList != null) {
            users.addAll(usersList);
        }
        notifyDataSetChanged();
    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return ContactViewHolder.create(inflater, parent);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
