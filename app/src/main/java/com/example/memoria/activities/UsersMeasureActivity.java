package com.example.memoria.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;
import com.example.memoria.adapters.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class UsersMeasureActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> names;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_measure);
        names = this.getAllNames();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewNames);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyAdapter(names, R.layout.recycle_view_item, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, int position) {
                Toast.makeText(UsersMeasureActivity.this,name + "-"+ position, Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private List<String> getAllNames(){
        return new ArrayList<String>(){{
            add("Cata");
            add("Jhon");
            add("Pauli");
            add("Deni");
            add("Dani");
        }};
    }


}
