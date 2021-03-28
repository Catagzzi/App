    package com.example.memoria.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memoria.R;

import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    private Button buttonPeople;
    private TextView instruction;
    private TextView title;
    private EditText Quantity;

    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        instruction = (TextView) findViewById(R.id.textViewInstructionThird);
        instruction.setText("Ingrese la cantidad de personas que planean usar WiFi para conectarse a Internet");
        title  = (TextView) findViewById(R.id.textViewTitleThird);
        title.setText("Análisis de Conexión");
        buttonPeople = (Button) findViewById(R.id.buttonPeople);
        Quantity = (EditText) findViewById(R.id.editTextPeople);


        buttonPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) ThirdActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                //Cambiar a TYPE_WIFI
                if (activeNetwork != null) {
                    Boolean Mobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
                    if(Mobile == true) {
                        Toast.makeText(ThirdActivity.this, "Por favor conéctese a WiFi ", Toast.LENGTH_SHORT).show();
                    } else {
                        String quantity = Quantity.getText().toString();
                        if (quantity.matches("")){
                            Toast.makeText(ThirdActivity.this, "No ha ingresado un valor", Toast.LENGTH_SHORT).show();
                            return;
                        } else{
                            Intent goPage2 = new Intent(ThirdActivity.this, UsersMeasureActivity.class);
                            List<String> users = usersList(Integer.parseInt(quantity));
                            goPage2.putExtra("quantity", quantity);
                            startActivity(goPage2);
                        }
                    }
                }  else {
                    Toast.makeText(ThirdActivity.this, "Sin conexión a internet", Toast.LENGTH_SHORT).show();
                }

            }

        });



    }

    public  static List<String> usersList(int quantity){
        List<String> list = new ArrayList<>();
        for (int i=0; i<=quantity; ++i){
            list.add("Usuario "+ String.valueOf(i));
        }
        return list;
    }

}