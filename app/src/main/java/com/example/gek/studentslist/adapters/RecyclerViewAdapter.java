/**
 *  Кастом адаптер для RecyclerView
 */

package com.example.gek.studentslist.adapters;

import com.example.gek.studentslist.activity.PersonActivity;
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

import io.realm.RealmResults;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {
    private RealmResults<Student> students;
    private Context ctx;

    // На вход конструктору адаптера подаем контекст для запуска активити и список студентов
    public RecyclerViewAdapter(Context ctx,  RealmResults<Student> students){
        this.ctx = ctx;
        this.students = students;
    }

    /** Реализация абстрактного класса ViewHolder, хранящего ссылки на виджеты.
    / Он же реализует функцию OnClickListener, что бы не создавать их на каждое поле
    / при прокрутке в onBindViewHolder. Максимум таких холдеров будет на два больше
    / чем вмещается на экране */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvName;
        private Button btnGit;
        private LinearLayout llItem;


        public ViewHolder(View itemView){
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btnGit = (Button) itemView.findViewById(R.id.btnGit);
            btnGit.setOnClickListener(this);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
            llItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // изымаем данные нашего студента по позиции где был клик
            Student student = students.get(getAdapterPosition());
            switch (v.getId()){
                case R.id.llItem:
                    Intent intentGooglePlus = new Intent(ctx, PersonActivity.class);
                    intentGooglePlus.putExtra(Consts.TYPE_CARD, Consts.TYPE_CARD_GOOGLE);
                    intentGooglePlus.putExtra(Consts.ID_GOOGLE, student.getIdGoogle());
                    ctx.startActivity(intentGooglePlus);
                    break;
                case R.id.btnGit:
                    Intent intentGit = new Intent(ctx, PersonActivity.class);
                    intentGit.putExtra(Consts.TYPE_CARD, Consts.TYPE_CARD_GIT);
                    intentGit.putExtra(Consts.ID_GIT, student.getIdGit());
                    ctx.startActivity(intentGit);
                    break;
                default:
                    break;
            }
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
    }

    @Override
    public int getItemCount() {
        return students.size();
    }
}
