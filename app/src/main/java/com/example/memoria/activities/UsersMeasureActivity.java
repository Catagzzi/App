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
import androidx.core.math.MathUtils;
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
                            Toast.makeText(UsersMeasureActivity.this, "Por favor, habilite los permisos", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:"+ getPackageName()));
                            startActivity(i);
                        }
                    } else {
                        //Obtención de datos
                        loading.setImageResource(R.drawable.waiting);
                        wifiManager.startScan(); //Escaneo del WiFi, requiere el permiso de localizacion
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        int frequency =  wifiInfo.getFrequency(); //frecuencia
                        List<ScanResult> results = wifiManager.getScanResults(); //Resultados
                        //0 es 20 1 es 40 2 es 80 3 es 160 4 es 80x80(80 plus)
                        int size =  getSize(results, frequency);// Cantidad de WiFis existentes
                        ConnectivityManager cm = (ConnectivityManager) UsersMeasureActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                        String MAC = wifiInfo.getBSSID(); //direccion MAC
                        int bandwidth = getBandWidth(results,MAC);
                        int standard = standard(results,MAC);

                        String IMEI = getDeviceId(UsersMeasureActivity.this);


                        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
                        String extra = activeNetwork.getExtraInfo();
                        double latency = getLatency("8.8.8.8");

                        createNewWifi(IMEI, MAC, extra, size, frequency, linkSpeed, latency);

                        //Función que calcula th maximo
                        double maxThroughput = getMaxThroughput(results, bandwidth, frequency, MAC, standard, linkSpeed);


                        //Agregar th maximo a lista
                        if (TextUtils.isEmpty(name)){
                            Toast.makeText(UsersMeasureActivity.this, "Por favor ingrese un nombre", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if (throughputList.size() > position){
                                // SI AGREGAN EN DISTINTO ORDEN ESTO COLAPSA
                                throughputList.set(position, maxThroughput);
                            }else{
                                medir.setText("Actualizar");
                                throughputList.add(maxThroughput);
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

    public int getSize(List<ScanResult> results, int frec ){
        int size =0;
        for (int i=0; i<results.size(); i++ ){
            if (results.get(i).frequency>frec-1000 && results.get(i).frequency<frec+1000 ){
                size = size+1;
            }
        }
        return size;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public double getMaxThroughput(List<ScanResult> results, int bandwidth, int frec, String MAC, int standard, int myRSSI){
        //Lista de porcentajes de las señales que no ocupan mi canal por completo
        List<Double> percentages = new ArrayList<Double>();
        List<Integer> levels = new ArrayList<Integer>();
        int maxRSSI = -121;
        double quantity = 0;
        int band = 0;
        for (int i=0; i<results.size(); i++ ){
            if (!(results.get(i).BSSID).equals(MAC)){
                int fOther = results.get(i).frequency;
                int bdOther = toFrequency(results.get(i));
                double percentage = 0.0;
                if(fOther - (bdOther/2) <= frec-(bandwidth/2) &&  fOther + (bdOther/2) >= frec+(bandwidth/2) ){ //Si la señal cubre el canal por completo
                    if (maxRSSI < results.get(i).level){
                        maxRSSI = results.get(i).level; //que hago con esto
                    }
                } else if ( (frec-(bandwidth/2)) >= (fOther + (bdOther/2))  ||  (frec+(bandwidth/2)) < (fOther - (bdOther/2)) ){
                    continue;
                } else {
                    if (frec+(bandwidth/2)  <= fOther+(bdOther/2)){
                        double p = ( ((( (float) (frec+(bandwidth/2))- (fOther - (bdOther/2)) ))/bandwidth) *100.0);
                        percentage = p;
                    } else if ( frec-(bandwidth/2)  >= fOther-(bdOther/2) ){
                        double p = ( ((( (float) (fOther+(bdOther/2)) - (frec-(bandwidth/2))  ))/bandwidth)  *100.0);
                        percentage = p;
                    } else {
                        double p = (  (( (float) bdOther )/bandwidth)  *100.0);
                        percentage = p;
                    }

                    percentages.add( percentage );
                    levels.add(results.get(i).level);
                    quantity = quantity + percentage;
                }
            } else {
                band = results.get(i).channelWidth;
            }
        }
        if (maxRSSI != -121){
            percentages.add(100.0);
            levels.add(maxRSSI);
            quantity = quantity + 100.0;
        }

        List<Integer> SNRs = getSNR(levels, myRSSI);
        List<Double> carriers = getCarriers(percentages,standard,quantity,band);
        List<Double> throughputs = getThroughput(SNRs,band);
        double throughputFinal = 0.0;
        if (standard == 6){
            throughputFinal = (float) multiplicaSuma(carriers,throughputs)/(256*(band+1));
        } else {
            throughputFinal = (float) multiplicaSuma(carriers,throughputs)/(64*(band+1));
        }


        return throughputFinal;
    }

    public double multiplicaSuma(List<Double> carriers, List<Double> throughputs){
        double suma = 0;
        for (int i=0; i<carriers.size(); i++){
            suma = suma + carriers.get(i)*throughputs.get(i);
        }
        return  suma;
    }

    public  List<Integer> getSNR(List<Integer> levels, int myRSSI){
        List<Integer> SNR = new ArrayList<Integer>();
        for (int i=0; i<levels.size(); i++ ) {
            if (myRSSI > levels.get(i) ){
                SNR.add( myRSSI - levels.get(i) );
            } else {
                SNR.add(0); //corregir
            }
        }
        return  SNR;
    }

    public List<Double> getThroughput(List<Integer> SNRlist, int band){
        List<Double> th = new ArrayList<Double>();
        for (int i=0; i<SNRlist.size(); i++ ) {
            int snr = SNRlist.get(i);
            if (band == 0) {
                if (snr<2){th.add(6.5*snr);}
                else if (2<=snr && snr<5){th.add(4.3333*snr+4.3333);}
                else if (5<=snr && snr<9){th.add(3.25*snr+9.75);}
                else if (9<=snr && snr<15){th.add(6.5*snr-19.5);}
                else if (15<=snr && snr<18){th.add(7.6667*snr-37);}
                else if (18<=snr && snr<20){th.add(6.5*snr-13);}
                else if (20<=snr && snr<25){th.add(2.6*snr+65);}
                else if (25<=snr && snr<31.6154){th.add(6.5*snr-32.5);}
                else{th.add(173.0);}
            } else if (band ==1){
                if (snr<5){th.add(5.4*snr);}
                else if (5<=snr && snr<8){th.add(9.0*snr-18);}
                else if (8<=snr && snr<12){th.add(6.75*snr);}
                else if (12<=snr && snr<18){th.add(13.5*snr-81);}
                else if (18<=snr && snr<21){th.add(18.0*snr-162);}
                else if (21<=snr && snr<23){th.add(13.5*snr-67.5);}
                else if (23<=snr && snr<28){th.add(5.4*snr+118.8);}
                else if (28<=snr && snr<32){th.add(13.5*snr-108);}
                else if (32<=snr && snr<36.2222){th.add(18.0*snr-252);}
                else{th.add(400.0);}
            } else if (band==2){
                if (snr<8){th.add(7.3125*snr);}
                else if (8<=snr && snr<11){th.add(19.5*snr-97.5);}
                else if (11<=snr && snr<15){th.add(14.625*snr-43.875);}
                else if (15<=snr && snr<21){th.add(29.25*snr-263.25);}
                else if (21<=snr && snr<24){th.add(39.0*snr-468);}
                else if (24<=snr && snr<26){th.add(29.25*snr-234);}
                else if (26<=snr && snr<31){th.add(11.7*snr+222.3);}
                else if (31<=snr && snr<35){th.add(29.25*snr-321.75);}
                else if (35<=snr && snr<39.2051){th.add(39.0*snr-663);}
                else{th.add(866.0);}
            } else{
                if (snr<11){th.add(10.6364*snr);}
                else if (11<=snr && snr<14){th.add(39.0*snr-312);}
                else if (14<=snr && snr<18){th.add(29.25*snr-175.5);}
                else if (18<=snr && snr<24){th.add(58.5*snr-702);}
                else if (24<=snr && snr<27){th.add(78.0*snr-1170);}
                else if (27<=snr && snr<29){th.add(58.5*snr-643.5);}
                else if (29<=snr && snr<34){th.add(23.4*snr+374.4);}
                else if (34<=snr && snr<38){th.add(58.5*snr-819);}
                else if (38<=snr && snr<42.1795){th.add(78.0*snr-1560);}
                else{th.add(1730.0);}
            }
        }
        return th;
    }





    public List<Double> getCarriers(List<Double> percentages, int standard, double quantity, int band){
        List<Double> carriers = new ArrayList<Double>();
        for (int i=0; i<percentages.size();i++){
            if (standard == 6){
                carriers.add( (float) (percentages.get(i)*(256*(band+1)))/quantity);
            } else { //64 sub carriers
                carriers.add( (float) (percentages.get(i)*(64*(band+1)))/quantity);
            }
        }
        return carriers;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getBandWidth(List<ScanResult> results, String MAC){
        int bandwidth = 0;
        for (int i=0; i<results.size(); i++ ){
            if(results.get(i).BSSID.equals(MAC) ){
                bandwidth = toFrequency(results.get(i));//results.get(i).channelWidth;
                break;
            }
        }
        return bandwidth;
    }

    public int standard(List<ScanResult> results, String MAC){
        int standard = 0;
        for (int i=0; i<results.size(); i++ ){
            if (results.get(i).BSSID.equals(MAC)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    standard = results.get(i).getWifiStandard(); //802.11ax es 6, si no 0
                }
            }
        }
        return standard;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public int toFrequency(ScanResult scan){
        int bandwidth = 0;
        if(scan.channelWidth==0)  bandwidth= 22;
        else if(scan.channelWidth==1) bandwidth = 44;
        else if(scan.channelWidth==2) bandwidth = 88;
        else if(scan.channelWidth==3) bandwidth = 160;
        else if(scan.channelWidth==4) bandwidth = 160;
        return bandwidth;
    }


    public static UsersMeasureActivity getActivityInstance()
    {
        return INSTANCE;
    }

    public List<Double> getData()
    {
        return this.throughputList;
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
