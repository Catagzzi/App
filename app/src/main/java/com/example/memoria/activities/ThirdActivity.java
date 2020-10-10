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
import android.widget.Toast;

import com.example.memoria.R;

public class ThirdActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextWeb;
    private ImageButton imageButtonPhone;
    private ImageButton imageButtonWeb;
    private ImageButton imageButtonCamera;

    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextWeb = (EditText) findViewById(R.id.editTextWeb);
        imageButtonPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        imageButtonWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        imageButtonCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

        imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhone.getText().toString();
                if (phoneNumber != null && !phoneNumber.isEmpty()){
                    // Comprobar si no ha aeptado, ha aceptado o nunca se le ha preguntado
                    //comprobar version actual de android que está corriendo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (CheckPermission(Manifest.permission.CALL_PHONE)){
                            //Ha aceptado
                            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+phoneNumber));
                            startActivity(i);
                        }
                        else {
                            // Ha denegado o es la primera vez que se pregunta
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                //No se le ha preguntado aun
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                            } else {
                                // Ha denegado
                                Toast.makeText(ThirdActivity.this, "Please, enable the request permission", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:"+ getPackageName()));
                                startActivity(i);
                            }
                        }
                    } else {
                        olderVersions(phoneNumber);
                    }

                } else {
                    Toast.makeText(ThirdActivity.this, "Insert a phone number", Toast.LENGTH_LONG).show();
                }
            }

            private void olderVersions(String phoneNumber){
                Intent intentCall =  new Intent( Intent.ACTION_CALL, Uri.parse("tel: "+phoneNumber));
                if (CheckPermission(Manifest.permission.CALL_PHONE)){
                    startActivity(intentCall);
                } else{
                    Toast.makeText(ThirdActivity.this, "You declined de access", Toast.LENGTH_LONG).show();
                }

            }
        });

        // Botón para el navegador web
        imageButtonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editTextWeb.getText().toString();
                String mail = "jhon.maycol.bryan@gmail.com";
                if ( url!=null && !url.isEmpty() ){
                    Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" +url));
                    // Contactos
                    Intent intentContacts = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people"));
                    //Email rápido
                    Intent intentMailTo = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mail));
                    // Telefono 2 sin permisos requeridos
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:976175091"));
                    // Email completo
                    Intent intentMail = new Intent(Intent.ACTION_SEND, Uri.parse(mail));
                    intentMail.setType("plain/text");
                    intentMail.putExtra(Intent.EXTRA_SUBJECT, "Titulo");
                    intentMail.putExtra(Intent.EXTRA_TEXT, "Hola esto es un mail");
                    intentMail.putExtra(Intent.EXTRA_EMAIL, new String[] {"jhon.salgado@ug.uchile.cl", "catalina.gonzalez.i@ug.uchile.cl"});
                    startActivity(Intent.createChooser(intentMail, "Elige tu correo"));

                }
            }
        });

        // Botón Cámara
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamara = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intentCamara);
            }
        });
    }

    @Override
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
    }

    private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}