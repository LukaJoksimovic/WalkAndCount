package ch.zli.walkandcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class showCurrentLocationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    Button startTracking;

    GoogleMap mapTest;
    LocationManager lm;
    Location location;
    double longitude = 0;
    double latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_current_location);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.item_2);

        startTracking = (Button) findViewById(R.id.startTrackingAct);

        startTracking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(showCurrentLocationActivity.this, CountStepAndLocationPageActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        /*
        // TODO: Umbauen, damit nicht letze bekannte stelle genommen wrid, sondern, die jetzige stelle des Benutzers
        // Looks like it is working now with the Network provider, but it will probably only work if it is connnected to mobile Daten
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null){
            longitude = (double) location.getLongitude();
            latitude = (double) location.getLatitude();
        }
        */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.item_1) {
            Intent i = new Intent(showCurrentLocationActivity.this, homePageActivity.class);
            startActivity(i);
        } else if (itemId == R.id.item_2) {
            // Do nothing
        }
        return true;
    };

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapTest = googleMap;
        mapTest.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Marker"));
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
        mapTest.animateCamera(cameraUpdate);
        mapTest.addCircle(new CircleOptions()
                .center(latLng)
                .radius(20)
                .strokeColor(Color.BLACK)
                .fillColor(0x30ff0000)
                .strokeWidth(2)
        );
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        longitude = location.getLongitude();
        latitude = location.getLatitude();
        //Toast.makeText(showCurrentLocationActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
        mapTest.clear();
        mapTest.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Marker"));
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
        mapTest.animateCamera(cameraUpdate);
        mapTest.addCircle(new CircleOptions()
                .center(latLng)
                .radius(20)
                .strokeColor(Color.BLACK)
                .fillColor(0x30ff0000)
                .strokeWidth(2)
        );

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}