package com.example.memoria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button buttonFirstAnalysis;


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
            mAdapter = new SecondPageAdapter(names);//, R.layout.item_page2_analysis1

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            buttonFirstAnalysis = (Button) findViewById(R.id.buttonGoAnalysis);


            buttonFirstAnalysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UsersMeasureActivity.this, "Funciona", Toast.LENGTH_LONG).show();
                    String[] escrito = ((SecondPageAdapter)mAdapter).getEscrito();
                    Intent goPage3 = new Intent(UsersMeasureActivity.this, FirstAnalysisActivity.class);
                    goPage3.putExtra("people", escrito);
                    startActivity(goPage3);

                }
            });
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
