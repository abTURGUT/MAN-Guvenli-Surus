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
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.Users.UserListAdapter;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KurumsalRecords2Screen extends AppCompatActivity implements FirebaseInterface {
    private static final Logger LOGGER = new Logger();

    SharedPreferencesHelper sharedPreferencesHelper;
    Activity activity;
    FirebaseHelper firebaseHelper;

    TextView kr2s_accountNameTV;
    TextView kr2s_notfoundUserTV;
    ListView kr2s_listLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kurumsal_records_2_screen);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);

        kr2s_accountNameTV = findViewById(R.id.kr2s_accountNameTV);
        kr2s_notfoundUserTV = findViewById(R.id.kr2s_notfoundUserTV);
        kr2s_listLV = findViewById(R.id.kr2s_listLV);

        Bundle extras = getIntent().getExtras();
        String userId = extras.getString("userId");
        String userFirstName = extras.getString("firstName");
        String userLastName = extras.getString("lastName");

        kr2s_accountNameTV.setText("Kayıt Sahibi : " + userFirstName.toUpperCase(Locale.ROOT) + " " + userLastName.toUpperCase(Locale.ROOT));


        firebaseHelper.getBireyselUserRecords(userId, userFirstName, userLastName);

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

    List<Record> tempRecordList = new ArrayList<>();
    @Override
    public void getBireyselUserRecordsResult(List<Record> recordList) {
        if(recordList.size() > 0){

            for(Record record : recordList){
                tempRecordList.add(record);
            }

            RecordListAdapter recordListAdapter = new RecordListAdapter(this, R.layout.record_list_item, tempRecordList);
            kr2s_listLV.setAdapter(recordListAdapter);
        }
        else{
            kr2s_notfoundUserTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getBireyselUserViolentRecordsResult(List<Record> recordList) {

    }

}
