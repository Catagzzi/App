package com.example.memoria.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;
import com.example.memoria.adapters.ThirdPageAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirstAnalysisActivityII extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> names;
    private List<Integer> throughputMax;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_analysis);
        String[] bankNames={"BOI","SBI","HDFC","PNB","OBC"};

        Bundle bundle = getIntent().getExtras();
        names = Arrays.asList(bundle.getStringArray("finalNames"));
        throughputMax = UsersMeasureActivity.getActivityInstance().getData();
        Toast.makeText(FirstAnalysisActivityII.this,"Data from first activity is"+throughputMax,Toast.LENGTH_SHORT).show();


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAnalysis);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new ThirdPageAdapter(names, R.layout.item_page3_analysis1, new ThirdPageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, int position) {
                Toast.makeText(FirstAnalysisActivityII.this,name + "-"+ position, Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private int confirmation(int thMax, int thNecessary){
        if(thMax > thNecessary ){
            return 0;
        } else if (thMax < thNecessary){
            return 1;
        } else{
            return 2;
        }
    }


}
