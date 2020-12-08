package com.example.memoria.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;
import com.example.memoria.adapters.SecondPageAdapter;
import com.example.memoria.models.Phone;
import com.example.memoria.models.WiFi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;

public class UsersMeasureActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> names;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button buttonFirstAnalysis;
    public List<Double> throughputList = new ArrayList<Double>();
    //public ImageView loading;
    static UsersMeasureActivity INSTANCE;
    private Realm realm;
    private static final int REQUEST_READ_PHONE_STATE = 200;
    private final int PHONE_ACCESS_CODE = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_measure);
        INSTANCE = this;
        realm = Realm.getDefaultInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            final String users = bundle.getString("quantity", "0");
            final int usersSize = Integer.parseInt(users);
            names = this.usersList(usersSize);

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewNames);
            mLayoutManager = new LinearLayoutManager(this);


            mAdapter = new SecondPageAdapter(names, new SecondPageAdapter.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onItemClick(String name, ImageView loading, Button medir, int position) {
                    int permissionCheck2 = ActivityCompat.checkSelfPermission(UsersMeasureActivity.this, Manifest.permission.READ_PHONE_STATE);
                    int permissionCheck1 = ActivityCompat.checkSelfPermission(UsersMeasureActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (permissionCheck2 != PackageManager.PERMISSION_GRANTED ) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_READ_PHONE_STATE);
                        } else {
                            Toast.makeText(UsersMeasureActivity.this, "Please, enable the request permission", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:"+ getPackageName()));
                            startActivity(i);
                        }
                    } else {
                        //Obtención de datos
                        loading.setImageResource(R.drawable.waiting);
                        wifiManager.startScan(); //Escaneo del WiFi
                        List<ScanResult> results = wifiManager.getScanResults(); //Resultados
                        int size =  getSize(results);// Cantidad de WiFis existentes
                        ConnectivityManager cm = (ConnectivityManager) UsersMeasureActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        int frequency =  wifiInfo.getFrequency(); //frecuencia
                        String MAC = wifiInfo.getBSSID(); //direccion MAC
                        String IMEI = getDeviceId(UsersMeasureActivity.this);

                        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
                        String extra = activeNetwork.getExtraInfo();
                        double latency = getLatency("8.8.8.8");

                        createNewWifi(IMEI, MAC, extra, size, frequency, linkSpeed, latency);

                        //Función que calcula th maximo
                        double th = throughputFunction(linkSpeed);

                        //Agregar th maximo a lista
                        if (TextUtils.isEmpty(name)){
                            Toast.makeText(UsersMeasureActivity.this, "Por favor ingrese un nombre", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if (throughputList.size() > position){
                                // SI AGREGAN EN DISTINTO ORDEN ESTO COLAPSA
                                throughputList.set(position, th);
                            }else{
                                medir.setText("Actualizar");
                                throughputList.add(th);
                            }
                            loading.setImageResource(R.drawable.done);
                        }
                    }



                }
            });

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);



            buttonFirstAnalysis = (Button) findViewById(R.id.buttonGoAnalysis);


            buttonFirstAnalysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (usersSize > throughputList.size()){
                        Toast.makeText(UsersMeasureActivity.this, "Faltan usuarios por medir", Toast.LENGTH_SHORT).show();
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

    public int getSize(List<ScanResult> results ){
        int size =0;
        for (int i=0; i<results.size(); i++ ){
            if (results.get(i).frequency>2000 && results.get(i).frequency<3000 ){
                size = size+1;
            }
        }
        return size;
    }


    public static UsersMeasureActivity getActivityInstance()
    {
        return INSTANCE;
    }

    public List<Double> getData()
    {
        return this.throughputList;
    }

    private double throughputFunction(int RSSI){
        //valor en Mbps
        double thMax = 0.3382 * RSSI + 26.5;
        if (thMax>12){
            thMax =12;
        }
        return thMax;
    }


    private List<String> usersList(int quantity){
        List<String> list = new ArrayList<>();
        for (int i=1; i<=quantity; ++i){
            list.add("Usuario "+ String.valueOf(i));
        }
        return list;
    }


    private void createNewPhone(String IMEI){
        realm.beginTransaction();
        Phone phone = new Phone(IMEI);
        realm.copyToRealm(phone);
        realm.commitTransaction();
    }

    public void createNewWifi(String IMEI, String MAC, String red, int nets, int freq, int rssi, double lat) {
        realm.beginTransaction();
        WiFi wifi = new WiFi(IMEI);
        wifi.MAC = MAC;
        wifi.red = red;
        wifi.wifiNets = nets;
        wifi.frequency = freq;
        wifi.wifiRSSI = rssi;
        wifi.latency = lat;
        //wifi.packages = pack;
        realm.copyToRealm(wifi);
        realm.commitTransaction();
    }

    public static String getDeviceId(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return  telephonyManager.getDeviceId();
    }

    public double getLatency(String ipAddress){
        String pingCommand = "/system/bin/ping -c " + "3" + " " + ipAddress;
        String inputLine = "";
        double avgRtt = 0;

        try {
            // execute the command on the environment interface
            Process process = Runtime.getRuntime().exec(pingCommand);
            // gets the input stream to get the output of the executed command
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            inputLine = bufferedReader.readLine();
            if (inputLine == null){
                return 0;
            }
            while ((inputLine != null)) {
                if (inputLine.length() > 0 && inputLine.contains("avg")) {  // when we get to the last line of executed ping command
                    break;
                }
                inputLine = bufferedReader.readLine();
            }
        }
        catch ( IOException e){
            Log.v("DEBUG_TAG", "getLatency: EXCEPTION");
            e.printStackTrace();
        }

        // Extracting the average round trip time from the inputLine string
        String afterEqual = inputLine.substring(inputLine.indexOf("="), inputLine.length()).trim();
        String afterFirstSlash = afterEqual.substring(afterEqual.indexOf('/') + 1, afterEqual.length()).trim();
        String strAvgRtt = afterFirstSlash.substring(0, afterFirstSlash.indexOf('/'));
        avgRtt = Double.valueOf(strAvgRtt);

        return avgRtt;
    }

    private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }


}
