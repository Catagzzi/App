package com.example.memoria.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;
import com.example.memoria.adapters.SpinnerAdapter;
import com.example.memoria.adapters.ThirdPageAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirstAnalysisActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView recyclerSpinner;
    private List<String> names;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter spinnerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager spinnerLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis1);
        Bundle bundle = getIntent().getExtras();
        names = Arrays.asList(bundle.getStringArray("people")); //this.getAllNames();
        String[] bankNames={"BOI","SBI","HDFC","PNB","OBC"};

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAnalysis);
        recyclerSpinner = (RecyclerView) findViewById(R.id.recyclerViewSpinner);
        mLayoutManager = new LinearLayoutManager(this);
        spinnerLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ThirdPageAdapter(names, R.layout.item_page3_analysis1, new ThirdPageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, int position) {
                Toast.makeText(FirstAnalysisActivity.this,name + "-"+ position, Toast.LENGTH_LONG).show();
            }
        });


        spinnerAdapter = new SpinnerAdapter(FirstAnalysisActivity.this, names, R.layout.item_spinner, bankNames);
        recyclerSpinner.setLayoutManager(mLayoutManager);
        recyclerSpinner.setAdapter(spinnerAdapter);

        mRecyclerView.setLayoutManager(spinnerLayoutManager);
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
