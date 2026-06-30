package com.mohitingale.agroguide.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mohitingale.agroguide.LoginActivity;
import com.mohitingale.agroguide.ProfileFragmentActivities.EditProfileActivity;
import com.mohitingale.agroguide.R;

public class ProfileFragment extends Fragment {
    ImageView imgProfile;
    TextView txtName, txtPhone, txtEmail, txtAddress;
    Button btnEdit;
    LinearLayout myProfileLogout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfile = view.findViewById(R.id.imgProfile);
        txtName = view.findViewById(R.id.txtName);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtAddress = view.findViewById(R.id.txtAddress);
        btnEdit = view.findViewById(R.id.btnEdit);

        myProfileLogout = view.findViewById(R.id.myProfileLogout);
        myProfileLogout.setOnClickListener(v -> {

            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(requireContext());

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLogin", false);
            editor.apply();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            requireActivity().finish();

        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        loadProfile();
        return view;
    }
    private void loadProfile() {
        if (getActivity() == null) return;

        SharedPreferences sp = getActivity().getSharedPreferences("ProfileData", Context.MODE_PRIVATE);

        txtName.setText(sp.getString("name", "Mohit"));
        txtPhone.setText(sp.getString("phone", "9876543210"));
        txtEmail.setText(sp.getString("email", "mohit@gmail.com"));
        txtAddress.setText(sp.getString("address", "Ahmednagar"));

        String image = sp.getString("image", "");

        if (!image.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(image))
                    .placeholder(R.drawable.farmer)
                    .error(R.drawable.farmer)
                    .into(imgProfile);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
    }
}