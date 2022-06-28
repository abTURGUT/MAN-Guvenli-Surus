package org.tensorflow.lite.examples.detection.KurumsalScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.BireyselScreen.BireyselScreenMain;
import org.tensorflow.lite.examples.detection.BireyselScreen.BireyselSettingsScreen;
import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Database.SharedPreferencesHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.LoginScreen.LoginScreenMain;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.StaticObjectClass;

import java.util.Locale;
import java.util.Map;

public class KurumsalScreenMain extends AppCompatActivity {

    SharedPreferencesHelper sharedPreferencesHelper;

    TextView ksm_welcomeTextET;
    Button ksm_addNewUserBTN;
    Button ksm_myUsersBTN;
    Button ksm_settingsBTN;
    Button ksm_logoutBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kurumsal_screen_main);

        sharedPreferencesHelper = new SharedPreferencesHelper();

        ksm_welcomeTextET = findViewById(R.id.ksm_welcomeTextET);
        ksm_addNewUserBTN = findViewById(R.id.ksm_addNewUserBTN);
        ksm_myUsersBTN = findViewById(R.id.ksm_myUsersBTN);
        ksm_settingsBTN = findViewById(R.id.ksm_settingsBTN);
        ksm_logoutBTN = findViewById(R.id.ksm_logoutBTN);

        ksm_welcomeTextET.setText(StaticObjectClass.myKurumsalProfile.getFirstName().toUpperCase(Locale.ROOT) + " " + StaticObjectClass.myKurumsalProfile.getLastName().toUpperCase(Locale.ROOT));

        ksm_addNewUserBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KurumsalScreenMain.this, KurumsalScreenAddNewUser.class);
                startActivity(intent);
            }
        });

        ksm_myUsersBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KurumsalScreenMain.this, KurumsalRecords1Screen.class);
                startActivity(intent);
            }
        });

        ksm_settingsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KurumsalScreenMain.this, KurumsalSettingsScreen.class);
                startActivity(intent);
            }
        });

        ksm_logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kurumsal kurumsal = new Kurumsal("-1","","","","","","",false);
                sharedPreferencesHelper.setKurumsal(KurumsalScreenMain.this, kurumsal);
                Intent intent = new Intent(KurumsalScreenMain.this, LoginScreenMain.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
