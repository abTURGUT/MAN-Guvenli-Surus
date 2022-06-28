package org.tensorflow.lite.examples.detection.KurumsalScreen;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.MailService.MailService;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.Users.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KurumsalScreenAddNewUser extends AppCompatActivity implements FirebaseInterface {

    MailService mailService;
    Activity activity;
    FirebaseHelper firebaseHelper;

    EditText ksan_mailET;
    Button ksan_addNewUserBTN;
    TextView ksan_errorMessageTXT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kurumsal_screen_add_new_user);

        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);
        mailService = new MailService();

        ksan_mailET = findViewById(R.id.ksan_mailET);
        ksan_addNewUserBTN = findViewById(R.id.ksan_addNewUserBTN);
        ksan_errorMessageTXT = findViewById(R.id.ksan_errorMessageTXT);

        ksan_addNewUserBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ksan_errorMessageTXT.setVisibility(View.INVISIBLE);

                String mail = ksan_mailET.getText().toString();

                if(mail.length() == 0){
                    ksan_errorMessageTXT.setText("Mail boş olamaz !");
                    ksan_errorMessageTXT.setBackgroundColor(Color.RED);
                    ksan_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                //uygun mail formatı
                if(!mail.contains(".com") || !mail.contains("@")){
                    ksan_errorMessageTXT.setText("Mail formatı uygun değil !");
                    ksan_errorMessageTXT.setBackgroundColor(Color.RED);
                    ksan_errorMessageTXT.setVisibility(View.VISIBLE);
                    return;
                }

                //daha önce eklenen bir kullanıcı mı diye kontrol et, değilse ekle
                firebaseHelper.checkMailExist("bireysel", mail);
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
        if(mailType.equals("bireysel")){
            if(!result){
                String mail = ksan_mailET.getText().toString();

                //bireyselde kayıt açma
                firebaseHelper.registerBireyselUser(StaticObjectClass.myKurumsalProfile.getId(), "", "", mail, "", "");
                //işlem bittikten sonra interface fonksiyonundan devam eder
            }
            else{
                ksan_errorMessageTXT.setText("Lütfen sisteme kayıtlı olmayan bir mail giriniz.");
                ksan_errorMessageTXT.setBackgroundColor(Color.RED);
                ksan_errorMessageTXT.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void registerKurumsalUser() {

    }

    @Override
    public void registerBireyselUser() {
        String mail = ksan_mailET.getText().toString();

        //davette bulunan kurumsal hesaba bu kişiyi ekleme
        firebaseHelper.registerKurumsalSubUser(StaticObjectClass.myKurumsalProfile.getId(), mail);
        //davet kodu oluşturma
        String inviteCode = generatePassword();
        //davet kodunu kodlar bölümüne ekleme
        firebaseHelper.addUserCode(StaticObjectClass.myKurumsalProfile.getId(), mail, inviteCode);
        //davet edilen kullanıcıya mail gönderme
        mailService.sendBireyselMail(StaticObjectClass.myKurumsalProfile.getFirstName(), StaticObjectClass.myKurumsalProfile.getLastName(),mail, inviteCode);


        ksan_errorMessageTXT.setText("Kullanıcı başarılı bir şekilde eklendi.");
        ksan_errorMessageTXT.setBackgroundColor(Color.GREEN);
        ksan_errorMessageTXT.setVisibility(View.VISIBLE);

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
}
