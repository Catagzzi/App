package com.example.memoria.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memoria.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.memoria.models.Phone;

import com.example.memoria.models.WiFi;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SecondActivity extends AppCompatActivity {

    private Realm realm;

    private TextView textViewLatency;
    private TextView textViewPing;
    private  TextView textViewExtra;
    private TextView textViewConnection;
    private TextView textViewMobileSignal;
    private TextView textViewMobileNet;
    private Button buttonNext;

    private RealmResults<WiFi> wifiData;
    private RealmResults<WiFi> wifiDataYear;
    private WiFi wifiDataLast;

    @RequiresApi(api = Build.VERSION_CODES.Q)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // DB Realm
        realm = Realm.getDefaultInstance();

        textViewLatency = (TextView) findViewById(R.id.secondTextViewLatency);
        textViewPing = (TextView) findViewById(R.id.secondTextViewPing);
        textViewExtra = (TextView) findViewById(R.id.secondTextViewExtra);
        textViewConnection = (TextView) findViewById(R.id.secondTextViewConnection);
        textViewMobileSignal = (TextView) findViewById(R.id.secondTextViewMobileSignal);
        textViewMobileNet = (TextView) findViewById(R.id.secondTextViewNet);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        // get data of database

        //All
        wifiData = realm.where(WiFi.class).findAll();
        // Last
        wifiDataLast = realm.where(WiFi.class).findAll().last();
        // Year
        Calendar today = Calendar.getInstance();
        Date todayDate =  today.getTime();
        today.add(Calendar.YEAR, -1);
        Date lastYearDate = today.getTime();
        wifiDataYear = realm.where(WiFi.class).between("createdAt", lastYearDate , todayDate).findAll();

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
            // Size
            int size = bundle.getInt("size",0);
            textViewMobileNet.setText("Redes WiFi disponibles: " + size);

            if (wifi == true ){
                textViewConnection.setText("Conexión por WiFi");
                int RSSI = bundle.getInt("wifiSignal", 0);
                String MAC = bundle.getString("MAC", "");
                int frequency = bundle.getInt("frequency", 0);
                textViewMobileSignal.setText("Intensidad: "+ RSSI+", MAC: " +MAC + ", frecuencia: " +frequency);
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "https://monitor.internetworking.cl/iot/?i=tel-cata&k=1234&d=Latencia:"+latency+"|Intensidad:"+String.valueOf(RSSI);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Toast.makeText(SecondActivity.this, "Response is: "+ "response.substring(0,500)", Toast.LENGTH_LONG );
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SecondActivity.this,"That didn't work!", Toast.LENGTH_LONG );
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);


            }
            if (mobile == true){
                textViewConnection.setText("Conexión por Datos Móviles");
                int RSSI = bundle.getInt("mobileSignal",0);
                String technology = bundle.getString("technology","");
                textViewMobileSignal.setText("Intensidad: " + RSSI + ", tecnología " + technology );

            }

            textViewLatency.setText("Latencia: " +latency);

            textViewPing.setText("Paquetes perdidos: "+ping + "%");;

            textViewExtra.setText("Red: " +extra);
        } else{
            Toast.makeText(SecondActivity.this, "It is empty", Toast.LENGTH_LONG);
        }



    }


}