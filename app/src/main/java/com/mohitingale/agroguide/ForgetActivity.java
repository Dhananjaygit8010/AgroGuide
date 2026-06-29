package com.mohitingale.agroguide;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ForgetActivity extends AppCompatActivity
{
    @Override
    public void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Toast.makeText(ForgetActivity.this,"Forget Activity",Toast.LENGTH_SHORT).show();
    }
}
