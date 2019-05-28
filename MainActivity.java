package com.zeki.admanager;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity { // Latitude = Enlem  ||||  Longtitude = Boylam
    TextView konumText;
    static Float konum_longtitude=null;
    static Float konum_latitude=null;
    EditText et1,et2,et3;
    static int esik_degeri;
    RadioGroup radioGroup;
    RadioButton radioButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#820d2a")));
        Button button = findViewById(R.id.button1); // gps
        Button button2 = findViewById(R.id.button2); // manuel
        Button button4 = findViewById(R.id.button4); // çıkış yap
        konumText = findViewById(R.id.textView);
        et1=findViewById(R.id.edittext1);
        et2=findViewById(R.id.edittext2);
        et3=findViewById(R.id.edittext3);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et3.getText().toString().equals("")){

                    final LocationManager konumYoneticisi = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    LocationListener konumDinleyicisi = new LocationListener() {

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Toast.makeText( getApplicationContext(),"GPS Açıldı.",Toast.LENGTH_SHORT).show();
                            konumText.setText("Konum Bilgisi Alınıyor...");
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Toast.makeText( getApplicationContext(),"GPS Kapalı.",Toast.LENGTH_SHORT).show();
                            konumText.setText("Lütfen GPS'i açınız.");
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }

                        @Override
                        public void onLocationChanged(Location loc) {
                            konum_latitude=Float.parseFloat(""+loc.getLatitude());
                            konum_longtitude=Float.parseFloat(""+loc.getLongitude());

                            if(!et3.getText().toString().equals("") || Integer.parseInt(et3.getText().toString())>=2147483647){



                                esik_degeri=Integer.parseInt(et3.getText().toString());
                                ArrayList dizi =new ArrayList();
                                dizi.add(konum_latitude.toString());
                                dizi.add(konum_longtitude.toString());
                                dizi.add(""+esik_degeri);

                                Intent intent = new Intent(getApplicationContext(),Filtre.class);
                                intent.putExtra("dizi",dizi);
                                //finish();
                                startActivity(intent);

                            }else{
                                Toast.makeText( getApplicationContext(),"Lütfen değerleri hatasız giriniz.",Toast.LENGTH_SHORT).show();
                                //esik_degeri=100;
                            }
                            konumText.setText("Latitude - Enlem: "+konum_latitude+"\nLongtitude - Boylam: "+konum_longtitude+"\nEşik değeri: "+esik_degeri);
                            konumYoneticisi.removeUpdates(this);

                        }
                    };

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {
                        konumYoneticisi.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, konumDinleyicisi);
                    } else {
                        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                        konumText.setText("Konum Alınamadı.");
                    }
                }else{
                    Toast.makeText( getApplicationContext(),"Eşik değeri giriniz.",Toast.LENGTH_SHORT).show();
                }

            }
        });



    button2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(et1.getText().toString().equals("")|| et2.getText().toString().equals("") || et3.getText().toString().equals("") || Integer.parseInt(et3.getText().toString())>=2147483647){
                Toast.makeText( getApplicationContext(),"Alanlar boş bırakılamaz.",Toast.LENGTH_SHORT).show();
            }else{


                konum_latitude=Float.parseFloat(""+et1.getText().toString());
                konum_longtitude=Float.parseFloat(""+et2.getText().toString());
                esik_degeri=Integer.parseInt(et3.getText().toString());
                konumText.setText("Latitude - Enlem: "+konum_latitude+"\nLongtitude - Boylam: "+konum_longtitude+"\nEşik değeri: "+esik_degeri);
                ArrayList dizi =new ArrayList();
                dizi.add(konum_latitude.toString());
                dizi.add(konum_longtitude.toString());
                dizi.add(""+esik_degeri);

                Intent intent = new Intent(getApplicationContext(),Filtre.class);
                intent.putExtra("dizi",dizi);
                //finish();
                startActivity(intent);

            }

        }
    });

    button4.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            Intent intent = new Intent(getApplicationContext(),Giris.class);
            startActivity(intent);
        }
    });








    }




}



