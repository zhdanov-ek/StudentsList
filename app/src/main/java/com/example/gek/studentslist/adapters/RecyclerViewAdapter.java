/**
 *  Кастом адаптер для RecyclerView
 */

package com.example.gek.studentslist.adapters;

import com.example.gek.studentslist.data.*;
import com.example.gek.studentslist.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {
    private ArrayList<Student> students;
    private Context ctx;

    // На вход конструктору адаптера подаем контекст для запуска активити и список студентов
    public RecyclerViewAdapter(Context ctx, ArrayList<Student> students){
        this.ctx = ctx;
        this.students = students;
    }

    // Реализация абстрактного класса ViewHolder, хранящего ссылки на виджеты.
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private Button btnGit;
        private LinearLayout llItem;

        public ViewHolder(View itemView){
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btnGit = (Button) itemView.findViewById(R.id.btnGit);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
        }
    }

    // Создает новые views (элементы списка) (вызывается layout manager-ом)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заполнение данными с позицией position, наших вью элементов
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Student student = students.get(position);
        holder.tvName.setText(student.getName());
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(student.getGooglePlus())));
            }
        });
        holder.btnGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(student.getGit())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }
}
