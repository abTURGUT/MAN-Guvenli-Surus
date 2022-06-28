package org.tensorflow.lite.examples.detection.BireyselScreen;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.tensorflow.lite.examples.detection.CameraConnectionFragment;
import org.tensorflow.lite.examples.detection.LegacyCameraConnectionFragment;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.Records.Record;
import org.tensorflow.lite.examples.detection.StaticObjectClass;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.env.Logger;
import org.tensorflow.lite.examples.detection.tflite.Classifier;

public abstract class CameraActivity extends AppCompatActivity
    implements OnImageAvailableListener,
        Camera.PreviewCallback,
//        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {
  private static final Logger LOGGER = new Logger();

  private static final int PERMISSIONS_REQUEST = 1;

  private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
  private static final String ASSET_PATH = "";
  protected int previewWidth = 0;
  protected int previewHeight = 0;
  private boolean debug = false;
  protected Handler handler;
  private HandlerThread handlerThread;
  private boolean useCamera2API;
  private boolean isProcessingFrame = false;
  private byte[][] yuvBytes = new byte[3][];
  private int[] rgbBytes = null;
  private int yRowStride;
  protected int defaultModelIndex = 0;
  protected int defaultDeviceIndex = 0;
  private Runnable postInferenceCallback;
  private Runnable imageConverter;
  protected ArrayList<String> modelStrings = new ArrayList<String>();

  //private LinearLayout bottomSheetLayout;
  private LinearLayout gestureLayout;
  //private BottomSheetBehavior<LinearLayout> sheetBehavior;

  protected TextView frameValueTextView, cropValueTextView, inferenceTimeTextView;
  //protected ImageView bottomSheetArrowImageView;
  private ImageView plusImageView, minusImageView;
  protected ListView deviceView;
  protected TextView threadsTextView;
  protected ListView modelView;
  /** Current indices of device and model. */
  int currentDevice = -1;
  int currentModel = -1;
  int currentNumThreads = -1;

  ArrayList<String> deviceStrings = new ArrayList<String>();

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    LOGGER.d("onCreate " + this);
    super.onCreate(null);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    setContentView(R.layout.tfe_od_activity_camera);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);


    threadsTextView = findViewById(R.id.threads);
    currentNumThreads = Integer.parseInt(threadsTextView.getText().toString().trim());
    plusImageView = findViewById(R.id.plus);
    minusImageView = findViewById(R.id.minus);
    deviceView = findViewById(R.id.device_list);

    deviceView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    ArrayAdapter<String> deviceAdapter =
            new ArrayAdapter<>(
                    CameraActivity.this , R.layout.deviceview_row, R.id.deviceview_row_text, deviceStrings);
    deviceView.setAdapter(deviceAdapter);
    deviceView.setItemChecked(defaultDeviceIndex, true);
    currentDevice = defaultDeviceIndex;

    /*
    deviceView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateActiveModel();
              }
            });
    */

    //bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
    gestureLayout = findViewById(R.id.gesture_layout);
    //sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
    //bottomSheetArrowImageView = findViewById(R.id.bottom_sheet_arrow);
    modelView = findViewById((R.id.model_list));

    modelStrings = getModelStrings(getAssets(), ASSET_PATH);
    modelView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    ArrayAdapter<String> modelAdapter =
            new ArrayAdapter<>(
                    CameraActivity.this , R.layout.listview_row, R.id.listview_row_text, modelStrings);
    modelView.setAdapter(modelAdapter);
    modelView.setItemChecked(defaultModelIndex, true);
    currentModel = defaultModelIndex;


    /*
    modelView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateActiveModel();
              }
            });
    */
    ViewTreeObserver vto = gestureLayout.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
              gestureLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
              gestureLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            //                int width = bottomSheetLayout.getMeasuredWidth();
            int height = gestureLayout.getMeasuredHeight();

            //sheetBehavior.setPeekHeight(0);
          }
        });
    //sheetBehavior.setHideable(true);


    frameValueTextView = findViewById(R.id.frame_info);
    cropValueTextView = findViewById(R.id.crop_info);
    inferenceTimeTextView = findViewById(R.id.inference_info);

    plusImageView.setOnClickListener(this);
    minusImageView.setOnClickListener(this);



    setFragment();

  }

  public static void updateLightStatus(List<Classifier.Recognition> results){
    int greenScore = 0;
    int redScore = 0;
    int yellowScore = 0;

    float greenConfidenceScore = 0;
    float redConfidenceScore = 0;
    float yellowConfidenceScore = 0;

    String trafficLightResult = "-";

    boolean isListEmpty = true;

    if(results.size() > 0){
      isListEmpty = false;

      //puanların toplanması
      for (Classifier.Recognition result: results) {

        if(result.getDetectedClass() == 0)
        {
          greenScore++;
          greenConfidenceScore += result.getConfidence();
        }
        else if (result.getDetectedClass() == 1)
        {
          redScore++;
          redConfidenceScore += result.getConfidence();
        }
        else if (result.getDetectedClass() == 3)
        {
          yellowScore++;
          yellowConfidenceScore += result.getConfidence();
        }
      }
    }

    if(!isListEmpty){
      //üstün olan ışığın seçilmesi

      if(greenScore > redScore && greenScore > yellowScore){ trafficLightResult = "Yeşil"; }
      else if(redScore > greenScore && redScore > yellowScore){ trafficLightResult = "Kırmızı"; }
      else if(yellowScore > greenScore && yellowScore > redScore){ trafficLightResult = "Sarı"; }

      else if (greenScore == redScore)
      {
        if(greenConfidenceScore > redConfidenceScore){  trafficLightResult = "Yeşil";  }
        else if(redConfidenceScore > greenConfidenceScore){ trafficLightResult = "Kırmızı"; }
      }
      else if (greenScore == yellowScore)
      {
        if(greenConfidenceScore > yellowConfidenceScore){  trafficLightResult = "Yeşil";  }
        else if(yellowConfidenceScore > greenConfidenceScore){ trafficLightResult = "Sarı"; }
      }
      else if (redScore == yellowScore)
      {
        if(redConfidenceScore > yellowConfidenceScore){ trafficLightResult = "Kırmızı"; }
        else if(yellowConfidenceScore > redConfidenceScore){ trafficLightResult = "Sarı"; }
      }

      LOGGER.i("Trafik Işığı Rengi : " + trafficLightResult);

      //trafik ışığını ekrana yazma ve rengini değiştirme
      try
      {
        StaticObjectClass.lightNameTV.setText(trafficLightResult);

        if(trafficLightResult.equals("Yeşil")){ StaticObjectClass.lightNameTV.setTextColor(Color.GREEN); }
        else if(trafficLightResult.equals("Kırmızı")){ StaticObjectClass.lightNameTV.setTextColor(Color.RED); }
        else if(trafficLightResult.equals("Sarı")){ StaticObjectClass.lightNameTV.setTextColor(Color.YELLOW); }
        else { StaticObjectClass.lightNameTV.setTextColor(Color.BLACK); }

        trafficLightViolation(trafficLightResult);
      }
      catch (Exception ex){}
    }
    else{
      StaticObjectClass.lightNameTV.setText("-");
      StaticObjectClass.lightNameTV.setTextColor(Color.BLACK);
      trafficLightViolation("Empty");
    }

  }

  static List<String> trafficLightList = new ArrayList<>();
  static int trafficLightViolationFlag = 0;
  public static void trafficLightViolation(String trafficLight){

    LOGGER.i("trafik ışığı listesi : " + trafficLightList.toString());

    if(trafficLight == "Yeşil"){
      trafficLightList.clear();
    }
    else if (trafficLight == "Sarı"){}
    else if (trafficLight == "Kırmızı"){
      if(trafficLightList.size() < 5){
        trafficLightList.add("Kırmızı");
      }
      else{
        trafficLightList.remove(0);
        trafficLightList.add("Kırmızı");
      }
    }
    else if (trafficLight == "Empty"){
      if(trafficLightList.size() < 5){
        trafficLightList.add("Empty");
      }
      else{
        String s1 = trafficLightList.get(1);
        String s2 = trafficLightList.get(2);
        String s3 = trafficLightList.get(3);
        String s4 = trafficLightList.get(4);

        if(s1.equals("Kırmızı") && s2.equals("Kırmızı") && s3.equals("Empty") && s4.equals("Empty")){

          //kırmızı ışık ihlali varsa diğer koşul olan hızları kontrol et
          /*
          double sumOfSpeeds = 0;
          double meanSpeed = 0;

          for (Double speed : vehicleSpeedList ) {
            sumOfSpeeds += speed;
          }

          meanSpeed = sumOfSpeeds / vehicleSpeedList.size();

          if(meanSpeed <= 10){
            trafficLightList.remove(0);
            trafficLightList.add("Empty");
          }
          else{
            if(trafficLightViolationFlag == 0){
              String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
              String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
              Record violationRecord = new Record("", "", "","",currentDate, currentTime, "" );
              StaticObjectClass.violentRecords.add(violationRecord);
            }

            StaticObjectClass.trafficLightViolentTV.setText("İHLAL VAR !");
            StaticObjectClass.trafficLightViolentTV.setBackgroundColor(Color.RED);
            StaticObjectClass.trafficLightViolentTV.setTextColor(Color.WHITE);
            trafficLightList.remove(0);
            trafficLightList.add("Empty");
            trafficLightViolationFlag = 5;
          }

          */

          if(trafficLightViolationFlag == 0){
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            Record violationRecord = new Record("", "", "","",currentDate, currentTime, "" );
            StaticObjectClass.violentRecords.add(violationRecord);
          }

          StaticObjectClass.trafficLightViolentTV.setText("İHLAL VAR !");
          StaticObjectClass.trafficLightViolentTV.setBackgroundColor(Color.RED);
          StaticObjectClass.trafficLightViolentTV.setTextColor(Color.WHITE);
          trafficLightList.remove(0);
          trafficLightList.add("Empty");
          trafficLightViolationFlag = 5;




        }
        else{
          trafficLightList.remove(0);
          trafficLightList.add("Empty");
        }
      }
    }

    if(trafficLightViolationFlag > 0){
      StaticObjectClass.trafficLightViolentTV.setText("İHLAL VAR !");
      StaticObjectClass.trafficLightViolentTV.setBackgroundColor(Color.RED);
      StaticObjectClass.trafficLightViolentTV.setTextColor(Color.WHITE);
      trafficLightViolationFlag--;
    }
    else{
      StaticObjectClass.trafficLightViolentTV.setText("-");
      StaticObjectClass.trafficLightViolentTV.setBackgroundColor(Color.WHITE);
      StaticObjectClass.trafficLightViolentTV.setTextColor(Color.BLACK);
    }


  }

  static List<Double> vehicleSpeedList = new ArrayList<>();
  static int vehicleSpeed = 0;
  public static void updateSpeed(String latitude, String longitude, String speed){

    vehicleSpeed = Integer.parseInt(speed);
    StaticObjectClass.vehicleSpeedTV.setText(speed);

    vehicleSpeedList.add(Double.parseDouble(speed));

    LOGGER.i("speed list : " + vehicleSpeedList.toString());

    if(vehicleSpeedList.size() > 5){ vehicleSpeedList.remove(0); }
  }

  public static void updateFollowingDistance(List<Classifier.Recognition> results){

    boolean flag1 = false;

    double maxSizeTemp = 0;

    for(Classifier.Recognition result : results){
      if(result.getDetectedClass() == 2){
        double size = result.getLocation().right - result.getLocation().left;
        if(size > maxSizeTemp){ maxSizeTemp = size; }
        flag1 = true;
      }
    }

    if(!flag1){ StaticObjectClass.followingDistanceTV.setText("-"); }
    else{

      double realDistance = Math.round(((900.0 / maxSizeTemp) * 1.25) - 1.25);

      if(realDistance < 0){ realDistance = 0; }

      StaticObjectClass.followingDistanceTV.setText(String.valueOf(realDistance));
    }
  }



  protected ArrayList<String> getModelStrings(AssetManager mgr, String path){
    ArrayList<String> res = new ArrayList<String>();
    try {
      String[] files = mgr.list(path);
      for (String file : files) {
        String[] splits = file.split("\\.");
        if (splits[splits.length - 1].equals("tflite")) {
          res.add(file);
        }
      }

    }
    catch (IOException e){
      System.err.println("getModelStrings: " + e.getMessage());
    }
    return res;
  }

  protected int[] getRgbBytes() {
    imageConverter.run();
    return rgbBytes;
  }

  protected int getLuminanceStride() {
    return yRowStride;
  }

  protected byte[] getLuminance() {
    return yuvBytes[0];
  }

  @Override
  public void onPreviewFrame(final byte[] bytes, final Camera camera) {
    if (isProcessingFrame) {
      LOGGER.w("Dropping frame!");
      return;
    }

    try {

      if (rgbBytes == null) {
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        previewHeight = previewSize.height;
        previewWidth = previewSize.width;
        rgbBytes = new int[previewWidth * previewHeight];
        onPreviewSizeChosen(new Size(previewSize.width, previewSize.height), 90);
      }
    } catch (final Exception e) {
      LOGGER.e(e, "Exception!");
      return;
    }

    isProcessingFrame = true;
    yuvBytes[0] = bytes;
    yRowStride = previewWidth;

    imageConverter =
        new Runnable() {
          @Override
          public void run() {
            ImageUtils.convertYUV420SPToARGB8888(bytes, previewWidth, previewHeight, rgbBytes);
          }
        };

    postInferenceCallback =
        new Runnable() {
          @Override
          public void run() {
            camera.addCallbackBuffer(bytes);
            isProcessingFrame = false;
          }
        };
    processImage();
  }

  @Override
  public void onImageAvailable(final ImageReader reader) {

    if (previewWidth == 0 || previewHeight == 0) {
      return;
    }
    if (rgbBytes == null) {
      rgbBytes = new int[previewWidth * previewHeight];
    }
    try {
      final Image image = reader.acquireLatestImage();

      if (image == null) {
        return;
      }

      if (isProcessingFrame) {
        image.close();
        return;
      }
      isProcessingFrame = true;
      Trace.beginSection("imageAvailable");
      final Plane[] planes = image.getPlanes();
      fillBytes(planes, yuvBytes);
      yRowStride = planes[0].getRowStride();
      final int uvRowStride = planes[1].getRowStride();
      final int uvPixelStride = planes[1].getPixelStride();

      imageConverter =
          new Runnable() {
            @Override
            public void run() {
              ImageUtils.convertYUV420ToARGB8888(
                  yuvBytes[0],
                  yuvBytes[1],
                  yuvBytes[2],
                  previewWidth,
                  previewHeight,
                  yRowStride,
                  uvRowStride,
                  uvPixelStride,
                  rgbBytes);
            }
          };

      postInferenceCallback =
          new Runnable() {
            @Override
            public void run() {
              image.close();
              isProcessingFrame = false;
            }
          };
      processImage();
    } catch (final Exception e) {
      LOGGER.e(e, "Exception!");
      Trace.endSection();
      return;
    }
    Trace.endSection();
  }

  @Override
  public synchronized void onStart() {
    LOGGER.d("onStart " + this);
    super.onStart();
  }

  @Override
  public synchronized void onResume() {
    LOGGER.d("onResume " + this);
    super.onResume();

    handlerThread = new HandlerThread("inference");
    handlerThread.start();
    handler = new Handler(handlerThread.getLooper());
  }

  @Override
  public synchronized void onPause() {
    LOGGER.d("onPause " + this);

    handlerThread.quitSafely();
    try {
      handlerThread.join();
      handlerThread = null;
      handler = null;
    } catch (final InterruptedException e) {
      LOGGER.e(e, "Exception!");
    }

    super.onPause();
  }

  @Override
  public synchronized void onStop() {
    LOGGER.d("onStop " + this);
    super.onStop();
  }

  @Override
  public synchronized void onDestroy() {
    LOGGER.d("onDestroy " + this);
    super.onDestroy();
  }

  protected synchronized void runInBackground(final Runnable r) {
    if (handler != null) {
      handler.post(r);
    }
  }



  private static boolean allPermissionsGranted(final int[] grantResults) {
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }




  private boolean isHardwareLevelSupported(
      CameraCharacteristics characteristics, int requiredLevel) {
    int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
    if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
      return requiredLevel == deviceLevel;
    }
    return requiredLevel <= deviceLevel;
  }

  private String chooseCamera() {
    final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    try {
      for (final String cameraId : manager.getCameraIdList()) {
        final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
        if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
          continue;
        }

        final StreamConfigurationMap map =
            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        if (map == null) {
          continue;
        }

        useCamera2API =
            (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
                || isHardwareLevelSupported(
                    characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL);
        LOGGER.i("Camera API lv2?: %s", useCamera2API);
        return cameraId;
      }
    } catch (CameraAccessException e) {
      LOGGER.e(e, "Not allowed to access camera");
    }

    return null;
  }

  protected void setFragment() {
    String cameraId = chooseCamera();

    Fragment fragment;
    if (useCamera2API) {
      CameraConnectionFragment camera2Fragment =
          CameraConnectionFragment.newInstance(
              new CameraConnectionFragment.ConnectionCallback() {
                @Override
                public void onPreviewSizeChosen(final Size size, final int rotation) {
                  previewHeight = size.getHeight();
                  previewWidth = size.getWidth();
                  CameraActivity.this.onPreviewSizeChosen(size, rotation);
                }
              },
              this,
              getLayoutId(),
              getDesiredPreviewFrameSize());

      camera2Fragment.setCamera(cameraId);
      fragment = camera2Fragment;
    } else {
      fragment =
          new LegacyCameraConnectionFragment(this, getLayoutId(), getDesiredPreviewFrameSize());
    }

    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

  }

  protected void fillBytes(final Plane[] planes, final byte[][] yuvBytes) {
    for (int i = 0; i < planes.length; ++i) {
      final ByteBuffer buffer = planes[i].getBuffer();
      if (yuvBytes[i] == null) {
        LOGGER.d("Initializing buffer %d at size %d", i, buffer.capacity());
        yuvBytes[i] = new byte[buffer.capacity()];
      }
      buffer.get(yuvBytes[i]);
    }
  }

  public boolean isDebug() {
    return debug;
  }

  protected void readyForNextImage() {
    if (postInferenceCallback != null) {
      postInferenceCallback.run();
    }
  }

  protected int getScreenOrientation() {
    switch (getWindowManager().getDefaultDisplay().getRotation()) {
      case Surface.ROTATION_270:
        return 270;
      case Surface.ROTATION_180:
        return 180;
      case Surface.ROTATION_90:
        return 90;
      default:
        return 0;
    }
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.plus) {
      String threads = threadsTextView.getText().toString().trim();
      int numThreads = Integer.parseInt(threads);
      if (numThreads >= 9) return;
      numThreads++;
      threadsTextView.setText(String.valueOf(numThreads));
      setNumThreads(numThreads);
    } else if (v.getId() == R.id.minus) {
      String threads = threadsTextView.getText().toString().trim();
      int numThreads = Integer.parseInt(threads);
      if (numThreads == 1) {
        return;
      }
      numThreads--;
      threadsTextView.setText(String.valueOf(numThreads));
      setNumThreads(numThreads);
    }
  }

  protected void showFrameInfo(String frameInfo) {
    frameValueTextView.setText(frameInfo);
  }

  protected void showCropInfo(String cropInfo) {
    cropValueTextView.setText(cropInfo);
  }

  protected void showInference(String inferenceTime) {
    inferenceTimeTextView.setText(inferenceTime);
  }

  protected abstract void updateActiveModel(String processorType);
  protected abstract void processImage();

  protected abstract void onPreviewSizeChosen(final Size size, final int rotation);

  protected abstract int getLayoutId();

  protected abstract Size getDesiredPreviewFrameSize();

  protected abstract void setNumThreads(int numThreads);

  protected abstract void setUseNNAPI(boolean isChecked);

}
