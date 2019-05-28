package com.zeki.admanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Filtre extends AppCompatActivity {
    TextView tv1,tv2;
    EditText et1,et2;
    static int flag=0;
    static String firma_adi="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#820d2a")));
        flag=0;
        String mesaj="";
        tv1=findViewById(R.id.textView1);
        tv2=findViewById(R.id.textView2);
        et1=findViewById(R.id.edittext1);
        et2=findViewById(R.id.edittext2);
        Button button = findViewById(R.id.button1);

        Intent intent = getIntent();
        ArrayList<String> dizi = getIntent().getStringArrayListExtra("dizi");


        tv1.setText("Koordinatlarınız:\nEnlem: "+dizi.get(0)+" Boylam: "+dizi.get(1)+"\nEşik Değeri (m): "+dizi.get(2));
        kategori_sayısı(); //sayıyı bulmak için

        for (int i = 1; i <=flag ; i++) {
            mesaj+="ID:"+i+" "+kategori_ismi(i)+"\n";
        }

        tv2.setText(mesaj);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et1.getText().toString().equals("") ||Integer.parseInt(et1.getText().toString())> flag || Integer.parseInt(et1.getText().toString()) <1 ){
                    Toast.makeText(getApplicationContext(), "Geçersiz ID Girişi", Toast.LENGTH_LONG).show();
                }else{
                    firma_adi=et2.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),Kampanya.class);
                    int kategori = Integer.parseInt(et1.getText().toString());
                    intent.putExtra("kategori",kategori);
                    startActivity(intent);
                }
            }
        });





    }

    public ArrayList<String> kategori_sayısı(){
        HttpURLConnection connection =null;
        BufferedReader br = null;
        ArrayList dizi = new ArrayList();

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        for (int i = 1; i <20 ; i++) {

            try{
                String link ="http://104.248.131.197/api/get_catname/"+i;
                URL url = new URL(link);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String satir;
                String dosya="";
                while((satir=br.readLine())!=null){
                    dosya += satir;
                }
                if(!dosya.equals("null")){
                    dizi.add(dosya);
                    flag++;
                }else{
                    return dizi;
                }

            }catch(Exception e){
                e.printStackTrace();
                ArrayList hata =new ArrayList();
                hata.add("hata");
                return hata;
            }
        }


        return dizi;

    }

    public String kategori_ismi(int sayi){
        HttpURLConnection connection =null;
        BufferedReader br = null;
        ArrayList dizi = new ArrayList();

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        try{
            String link ="http://104.248.131.197/api/get_catname/"+sayi;
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String satir;
            String dosya="";
            while((satir=br.readLine())!=null){
                dosya += satir;
            }
            String yeni_dosya = dosya.substring(1,dosya.length()-1);

            return yeni_dosya;

        }catch(Exception e){
            e.printStackTrace();
            return "hata";
        }





    }





}
