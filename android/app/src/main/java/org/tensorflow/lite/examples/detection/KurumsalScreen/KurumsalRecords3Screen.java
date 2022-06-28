package org.tensorflow.lite.examples.detection.KurumsalScreen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Database.SharedPreferencesHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.Records.RecordListAdapter;
import org.tensorflow.lite.examples.detection.Records.ViolentRecordListAdapter;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KurumsalRecords3Screen extends AppCompatActivity implements FirebaseInterface{
    private static final Logger LOGGER = new Logger();

    SharedPreferencesHelper sharedPreferencesHelper;
    Activity activity;
    FirebaseHelper firebaseHelper;

    TextView kr3s_accountNameTV;
    TextView kr3s_recordDateTV;
    TextView kr3s_notfoundUserTV;
    ListView kr3s_listLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kurumsal_records_3_screen);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);

        kr3s_accountNameTV = findViewById(R.id.kr3s_accountNameTV);
        kr3s_recordDateTV = findViewById(R.id.kr3s_recordDateTV);
        kr3s_notfoundUserTV = findViewById(R.id.kr3s_notfoundUserTV);
        kr3s_listLV = findViewById(R.id.kr3s_listLV);

        Bundle extras = getIntent().getExtras();
        String userFirstName = extras.getString("userFirstName");
        String userLastName = extras.getString("userLastName");
        String userId = extras.getString("userId");
        String recordId = extras.getString("recordId");
        String recordDate = extras.getString("recordDate");

        kr3s_recordDateTV.setText("Kayıt Tarihi : " + recordDate);
        kr3s_accountNameTV.setText("Kayıt Sahibi : " + userFirstName.toUpperCase(Locale.ROOT) + " " + userLastName.toUpperCase(Locale.ROOT));


        firebaseHelper.getBireyselUserViolentRecords(userId, recordId, userFirstName, userLastName);

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

    }

    List<Record> tempRecordList = new ArrayList<>();
    @Override
    public void getBireyselUserViolentRecordsResult(List<Record> recordList) {
        if(recordList.size() > 0){

            for(Record record : recordList){
                tempRecordList.add(record);
            }

            ViolentRecordListAdapter recordListAdapter = new ViolentRecordListAdapter(this, R.layout.violent_record_list_item, tempRecordList);
            kr3s_listLV.setAdapter(recordListAdapter);
        }
        else{
            kr3s_notfoundUserTV.setVisibility(View.VISIBLE);
        }
    }

}
