package org.tensorflow.lite.examples.detection.LoginScreen;

import android.app.Activity;
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
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.List;
import java.util.Map;

public class LoginScreenBireyselCode2 extends AppCompatActivity implements FirebaseInterface {

    private static final Logger LOGGER = new Logger();

    Activity activity;
    FirebaseHelper firebaseHelper;

    Button lsbc2_invitorET;
    Button lsbc2_confirmBTN;
    Button lsbc2_errorMessageTXT;
    EditText lsbc2_nameET;
    EditText lsbc2_surnameET;
    EditText lsbc2_phoneET;
    EditText lsbc2_passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_bireysel_code_2);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);

        lsbc2_invitorET = findViewById(R.id.lsbc2_invitorET);
        lsbc2_confirmBTN = findViewById(R.id.lsbc2_confirmBTN);
        lsbc2_errorMessageTXT = findViewById(R.id.lsbc2_errorMessageTXT);
        lsbc2_nameET = findViewById(R.id.lsbc2_nameET);
        lsbc2_surnameET = findViewById(R.id.lsbc2_surnameET);
        lsbc2_phoneET = findViewById(R.id.lsbc2_phoneET);
        lsbc2_passwordET = findViewById(R.id.lsbc2_passwordET);


        Bundle extras = getIntent().getExtras();
        String userId = extras.getString("userId");
        String rootFirstName = extras.getString("rootFirstName");
        String rootLastName = extras.getString("rootLastName");

        lsbc2_invitorET.setText(rootFirstName + " " + rootLastName);

        lsbc2_confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = lsbc2_nameET.getText().toString();
                String surname = lsbc2_surnameET.getText().toString();
                String phone = lsbc2_phoneET.getText().toString();
                String password = lsbc2_passwordET.getText().toString();

                if(name.length() == 0){
                    lsbc2_errorMessageTXT.setText("İsim boş olamaz !");
                    lsbc2_errorMessageTXT.setBackgroundColor(Color.RED);
                    lsbc2_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(surname.length() == 0){
                    lsbc2_errorMessageTXT.setText("Soyisim boş olamaz !");
                    lsbc2_errorMessageTXT.setBackgroundColor(Color.RED);
                    lsbc2_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(phone.length() == 0){
                    lsbc2_errorMessageTXT.setText("Telefon numarası boş olamaz !");
                    lsbc2_errorMessageTXT.setBackgroundColor(Color.RED);
                    lsbc2_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(password.length() == 0){
                    lsbc2_errorMessageTXT.setText("Şifre boş olamaz !");
                    lsbc2_errorMessageTXT.setBackgroundColor(Color.RED);
                    lsbc2_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                firebaseHelper.updatebireyselUser(userId, name, surname, phone, password);


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
        firebaseHelper.deleteUserCode(userId);
        lsbc2_errorMessageTXT.setText("Kaydınız Başarılı.");
        lsbc2_errorMessageTXT.setBackgroundColor(Color.GREEN);
        lsbc2_errorMessageTXT.setVisibility(View.VISIBLE);
        lsbc2_confirmBTN.setVisibility(View.INVISIBLE);
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
