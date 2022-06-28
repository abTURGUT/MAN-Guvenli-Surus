package org.tensorflow.lite.examples.detection.BireyselScreen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Database.SharedPreferencesHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.Records.RecordListAdapter;
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.Users.UserListAdapter;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BireysellRecords1Screen extends AppCompatActivity implements FirebaseInterface {
    private static final Logger LOGGER = new Logger();

    SharedPreferencesHelper sharedPreferencesHelper;
    Activity activity;
    FirebaseHelper firebaseHelper;

    TextView br1s_notfoundUserTV;
    ListView br1s_listLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bireysel_records_1_screen);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);

        br1s_listLV = findViewById(R.id.br1s_listLV);

        br1s_notfoundUserTV = findViewById(R.id.br1s_notfoundUserTV);

        firebaseHelper.getBireyselUserRecords(StaticObjectClass.myBireyselProfile.getId(), StaticObjectClass.myBireyselProfile.getFirstName(), StaticObjectClass.myBireyselProfile.getLastName());

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


    @Override
    public void getKurumsalSubUsersResult(List<User> subUsers) {

    }

    @Override
    public void getBireyselUserInfo(User user) {


    }

    @Override
    public void getBireyselUserRecordsResult(List<Record> recordList) {
        if(recordList.size() > 0){
            RecordListAdapter recordListAdapter = new RecordListAdapter(this, R.layout.record_list_item, recordList);
            br1s_listLV.setAdapter(recordListAdapter);
        }
        else{
            br1s_notfoundUserTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getBireyselUserViolentRecordsResult(List<Record> recordList) {

    }

}
