package com.example.memoria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
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
    private RecyclerView recyclerSpinner;
    private List<String> names;
    private RecyclerView.Adapter spinnerAdapter;
    private RecyclerView.LayoutManager spinnerLayoutManager;
    private Button buttonOk;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis1);
        final Bundle bundle = getIntent().getExtras();
        final String[] lista = bundle.getStringArray("people");
        names = Arrays.asList(lista); //this.getAllNames();
        String[] bankNames={"BOI","SBI","HDFC","PNB","OBC"};

        recyclerSpinner = (RecyclerView) findViewById(R.id.recyclerViewSpinner);
        spinnerLayoutManager = new LinearLayoutManager(this);

        spinnerAdapter = new SpinnerAdapter(FirstAnalysisActivity.this, names, R.layout.item_spinner, bankNames);
        recyclerSpinner.setLayoutManager(spinnerLayoutManager);
        recyclerSpinner.setAdapter(spinnerAdapter);

        buttonOk = (Button) findViewById(R.id.buttonAllOk);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goPage4 = new Intent(FirstAnalysisActivity.this, FirstAnalysisActivityII.class);
                goPage4.putExtra("finalNames", lista);
                startActivity(goPage4);
            }
        });


    }

}
