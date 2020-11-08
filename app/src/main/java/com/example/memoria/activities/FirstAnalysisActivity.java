package com.example.memoria.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;
import com.example.memoria.adapters.ThirdPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class FirstAnalysisActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> names;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis1);
        names = this.getAllNames();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAnalysis);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ThirdPageAdapter(names, R.layout.item_page3_analysis1, new ThirdPageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, int position) {
                Toast.makeText(FirstAnalysisActivity.this,name + "-"+ position, Toast.LENGTH_LONG).show();
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
