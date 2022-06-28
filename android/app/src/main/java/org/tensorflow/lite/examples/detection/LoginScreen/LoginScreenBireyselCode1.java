package org.tensorflow.lite.examples.detection.LoginScreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.Users.User;

import java.util.List;
import java.util.Map;

public class LoginScreenBireyselCode1 extends AppCompatActivity implements FirebaseInterface {

    Activity activity;
    FirebaseHelper firebaseHelper;

    EditText lsbc1_mailET;
    EditText lsbc1_passwordET;
    Button lsbc1_confirmBTN;
    Button lsbc1_errorMessageTXT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_bireysel_code_1);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);

        lsbc1_mailET = findViewById(R.id.lsbc1_mailET);
        lsbc1_passwordET = findViewById(R.id.lsbc1_passwordET);
        lsbc1_confirmBTN = findViewById(R.id.lsbc1_confirmBTN);
        lsbc1_errorMessageTXT = findViewById(R.id.lsbc1_errorMessageTXT);

        lsbc1_confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = lsbc1_mailET.getText().toString();
                String inviteCode = lsbc1_passwordET.getText().toString();

                if(mail.length() == 0){
                    lsbc1_errorMessageTXT.setText("Mail boş olamaz !");
                    lsbc1_errorMessageTXT.setBackgroundColor(Color.RED);
                    lsbc1_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(!mail.contains(".com") || !mail.contains("@")){
                    lsbc1_errorMessageTXT.setText("Mail uygun formatta olmalı !");
                    lsbc1_errorMessageTXT.setBackgroundColor(Color.RED);
                    lsbc1_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(inviteCode.length() == 0){
                    lsbc1_errorMessageTXT.setText("Davet kodu boş olamaz !");
                    lsbc1_errorMessageTXT.setBackgroundColor(Color.RED);
                    lsbc1_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                firebaseHelper.checkUserCode1(inviteCode, mail);
            }
        });


    }

    @Override
    public void loginKurumsalResult(Kurumsal result) {

    }

    @Override
    public void loginBireyselResult(Bireysel result) {

    }

    @Override
    public void getKurumsalInfoResult(Kurumsal result) {

    }

    @Override
    public void checkMailExist(String mailType, boolean result) {

    }

    @Override
    public void registerKurumsalUser() {

    }

    @Override
    public void registerBireyselUser() {

    }

    @Override
    public void updateKurumsalUserResult(String userId) {

    }

    @Override
    public void updatebireyselUserResult(String userId) {

    }

    @Override
    public void checkUserCode(Map<String, Object> record) {
        if(record.get("error").equals("-1")){
            lsbc1_errorMessageTXT.setText("Hatalı mail veya kod !");
            lsbc1_errorMessageTXT.setBackgroundColor(Color.RED);
            lsbc1_errorMessageTXT.setVisibility(View.VISIBLE);
            return;
        }
        else{
            Intent intent = new Intent(LoginScreenBireyselCode1.this, LoginScreenBireyselCode2.class);
            intent.putExtra("userId",record.get("userId").toString());
            //intent.putExtra("userMail",record.get("userMail").toString());
            intent.putExtra("rootFirstName",record.get("rootFirstName").toString());
            intent.putExtra("rootLastName",record.get("rootLastName").toString());
            startActivity(intent);
        }
    }

    @Override
    public void addBireyselCameraRecord(String recordId) {

    }

    @Override
    public void getKurumsalSubUsersResult(List<User> subUsers) {

    }

    @Override
    public void getBireyselUserInfo(User user) {

    }

    @Override
    public void getBireyselUserRecordsResult(List<Record> recordList) {

    }

    @Override
    public void getBireyselUserViolentRecordsResult(List<Record> recordList) {

    }
}
