package ch.zli.walkandcount;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class homePageActivity extends AppCompatActivity {

    Button startTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.item_1);

        startTracking = (Button) findViewById(R.id.startTrackingAct);

        startTracking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, CountStepAndLocationPageActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new testHomeFragement()).commit();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.item_1) {
            // Do nothing
        } else if (itemId == R.id.item_2) {
            Intent i = new Intent(homePageActivity.this, showCurrentLocationActivity.class);
            startActivity(i);
        }
        return true;
    };

}