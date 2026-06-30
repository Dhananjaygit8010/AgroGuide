package com.mohitingale.agroguide.ProfileFragmentActivities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mohitingale.agroguide.R;

public class EmailAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_address);
        Toast.makeText(EmailAddressActivity.this, "Email Address Activity", Toast.LENGTH_SHORT).show();
    }
}