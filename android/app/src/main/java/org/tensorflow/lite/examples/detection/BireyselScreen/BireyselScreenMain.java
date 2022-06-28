package org.tensorflow.lite.examples.detection.BireyselScreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.examples.detection.Database.SharedPreferencesHelper;
import org.tensorflow.lite.examples.detection.KurumsalScreen.KurumsalScreenAddNewUser;
import org.tensorflow.lite.examples.detection.KurumsalScreen.KurumsalScreenMain;
import org.tensorflow.lite.examples.detection.LoginScreen.LoginScreenMain;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BireyselScreenMain extends AppCompatActivity {

    private static final Logger LOGGER = new Logger();

    SharedPreferencesHelper sharedPreferencesHelper;

    TextView bsm_welcomeTextET;
    Button bsm_logoutBTN;
    Button bsm_startCameraBTN;
    Button bsm_myRecordsBTN;
    Button bsm_settingsBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bireysel_screen_main);

        sharedPreferencesHelper = new SharedPreferencesHelper();

        bsm_startCameraBTN = findViewById(R.id.bsm_startCameraBTN);
        bsm_myRecordsBTN = findViewById(R.id.bsm_myRecordsBTN);
        bsm_settingsBTN = findViewById(R.id.bsm_settingsBTN);
        bsm_welcomeTextET = findViewById(R.id.bsm_welcomeTextET);
        bsm_logoutBTN = findViewById(R.id.bsm_logoutBTN);

        bsm_welcomeTextET.setText(StaticObjectClass.myBireyselProfile.getFirstName().toUpperCase(Locale.ROOT) + " " + StaticObjectClass.myBireyselProfile.getLastName().toUpperCase(Locale.ROOT));

        bsm_startCameraBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
            }
        });

        bsm_settingsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BireyselScreenMain.this, BireyselSettingsScreen.class);
                startActivity(intent);
            }
        });

        bsm_myRecordsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BireyselScreenMain.this, BireysellRecords1Screen.class);
                startActivity(intent);
            }
        });


        bsm_logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bireysel bireysel = new Bireysel("-1","","","","","","",false);
                sharedPreferencesHelper.setBireysel(BireyselScreenMain.this, bireysel);
                Intent intent = new Intent(BireyselScreenMain.this, LoginScreenMain.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int PERMISSION_REQUEST = 1;

    public void StartCameraScreen(){
        StaticObjectClass.initializeRecord = new Record();
        StaticObjectClass.violentRecords = new ArrayList<Record>();

        //kayıt tarihini ve süresini ayarlama
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        StaticObjectClass.initializeRecord.setDate(String.valueOf(currentDate));
        StaticObjectClass.initializeRecord.setStartTime(String.valueOf(currentTime));

        Intent intent = new Intent(BireyselScreenMain.this, DetectorActivity.class);
        startActivity(intent);
    }


    public void requestPermissions(){
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), PERMISSION_CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), PERMISSION_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION_CAMERA, PERMISSION_LOCATION}, PERMISSION_REQUEST);
            }
            else{
                StartCameraScreen();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST) {
            if(grantResults.length == 1){
                if(grantResults[0] == 0){
                    StartCameraScreen();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Kamerayı başlatabilmek için lütfen izinleri verin.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                if(grantResults[0] == 0 && grantResults[1] == 0){
                    StartCameraScreen();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Kamerayı başlatabilmek için lütfen izinleri verin.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }



    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
