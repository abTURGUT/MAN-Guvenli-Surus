/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.detection.BireyselScreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ImageReader.OnImageAvailableListener;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.tensorflow.lite.examples.detection.Database.FirebaseHelper;
import org.tensorflow.lite.examples.detection.Interface.FirebaseInterface;
import org.tensorflow.lite.examples.detection.KurumsalScreen.Kurumsal;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.Users.User;
import org.tensorflow.lite.examples.detection.customview.OverlayView;
import org.tensorflow.lite.examples.detection.customview.OverlayView.DrawCallback;
import org.tensorflow.lite.examples.detection.env.BorderedText;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.env.Logger;
import org.tensorflow.lite.examples.detection.tflite.Classifier;
import org.tensorflow.lite.examples.detection.tflite.DetectorFactory;
import org.tensorflow.lite.examples.detection.tflite.YoloV5Classifier;
import org.tensorflow.lite.examples.detection.tracking.MultiBoxTracker;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements FirebaseInterface, OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();

    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.3f;
    private static final boolean MAINTAIN_ASPECT = true;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 640);
    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private Integer sensorOrientation;

    private YoloV5Classifier detector;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingDetection = false;

    private long timestamp = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;

    private BorderedText borderedText;

    Activity activity;
    FirebaseHelper firebaseHelper;


    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        tracker = new MultiBoxTracker(this);

        //Static Tanımlamalar ----------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------

        StaticObjectClass.ca_closeBTN = findViewById(R.id.ca_closeBTN);
        StaticObjectClass.ca_closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(DialogInterface.BUTTON_POSITIVE == which){

                            //kayıt tarihini ve süresini ayarlama
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                            StaticObjectClass.initializeRecord.setEndTime(String.valueOf(currentTime));

                            firebaseHelper.addBireyselCameraRecord(StaticObjectClass.myBireyselProfile.getId(), StaticObjectClass.initializeRecord);

                        }
                        else if(DialogInterface.BUTTON_NEGATIVE == which){

                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(DetectorActivity.this);
                builder.setMessage("Kamera kapatılsın mı ?").setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Hayır", dialogClickListener).show();
            }
        });
        StaticObjectClass.trafficLightViolentTV = findViewById(R.id.trafficLightViolentTV);
        StaticObjectClass.vehicleSpeedTV = findViewById(R.id.vehicleSpeedTV);
        StaticObjectClass.lightNameTV = findViewById(R.id.lightNameTV);
        StaticObjectClass.cameraWorkTypeSPINNER = findViewById(R.id.cameraWorkTypeSPINNER);
        StaticObjectClass.followingDistanceTV = findViewById(R.id.followingDistanceTV);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"GPU","CPU"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StaticObjectClass.cameraWorkTypeSPINNER.setAdapter(spinnerAdapter);
        StaticObjectClass.cameraWorkTypeSPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getSelectedItem().toString();
                LOGGER.i("Processor Type : " + selectedItem);
                updateActiveModel(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        final int modelIndex = modelView.getCheckedItemPosition();
        final String modelString = modelStrings.get(modelIndex);

        try {
            detector = DetectorFactory.getDetector(getAssets(), modelString);
        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.e(e, "Exception initializing classifier!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        int cropSize = detector.getInputSize();

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        cropSize, cropSize,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                new DrawCallback() {
                    @Override
                    public void drawCallback(final Canvas canvas) {
                        tracker.draw(canvas);
                        if (isDebug()) {
                            tracker.drawDebug(canvas);
                        }
                    }
                });

        tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
    }



    protected void updateActiveModel(String processorType) {
        // Get UI information before delegating to background
        final int modelIndex = modelView.getCheckedItemPosition();
        final int deviceIndex = deviceView.getCheckedItemPosition();
        String threads = threadsTextView.getText().toString().trim();
        final int numThreads = Integer.parseInt(threads);


        handler.post(() -> {

            /*
            if (modelIndex == currentModel && deviceIndex == currentDevice
                    && numThreads == currentNumThreads) {
                return;
            }
             */
            currentModel = modelIndex;
            currentDevice = deviceIndex;
            currentNumThreads = numThreads;

            // Disable classifier while updating
            if (detector != null) {
                detector.close();
                detector = null;
            }

            // Lookup names of parameters.
            String modelString = modelStrings.get(modelIndex);
            //String device = deviceStrings.get(deviceIndex);
            String device = processorType;

            LOGGER.i("GİRDİİİİİ");

            LOGGER.i("Changing model to " + modelString + " device " + device);

            // Try to load model.

            try {
                detector = DetectorFactory.getDetector(getAssets(), modelString);
                // Customize the interpreter to the type of device we want to use.
                if (detector == null) {
                    return;
                }
            }
            catch(IOException e) {
                e.printStackTrace();
                LOGGER.e(e, "Exception in updateActiveModel()");
                Toast toast =
                        Toast.makeText(
                                getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }



            if (device.equals("CPU")) {
                detector.useCPU();
            } else if (device.equals("GPU")) {
                detector.useGpu();
            } else if (device.equals("NNAPI")) {
                detector.useNNAPI();
            }
            detector.setNumThreads(numThreads);


            int cropSize = detector.getInputSize();
            croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

            frameToCropTransform =
                    ImageUtils.getTransformationMatrix(
                            previewWidth, previewHeight,
                            cropSize, cropSize,
                            sensorOrientation, MAINTAIN_ASPECT);

            cropToFrameTransform = new Matrix();
            frameToCropTransform.invert(cropToFrameTransform);
        });
    }

    public void getLocation(){
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY;

        fusedLocationProviderClient.getCurrentLocation(priority, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){
                    Geocoder geocoder = new Geocoder(DetectorActivity.this, Locale.getDefault());

                    LOGGER.i("gps takip : " + String.valueOf(location.getLatitude()));
                    LOGGER.i("gps takip : " + String.valueOf(location.getLongitude()));
                    double speed = location.getSpeed();
                    int kmphSpeed = (int)((int)speed*3.6);
                    LOGGER.i("gps takip : " + String.valueOf(kmphSpeed));
                    //StaticObjectClass.vehicleSpeedTV.setText(String.valueOf(kmphSpeed));
                    CameraActivity.updateSpeed(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), String.valueOf(kmphSpeed));

                }
            }
        });





        /*
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){

                    Geocoder geocoder = new Geocoder(DetectorActivity.this, Locale.getDefault());

                    LOGGER.i("gps takip : " + String.valueOf(location.getLatitude()));
                    LOGGER.i("gps takip : " + String.valueOf(location.getLongitude()));
                    double speed = location.getSpeed();
                    int kmphSpeed = (int)(speed*3.6);
                    LOGGER.i("gps takip : " + String.valueOf(kmphSpeed));
                    StaticObjectClass.vehicleSpeedTV.setText(String.valueOf(location.getLatitude()));
                    //CameraActivity.updateSpeed(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), String.valueOf(kmphSpeed));

                }

            }
        });
        */

    }


    @Override
    protected void processImage() {
        ++timestamp;
        final long currTimestamp = timestamp;
        trackingOverlay.postInvalidate();

        if (computingDetection) {
            readyForNextImage();
            return;
        }
        computingDetection = true;

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

        readyForNextImage();

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }

        //firebase
        activity = (Activity) this;
        firebaseHelper = new FirebaseHelper((FirebaseInterface)activity);



        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        //getLocation();

                        final List<Classifier.Recognition> results = detector.recognizeImage(croppedBitmap);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                CameraActivity.updateLightStatus(results);
                                CameraActivity.updateFollowingDistance(results);
                            }
                        });

                        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                        final Canvas canvas = new Canvas(cropCopyBitmap);
                        final Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStyle(Style.STROKE);
                        paint.setStrokeWidth(2.0f);

                        float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                        switch (MODE) {
                            case TF_OD_API:
                                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                                break;
                        }

                        final List<Classifier.Recognition> mappedRecognitions =
                                new LinkedList<Classifier.Recognition>();

                        for (final Classifier.Recognition result : results) {
                            final RectF location = result.getLocation();
                            if (location != null && result.getConfidence() >= minimumConfidence) {
                                canvas.drawRect(location, paint);

                                cropToFrameTransform.mapRect(location);

                                result.setLocation(location);
                                mappedRecognitions.add(result);
                            }
                        }

                        tracker.trackResults(mappedRecognitions, currTimestamp);
                        trackingOverlay.postInvalidate();

                        computingDetection = false;

                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showFrameInfo(previewWidth + "x" + previewHeight);
                                        showCropInfo(cropCopyBitmap.getWidth() + "x" + cropCopyBitmap.getHeight());
                                        showInference(lastProcessingTimeMs + "ms");
                                    }
                                });
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tfe_od_camera_connection_fragment_tracking;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
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

        //ihlal kayıtlarını sisteme ekleme
        for(Record record : StaticObjectClass.violentRecords){
            firebaseHelper.addBireyselCameraViolentRecord(StaticObjectClass.myBireyselProfile.getId(), recordId, record);
        }

        StaticObjectClass.violentRecords.clear();

        onBackPressed();
        finish();
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

    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum DetectorMode {
        TF_OD_API;
    }

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        runInBackground(() -> detector.setUseNNAPI(isChecked));
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        runInBackground(() -> detector.setNumThreads(numThreads));
    }
}
