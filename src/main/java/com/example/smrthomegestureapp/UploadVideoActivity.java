package com.example.smrthomegestureapp;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UploadVideoActivity extends AppCompatActivity {

    private VideoView replayVideo;
    private Button uploadVideo;
    private HttpURLConnection urlConnection;
    private File userVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        // Grab the user recorded video from the previous screen
        Intent intent = getIntent();
        userVideo = intent.getSerializableExtra(GestureDemoViewsActivity.VIDEO_FILE, File.class);

        // Grab the required UI elements
        replayVideo = findViewById(R.id.replayVideo);
        uploadVideo = findViewById(R.id.uploadButton);

        // Play the user recorded video
        replayVideo.setVideoPath(userVideo.getPath());
        replayVideo.setOnPreparedListener(mp -> replayVideo.start());

        // On click listener to upload video to server
        uploadVideo.setOnClickListener(v -> {
            uploadVideo(userVideo);
        });

    }

    private void uploadVideo(File videoFile) {
        String url = "http://10.0.0.249:5000";

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", videoFile.getName(), RequestBody.create(MediaType.parse("video/*"), videoFile))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("successfully uploaded video!");
                // Return user back to screen 1
                Intent intent = new Intent(UploadVideoActivity.this, MainActivity.class);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }







}