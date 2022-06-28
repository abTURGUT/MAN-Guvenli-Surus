package org.tensorflow.lite.examples.detection.BireyselScreen;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.List;
import java.util.Map;

public class BireyselSettingsScreen extends AppCompatActivity implements FirebaseInterface {
    private static final Logger LOGGER = new Logger();

    SharedPreferencesHelper sharedPreferencesHelper;
    Activity activity;
    FirebaseHelper firebaseHelper;

    EditText bss_firstNameET;
    EditText bss_lastNameET;
    EditText bss_mailET;
    EditText bss_phoneET;
    EditText bss_rootNameET;
    EditText bss_passwordET;
    TextView bss_errorMessageTXT;
    CheckBox bss_showPasswordCHK;
    Button bss_saveSettingsBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bireysel_settings_screen);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);
        sharedPreferencesHelper = new SharedPreferencesHelper();

        bss_firstNameET = findViewById(R.id.bss_firstNameET);
        bss_lastNameET = findViewById(R.id.bss_lastNameET);
        bss_mailET = findViewById(R.id.bss_mailET);
        bss_phoneET = findViewById(R.id.bss_phoneNoET);
        bss_rootNameET = findViewById(R.id.bss_rootNameET);
        bss_passwordET = findViewById(R.id.bss_passwordET);
        bss_showPasswordCHK = findViewById(R.id.bss_showPasswordCHK);
        bss_errorMessageTXT = findViewById(R.id.bss_errorMessageTXT);
        bss_saveSettingsBTN = findViewById(R.id.bss_saveSettingsBTN);

        firebaseHelper.getKurumsalInfo(StaticObjectClass.myBireyselProfile.getRootId(), 1, "0", StaticObjectClass.myBireyselProfile.getId());

        bss_showPasswordCHK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();

                if(checked){
                    bss_passwordET.setInputType(InputType.TYPE_CLASS_TEXT);
                    bss_passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    bss_passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    bss_passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        bss_saveSettingsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = bss_firstNameET.getText().toString();
                String lastName = bss_lastNameET.getText().toString();
                String mail = bss_mailET.getText().toString();
                String phoneNumber = bss_phoneET.getText().toString();
                String password = bss_passwordET.getText().toString();

                if(firstName.length() == 0){
                    bss_errorMessageTXT.setText("İsim boş olamaz !");
                    bss_errorMessageTXT.setBackgroundColor(Color.RED);
                    bss_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }
                if(lastName.length() == 0){
                    bss_errorMessageTXT.setText("Soyisim boş olamaz !");
                    bss_errorMessageTXT.setBackgroundColor(Color.RED);
                    bss_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }
                if(phoneNumber.length() == 0){
                    bss_errorMessageTXT.setText("Telefon numarası olamaz !");
                    bss_errorMessageTXT.setBackgroundColor(Color.RED);
                    bss_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }
                if(password.length() == 0){
                    bss_errorMessageTXT.setText("Şifre boş olamaz !");
                    bss_errorMessageTXT.setBackgroundColor(Color.RED);
                    bss_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                firebaseHelper.updatebireyselUser(StaticObjectClass.myBireyselProfile.getId(), firstName, lastName, phoneNumber, password);

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
        bss_rootNameET.setText(result.getFirstName() + " " + result.getLastName());
        bss_firstNameET.setText(StaticObjectClass.myBireyselProfile.getFirstName());
        bss_lastNameET.setText(StaticObjectClass.myBireyselProfile.getLastName());
        bss_mailET.setText(StaticObjectClass.myBireyselProfile.getMail());
        bss_phoneET.setText(StaticObjectClass.myBireyselProfile.getPhoneNumber());
        bss_passwordET.setText(StaticObjectClass.myBireyselProfile.getPassword());
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
        String firstName = bss_firstNameET.getText().toString();
        String lastName = bss_lastNameET.getText().toString();
        String mail = bss_mailET.getText().toString();
        String phoneNumber = bss_phoneET.getText().toString();
        String password = bss_passwordET.getText().toString();


        Bireysel bireysel = new Bireysel(StaticObjectClass.myBireyselProfile.getId(), firstName, lastName, mail, password, phoneNumber, StaticObjectClass.myBireyselProfile.getRootId(), true);
        sharedPreferencesHelper.setBireysel(BireyselSettingsScreen.this, bireysel);
        StaticObjectClass.myBireyselProfile = sharedPreferencesHelper.getBireysel(this);

        bss_errorMessageTXT.setText("Bilgiler kaydedildi !");
        bss_errorMessageTXT.setBackgroundColor(Color.GREEN);
        bss_errorMessageTXT.setVisibility(View.VISIBLE);


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
