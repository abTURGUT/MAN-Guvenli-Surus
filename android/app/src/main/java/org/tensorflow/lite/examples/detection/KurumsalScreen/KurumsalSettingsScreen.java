package org.tensorflow.lite.examples.detection.KurumsalScreen;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Database.SharedPreferencesHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.List;
import java.util.Map;

public class KurumsalSettingsScreen extends AppCompatActivity implements FirebaseInterface {
    private static final Logger LOGGER = new Logger();

    SharedPreferencesHelper sharedPreferencesHelper;
    Activity activity;
    FirebaseHelper firebaseHelper;

    EditText kss_firstNameET;
    EditText kss_lastNameET;
    EditText kss_mailET;
    EditText kss_phoneET;
    EditText kss_companyNameET;
    EditText kss_passwordET;
    TextView kss_errorMessageTXT;
    CheckBox kss_showPasswordCHK;
    Button kss_saveSettingsBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kurumsal_settings_screen);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);
        sharedPreferencesHelper = new SharedPreferencesHelper();

        kss_firstNameET = findViewById(R.id.kss_firstNameET);
        kss_lastNameET = findViewById(R.id.kss_lastNameET);
        kss_mailET = findViewById(R.id.kss_mailET);
        kss_phoneET = findViewById(R.id.kss_phoneNoET);
        kss_companyNameET = findViewById(R.id.kss_companyNameET);
        kss_passwordET = findViewById(R.id.kss_passwordET);
        kss_showPasswordCHK = findViewById(R.id.kss_showPasswordCHK);
        kss_errorMessageTXT = findViewById(R.id.kss_errorMessageTXT);
        kss_saveSettingsBTN = findViewById(R.id.kss_saveSettingsBTN);

        kss_showPasswordCHK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();

                if(checked){
                    kss_passwordET.setInputType(InputType.TYPE_CLASS_TEXT);
                    kss_passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    kss_passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    kss_passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        kss_saveSettingsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = kss_firstNameET.getText().toString();
                String lastName = kss_lastNameET.getText().toString();
                String mail = kss_mailET.getText().toString();
                String companyName = kss_companyNameET.getText().toString();
                String phoneNumber = kss_phoneET.getText().toString();
                String password = kss_passwordET.getText().toString();

                if(firstName.length() == 0){
                    kss_errorMessageTXT.setText("İsim boş olamaz !");
                    kss_errorMessageTXT.setBackgroundColor(Color.RED);
                    kss_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }
                if(lastName.length() == 0){
                    kss_errorMessageTXT.setText("Soyisim boş olamaz !");
                    kss_errorMessageTXT.setBackgroundColor(Color.RED);
                    kss_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }
                if(companyName.length() == 0){
                    kss_errorMessageTXT.setText("Şirket adı boş olamaz !");
                    kss_errorMessageTXT.setBackgroundColor(Color.RED);
                    kss_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }
                if(phoneNumber.length() == 0){
                    kss_errorMessageTXT.setText("Telefon numarası olamaz !");
                    kss_errorMessageTXT.setBackgroundColor(Color.RED);
                    kss_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }
                if(password.length() == 0){
                    kss_errorMessageTXT.setText("Şifre boş olamaz !");
                    kss_errorMessageTXT.setBackgroundColor(Color.RED);
                    kss_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                firebaseHelper.updateKurumsalUser(StaticObjectClass.myKurumsalProfile.getId(), firstName, lastName, phoneNumber, companyName, password);

            }
        });

        kss_firstNameET.setText(StaticObjectClass.myKurumsalProfile.getFirstName());
        kss_lastNameET.setText(StaticObjectClass.myKurumsalProfile.getLastName());
        kss_mailET.setText(StaticObjectClass.myKurumsalProfile.getMail());
        kss_phoneET.setText(StaticObjectClass.myKurumsalProfile.getPhoneNumber());
        kss_companyNameET.setText(StaticObjectClass.myKurumsalProfile.getCompanyName());
        kss_passwordET.setText(StaticObjectClass.myKurumsalProfile.getPassword());
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
        String firstName = kss_firstNameET.getText().toString();
        String lastName = kss_lastNameET.getText().toString();
        String mail = kss_mailET.getText().toString();
        String companyName = kss_companyNameET.getText().toString();
        String phoneNumber = kss_phoneET.getText().toString();
        String password = kss_passwordET.getText().toString();


        Kurumsal kurumsal = new Kurumsal(StaticObjectClass.myKurumsalProfile.getId(), firstName, lastName, mail, password, phoneNumber, companyName, true);
        sharedPreferencesHelper.setKurumsal(KurumsalSettingsScreen.this, kurumsal);
        StaticObjectClass.myKurumsalProfile = sharedPreferencesHelper.getKurumsal(this);

        kss_errorMessageTXT.setText("Bilgiler kaydedildi !");
        kss_errorMessageTXT.setBackgroundColor(Color.GREEN);
        kss_errorMessageTXT.setVisibility(View.VISIBLE);


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
