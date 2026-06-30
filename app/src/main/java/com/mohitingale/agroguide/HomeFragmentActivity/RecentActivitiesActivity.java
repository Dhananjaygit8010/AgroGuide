package com.mohitingale.agroguide.HomeFragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mohitingale.agroguide.R;

public class RecentActivitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_activities);
        Toast.makeText(RecentActivitiesActivity.this,"Recent Activities Activity",Toast.LENGTH_SHORT).show();
    }
}