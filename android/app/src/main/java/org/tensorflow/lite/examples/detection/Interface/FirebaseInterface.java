package org.tensorflow.lite.examples.detection.Interface;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.Users.User;

import java.util.List;
import java.util.Map;

public interface FirebaseInterface {
    void loginKurumsalResult(Kurumsal result);
    void loginBireyselResult(Bireysel result);
    void getKurumsalInfoResult(Kurumsal result);
    void checkMailExist(String mailType, boolean result);
    void registerKurumsalUser();
    void registerBireyselUser();
    void updateKurumsalUserResult(String userId);
    void updatebireyselUserResult(String userId);
    void checkUserCode(Map<String, Object> record);
    void addBireyselCameraRecord(String recordId);
    void getKurumsalSubUsersResult(List<User> subUsers);
    void getBireyselUserInfo(User user);
    void getBireyselUserRecordsResult(List<Record> recordList);
    void getBireyselUserViolentRecordsResult(List<Record> recordList);
}
