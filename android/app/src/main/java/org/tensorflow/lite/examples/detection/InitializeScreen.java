package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.BireyselScreen.BireyselScreenMain;
import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Database.SharedPreferencesHelper;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;
import org.tensorflow.lite.examples.detection.KurumsalScreen.KurumsalScreenMain;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.LoginScreen.LoginScreenMain;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.List;
import java.util.Map;

public class InitializeScreen extends AppCompatActivity implements FirebaseInterface {

    private static final Logger LOGGER = new Logger();
    Activity activity;
    FirebaseHelper firebaseHelper;
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initialize_screen);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);
        sharedPreferencesHelper = new SharedPreferencesHelper();

        checkLogin();

    }

    public void checkLogin(){
        String loginType = sharedPreferencesHelper.getLoginType(this);

        if(loginType.equals("bireysel")){
            Bireysel myBireyselProfile = sharedPreferencesHelper.getBireysel(this);
            if(!myBireyselProfile.getId().equals("-1") && myBireyselProfile.isRememberMe()){
                firebaseHelper.loginBireysel(myBireyselProfile.getMail(), myBireyselProfile.getPassword(), true);
            }
            else{
                Intent intent = new Intent(InitializeScreen.this, LoginScreenMain.class);
                startActivity(intent);
            }
        }
        else if(loginType.equals("kurumsal")){
            Kurumsal myKurumsalProfile = sharedPreferencesHelper.getKurumsal(this);
            if(!myKurumsalProfile.getId().equals("-1") && myKurumsalProfile.isRememberMe()){
                firebaseHelper.loginKurumsal(myKurumsalProfile.getMail(), myKurumsalProfile.getPassword(), true);
            }
            else{
                Intent intent = new Intent(InitializeScreen.this, LoginScreenMain.class);
                startActivity(intent);
            }
        }
        else{
            Intent intent = new Intent(InitializeScreen.this, LoginScreenMain.class);
            startActivity(intent);
        }
    }


    @Override
    public void loginKurumsalResult(Kurumsal result) {
        if(result.getId() == "-1"){
            Kurumsal kurumsal = new Kurumsal("-1","","","","","","",false);
            sharedPreferencesHelper.setKurumsal(this, kurumsal);
            Intent intent = new Intent(this, LoginScreenMain.class);
            startActivity(intent);
        }
        else{
            StaticObjectClass.myKurumsalProfile = result;
            sharedPreferencesHelper.setKurumsal(this, result);
            sharedPreferencesHelper.setLoginType(this,"kurumsal");
            Intent intent = new Intent(this, KurumsalScreenMain.class);
            startActivity(intent);
        }
    }

    @Override
    public void loginBireyselResult(Bireysel result) {
        if(result.getId() == "-1"){
            Bireysel bireysel = new Bireysel("-1","","","","","","",false);
            sharedPreferencesHelper.setBireysel(this, bireysel);
            Intent intent = new Intent(this, LoginScreenMain.class);
            startActivity(intent);
        }
        else{
            StaticObjectClass.myBireyselProfile = result;
            sharedPreferencesHelper.setBireysel(this, result);
            sharedPreferencesHelper.setLoginType(this,"bireysel");
            Intent intent = new Intent(this, BireyselScreenMain.class);
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
