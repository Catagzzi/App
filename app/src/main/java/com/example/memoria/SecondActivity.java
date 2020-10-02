package com.example.memoria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private TextView textViewLatency;
    private TextView textViewPing;
    private  TextView textViewExtra;
    private TextView textViewConnection;
    private TextView textViewMobileSignal;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textViewLatency = (TextView) findViewById(R.id.secondTextViewLatency);
        textViewPing = (TextView) findViewById(R.id.secondTextViewPing);
        textViewExtra = (TextView) findViewById(R.id.secondTextViewExtra);
        textViewConnection = (TextView) findViewById(R.id.secondTextViewConnection);
        textViewMobileSignal = (TextView) findViewById(R.id.secondTextViewMobileSignal);
        buttonNext = (Button) findViewById(R.id.buttonGo);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        //Tomar los datos del intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            // Latency
            Double rtt = bundle.getDouble("latency", 0.0);
            String latency = String.valueOf(rtt);
            // Ping
            String ping = bundle.getString("loss", "");
            // Extra
            String extra = bundle.getString("extra");
            // Connection
            Boolean wifi = bundle.getBoolean("Wifi");
            Boolean mobile = bundle.getBoolean("Mobile");

            if (wifi == true ){
                textViewConnection.setText("Conexión por WiFi");
                int RSSI = bundle.getInt("wifiSignal", 0);
                textViewMobileSignal.setText("Intensidad señal: "+ RSSI);
            }
            if (mobile == true){
                textViewConnection.setText("Conexión por Datos Móviles");
                int RSSI = bundle.getInt("mobileSignal",0);
                String technology = bundle.getString("technology","");
                textViewMobileSignal.setText("Intensidad señal: " + RSSI + ", tecnología " + technology );
            }

            textViewLatency.setText("Latencia: " +latency);

            textViewPing.setText("Paquetes perdidos: "+ping + "%");;

            textViewExtra.setText("Red: " +extra);
        } else{
            Toast.makeText(SecondActivity.this, "It is empty", Toast.LENGTH_LONG);
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,ThirdActivity.class);
                startActivity(intent);
            }
        });


    }
}