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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class CountStepAndLocationPageActivity extends AppCompatActivity implements SensorEventListener, OnMapReadyCallback, LocationListener {

    private SensorManager sensorManager;

    private static final int PERMISSION_REQUEST_CODE = 200;

    TextView showCurrentSteps;
    Button endActivity;

    int initalSteps;
    int totalStepsMade;
    boolean firstTimeLoading = true;
    boolean running = false;

    List<position> positions = new ArrayList<>();

    GoogleMap mapTest;
    LocationManager lm;
    Location location;
    double longitude = 0;
    double latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_step_and_location_page);

        getSupportActionBar().hide();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_CODE);
        }

        endActivity = (Button) findViewById(R.id.endAct);

        endActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CountStepAndLocationPageActivity.this, resultsActivity.class);
                intent.putExtra("totalStepCount", String.valueOf(totalStepsMade));
                // Parameter übergeben, totale Schritte, geloffene Distanz und eventuell Zeit?
                startActivity(intent);
            }
        });

        showCurrentSteps = (TextView) findViewById(R.id.stepCount);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        running = true;
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        //Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        showCurrentSteps.setText("0");

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "No sensor detected on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running){
            if (firstTimeLoading) {
                initalSteps = (int) event.values[0];
                firstTimeLoading = false;
            }
            totalStepsMade = (int) event.values[0] - initalSteps;
            showCurrentSteps.setText(String.valueOf(event.values[0] - initalSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

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

        mapTest.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));
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