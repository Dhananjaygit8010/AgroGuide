package com.mohitingale.agroguide;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AboutUsActivity extends AppCompatActivity {

    VideoView videoView;
    RecyclerView recyclerVideos;

    ArrayList<Integer> videoList = new ArrayList<>();
    ArrayList<String> videoNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        videoView = findViewById(R.id.ivaboutus_vedioview);
        recyclerVideos = findViewById(R.id.recyclerVideos);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Videos
        videoList.add(R.raw.video_1);
        videoList.add(R.raw.video_2);
        videoList.add(R.raw.video_3);
        videoList.add(R.raw.video_4);

        // Names
        videoNames.add("Video 1");
        videoNames.add("Video 2");
        videoNames.add("Video 3");
        videoNames.add("Video 4");

        playVideo(videoList.get(0));

        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        VideoAdapter adapter = new VideoAdapter(videoNames, position -> {
            playVideo(videoList.get(position));
        });

        recyclerVideos.setAdapter(adapter);
    }

    private void playVideo(int video) {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + video);
        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);
        videoView.setMediaController(controller);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}