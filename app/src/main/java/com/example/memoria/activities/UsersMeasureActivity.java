package com.example.memoria.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;
import com.example.memoria.adapters.SecondPageAdapter;
import com.example.memoria.adapters.ThirdPageAdapter;

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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String users = bundle.getString("quantity", "0");
            int usersSize = Integer.parseInt(users);
            names = this.usersList(usersSize);

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewNames);
            mLayoutManager = new LinearLayoutManager(this);
            //Aquí digo que vista quiero adaptar
            mAdapter = new SecondPageAdapter(names, R.layout.item_page2_analysis1, new SecondPageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String name, int position) {
                    Toast.makeText(UsersMeasureActivity.this,name + "-"+ position, Toast.LENGTH_LONG).show();
                }
            });

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        } else{
            Toast.makeText(UsersMeasureActivity.this, "Por favor vuelva a intentarlo", Toast.LENGTH_LONG).show();
        }


    }


    private List<String> usersList(int quantity){
        List<String> list = new ArrayList<>();
        for (int i=1; i<=quantity; ++i){
            list.add("Usuario "+ String.valueOf(i));
        }
        return list;
    }


}
