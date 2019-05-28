package com.zeki.admanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.zeki.admanager.Filtre.firma_adi;
import static com.zeki.admanager.MainActivity.esik_degeri;
import static com.zeki.admanager.MainActivity.konum_latitude;
import static com.zeki.admanager.MainActivity.konum_longtitude;

public class Kampanya extends AppCompatActivity {

    TextView tv1,tv2;
    static Double konum_lat = Double.parseDouble(konum_latitude.toString());
    static Double konum_lon = Double.parseDouble(konum_longtitude.toString());
    int ilan_sayisi=0;
    int firma_metod_sayisi = firma_sayisi();
    double aradaki_mesafe;
    static Double firma_lat,firma_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kampanya);
        //tv1=findViewById(R.id.textView1);
        tv2=findViewById(R.id.textView2);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#820d2a")));
        //ilan_sayisi=0; // intentler arası bozulmayı düzeltmek için

        konum_lat = Double.parseDouble(konum_latitude.toString());
        konum_lon = Double.parseDouble(konum_longtitude.toString());


        Intent intent = getIntent();
        int kategori = getIntent().getExtras().getInt("kategori");

        int uyan_ilan_sayisi = en_yakin_kampanya(kategori).size();
        if(uyan_ilan_sayisi==0){
            Toast.makeText(getApplicationContext(), "Uygun kampanya bulunamadı.", Toast.LENGTH_LONG).show();
        }else{
            ArrayList firma = tek_kampanya(Integer.parseInt(""+en_yakin_kampanya(kategori).get(0)));
            //tv1.setText("Uygun Kampanya\n\n"+"Mesafeniz: "+mesafe(Double.parseDouble(""+firma.get(1)),Double.parseDouble(""+firma.get(2)))+" m"+"\nFirma: "+firma.get(0)+"\nKategori: "+firma.get(3)+"\nİçerik: "+firma.get(4)+"\nBitiş Tarihi: "+firma.get(5));
            firma_lat=Double.parseDouble(""+firma.get(1));
            firma_long=Double.parseDouble(""+firma.get(2));

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID","YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                mNotificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                    .setSmallIcon(R.drawable.bg_gradient) // icon
                    .setContentTitle(""+firma.get(0)) // title
                    .setContentText("Aranızdaki mesafe: "+mesafe(firma_lat,firma_long)+" m")// message
                    .setAutoCancel(true);

            Intent intent1 = new Intent(getApplicationContext(),Harita.class);

            /*ArrayList<String> gonder = null;
            gonder.add(""+konum_lat);
            gonder.add(""+konum_lon);*/
            //intent1.putExtra("gonder",gonder);
            PendingIntent pi = PendingIntent.getActivity( getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);
            mNotificationManager.notify(0, mBuilder.build());
        }


        //tv1.setText("Uygun Kampanya\n\n"+"Mesafeniz: "+mesafe(Double.parseDouble(""+firma.get(1)),Double.parseDouble(""+firma.get(2)))+" m"+"\nFirma: "+firma.get(0)+"\nKategori: "+firma.get(3)+"\nİçerik: "+firma.get(4)+"\nBitiş Tarihi: "+firma.get(5));


        String metin="Tüm Uygun Kampanyalar:\n\n";

        if(uyan_ilan_sayisi>=1){
            ArrayList uyanlar = en_yakin_kampanya(kategori);

            for (int i = 0; i <= uyan_ilan_sayisi-1 ; i++) {
                ArrayList firma = tek_kampanya(Integer.parseInt(""+uyanlar.get(i)));

                metin+=firma.get(0)+": "+firma.get(4)+"\n";
                metin+="Bitiş tarihi: "+firma.get(5);
                metin += "\nMesafeniz: "+mesafe(Double.parseDouble(""+firma.get(1)),Double.parseDouble(""+firma.get(2)))+" m\n\n";
            }

        }

        tv2.setText(metin);





    }


    public Double derece_radyan(Double deg){
        return (deg*Math.PI/180.0);
    }

    public Double mesafe(double lat2, double lon2){
        double R = 6371 * 1000;

        double lat1 = derece_radyan(lat2-konum_lat);
        double lon1 = derece_radyan(lon2-konum_lon);

        double x = Math.sin(lat1/2)*Math.sin(lat1/2) + (Math.cos(derece_radyan(konum_lat))*Math.cos(derece_radyan(lat2))) * Math.sin(lon1/2)*Math.sin(lon1/2);
        double y = 2* Math.asin(Math.min(1,Math.sqrt(x)));
        double z = R*y;
        return z;
    }

    public ArrayList<String> tek_kampanya(int k){
        HttpURLConnection connection =null;
        BufferedReader br = null;
        String link = "http://104.248.131.197/api/firma/"+k;

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try{
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

            JSONObject jobj = new JSONObject(yeni_dosya);
            ArrayList dizi =new ArrayList();

            dizi.add(jobj.getString("firma_ad"));                                       // 0 firma
            dizi.add(jobj.getString("lat"));                                            // 1 lat
            dizi.add(jobj.getString("lng"));                                            // 2 long
            dizi.add(jobj.getString("category"));                                       // 3 category
            dizi.add(jobj.getString("icerik"));                                         // 4 icerik
            dizi.add(jobj.getString("bitis"));                                          // 5 bitiş

            return dizi;

        }catch(Exception e){
            e.printStackTrace();
            ArrayList hata =new ArrayList();
            hata.add("hata");
            return hata;
        }


    }

    public int firma_sayisi(){

        HttpURLConnection connection =null;
        BufferedReader br = null;
        String link = "http://104.248.131.197/api/firma/";

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        for (int i = 1; i <100 ; i++) {

            try{
                URL url = new URL(link+i);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String satir;
                String dosya="";
                while((satir=br.readLine())!=null){
                    dosya += satir;
                }
                if(!dosya.equals("[]")){
                    ilan_sayisi++;
                }else{
                    return ilan_sayisi;
                }

            }catch(Exception e){
                e.printStackTrace();
                return ilan_sayisi;
            }


        }
        return ilan_sayisi;
    }





    public ArrayList en_yakin_kampanya(int kategori){

        ArrayList<Integer> dizi = new ArrayList<Integer>();

        double flag_mesafe=9000000000000000000000.0;
        double lat1,lng1;
        int flag_id=0; // hangisi en yakınsa
        double esik = Double.parseDouble(""+esik_degeri+".0");

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(firma_adi.equals("")){

            for (int i = 1; i <= firma_metod_sayisi ; i++) {

                try{
                    if(tek_kampanya(i).get(3).equals(""+kategori)){
                        lat1=Double.parseDouble(tek_kampanya(i).get(1));
                        lng1=Double.parseDouble(tek_kampanya(i).get(2));

                        if(mesafe(lat1,lng1) <= esik /*&& mesafe(lat1,lng1) < flag_mesafe*/ ){
                            flag_mesafe = mesafe(lat1,lng1);
                            //flag_id = i;
                            aradaki_mesafe=flag_mesafe;
                            dizi.add(i);
                        }

                    }


                }catch (Exception e){
                    e.printStackTrace();
                    //return flag_id;
                }

            }


        }else{

            for (int i = 1; i <= firma_metod_sayisi ; i++) {

                try{
                    if(tek_kampanya(i).get(3).equals(""+kategori) && tek_kampanya(i).get(0).toLowerCase().contains(firma_adi.toLowerCase())){
                        lat1=Double.parseDouble(tek_kampanya(i).get(1));
                        lng1=Double.parseDouble(tek_kampanya(i).get(2));

                        if(mesafe(lat1,lng1) <= esik /*&& mesafe(lat1,lng1) < flag_mesafe*/ ){
                            flag_mesafe = mesafe(lat1,lng1);
                            //flag_id = i;
                            aradaki_mesafe=flag_mesafe;
                            dizi.add(i);
                        }

                    }


                }catch (Exception e){
                    e.printStackTrace();
                    //return flag_id;
                }

            }


        }

        return dizi;


    }



}
