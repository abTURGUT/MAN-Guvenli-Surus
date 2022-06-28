package org.tensorflow.lite.examples.detection.KurumsalScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Database.SharedPreferencesHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.LoginScreen.LoginScreenMain;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.Records.RecordListAdapter;
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.Users.UserListAdapter;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KurumsalRecords1Screen extends AppCompatActivity implements FirebaseInterface {
    private static final Logger LOGGER = new Logger();

    SharedPreferencesHelper sharedPreferencesHelper;
    Activity activity;
    FirebaseHelper firebaseHelper;

    TextView kr1s_notfoundUserTV;
    ListView kr1s_listLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kurumsal_records_1_screen);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);

        kr1s_listLV = findViewById(R.id.kr1s_listLV);

        kr1s_notfoundUserTV = findViewById(R.id.kr1s_notfoundUserTV);

        firebaseHelper.getKurumsalSubUsers(StaticObjectClass.myKurumsalProfile.getId());

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

    }

    @Override
    public void addBireyselCameraRecord(String recordId) {

    }
    int subUserCount;
    List<User> tempSubUsers;
    @Override
    public void getKurumsalSubUsersResult(List<User> subUsers) {
        subUserCount = subUsers.size();
        tempSubUsers = new ArrayList<>();

        if(subUsers.size() > 0){
            for(User user : subUsers){
                firebaseHelper.getBireyselUserInfo(user.getId());
            }
        }
        else{
            kr1s_notfoundUserTV.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void getBireyselUserInfo(User user) {
        tempSubUsers.add(user);
        decreaseSubUserCount();
    }

    @Override
    public void getBireyselUserRecordsResult(List<Record> recordList) {

    }

    @Override
    public void getBireyselUserViolentRecordsResult(List<Record> recordList) {

    }

    boolean decreaseSubUserCountFlag = false;
    public void decreaseSubUserCount(){
        while(decreaseSubUserCountFlag);

        decreaseSubUserCountFlag = true;
        subUserCount--;

        if(subUserCount == 0){
            UserListAdapter userListAdapter = new UserListAdapter(this, R.layout.user_list_item, tempSubUsers);
            kr1s_listLV.setAdapter(userListAdapter);
        }

        decreaseSubUserCountFlag = false;
    }
}
