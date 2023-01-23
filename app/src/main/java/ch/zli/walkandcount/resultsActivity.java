package ch.zli.walkandcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class resultsActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView displayTotalSteps;
    TextView displayWalkedDistance;
    Button backToHomeMenu;

    List<LatLng> positions = new ArrayList<>();

    GoogleMap mapTest;
    LocationManager lm;
    Location location;
    double longitude = 0;
    double latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        int totalSteps = Integer.parseInt(intent.getStringExtra("totalStepCount"));

        listHolder ListHolder = listHolder.getInstance();
        positions.addAll(ListHolder.positions);

        displayTotalSteps = (TextView) findViewById(R.id.totalCountSteps);

        displayTotalSteps.setText(String.valueOf(totalSteps));

        backToHomeMenu = (Button) findViewById(R.id.backtoHome);

        backToHomeMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(resultsActivity.this, homePageActivity.class);
                // Delete information of intents and singleton
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapTest = googleMap;
        mapTest.addMarker(new MarkerOptions()
                .position(positions.get(0))
                .title("Start"));
        mapTest.addPolyline(new PolylineOptions()
                .addAll(positions)
                .color(0xFF3d85c6));
        mapTest.addMarker(new MarkerOptions()
                .position(positions.get(positions.size() - 1))
                .title("Ende"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(positions.get(0), 20);
        mapTest.animateCamera(cameraUpdate);

    }

}