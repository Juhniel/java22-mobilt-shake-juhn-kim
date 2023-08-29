package com.juhnkim.shake;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView image;
    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private Sensor accelSensor;
    private TextView tv;

    private TextView tv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        tv.setText("Gyroscope \nX: \nY: \nZ:");

        tv2 = findViewById(R.id.textView2);

        // Initialize the SensorManager and the gyroscope sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        image = findViewById(R.id.imageView);
        // Register the listener for the sensor
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Button nextABtn = findViewById(R.id.nextActivityBtn);

        nextABtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Threshold for detecting a shake or rapid rotation.
            // You can calibrate this value as needed.
            final float THRESHOLD = 5;

            // Update the text view with the gyroscope data
            tv.setText("X: " + x + "\nY: " + y + "\nZ: " + z);

            // rotate image
            rotateImage(x, y, z);
            // Check if any of the axes cross the threshold
            if (Math.abs(x) > THRESHOLD || Math.abs(y) > THRESHOLD || Math.abs(z) > THRESHOLD) {
                Log.d("SensorData", "Rapid rotation detected: " +
                        "X: " + x +
                        " Y: " + y +
                        " Z: " + z);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        }  else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float ax = event.values[0];
            float ay = event.values[1];
            float az = event.values[2];

            // You can display these values in your UI here
            // For example, appending it to your TextView:
            tv2.setText("\nAccelerometer:\nAX: " + ax + "\nAY: " + ay + "\nAZ: " + az);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing here
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener to save battery
        sensorManager.unregisterListener(this, gyroSensor);
        sensorManager.unregisterListener(this, accelSensor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-register the sensor listener
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void rotateImage(float x, float y, float z) {
        // Convert radians/second to degrees/second
        float xDegree = (float) Math.toDegrees(x);
        float yDegree = (float) Math.toDegrees(y);
        float zDegree = (float) Math.toDegrees(z);

        // Apply rotation to the ImageView
        image.setRotation(image.getRotation() + xDegree + yDegree + zDegree);
    }
}
