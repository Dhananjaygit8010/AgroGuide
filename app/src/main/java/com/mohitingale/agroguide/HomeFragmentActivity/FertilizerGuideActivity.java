package com.mohitingale.agroguide.HomeFragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mohitingale.agroguide.ForgetActivity;
import com.mohitingale.agroguide.R;

public class FertilizerGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_guide);
        Toast.makeText(FertilizerGuideActivity.this,"Fertilizer Guide Activity",Toast.LENGTH_SHORT).show();
    }
}