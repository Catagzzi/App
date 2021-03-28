package com.example.memoria.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;
import com.example.memoria.adapters.SecondPageAdapter;
import com.example.memoria.adapters.SpinnerAdapter;
import com.example.memoria.adapters.ThirdPageAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirstAnalysisActivityII extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> names;
    private int[] positions;
    private List<services> services;
    private List<Double> throughputMax;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_analysis);
        String[] bankNames={"Netflix","Zoom","Instagram","Facebook"};

        Bundle bundle = getIntent().getExtras();
        names = Arrays.asList(bundle.getStringArray("finalNames"));
        positions = bundle.getIntArray("positions");
        throughputMax = UsersMeasureActivity.getActivityInstance().getData();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAnalysis);
        mLayoutManager = new LinearLayoutManager(this);

        services = getAllServices();

        mAdapter = new ThirdPageAdapter(names, R.layout.item_page3_analysis1,  throughputMax,  services, positions);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }



    private List<services> getAllServices(){
        return new ArrayList<services>(){{
            add(new services("Netflix", 35, R.drawable.netflix));
            add(new services("Zoom", 40, R.drawable.zoom));
            add(new services("Instagram", 20, R.drawable.instagram));
            add(new services("Facebook", 20, R.drawable.facebook));
        }};

    }


}
