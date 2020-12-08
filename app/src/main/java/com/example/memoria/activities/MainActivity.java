package com.example.memoria.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memoria.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    /*private Realm realm;

    private static final String DEBUG_TAG = "MainActivity";
    private static final int REQUEST_READ_PHONE_STATE = 200;
    private Button btn;

    private final String msg = "Hello from the other side";
    private double latency = 0;
    private String loss = "";
    private String extra = "";
    private int cellStrength = 0;
    public boolean Wifi = false;
    public boolean Mobile = false;
    private final int PHONE_ACCESS_CODE = 100;
    public String IMEI;
    private String MAC = "";
    private int size = 0;
    private int frequency = 0;
    private int linkSpeed = 0;
    private String technology = "";

    private ArrayAdapter adapter;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private List<ScanResult> results;
    WifiManager wifi;*/

    private Button instantButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Go to instant analysis
        instantButton = (Button) findViewById(R.id.buttonInstantMain);
        instantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent analysis1 = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(analysis1);
            }
        });

        // DB Realm
        //realm = Realm.getDefaultInstance();

        //btn = (Button) findViewById(R.id.buttonMain);

        //ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        //final NetworkInfo activeNetwork = cm.getActiveNetworkInfo(); //True if network is available

        /*btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNetwork = cm.getActiveNetworkInfo(); //True if network is available
                if (activeNetwork != null) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                    ///////

                    int permissionCheck1 = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                    int permissionCheck2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);

                    if (permissionCheck2 != PackageManager.PERMISSION_GRANTED && permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                        } else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PHONE_ACCESS_CODE);
                        } else {
                            Toast.makeText(MainActivity.this, "Please, enable the request permission", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:"+ getPackageName()));
                            startActivity(i);
                        }
                    } else {
                        // intent definition
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        //frequency
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        int frequency =  wifiInfo.getFrequency();
                        intent.putExtra("frequency", frequency);
                        // MAC
                        String MAC = wifiInfo.getBSSID();
                        intent.putExtra("MAC", MAC);
                        // IMEI
                        String IMEI = getDeviceId(MainActivity.this); // necesario enviarlo
                        intent.putExtra("IMEI", IMEI);
                        // number of wifis
                        wifiManager.startScan();
                        List<ScanResult> results = wifiManager.getScanResults();
                        size = results.size();
                        intent.putExtra("size", size);
                        // wifi signal
                        linkSpeed = wifiManager.getConnectionInfo().getRssi();
                        intent.putExtra("wifiSignal",linkSpeed);
                        // Wifi o Datos
                        Boolean Wifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
                        Boolean Mobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
                        intent.putExtra("Wifi", Wifi);
                        intent.putExtra("Mobile", Mobile);
                        // Empresa o nombre wifi
                        extra = activeNetwork.getExtraInfo();
                        intent.putExtra("extra", extra);
                        // Latencia
                        latency = getLatency("8.8.8.8");
                        intent.putExtra("latency", latency);
                        // Loss Package
                        loss = getPingResult("8.8.8.8");
                        intent.putExtra("loss", loss);
                        // technology
                        try {
                            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

                            for (final CellInfo info: telephonyManager.getAllCellInfo()){
                                if (info instanceof CellInfoGsm){
                                    final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                                    int cellStrength = gsm.getDbm();
                                    String technology = "GSM";
                                    intent.putExtra("mobileSignal", cellStrength);
                                    intent.putExtra("technology", technology);
                                } else if (info instanceof CellInfoCdma) {
                                    final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                                    int cellStrength = cdma.getDbm();
                                    String technology = "CDMA";
                                    intent.putExtra("mobileSignal", cellStrength);
                                    intent.putExtra("technology", technology);
                                } else if (info instanceof CellInfoLte) {
                                    CellInfoLte lte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                                    CellSignalStrengthLte cellSignalStrengthLte = lte.getCellSignalStrength();
                                    int cellStrength = cellSignalStrengthLte.getDbm();
                                    String technology = "LTE";
                                    intent.putExtra("mobileSignal", cellStrength);
                                    intent.putExtra("technology", technology);
                                } else {
                                    throw new Exception("Unknown type of cell signal");
                                }
                            }

                        } catch (Exception e){
                            Log.e(DEBUG_TAG, "Unable to obtain cell information", e);
                            System.out.println(e);
                        }

                        // start activity

                        startActivity(intent);

                        // create data
                        if (Wifi == true) {
                            createNewWifi(IMEI, MAC, extra, size, frequency, linkSpeed, latency, loss);

                        } else if (Mobile == true){
                            createNewMobile(IMEI, extra, technology, cellStrength, latency, loss);
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Sin conexión a internet", Toast.LENGTH_LONG).show();
                }

            }
        });*/

    }

    /*private void createNewPhone(String IMEI){
        realm.beginTransaction();
        Phone phone = new Phone(IMEI);
        realm.copyToRealm(phone);
        realm.commitTransaction();
    }

    private void createNewWifi(String IMEI, String MAC, String red, int nets, int freq, int rssi, double lat, String pack) {
        realm.beginTransaction();
        WiFi wifi = new WiFi(IMEI);
        wifi.MAC = MAC;
        wifi.red = red;
        wifi.wifiNets = nets;
        wifi.frequency = freq;
        wifi.wifiRSSI = rssi;
        wifi.latency = lat;
        wifi.packages = pack;
        realm.copyToRealm(wifi);
        realm.commitTransaction();
    }

    private void createNewMobile(String IMEI, String red, String tec, int rssi, double lat, String pack){
        realm.beginTransaction();
        Mobile mobile = new Mobile(IMEI);
        mobile.red = red;
        mobile.technology = tec;
        mobile.mobileRSSI = rssi;
        mobile.latency = lat;
        mobile.packages = pack;
        realm.copyToRealm(mobile);
        realm.commitTransaction();
    }
*/
    /*public String getDeviceId(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return  telephonyManager.getDeviceId();
    }



    public static String getPingResult(String a) {
        String str = "";
        String result = "";
        BufferedReader reader = null;
        char[] buffer = new char[4096];
        StringBuffer output = new StringBuffer();

        try {
            Runtime r = Runtime.getRuntime();
            Process process = r.exec("ping -c 4 " + a);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            int i;

            while ((i = reader.read(buffer)) > 0)
                output.append(buffer, 0, i);


            str = output.toString();

            final String[] b = str.split("---");
            final String[] c = b[2].split("rtt");
            final String[] d = c[0].split(",");
            final String[] number = d[2].split("%");
            result = number[0];
            if (d.length ==0 )
                return null;

        } catch (IOException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        finally
        {
            if(reader != null)
            {
                try{reader.close();}catch(IOException ie){}
            }
        }

        return result;
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
            Log.v(DEBUG_TAG, "getLatency: EXCEPTION");
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
    }*/

}