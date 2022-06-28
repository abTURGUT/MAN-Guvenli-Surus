package org.tensorflow.lite.examples.detection.LoginScreen;

import static java.lang.Math.round;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.Locale;

public class LoginScreenMain extends AppCompatActivity{

    private static final Logger LOGGER = new Logger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_main);


        Button bireyselBTN = findViewById(R.id.ls_bireyselBTN);
        Button kurumsalBTN = findViewById(R.id.ls_kurumsalBTN);

        bireyselBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreenMain.this, LoginScreenBireysel.class);
                startActivity(intent);
            }
        });

        kurumsalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreenMain.this, LoginScreenKurumsal.class);
                startActivity(intent);
            }
        });

    }



    public void getLocation(){

        FusedLocationProviderClient fusedLocationProviderClient;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){
                    Geocoder geocoder = new Geocoder(LoginScreenMain.this, Locale.getDefault());

                    LOGGER.i("gps takip : " + String.valueOf(location.getLatitude()));
                    LOGGER.i("gps takip : " + String.valueOf(location.getLongitude()));
                    double speed = location.getSpeed();
                    double kmphSpeed = speed*3.6;
                    LOGGER.i("gps takip : " + String.valueOf(kmphSpeed));

                }
                else{LOGGER.i("gps takip null");}
            }
        });


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }


}
