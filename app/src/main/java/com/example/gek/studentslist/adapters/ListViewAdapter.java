/**
 * Кастом адаптер для ListView
 */

package com.example.gek.studentslist;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Student> students;

    // В конструкторе получаем контекст, данные и инфлейтер
    ListViewAdapter(Context context, ArrayList<Student> students){
        ctx = context;
        this.students = students;
        lInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во записей
    @Override
    public int getCount() {
        return students.size();
    }

    // возвращает элемент по позиции
    @Override
    public Object getItem(int i) {
        return students.get(i);
    }

    // возвращает id по позиции
    @Override
    public long getItemId(int i) {
        return i;
    }

    // отрисовка одной записи
    @Override
    public View getView(int i, View inputView, ViewGroup viewGroup) {
        // используем созданные, но не используемые view (которые при прокрутке исчезают с экрана)
        View view = inputView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, viewGroup, false);
        }

        // получаем данные о студенте и заносим их в созданное выше вью
        final Student student = (Student) getItem(i);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(student.getName());
        LinearLayout llItem = (LinearLayout) view.findViewById(R.id.llItem);
        llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(student.getGooglePlus())));
            }
        });
        Button button = (Button)view.findViewById(R.id.btnGit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(student.getGit())));
            }
        });
        return view;
    }

}
