package com.example.smrthomegestureapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.VideoView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class GestureDemoViewsActivity extends AppCompatActivity {

    private VideoView videoview;
    private Uri uri;
    private String gestureName;
    private Button replayButton;
    private Button practiceButton;
    private Intent recordVideoIntent;

    public static final String VIDEO_FILE = "videoFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_demo_views);

        // Grab the selected gesture from the previous screen
        Intent intent = getIntent();
        String selectedGesture = intent.getStringExtra(MainActivity.SELECTED_GESTURE);
        // System.out.println(selectedGesture);

        // Grab all the view components from the screen
        videoview = findViewById(R.id.gestureVideoView);
        replayButton = findViewById(R.id.replayButton);
        practiceButton = findViewById(R.id.practiceButton);

        // Set all the listeners

        // TODO: Add cases for all the remaining videos
        switch(selectedGesture) {
            case "Gesture Digit 0":
                gestureName = "Num0";
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.h0);
                break;
            case "Gesture Digit 1":
                gestureName = "Num1";
                uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.h1);
                break;
            default:
                break;
        }

        videoview.setVideoURI(uri);
        videoview.setOnPreparedListener(mp -> videoview.start());

        replayButton.setOnClickListener( v -> {
            videoview.start();
        });

        ActivityResultLauncher<Intent> mGetVideoContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                Uri data = o.getData().getData();
                if(null == data) {
                    System.out.println("I am null!");
                    return;
                    // Handle the error
                }
                // System.out.println(data.toString());
                // Send URI to third screen for upload
                File savedFile = saveFileWithUri(data, gestureName, "1");
                Intent intent = new Intent(GestureDemoViewsActivity.this, UploadVideoActivity.class);
                intent.putExtra(VIDEO_FILE, savedFile);
                startActivity(intent);
            }
        });

        practiceButton.setOnClickListener( v -> {
            recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            recordVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 6);
            // Use MediaStore.EXTRA_OUTPUT to specify how the file should be stored (name, etc.)
            mGetVideoContent.launch(recordVideoIntent);
        });
    }

    private File saveFileWithUri(Uri uri, String gestureType, String practiceNo)
    {
        String fileName = gestureType + "_" + "PRACTICE_" + practiceNo + "_KRISHNAN.mp4";
        File savedFile;

        try (InputStream in = getContentResolver().openInputStream(uri)) {
            savedFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM), fileName);
            if(!savedFile.createNewFile())
            {
                System.out.println("An error occurred in creating the file");
            }
            savedFile.deleteOnExit();
            Files.copy(in, savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("An error occurred when writing out to the file");
            throw new RuntimeException(e);
        }

        return savedFile;
    }
}