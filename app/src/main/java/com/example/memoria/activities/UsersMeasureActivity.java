package com.example.memoria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Random;

public class UsersMeasureActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> names;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button buttonFirstAnalysis;
    private List<Integer> throughputList;
    //public ImageView loading;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_measure);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            final String users = bundle.getString("quantity", "0");
            final int usersSize = Integer.parseInt(users);
            names = this.usersList(usersSize);

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewNames);
            mLayoutManager = new LinearLayoutManager(this);
            //Aquí digo que vista quiero adaptar
            throughputList = new ArrayList<Integer>();
            mAdapter = new SecondPageAdapter(names, new SecondPageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String name, int throughputMax, ImageView loading, Button medir, int position) {
                    int th = throughputFunction();
                    if (TextUtils.isEmpty(name)){
                        Toast.makeText(UsersMeasureActivity.this, "Por favor ingrese un nombre", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        loading.setImageResource(R.drawable.waiting);
                        if (throughputList.size() > position){
                            // SI AGREGAN EN DISTINTO ORDEN ESTO COLAPSA
                            throughputList.set(position, th);
                        }else{
                            medir.setText("Actualizar");
                            throughputList.add(th);
                        }
                        Toast.makeText(UsersMeasureActivity.this,name + "-"+ th, Toast.LENGTH_LONG).show();
                        loading.setImageResource(R.drawable.done);
                    }

                }
            });

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            buttonFirstAnalysis = (Button) findViewById(R.id.buttonGoAnalysis);


            //SI NO HAY NOMBRES AVANZA IGUAL
            buttonFirstAnalysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (usersSize > throughputList.size()){
                        Toast.makeText(UsersMeasureActivity.this, "Faltan usuarios por medir", Toast.LENGTH_LONG).show();
                    } else {
                        String[] escrito = ((SecondPageAdapter)mAdapter).getEscrito();
                        Intent goPage3 = new Intent(UsersMeasureActivity.this, FirstAnalysisActivity.class);
                        goPage3.putExtra("people", escrito);
                        startActivity(goPage3);
                    }


                }
            });
        } else{
            Toast.makeText(UsersMeasureActivity.this, "Por favor vuelva a intentarlo", Toast.LENGTH_LONG).show();
        }






    }

    private int throughputFunction(){
        Random rand = new Random();
        int throughput = rand.nextInt(1000);
        return  throughput;
    }


    private List<String> usersList(int quantity){
        List<String> list = new ArrayList<>();
        for (int i=1; i<=quantity; ++i){
            list.add("Usuario "+ String.valueOf(i));
        }
        return list;
    }


}
