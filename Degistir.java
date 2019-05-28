package com.zeki.admanager;

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

public class Degistir extends AppCompatActivity {
    EditText et1,et2,et3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degistir);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#820d2a")));

        Button button = findViewById(R.id.button1);
        et1=findViewById(R.id.edittext1); // email
        et2=findViewById(R.id.edittext3); // passwd
        et3=findViewById(R.id.edittext2); // uname

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(degistirme_kontrol(et1.getText().toString(),et2.getText().toString(),et3.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Güncelleme işlemi başarılı.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),Giris.class);
                    finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Güncelleme işlemi başarısız.", Toast.LENGTH_LONG).show();

                }
            }
        });


    }


    public boolean degistirme_kontrol(String email,String passwd, String uname){
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpURLConnection connection =null;
        BufferedReader br = null;

        // http://10.40.192.34/api/change_pw?email=zekiesenalp@hotmail.com&password=zeki&name=zeki

        if(email.equals("")){
            return false;
        }

        if(passwd.equals("")){
            return false;
        }

        if(uname.equals("")){
            return false;
        }

        try{
            URL url = new URL("http://104.248.131.197/api/change_pw?email="+email+"&password="+passwd+"&name="+uname);
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
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }



}