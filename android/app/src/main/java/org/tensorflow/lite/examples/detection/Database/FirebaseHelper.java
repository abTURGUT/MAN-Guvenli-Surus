package org.tensorflow.lite.examples.detection.Database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.env.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper{

    private static final Logger LOGGER = new Logger();
    public FirebaseInterface firebaseInterface;
    String globalDocumentId;

    public FirebaseHelper(FirebaseInterface firebaseInterface) {
        this.firebaseInterface = firebaseInterface;
    }

    public void loginKurumsal(String mail, String passwordParameter, boolean rememberMe) {

        final Kurumsal result = new Kurumsal("-1", "", "", "", "", "", "", false);

        FirebaseFirestore.getInstance()
                .collection("auth")
                .document("kurumsal")
                .collection("users")
                .whereEqualTo("mail", mail)
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {
                                result.setId("-1");
                            } else {
                                //mail varsa
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String id = document.getData().get("id").toString();
                                    String firstName = document.getData().get("firstName").toString();
                                    String lastName = document.getData().get("lastName").toString();
                                    String mail = document.getData().get("mail").toString();
                                    String password = document.getData().get("password").toString();
                                    String phoneNumber = document.getData().get("phoneNumber").toString();
                                    String companyName = document.getData().get("companyName").toString();

                                    //şifreler aynı mı diye bak
                                    if(passwordParameter.equals(password)){
                                        result.setId(id);
                                        result.setFirstName(firstName);
                                        result.setLastName(lastName);
                                        result.setMail(mail);
                                        result.setPassword(password);
                                        result.setPhoneNumber(phoneNumber);
                                        result.setCompanyName(companyName);
                                        result.setRememberMe(rememberMe);
                                    }

                                    break;
                                }
                            }
                        } else {
                            result.setId("-1");
                        }

                        firebaseInterface.loginKurumsalResult(result);
                    }
                });

    }
    public void loginBireysel(String mail, String passwordParameter, boolean rememberMe) {

        final Bireysel result = new Bireysel("-1", "", "", "", "", "", "", false);

        FirebaseFirestore.getInstance()
                .collection("auth")
                .document("bireysel")
                .collection("users")
                .whereEqualTo("mail", mail)
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {
                                result.setId("-1");
                            } else {
                                //mail varsa
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String id = document.getData().get("id").toString();
                                    String firstName = document.getData().get("firstName").toString();
                                    String lastName = document.getData().get("lastName").toString();
                                    String mail = document.getData().get("mail").toString();
                                    String password = document.getData().get("password").toString();
                                    String phoneNumber = document.getData().get("phoneNumber").toString();
                                    String rootId = document.getData().get("rootId").toString();

                                    //şifreler aynı mı diye bak
                                    if(passwordParameter.equals(password)){
                                        result.setId(id);
                                        result.setFirstName(firstName);
                                        result.setLastName(lastName);
                                        result.setMail(mail);
                                        result.setPassword(password);
                                        result.setPhoneNumber(phoneNumber);
                                        result.setRootId(rootId);
                                        result.setRememberMe(rememberMe);
                                    }

                                    break;
                                }
                            }
                        } else {
                            result.setId("-1");
                        }

                        firebaseInterface.loginBireyselResult(result);
                    }
                });

    }
    public void getKurumsalInfo(String id, int callType, String error, String userId){
        final Kurumsal result = new Kurumsal("-1", "", "", "", "", "", "", false);

        FirebaseFirestore.getInstance()
                .collection("auth")
                .document("kurumsal")
                .collection("users")
                .whereEqualTo("id", id)
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {
                                result.setId("-1");
                            } else {
                                //mail varsa
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String id = document.getData().get("id").toString();
                                    String firstName = document.getData().get("firstName").toString();
                                    String lastName = document.getData().get("lastName").toString();
                                    String mail = document.getData().get("mail").toString();
                                    String password = document.getData().get("password").toString();
                                    String phoneNumber = document.getData().get("phoneNumber").toString();
                                    String companyName = document.getData().get("companyName").toString();

                                    result.setId(id);
                                    result.setFirstName(firstName);
                                    result.setLastName(lastName);
                                    result.setMail(mail);
                                    result.setPassword(password);
                                    result.setPhoneNumber(phoneNumber);
                                    result.setCompanyName(companyName);
                                    result.setRememberMe(false);

                                    break;
                                }
                            }
                        } else {
                            result.setId("-1");
                        }

                        if(callType == 1) {firebaseInterface.getKurumsalInfoResult(result); }
                        else if (callType == 2){ checkUserCode2(error, userId, result);}
                    }
                });
    }
    public void checkMailExist(String mailType, String mail){
        if(mailType.equals("kurumsal")){
            FirebaseFirestore.getInstance()
                    .collection("auth")
                    .document("kurumsal")
                    .collection("users")
                    .whereEqualTo("mail", mail)
                    .get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Boolean result;

                            if (task.isSuccessful()) {

                                if (task.getResult().isEmpty()) {
                                    result = false;
                                } else {
                                    result = true;
                                }
                            } else {
                                result = false;
                            }

                            firebaseInterface.checkMailExist(mailType, result);
                        }
                    });
        }
        else if (mailType.equals("bireysel")){
            FirebaseFirestore.getInstance()
                    .collection("auth")
                    .document("bireysel")
                    .collection("users")
                    .whereEqualTo("mail", mail)
                    .get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Boolean result;

                            if (task.isSuccessful()) {

                                if (task.getResult().isEmpty()) {
                                    result = false;
                                } else {
                                    result = true;
                                }
                            } else {
                                result = false;
                            }

                            firebaseInterface.checkMailExist(mailType, result);
                        }
                    });
        }
    }
    public void registerKurumsalUser(String firstName, String lastName, String mail, String phoneNumber, String companyName, String password){
        CollectionReference kurumsalUserCollection = FirebaseFirestore.getInstance()
                .collection("auth")
                .document("kurumsal")
                .collection("users");

        Map<String, Object> record = new HashMap<>();
        record.put("id", "");
        record.put("firstName", firstName);
        record.put("lastName", lastName);
        record.put("mail", mail);
        record.put("phoneNumber", phoneNumber);
        record.put("companyName", companyName);
        record.put("password", password);

        kurumsalUserCollection.add(record).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String documentID = documentReference.getId();
                DocumentReference reference = kurumsalUserCollection.document(documentID);
                Map<String, Object> record = new HashMap<>();
                record.put("id", documentID);
                reference.update(record);

                firebaseInterface.registerKurumsalUser();
            }
        });

    }
    public void registerBireyselUser(String rootId, String firstName, String lastName, String mail, String phoneNumber, String password){
        CollectionReference bireyselUserCollection = FirebaseFirestore.getInstance()
                .collection("auth")
                .document("bireysel")
                .collection("users");

        Map<String, Object> record = new HashMap<>();
        record.put("id", "");
        record.put("rootId", rootId);
        record.put("firstName", firstName);
        record.put("lastName", lastName);
        record.put("mail", mail);
        record.put("phoneNumber", phoneNumber);
        record.put("password", password);
        record.put("active","0");

        bireyselUserCollection.add(record).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String documentID = documentReference.getId();
                globalDocumentId = documentID;
                DocumentReference reference = bireyselUserCollection.document(documentID);
                Map<String, Object> record = new HashMap<>();
                record.put("id", documentID);
                reference.update(record);

                firebaseInterface.registerBireyselUser();
            }
        });

    }
    public void updateKurumsalUser(String id, String firstName, String lastName, String phoneNumber, String companyName, String password){
        Map<String, Object> record = new HashMap<>();
        record.put("firstName", firstName);
        record.put("lastName", lastName);
        record.put("phoneNumber", phoneNumber);
        record.put("companyName", companyName);
        record.put("password", password);

        FirebaseFirestore.getInstance()
                .collection("auth")
                .document("kurumsal")
                .collection("users")
                .document(id)
                .update(record).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseInterface.updateKurumsalUserResult(id);
                    }
                });

    }
    public void updatebireyselUser(String id, String firstName, String lastName, String phoneNumber, String password){
        Map<String, Object> record = new HashMap<>();
        record.put("active", "1");
        record.put("firstName", firstName);
        record.put("lastName", lastName);
        record.put("phoneNumber", phoneNumber);
        record.put("password", password);

        FirebaseFirestore.getInstance()
                .collection("auth")
                .document("bireysel")
                .collection("users")
                .document(id)
                .update(record).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseInterface.updatebireyselUserResult(id);
                    }
                });

    }
    public void registerKurumsalSubUser(String kurumsalID, String mail){
        CollectionReference kurumsalUserCollection = FirebaseFirestore.getInstance()
                .collection("auth")
                .document("kurumsal")
                .collection("users")
                .document(kurumsalID)
                .collection("subUsers");

        Map<String, Object> record = new HashMap<>();
        record.put("id", globalDocumentId);
        record.put("mail", mail);

        kurumsalUserCollection.add(record);

    }
    public void addBireyselCameraRecord(String bireyselID, Record record){
        CollectionReference bireyselUserCollection = FirebaseFirestore.getInstance()
                .collection("auth")
                .document("bireysel")
                .collection("users")
                .document(bireyselID)
                .collection("records");


        bireyselUserCollection.add(record).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseInterface.addBireyselCameraRecord(documentReference.getId());
            }
        });
    }
    public void addBireyselCameraViolentRecord(String bireyselID, String rootRecordID, Record record){
        CollectionReference bireyselUserCollection = FirebaseFirestore.getInstance()
                .collection("auth")
                .document("bireysel")
                .collection("users")
                .document(bireyselID)
                .collection("records")
                .document(rootRecordID)
                .collection("violents");


        bireyselUserCollection.add(record);
    }
    public void addUserCode(String rootId, String mail, String code){
        CollectionReference kurumsalUserCollection = FirebaseFirestore.getInstance().collection("user_codes");

        Map<String, Object> record = new HashMap<>();
        record.put("rootId", rootId);
        record.put("userId", globalDocumentId);
        record.put("mail", mail);
        record.put("code", code);

        kurumsalUserCollection.add(record);
    }
    public void deleteUserCode(String userId){
        FirebaseFirestore.getInstance()
                .collection("user_codes")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                }
                            }
                        }
                    }
                });


    }
    public void checkUserCode1(String code, String mail){
        Map<String, Object> record = new HashMap<>();
        record.put("error", "");
        record.put("userId", "");
        record.put("rootFirstName", "");
        record.put("rootLastName", "");


        FirebaseFirestore.getInstance()
                .collection("user_codes")
                .whereEqualTo("code", code)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {
                                record.put("error", "-1");
                                getKurumsalInfo("-1", 2, record.get("error").toString(), record.get("userId").toString());
                            } else {
                                //mail varsa
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String rootId = document.getData().get("rootId").toString();
                                    String userId = document.getData().get("userId").toString();
                                    String userMail = document.getData().get("mail").toString();
                                    if(mail.equals(userMail)){
                                        record.put("error", "0");
                                        getKurumsalInfo(rootId, 2, record.get("error").toString(), userId);
                                    }
                                    else{
                                        record.put("error", "-1");
                                        getKurumsalInfo("-1", 2, record.get("error").toString(), record.get("userId").toString());
                                    }

                                    return;
                                }
                            }
                        } else {
                            record.put("error", "-1");
                            getKurumsalInfo("-1", 2, record.get("error").toString(), record.get("userId").toString());
                        }

                    }
                });
    }
    public void checkUserCode2(String error, String userId, Kurumsal result){
        Map<String, Object> record = new HashMap<>();
        record.put("error", error);
        record.put("userId", userId);
        record.put("rootFirstName", result.getFirstName());
        record.put("rootLastName", result.getLastName());

        firebaseInterface.checkUserCode(record);
    }
    public void getKurumsalSubUsers(String kurumsalID){

        List<User> subUserList = new ArrayList<>();

        FirebaseFirestore.getInstance()
                .collection("auth")
                .document("kurumsal")
                .collection("users")
                .document(kurumsalID)
                .collection("subUsers")
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {
                                firebaseInterface.getKurumsalSubUsersResult(subUserList);
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String id = document.getData().get("id").toString();

                                    User subUser = new User();
                                    subUser.setId(id);

                                    subUserList.add(subUser);

                                }

                                firebaseInterface.getKurumsalSubUsersResult(subUserList);
                            }
                        } else {
                            firebaseInterface.getKurumsalSubUsersResult(subUserList);
                        }


                    }
                });
    }
    public void getBireyselUserInfo(String bireyselID){
        FirebaseFirestore.getInstance()
                .collection("auth")
                .document("bireysel")
                .collection("users")
                .document(bireyselID)
                .get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            if (!task.getResult().exists()) {
                                firebaseInterface.getBireyselUserInfo(new User("-1", "", "", "", "",""));
                            } else {
                                //mail varsa
                                DocumentSnapshot document = task.getResult() ;
                                String id = document.getData().get("id").toString();
                                String firstName = document.getData().get("firstName").toString();
                                String lastName = document.getData().get("lastName").toString();
                                String mail = document.getData().get("mail").toString();
                                String password = document.getData().get("password").toString();
                                String phoneNumber = document.getData().get("phoneNumber").toString();
                                String rootId = document.getData().get("rootId").toString();
                                String active = document.getData().get("active").toString();

                                User user = new User(id, firstName, lastName, mail, active, "");
                                firebaseInterface.getBireyselUserInfo(user);

                            }
                        } else {
                            firebaseInterface.getBireyselUserInfo(new User("-1", "", "", "", "", ""));
                        }
                    }
                });
    }
    public void getBireyselUserRecords(String bireyselID, String userFirstName, String userLastName){

        List<Record> recordList = new ArrayList<>();

        FirebaseFirestore.getInstance()
                .collection("auth")
                .document("bireysel")
                .collection("users")
                .document(bireyselID)
                .collection("records")
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                firebaseInterface.getBireyselUserRecordsResult(recordList);
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String id = document.getId();
                                    String date = document.getData().get("date").toString();
                                    String startTime = document.getData().get("startTime").toString();
                                    String endTime = document.getData().get("endTime").toString();

                                    Record record = new Record(id, bireyselID, userFirstName, userLastName, date, startTime, endTime);
                                    recordList.add(record);

                                }
                                firebaseInterface.getBireyselUserRecordsResult(recordList);
                            }
                        } else {
                            firebaseInterface.getBireyselUserRecordsResult(recordList);
                        }
                    }
                });




    }
    public void getBireyselUserViolentRecords(String bireyselID, String recordID, String userFirstName, String userLastName){

        List<Record> violentRecordsList = new ArrayList<>();

        FirebaseFirestore.getInstance()
                .collection("auth")
                .document("bireysel")
                .collection("users")
                .document(bireyselID)
                .collection("records")
                .document(recordID)
                .collection("violents")
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {
                                firebaseInterface.getBireyselUserViolentRecordsResult(violentRecordsList);
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String id = document.getId();
                                    String date = document.getData().get("date").toString();
                                    String startTime = document.getData().get("startTime").toString();
                                    String endTime = document.getData().get("endTime").toString();

                                    Record violentRecord = new Record(id, bireyselID, userFirstName, userLastName, date, startTime, endTime);
                                    violentRecordsList.add(violentRecord);

                                }

                                firebaseInterface.getBireyselUserViolentRecordsResult(violentRecordsList);
                            }
                        } else {
                            firebaseInterface.getBireyselUserViolentRecordsResult(violentRecordsList);
                        }


                    }
                });
    }
}

