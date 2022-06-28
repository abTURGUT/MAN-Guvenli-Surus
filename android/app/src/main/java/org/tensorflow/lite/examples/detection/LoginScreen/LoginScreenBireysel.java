package org.tensorflow.lite.examples.detection.LoginScreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.BireyselScreen.BireyselScreenMain;
import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Database.SharedPreferencesHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.List;
import java.util.Map;

public class LoginScreenBireysel extends AppCompatActivity implements FirebaseInterface {

    private static final Logger LOGGER = new Logger();

    Activity activity;
    FirebaseHelper firebaseHelper;
    SharedPreferencesHelper sharedPreferencesHelper;

    EditText lsb_mailET;
    EditText lsb_passwordET;
    CheckBox lsb_rememberMeCHECK;

    Button lsb_confirmBTN;
    Button lsb_useCodeBTN;
    Button lsb_errorMessageTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_bireysel);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);
        sharedPreferencesHelper = new SharedPreferencesHelper();

        lsb_mailET = findViewById(R.id.lsb_mailET);
        lsb_passwordET = findViewById(R.id.lsb_passwordET);
        lsb_rememberMeCHECK = findViewById(R.id.lsb_rememberMeCHECK);
        lsb_confirmBTN = findViewById(R.id.lsb_confirmBTN);
        lsb_useCodeBTN = findViewById(R.id.lsb_useCodeBTN);
        lsb_errorMessageTXT = findViewById(R.id.lsb_errorMessageTXT);

        lsb_confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = lsb_mailET.getText().toString();
                String password = lsb_passwordET.getText().toString();
                Boolean rememberMe = lsb_rememberMeCHECK.isChecked();

                if(mail.length() == 0){
                    lsb_errorMessageTXT.setText("Mail boş olamaz !");
                    lsb_errorMessageTXT.setBackgroundColor(Color.RED);
                    lsb_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(password.length() == 0){
                    lsb_errorMessageTXT.setText("Şifre boş olamaz !");
                    lsb_errorMessageTXT.setBackgroundColor(Color.RED);
                    lsb_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                firebaseHelper.loginBireysel(mail, password, rememberMe);
            }
        });

        lsb_useCodeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreenBireysel.this, LoginScreenBireyselCode1.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void loginKurumsalResult(Kurumsal result) {

    }

    @Override
    public void loginBireyselResult(Bireysel result) {
        if(result.getId() == "-1"){
            lsb_errorMessageTXT.setText("Kullanıcı adı veya şifre yanlış !");
            lsb_errorMessageTXT.setVisibility(View.VISIBLE);
        }
        else{
            StaticObjectClass.myBireyselProfile = result;
            sharedPreferencesHelper.setBireysel(LoginScreenBireysel.this, result);
            sharedPreferencesHelper.setLoginType(LoginScreenBireysel.this,"bireysel");
            Intent intent = new Intent(LoginScreenBireysel.this, BireyselScreenMain.class);
            startActivity(intent);
        }
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
