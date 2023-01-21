package ch.zli.walkandcount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class CountStepAndLocationPageActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    public final static int SAMPLING_RATE = 1000;

    TextView showCurrentSteps;

    float totalSteps = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_step_and_location_page);

        showCurrentSteps = (TextView) findViewById(R.id.stepCount);
        showCurrentSteps.setText(0);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(listener, sensor, CountStepAndLocationPageActivity.SAMPLING_RATE);

    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            totalSteps = event.values[0];
            showCurrentSteps.setText(Float.toString(totalSteps));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            // NOT NEEDED
        }
    };

}