package com.lec.android.a008_practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PhonebookAdapter_practice adapter; // Adapter 객체
    RecyclerView rv;
    Button btnAdd;
    EditText a,b,c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a = findViewById(R.id.t_name);
        b = findViewById(R.id.t_age);
        c = findViewById(R.id.t_address);

        rv = findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                                                                        // context는 프로그램이 메모리에 올라갔을 때, 실행되기 위해서 필수로 필요한 메모리영역
        rv.setLayoutManager(layoutManager);
        adapter = new PhonebookAdapter_practice();

        rv.setAdapter(adapter); // RecyclerView 에 Adapter 장착!

        btnAdd = findViewById(R.id.btn_InfoAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(0, new Phonebook_practice(a.getText().toString(),b.getText().toString(),c.getText().toString()));
                adapter.notifyDataSetChanged();
            }
        });
    }
}
