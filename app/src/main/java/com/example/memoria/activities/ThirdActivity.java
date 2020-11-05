package com.example.memoria.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memoria.R;

public class ThirdActivity extends AppCompatActivity {

    private ImageButton imageButtonPhone;
    private TextView instruction;
    private TextView title;

    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        instruction = (TextView) findViewById(R.id.textViewInstructionThird);
        instruction.setText("Ingrese la cantidad de personas que planean usar WiFi para conectarse a Internet");
        title  = (TextView) findViewById(R.id.textViewTitleThird);
        title.setText("Análisis de Conexión");

    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Estamos en el caso del telefono
        switch (requestCode){
            case PHONE_CALL_CODE:
                String permission = permissions[0];
                int result = grantResults[0];
                if (permission.equals(Manifest.permission.CALL_PHONE)){
                    // Comprobar si ha sido aprobado o denegado por el usuario
                    if (result == PackageManager.PERMISSION_GRANTED){
                        // concedió su permiso
                        String phoneNumber = editTextPhone.getText().toString();
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        startActivity(intentCall);
                    } else {
                        // no concedió su permiso
                        Toast.makeText(ThirdActivity.this, "You declined de access", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }*/

    /*private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }*/
}