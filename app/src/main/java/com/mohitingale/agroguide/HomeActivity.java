package com.mohitingale.agroguide;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.res.ColorStateList;
import android.graphics.Color;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mohitingale.agroguide.Fragment.HomeFragment;
import com.mohitingale.agroguide.Fragment.MyFarmFragment;
import com.mohitingale.agroguide.Fragment.ProfileFragment;
import com.mohitingale.agroguide.Fragment.ReportsFragment;
import com.mohitingale.agroguide.Fragment.ScanFragment;
import com.mohitingale.agroguide.GoogleMap.MyLocationActivity;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean doubleTap = false;
    BottomNavigationView homeBottomNav;

    DrawerLayout drawerlayout;
    ImageView ivmenubtn;
    NavigationView navigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drawerlayout = findViewById(R.id.drawerlayout);
        ivmenubtn = findViewById(R.id.ivmenubtn);
        navigationView = findViewById(R.id.navigationView);

        ivmenubtn.setOnClickListener(v -> {
            drawerlayout.openDrawer(GravityCompat.START);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.myProfileFragment)
            {
               getSupportFragmentManager().beginTransaction().replace(R.id.myProfileFragment,new ProfileFragment()).commit();
            }
            else if (item.getItemId() == R.id.menuhomemylocation)
            {
                Intent i = new Intent(HomeActivity.this, MyLocationActivity.class);
                startActivity(i);
            }
            else if (item.getItemId() == R.id.menuhomesettings)
            {
                Intent i = new Intent(HomeActivity.this,SettingsActivity.class);
                startActivity(i);
                Toast.makeText(HomeActivity.this, "Settings Click", Toast.LENGTH_SHORT).show();
            }
            else if (item.getItemId() == R.id.menuhomecontactus)
            {
                Intent i = new Intent(HomeActivity.this,ContactUsActivity.class);
                startActivity(i);
                Toast.makeText(HomeActivity.this, "Contact Us Click", Toast.LENGTH_SHORT).show();
            }
            else if (item.getItemId() == R.id.menuhomeaboutus)
            {
                Intent i = new Intent(HomeActivity.this,AboutUsActivity.class);
                startActivity(i);
                Toast.makeText(HomeActivity.this, "About Us Click", Toast.LENGTH_SHORT).show();
            }
            else if (item.getItemId() == R.id.menuhomelogout)
            {
                logout();
            }
            drawerlayout.closeDrawer(GravityCompat.START);
            return true;

        });



        homeBottomNav=findViewById(R.id.homeBottomNav);
        homeBottomNav.setItemActiveIndicatorColor(ColorStateList.valueOf(Color.parseColor("#1A2E7D32")));
        homeBottomNav.setOnNavigationItemSelectedListener(this);
        homeBottomNav.setSelectedItemId(R.id.bottomMenuHome);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);


        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        editor = preferences.edit();

        boolean isFirstTime = preferences.getBoolean("isFirstTime",true);
        if (isFirstTime)
        {
            welcome();
        }

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (doubleTap)
                        {
                            finishAffinity();
                        }
                        else
                        {
                            Toast.makeText(HomeActivity.this, "Double tap to exit app",Toast.LENGTH_SHORT).show();
                            doubleTap = true;

                            new Handler(Looper.getMainLooper()).postDelayed(
                                    () -> doubleTap = false,
                                    2000
                            );
                        }
                    }
                }
        );
    }

    private void welcome()
    {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("AgroGuide");
        ad.setMessage("Welcome to AgroGuide");
        ad.setPositiveButton("Thank You", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        }).show().create();

        editor.putBoolean("isFirstTime",false).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    private void logout()
    {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("Logout");
        ad.setMessage("Are you sure,You want to Logout?");
        ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent i = new Intent(HomeActivity.this,LoginActivity.class);
                editor.putBoolean("isLogin",false).commit();
                startActivity(i);
                finishAffinity();
            }
        });

        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show().create();
    }

    HomeFragment homeFragment = new HomeFragment();
    MyFarmFragment myFarmFragment = new MyFarmFragment();
    ScanFragment scanFragment = new ScanFragment();
    ReportsFragment reportsFragment = new ReportsFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.bottomMenuHome)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, new HomeFragment()).commit();
        }
        else if (item.getItemId() == R.id.bottomMenuMyfarm)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,new MyFarmFragment()).commit();
        }
        else if (item.getItemId() == R.id.bottomMenuScan)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,new ScanFragment()).commit();
        }
        else if (item.getItemId() == R.id.bottomMenuProfile)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,new ProfileFragment()).commit();
        }

        return true;
    }
}
