package com.mohitingale.agroguide;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MusicActivity extends AppCompatActivity {

    TextView tvSongName, tvstartTime, tvEndTime;
    ImageView ivSongCover;

    ImageButton btnPrevious, btnbackward, btnPlay, btnForward, btnNext;

    SeekBar seekBar;
    ListView listSongs;

    MediaPlayer mediaPlayer;

    Handler handler = new Handler();

    int currentSong = 0;

    private final int[] songs = {
            R.raw.paifukata,
            R.raw.tuaabhal,
            R.raw.rajalalkari,
            R.raw.tufanaalaya,
            R.raw.kalyamatit
    };

    private final String[] songNames = {
            "Pai Fukata",
            "Tu Aabahl",
            "Raja Lalkari",
            "Tufan Aalaya",
            "Kalya matit matit"
    };

    private final int[] images = {
            R.drawable.pai_fukata,
            R.drawable.tu_aabhal,
            R.drawable.raja_lalkari,
            R.drawable.tufan_aalaya,
            R.drawable.kalya_matit
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        tvSongName = findViewById(R.id.tvEntertainmentSongName);
        tvstartTime = findViewById(R.id.tvEntertenmentsongstarttime);
        tvEndTime = findViewById(R.id.tvEntertenmentsongendtime);

        ivSongCover = findViewById(R.id.ivEntertenmentsongcoverpage);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnPrevious = findViewById(R.id.ibEntertenmentPreviousSong);
        btnbackward = findViewById(R.id.ibEntertenmentBackwardSong);
        btnPlay = findViewById(R.id.ibEntertainmentPlay);
        btnForward = findViewById(R.id.ibEnertenmentForward);
        btnNext = findViewById(R.id.ibEntertenmentNext);

        seekBar = findViewById(R.id.sbEntertenmentSongProgress);

        listSongs = findViewById(R.id.listSongs);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        songNames);

        listSongs.setAdapter(adapter);

        listSongs.setOnItemClickListener((parent, view1, position, id) -> {

            currentSong = position;

            loadSong(currentSong);

        });

        loadSong(currentSong);

        btnPlay.setOnClickListener(v -> {

            if (mediaPlayer == null)
                return;

            if (mediaPlayer.isPlaying()) {

                mediaPlayer.pause();

                btnPlay.setImageResource(R.drawable.play_icon);

            } else {

                mediaPlayer.start();

                btnPlay.setImageResource(R.drawable.pause_icon);

            }

        });

        btnNext.setOnClickListener(v -> {

            currentSong++;

            if (currentSong >= songs.length) {

                currentSong = 0;

            }

            loadSong(currentSong);

        });

        btnPrevious.setOnClickListener(v -> {

            currentSong--;

            if (currentSong < 0) {

                currentSong = songs.length - 1;

            }

            loadSong(currentSong);

        });

        btnForward.setOnClickListener(v -> {

            if (mediaPlayer != null) {

                mediaPlayer.seekTo(
                        mediaPlayer.getCurrentPosition() + 5000);

            }

        });

        btnbackward.setOnClickListener(v -> {

            if (mediaPlayer != null) {

                mediaPlayer.seekTo(
                        Math.max(
                                mediaPlayer.getCurrentPosition() - 5000,
                                0));

            }

        });

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress,
                                                  boolean fromUser) {

                        if (fromUser && mediaPlayer != null) {

                            mediaPlayer.seekTo(progress);

                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });
    }
    private void loadSong(int index) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(this, songs[index]);

        tvSongName.setText("Song Name :- " + songNames[index]);
        ivSongCover.setImageResource(images[index]);

        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());

        tvstartTime.setText("00:00");
        tvEndTime.setText(formatTime(mediaPlayer.getDuration()));

        mediaPlayer.start();

        btnPlay.setImageResource(R.drawable.pause_icon);

        handler.removeCallbacks(updateSeekBar);
        handler.post(updateSeekBar);

        mediaPlayer.setOnCompletionListener(mp -> {

            currentSong++;

            if (currentSong >= songs.length) {
                currentSong = 0;
            }

            loadSong(currentSong);

        });

    }

    private final Runnable updateSeekBar = new Runnable() {

        @Override
        public void run() {

            if (mediaPlayer != null) {

                try {

                    if (mediaPlayer.isPlaying()) {

                        seekBar.setProgress(mediaPlayer.getCurrentPosition());

                        tvstartTime.setText(
                                formatTime(mediaPlayer.getCurrentPosition()));

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

                handler.postDelayed(this, 1000);

            }

        }

    };

    private String formatTime(int milliseconds) {

        int minutes = (milliseconds / 1000) / 60;

        int seconds = (milliseconds / 1000) % 60;

        return String.format("%02d:%02d", minutes, seconds);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(updateSeekBar);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}