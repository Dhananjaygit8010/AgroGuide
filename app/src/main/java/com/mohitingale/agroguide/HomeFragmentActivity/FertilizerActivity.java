package com.mohitingale.agroguide.HomeFragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mohitingale.agroguide.R;

public class FertilizerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer);
        Toast.makeText(FertilizerActivity.this,"Fertilizer Activity",Toast.LENGTH_SHORT).show();
    }
}