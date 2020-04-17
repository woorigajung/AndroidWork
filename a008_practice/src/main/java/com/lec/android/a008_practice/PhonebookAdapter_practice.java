package com.lec.android.a008_practice;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PhonebookAdapter_practice extends RecyclerView.Adapter<PhonebookAdapter_practice.ViewHolder> {
    List<Phonebook_practice> items = new ArrayList<Phonebook_practice>();

    static PhonebookAdapter_practice adapter; // Adapter 객체

    public PhonebookAdapter_practice() { this.adapter = this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 주어진 ViewGroup 으로부터 LayoutInflater 추출
        LayoutInflater inf = LayoutInflater.from(parent.getContext());

        // 준비된 레이아웃(XML) 으로부터 View를 만들어 ViewGroup 에 붙이고
        // 만들어진 View 를 리턴한다.
        View itemView = inf.inflate(R.layout.item, parent, false);

        // 위에서 만들어진 새로운 View를 ViewHolder 에 담아 리턴
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Phonebook_practice item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();    // List<>의 size()
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView t_i_name, t_i_age, t_i_address;
        ImageButton btnDelete;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            // 객체 가져오기
            t_i_name = itemView.findViewById(R.id.t_i_name);
            t_i_age = itemView.findViewById(R.id.t_i_age);
            t_i_address = itemView.findViewById(R.id.t_i_address);

            btnDelete = itemView.findViewById(R.id.t_i_delete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.removeItem(getAdapterPosition());
                    adapter.notifyDataSetChanged();
                }
            });
        }   // 생성자

        public void setItem(Phonebook_practice item) {
            t_i_name.setText(item.getName());
            t_i_age.setText(item.getAge());
            t_i_address.setText(item.getAddress());

        }   // 멤버변수 세팅

    }   // end ViewHolder

    public void addItem(int position, Phonebook_practice item) {
        items.add(position, item);
    }

    public void removeItem(int position){ items.remove(position); }

} // end pbadapter
