package com.zeki.admanager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Giris extends AppCompatActivity {

    EditText et1,et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#820d2a")));


        Button giris = findViewById(R.id.button1);
        Button kayit = findViewById(R.id.button2);
        Button sifre = findViewById(R.id.button3);

        et1= findViewById(R.id.edittext1);
        et2 = findViewById(R.id.edittext2);

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(giris_kontrol(et1.getText().toString(),et2.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Giriş Başarısız.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Kayıt Formuna Yönlendiriliyor.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Kayit.class);
                startActivity(intent);
            }
        });

        sifre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Şifre Yenileme Formuna Yönlendiriliyor.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Degistir.class);
                startActivity(intent);
            }
        });

    }



    public boolean giris_kontrol(String email,String passwd){
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpURLConnection connection =null;
        BufferedReader br = null;
        // http://104.248.131.197/api/login?email=asd&password=asdsa
        try{
            URL url = new URL("http://104.248.131.197/api/login?email="+email+"&password="+passwd); // url parametresi
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String satir;
            String dosya="";
            while((satir=br.readLine())!=null){
                dosya += satir;
            }
            if(dosya.equals("\"true\"")){
                return true;
            }else{
                //Toast.makeText(getApplicationContext(), dosya, Toast.LENGTH_LONG).show();
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }



}
