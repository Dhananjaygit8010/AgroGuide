package com.mohitingale.agroguide.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mohitingale.agroguide.HomeFragmentActivity.DiseaseDetectionActivity;
import com.mohitingale.agroguide.R;

public class ScanFragment extends Fragment {

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(requireContext(), "Camera permission is required to scan crops", Toast.LENGTH_SHORT)
                            .show();
                }
            });

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar_doctor);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(v -> {
                // Return to home tab in HomeActivity bottom navigation if present, else finish activity
                View bottomNav = requireActivity().findViewById(R.id.homeBottomNav);
                if (bottomNav instanceof BottomNavigationView) {
                    ((BottomNavigationView) bottomNav).setSelectedItemId(R.id.bottomMenuHome);
                } else {
                    requireActivity().finish();
                }
            });
        }

        // Setup Scan Button
        View btnStartScan = view.findViewById(R.id.btn_start_scan);
        if (btnStartScan != null) {
            btnStartScan.setOnClickListener(v -> checkPermissionAndScan());
        }
    }

    private void checkPermissionAndScan() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(requireContext(), DiseaseDetectionActivity.class);
        startActivity(intent);
    }
}