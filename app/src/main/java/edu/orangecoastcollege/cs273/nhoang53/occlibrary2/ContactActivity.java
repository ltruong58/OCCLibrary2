package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng OCCLibrary = new LatLng(33.672534,-117.907814);
        mGoogleMap.addMarker(new MarkerOptions().title("OCC Library").position(OCCLibrary));
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OCCLibrary, 14.5f));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OCCLibrary, 14.5f), 2000, null); //move in 2sec
    }
}
