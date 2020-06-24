package com.mbudget0x01.mysmsalert.app.mapintegration;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mbudget0x01.mysmsalert.R;
import com.mbudget0x01.mysmsalert.app.util.SettingsHandler;

import java.io.IOException;
import java.util.List;

public class DisplayAdressActivity extends FragmentActivity implements OnMapReadyCallback {
    private String myLocation;
    private GoogleMap mMap;
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_adress);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent myIntent = getIntent();
        myLocation = myIntent.getStringExtra("address");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMyLocation();
    }

    private void setMyLocation(){
        if(myLocation.isEmpty()){return;}
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try{
            addresses = geocoder.getFromLocationName(myLocation, 1);
        } catch (IOException e) {
            Log.d(TAG, "Could not resolve address", e);
        }
        if (addresses == null) {
            return;
        } else if (addresses.size() == 0) {
            return;
        }
        Address address = addresses.get(0);
        LatLng point = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(point).title("Einsatzort"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( point, 18.0f ));

        if(SettingsHandler.useSatelliteMap(this)){
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);}
    }
}
