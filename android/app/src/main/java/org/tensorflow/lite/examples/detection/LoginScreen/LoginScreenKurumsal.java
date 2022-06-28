package org.tensorflow.lite.examples.detection.LoginScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Database.SharedPreferencesHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;
import org.tensorflow.lite.examples.detection.KurumsalScreen.KurumsalScreenMain;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.List;
import java.util.Map;

public class LoginScreenKurumsal extends AppCompatActivity implements FirebaseInterface {

    private static final Logger LOGGER = new Logger();

    Activity activity;
    FirebaseHelper firebaseHelper;
    SharedPreferencesHelper sharedPreferencesHelper;
    Button lgk_errorMessageTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_kurumsal);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);
        sharedPreferencesHelper = new SharedPreferencesHelper();

        Button lsk_loginBTN = findViewById(R.id.lsk_loginBTN);
        Button lsk_newAccountBTN = findViewById(R.id.lsk_newAccountBTN);
        lgk_errorMessageTXT = findViewById(R.id.lsk_errorMessageTXT);
        EditText lsk_maillET = findViewById(R.id.lsk_maillET);
        EditText lsk_passwordET = findViewById(R.id.lsk_passwordET);
        CheckBox lsk_rememberMeCHECK = findViewById(R.id.lsk_rememberMeCHECK);


        lsk_loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String mail = lsk_maillET.getText().toString();
                    String password = lsk_passwordET.getText().toString();
                    Boolean rememberMe = lsk_rememberMeCHECK.isChecked();

                    if(mail.length() == 0){
                        lgk_errorMessageTXT.setText("Mail boş olamaz.");
                        lgk_errorMessageTXT.setVisibility(View.VISIBLE);
                        return;
                    }

                    if(password.length() == 0){
                        lgk_errorMessageTXT.setText("Şifre boş olamaz.");
                        lgk_errorMessageTXT.setVisibility(View.VISIBLE);
                        return;
                    }

                    firebaseHelper.loginKurumsal(mail, password, rememberMe);

                }
                catch (Exception exception){
                    lgk_errorMessageTXT.setText("Kullanıcı Adı veya Şifre yanlış !");
                    lgk_errorMessageTXT.setVisibility(View.VISIBLE);
                }
            }
        });

        lsk_newAccountBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreenKurumsal.this, LoginScreenKurumsalNew.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void loginKurumsalResult(Kurumsal result) {
        if(result.getId() == "-1"){
            lgk_errorMessageTXT.setText("Kullanıcı adı veya şifre yanlış !");
            lgk_errorMessageTXT.setVisibility(View.VISIBLE);
        }
        else{
            StaticObjectClass.myKurumsalProfile = result;
            sharedPreferencesHelper.setKurumsal(LoginScreenKurumsal.this, result);
            sharedPreferencesHelper.setLoginType(LoginScreenKurumsal.this,"kurumsal");
            Intent intent = new Intent(LoginScreenKurumsal.this, KurumsalScreenMain.class);
            startActivity(intent);
        }
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
        LOGGER.i("KAYIT BAŞARILI");
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
