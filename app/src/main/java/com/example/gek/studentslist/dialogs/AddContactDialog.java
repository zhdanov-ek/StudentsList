package com.example.gek.studentslist.dialogs;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gek.studentslist.R;

import java.util.ArrayList;

/**
 * Created by gek on 30.11.2016.
 */

public class AddContactDialog extends DialogFragment implements View.OnClickListener {
    EditText etName, etPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setTitle(R.string.new_contact_title);
        View v = inflater.inflate(R.layout.dialog_add_contact, null);
        v.findViewById(R.id.btnOk).setOnClickListener(this);
        v.findViewById(R.id.btnCancel).setOnClickListener(this);
        etName = (EditText)v.findViewById(R.id.etName);
        etPhone = (EditText)v.findViewById(R.id.etPhone);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOk:
                Log.d("1111", "onClick: OK");
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                if (name.isEmpty() || phone.isEmpty()){
                    Toast.makeText(getContext(),R.string.new_contact_empty_fields, Toast.LENGTH_SHORT).show();
                } else {
                    if (insertContact(getContext().getContentResolver(), name, phone)){
                        Toast.makeText(getContext(), R.string.new_contact_created, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(),R.string.new_contact_error, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnCancel:
                Log.d("1111", "onClick: CANCEL");
                dismiss();
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    /** Добавление контакта в телефон */
    public static boolean insertContact(ContentResolver contentResolver, String firstName, String mobileNumber) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Сначала добавляем пустой контакт
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //Добавляем имя контакта
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,firstName)
                .build());

        //Добавляем телефон
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,mobileNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
        try {
            // Пробуем через резолвер все записать
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
