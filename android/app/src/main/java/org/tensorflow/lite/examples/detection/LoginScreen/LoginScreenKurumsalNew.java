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
import org.tensorflow.lite.examples.detection.MailService.MailService;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LoginScreenKurumsalNew extends AppCompatActivity implements FirebaseInterface {

    private static final Logger LOGGER = new Logger();

    Activity activity;
    FirebaseHelper firebaseHelper;
    MailService mailService;

    EditText lskn_nameET;
    EditText lskn_surnameET;
    EditText lskn_mailET;
    EditText lskn_phoneET;
    EditText lskn_foundationET;
    Button lskn_errorMessageTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_kurumsal_new);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);
        mailService = new MailService();

        lskn_nameET = findViewById(R.id.lskn_nameET);
        lskn_surnameET = findViewById(R.id.lskn_surnameET);
        lskn_mailET = findViewById(R.id.lskn_mailET);
        lskn_phoneET = findViewById(R.id.lskn_phoneET);
        lskn_foundationET = findViewById(R.id.lskn_foundationET);

        Button lskn_confirmBTN = findViewById(R.id.lskn_confirmBTN);
        lskn_errorMessageTXT = findViewById(R.id.lskn_errorMessageTXT);

        lskn_confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = lskn_nameET.getText().toString();
                String surname = lskn_surnameET.getText().toString();
                String mail = lskn_mailET.getText().toString();
                String phone = lskn_phoneET.getText().toString();
                String foundation = lskn_foundationET.getText().toString();

                if(name.length() == 0){
                    lskn_errorMessageTXT.setText("İsim boş olamaz !");
                    lskn_errorMessageTXT.setBackgroundColor(Color.RED);
                    lskn_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(surname.length() == 0){
                    lskn_errorMessageTXT.setText("Soyisim boş olamaz !");
                    lskn_errorMessageTXT.setBackgroundColor(Color.RED);
                    lskn_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(mail.length() == 0){
                    lskn_errorMessageTXT.setText("Mail boş olamaz !");
                    lskn_errorMessageTXT.setBackgroundColor(Color.RED);
                    lskn_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(phone.length() == 0){
                    lskn_errorMessageTXT.setText("Telefon numarası boş olamaz !");
                    lskn_errorMessageTXT.setBackgroundColor(Color.RED);
                    lskn_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                if(foundation.length() == 0){
                    lskn_errorMessageTXT.setText("Firma adı boş olamaz !");
                    lskn_errorMessageTXT.setBackgroundColor(Color.RED);
                    lskn_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                //uygun mail formatı
                if(!mail.contains(".com") || !mail.contains("@")){
                    lskn_errorMessageTXT.setText("Mail formatı uygun değil !");
                    lskn_errorMessageTXT.setBackgroundColor(Color.RED);
                    lskn_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                //sorun yoksa mail daha önce sisteme kayıtlı mı ona bak, değilse sisteme kaydet
                firebaseHelper.checkMailExist("kurumsal",mail);


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
        if(mailType.equals("kurumsal")){
            if(!result){
                String name = lskn_nameET.getText().toString();
                String surname = lskn_surnameET.getText().toString();
                String mail = lskn_mailET.getText().toString();
                String phone = lskn_phoneET.getText().toString();
                String foundation = lskn_foundationET.getText().toString();

                String password = generatePassword();

                firebaseHelper.registerKurumsalUser(name, surname, mail, phone, foundation, password);
                mailService.sendKurumsalMail(name, surname, mail, password);
            }
            else{
                lskn_errorMessageTXT.setText("Girilen mail sistemde kayıtlı.");
                lskn_errorMessageTXT.setBackgroundColor(Color.RED);
                lskn_errorMessageTXT.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    public String generatePassword(){
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));

        String password = "";

        for(int i = 0; i<4 ; i++){
            Random rand = new Random();
            int randNumber = rand.nextInt(numbers.size());
            password += numbers.get(randNumber);
        }

        return password;
    }

    @Override
    public void registerKurumsalUser() {
        lskn_errorMessageTXT.setText("Kaydınız alınmıştır.");
        lskn_errorMessageTXT.setBackgroundColor(Color.GREEN);
        lskn_errorMessageTXT.setVisibility(View.VISIBLE);
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
