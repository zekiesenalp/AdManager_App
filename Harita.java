package com.zeki.admanager;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

import static com.zeki.admanager.Kampanya.firma_lat;
import static com.zeki.admanager.Kampanya.firma_long;
import static com.zeki.admanager.Kampanya.konum_lat;
import static com.zeki.admanager.Kampanya.konum_lon;

public class Harita extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    /*
    Intent intent1 = getIntent();
    ArrayList<String> gonder = getIntent().getStringArrayListExtra("gonder");*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harita);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        LatLng cihaz_yeri = new LatLng(konum_lat,konum_lon);
        mMap.addMarker(new MarkerOptions().position(cihaz_yeri).title("Konumunuz"));

        LatLng firma_yeri = new LatLng(firma_lat,firma_long);
        mMap.addMarker(new MarkerOptions().position(firma_yeri).title("Firmanın Konumu"));

        PolygonOptions polygonOptions = new PolygonOptions()
                .add(new LatLng(konum_lat, konum_lon))
                .add(new LatLng(firma_lat, firma_long));

        Polygon polygon = googleMap.addPolygon(polygonOptions);
        polygon.setStrokeColor(Color.RED);
        polygon.setClickable(true);

        Toast.makeText(getApplicationContext(), "Aradaki mesafe: "+mesafe(firma_lat,firma_long)+" metre", Toast.LENGTH_LONG).show();

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(cihaz_yeri));

        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cihaz_yeri, zoomLevel));

        googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                Toast.makeText(getApplicationContext(), "Aradaki mesafe: "+mesafe(firma_lat,firma_long)+" metre", Toast.LENGTH_LONG).show();

            }
        });

    }


    public Double derece_radyan(Double deg){
        return (deg*Math.PI/180.0);
    }

    public Double mesafe(double fima_lat, double firma_long){
        double R = 6371 * 1000;

        double lat1 = derece_radyan(fima_lat-konum_lat);
        double lon1 = derece_radyan(firma_long-konum_lon);

        double x = Math.sin(lat1/2)*Math.sin(lat1/2) + (Math.cos(derece_radyan(konum_lat))*Math.cos(derece_radyan(firma_lat))) * Math.sin(lon1/2)*Math.sin(lon1/2);
        double y = 2* Math.asin(Math.min(1,Math.sqrt(x)));
        double z = R*y;
        return z;
    }
}
