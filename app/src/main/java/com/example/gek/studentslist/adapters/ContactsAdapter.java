package com.example.gek.studentslist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gek.studentslist.R;

/**
 * .Адаптер, который грузит данные с Cursor полученного через CursorLoader
 * в айтемы листвью
 */

public class ContactsAdapter extends CursorAdapter {

    public ContactsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    // Формируем айтем (вью)
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return view;
    }


    // Выводим данные с курсора в наши вью
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        TextView tvName = (TextView)view.findViewById(R.id.tvName);
        tvName.setText(cursor.getString(nameColumnIndex));
    }
}
