package com.mohitingale.agroguide.ProfileFragmentActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mohitingale.agroguide.R;

public class EditProfileActivity extends AppCompatActivity {

    ImageView imgProfile;
    Button btnChooseImage, btnSave;
    EditText etName, etPhone, etEmail, etAddress;

    Uri imageUri;

    ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri;
                    Glide.with(this).load(uri).into(imgProfile);

                    // Take persistable URI permission to access it later
                    final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    try {
                        getContentResolver().takePersistableUriPermission(uri, takeFlags);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imgProfile = findViewById(R.id.editImgProfile);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSave = findViewById(R.id.btnSaveProfile);

        etName = findViewById(R.id.editName);
        etPhone = findViewById(R.id.editPhone);
        etEmail = findViewById(R.id.editEmail);
        etAddress = findViewById(R.id.editAddress);

        SharedPreferences sp = getSharedPreferences("ProfileData", MODE_PRIVATE);

        etName.setText(sp.getString("name", ""));
        etPhone.setText(sp.getString("phone", ""));
        etEmail.setText(sp.getString("email", ""));
        etAddress.setText(sp.getString("address", ""));

        String image = sp.getString("image", "");

        if (!image.isEmpty()) {
            imageUri = Uri.parse(image);
            Glide.with(this).load(imageUri).error(R.drawable.farmer).into(imgProfile);
        }

        btnChooseImage.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

        btnSave.setOnClickListener(v -> {

            SharedPreferences.Editor editor = sp.edit();

            editor.putString("name", etName.getText().toString());
            editor.putString("phone", etPhone.getText().toString());
            editor.putString("email", etEmail.getText().toString());
            editor.putString("address", etAddress.getText().toString());

            if (imageUri != null) {
                editor.putString("image", imageUri.toString());
            }

            editor.apply();

            Toast.makeText(EditProfileActivity.this,
                    "Profile Updated Successfully",
                    Toast.LENGTH_SHORT).show();

            finish();

        });

    }
}