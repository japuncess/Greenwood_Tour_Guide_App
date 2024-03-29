package com.robotemi.sdk.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Button;

import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

/*
//facedec
import android.support.v7.app.AppCompatActivity;
import com.otaliastudios.cameraview.Facing;
import husaynhakeem.io.facedetector.FaceDetector;
import husaynhakeem.io.facedetector.models.Frame;
import husaynhakeem.io.facedetector.models.Size;
import kotlinx.android.synthetic.main.activity_main.*;
*/
import android.view.TextureView;

import org.w3c.dom.Text;

import java.util.List;

/*
 * Created by Shubham Jindal and Ivan Reinaldo Ridwan
 */

public class MainActivity extends AppCompatActivity implements
        Robot.NlpListener,
        OnRobotReadyListener,
        Robot.ConversationViewAttachesListener,
        Robot.WakeupWordListener,
        Robot.ActivityStreamPublishListener,
        Robot.TtsListener,
        OnBeWithMeStatusChangedListener,
        OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener {

    private Robot robot;
    public Locations Data = new Locations();

    /*
        Setting up all the event listeners
     */

    @Override
    protected void onStart() {
        super.onStart();
        robot.addOnRobotReadyListener(this);
        robot.addNlpListener(this);
        robot.addOnBeWithMeStatusChangedListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addConversationViewAttachesListenerListener(this);
        robot.addWakeupWordListener(this);
        robot.addTtsListener(this);
        robot.addOnLocationsUpdatedListener(this);
    }

    /*
        Removing the event listeners upon leaving the app.
     */

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnRobotReadyListener(this);
        robot.removeNlpListener(this);
        robot.removeOnBeWithMeStatusChangedListener(this);
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeConversationViewAttachesListenerListener(this);
        robot.removeWakeupWordListener(this);
        robot.removeTtsListener(this);
        robot.removeOnLocationsUpdateListener(this);
    }

    /*
        Places this application in the top bar for a quick access shortcut.
     */

    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.

        Button btnOne = findViewById(R.id.btnGoToManualTour);
        btnOne.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivity.this, SelectLocation.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    /*
        goTo checks that the location sent is saved then goes to that location.
     */

    public void goTo(View view) {
        Locations.currentLocation = 1;
        robot.speak(TtsRequest.create("I will now take you to the " + Data.LocationData.get(1).Name, true));
    }

    /*
        Hiding keyboard after every button press
     */

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onNlpCompleted(NlpResult nlpResult) {
        //do something with nlp result. Base the action specified in the AndroidManifest.xml
        Toast.makeText(MainActivity.this, nlpResult.action, Toast.LENGTH_SHORT).show();

        switch (nlpResult.action) {
            case "home.welcome":
                robot.tiltAngle(23, 5.3F);
                break;

            case "home.dance":
                long t= System.currentTimeMillis();
                long end = t+5000;
                while(System.currentTimeMillis() < end) {
                    robot.skidJoy(0F, 1F);
                }
                break;

            case "home.sleep":
                robot.goTo("home base");
                break;
        }
    }

    @Override
    public void onWakeupWord(String wakeupWord) {
       // Do anything on wakeup. Follow, go to location, or even try creating dance moves.
    }

    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {

        // Do whatever you like upon the status changing. after the robot finishes speaking
        Log.d("Tag1", "Speech: " + ttsRequest.getSpeech() + ", Status: " + ttsRequest.getStatus().toString());
        if (ttsRequest.getSpeech().equals(("I will now take you to the " + Data.LocationData.get(1).Name)) && ttsRequest.getStatus().toString().equals("COMPLETED")) {
            String str1 = Data.LocationData.get(1).Name;
            robot.goTo(str1.toLowerCase().trim());
        }
        if (ttsRequest.getSpeech().equals(("I will now take you to the " + Data.LocationData.get(2).Name)) && ttsRequest.getStatus().toString().equals("COMPLETED")) {
            String str2 = Data.LocationData.get(2).Name;
            robot.goTo(str2.toLowerCase().trim());
        }
        if (ttsRequest.getSpeech().equals(("I will now take you to the " + Data.LocationData.get(3).Name)) && ttsRequest.getStatus().toString().equals("COMPLETED")) {
            String str3 = Data.LocationData.get(3).Name;
            robot.goTo(str3.toLowerCase().trim());
        }
        if (ttsRequest.getSpeech().equals(("I will now take you to the " + Data.LocationData.get(5).Name)) && ttsRequest.getStatus().toString().equals("COMPLETED")) {
            String str5 = Data.LocationData.get(5).Name;
            robot.goTo(str5.toLowerCase().trim());
        }

    }

    @Override
    public void onBeWithMeStatusChanged(String status) {
      //  When status changes to "lock" the robot recognizes the user and begin to follow.
        switch(status) {
            case "abort":
                // do something i.e. speak
                robot.speak(TtsRequest.create("Abort", false));
                break;

            case "calculating":
                robot.speak(TtsRequest.create("Calculating", false));
                break;

            case "lock":
                robot.speak(TtsRequest.create("Lock", false));
                break;

            case "search":
                robot.speak(TtsRequest.create("search", false));
                break;

            case "start":
                robot.speak(TtsRequest.create("Start", false));
                break;

            case "track":
                robot.speak(TtsRequest.create("Track", false));
                break;
        }
    }


    @Override
    public void onGoToLocationStatusChanged(String location, String status) {
        switch (status) {
            case "start":
//                robot.speak(TtsRequest.create("Starting", false));
                break;

            case "calculating":
//                robot.speak(TtsRequest.create("Calculating", false));
                break;

            case "going":
//                robot.speak(TtsRequest.create("Going", false));
                break;

            case "complete":
//                robot.speak(TtsRequest.create("Completed", false));
                if (location.toLowerCase().equals(Data.LocationData.get(1).Name.toLowerCase())) {
                    robot.speak(TtsRequest.create(Data.LocationData.get(Locations.currentLocation).Description, true));
                    final Intent intent = new Intent(this, BetweenLocationActivity.class);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }

                    }, 1000);
//                    robot.goTo(str1.toLowerCase().trim());
                }
                break;

            case "abort":
//                robot.speak(TtsRequest.create("Cancelled", false));
                break;
        }
    }

    @Override
    public void onConversationAttaches(boolean isAttached) {
        if (isAttached) {
            //Do something as soon as the conversation is displayed.
        }
    }

    @Override
    public void onPublish(ActivityStreamPublishMessage message) {
        //After the activity stream finished publishing (photo or otherwise).
        //Do what you want based on the message returned.
    }

    @Override
    public void onLocationsUpdated(List<String> locations) {

        //Saving or deleting a location will update the list.

        Toast.makeText(this, "Locations updated :\n" + locations, Toast.LENGTH_LONG).show();
    }
}
