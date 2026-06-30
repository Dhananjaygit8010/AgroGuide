package com.mohitingale.agroguide.HomeFragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mohitingale.agroguide.R;

public class LearningCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_center);
        Toast.makeText(LearningCenterActivity.this,"Learning Center Activity",Toast.LENGTH_SHORT).show();
    }
}