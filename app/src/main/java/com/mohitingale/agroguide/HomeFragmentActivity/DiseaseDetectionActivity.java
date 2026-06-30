package com.mohitingale.agroguide.HomeFragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.mohitingale.agroguide.R;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DiseaseDetectionActivity extends AppCompatActivity {

    private static final String TAG = "DiseaseDetection";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";

    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private PreviewView viewFinder;
    private Camera camera;
    private boolean isTorchOn = false;
    private ImageButton btnFlash;
    private ImageButton btnSwitchCamera;
    
    // Default camera selector is Back camera
    private CameraSelector currentCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

    // Permission Launcher
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Toast.makeText(this, "Camera permission required for scanning", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

    // Gallery Launcher
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    if (selectedImage != null) {
                        openResultActivity(selectedImage);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Edge-to-Edge
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_disease_detection);

        // Initialize Views
        viewFinder = findViewById(R.id.viewFinder);
        View btnBack = findViewById(R.id.btnBack);
        View btnCapture = findViewById(R.id.btnCapture);
        View btnGallery = findViewById(R.id.btnGallery);
        btnFlash = findViewById(R.id.btnFlash);
        btnSwitchCamera = findViewById(R.id.btnSwitchCamera);

        // Initialize Camera Executor
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Check Permissions and Start Camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        // Back Button Action
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Torch Toggle
        if (btnFlash != null) {
            btnFlash.setOnClickListener(v -> toggleTorch());
        }

        // Switch Camera Action
        if (btnSwitchCamera != null) {
            btnSwitchCamera.setOnClickListener(v -> switchCamera());
        }

        // Gallery Button Action
        if (btnGallery != null) {
            btnGallery.setOnClickListener(v -> openGallery());
        }

        // Capture Button Action
        if (btnCapture != null) {
            btnCapture.setOnClickListener(v -> takePhoto());
        }

        // Tap-to-Focus
        setupTapToFocus();

        // Start Viewfinder scan line animation
        View scanLine = findViewById(R.id.viewScanLine);
        if (scanLine != null) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.scan_line_anim);
            scanLine.startAnimation(anim);
        }
    }

    /**
     * Toggle the camera torch (flashlight) on/off.
     */
    private void toggleTorch() {
        if (camera == null) return;
        if (!camera.getCameraInfo().hasFlashUnit()) {
            Toast.makeText(this, "Flash not available on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        isTorchOn = !isTorchOn;
        camera.getCameraControl().enableTorch(isTorchOn);

        if (btnFlash != null) {
            btnFlash.setImageResource(isTorchOn ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
        }
    }

    /**
     * Switch between Front and Back camera.
     */
    private void switchCamera() {
        if (currentCameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            currentCameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        } else {
            currentCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        }
        startCamera();
    }

    /**
     * Setup tap-to-focus: user taps the preview to focus on that point.
     */
    private void setupTapToFocus() {
        if (viewFinder == null) return;

        viewFinder.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (camera == null) return false;

                MeteringPointFactory factory = viewFinder.getMeteringPointFactory();
                MeteringPoint point = factory.createPoint(event.getX(), event.getY());

                FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                        .setAutoCancelDuration(3, TimeUnit.SECONDS)
                        .build();

                camera.getCameraControl().startFocusAndMetering(action);

                // Show visual tap pulse feedback on scan box
                View focusRing = findViewById(R.id.viewScanBox);
                if (focusRing != null) {
                    focusRing.animate()
                            .scaleX(0.92f)
                            .scaleY(0.92f)
                            .setDuration(150)
                            .withEndAction(() -> focusRing.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(150)
                                    .start())
                            .start();
                }

                v.performClick();
                return true;
            }
            return false;
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Viewfinder preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                // Quality-optimized image capturing
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build();

                try {
                    cameraProvider.unbindAll();
                    camera = cameraProvider.bindToLifecycle(
                            this, currentCameraSelector, preview, imageCapture);

                } catch (Exception exc) {
                    Log.e(TAG, "Use case binding failed", exc);
                }

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera provider initialization failed", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) return;

        // Display timestamped capture configuration
        String name = new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/AgroGuide-AI");
        }

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues).build();

        // Capture photo with standard CameraX callback
        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri savedUri = outputFileResults.getSavedUri();
                        if (savedUri != null) {
                            openResultActivity(savedUri);
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e(TAG, "Photo capture failed: " + exception.getMessage(), exception);
                        Toast.makeText(DiseaseDetectionActivity.this, "Photo capture failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openResultActivity(Uri imageUri) {
        Intent intent = new Intent(DiseaseDetectionActivity.this, ScanResultActivity.class);
        intent.putExtra("image_uri", imageUri.toString());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
}