package org.tensorflow.lite.examples.detection;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.tensorflow.lite.examples.detection.BireyselScreen.Bireysel;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;
import org.tensorflow.lite.examples.detection.Records.Record;

import java.util.ArrayList;
import java.util.List;

public class StaticObjectClass {
    public static TextView lightNameTV = null;
    public static Spinner cameraWorkTypeSPINNER = null;
    public static TextView vehicleSpeedTV = null;
    public static TextView trafficLightViolentTV = null;
    public static Button ca_closeBTN = null;
    public static TextView followingDistanceTV = null;

    public static Kurumsal myKurumsalProfile = new Kurumsal();
    public static Bireysel myBireyselProfile = new Bireysel();

    public static Record initializeRecord = new Record();
    public static List<Record> violentRecords = new ArrayList<Record>();
}
