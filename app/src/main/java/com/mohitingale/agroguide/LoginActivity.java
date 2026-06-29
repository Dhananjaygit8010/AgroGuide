package com.mohitingale.agroguide;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    LottieAnimationView lottie_leaf;

    TextInputEditText etLoginUsername, etLoginPassword;
    Button btn_login;
    CheckBox cbshowhidepass;

    TextView tvForget_pass,tvregister;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = preferences.edit();
        if (preferences.getBoolean("isLogin",false))
        {
            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }


        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);

        btn_login = findViewById(R.id.btn_login);
//        cbshowhidepass = findViewById(R.id.cbshowhidepass);

        lottie_leaf = findViewById(R.id.lottie_leaf);
        lottie_leaf.setRepeatCount(LottieDrawable.INFINITE);
        lottie_leaf.playAnimation();
//
//        cbshowhidepass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//                    etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                } else {
//                    etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }
//            }
//        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etLoginUsername.getText().toString().isEmpty())
                {
                    etLoginUsername.setError("Please Enter Your Username.");
                }
                else if (etLoginUsername.getText().toString().length() < 8)
                {
                    etLoginUsername.setError("Username must be more than 8");
                }
                else if (etLoginPassword.getText().toString().isEmpty())
                {
                    etLoginPassword.setError("Please Enter Your Password");
                }
                else if (etLoginPassword.getText().toString().length() < 8)
                {
                    etLoginPassword.setError("Password must be more than 8");
                }
                else if (!etLoginUsername.getText().toString().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$"))
                {
                    etLoginUsername.setError("Username must contain Uppercase, Lowercase, Number and Special Character");
                }
                else if (!etLoginPassword.getText().toString().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$"))
                {
                    etLoginPassword.setError("Password must contain Uppercase, Lowercase, Number and Special Character");
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Login Successfully Done", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                    editor.putBoolean("isLogin",true).commit();
                    startActivity(i);
                    finishAffinity();
                }
            }
        });

        tvForget_pass=findViewById(R.id.tvForget_pass);
        tvForget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(i);
                finish();
            }
        });
        tvregister=findViewById(R.id.tvregister);
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}