package com.mohitingale.agroguide.GoogleMap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mohitingale.agroguide.R;
import com.mohitingale.agroguide.databinding.ActivityMyLocationBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMyLocationBinding binding;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Marker currentMarker;

    private static final int REQUEST_LOCATION_SERVICE_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityMyLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        findViewById(R.id.fabLayers).setOnClickListener(v -> showMapTypeDialog());

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        checkPermission();
    }

    private void checkPermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    REQUEST_LOCATION_SERVICE_CODE);

        } else {

            startLiveLocation();
        }
    }

    private void startLiveLocation() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }

        locationListener = location -> {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            String address = "Current Location";

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {

                List<Address> list = geocoder.getFromLocation(latitude, longitude, 1);

                if (list != null && !list.isEmpty()) {
                    address = list.get(0).getAddressLine(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            LatLng myLocation = new LatLng(latitude, longitude);

            if (currentMarker != null) {
                currentMarker.remove();
            }

            currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .title(address)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(myLocation, 17));

            mMap.clear();

            currentMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title(address)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            mMap.addCircle(new CircleOptions()
                    .center(myLocation)
                    .radius(60)
                    .strokeWidth(3)
                    .strokeColor(0xFF2196F3)
                    .fillColor(0x332196F3));
        };

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    5,
                    locationListener);

        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    5,
                    locationListener);

        } else {

            Toast.makeText(this,
                    "Please Enable Location",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_SERVICE_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startLiveLocation();

            } else {

                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showMapTypeDialog() {

        BottomSheetDialog dialog = new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(R.layout.bottom_maps, null);

        LinearLayout normal = view.findViewById(R.id.layoutDefault);
        LinearLayout satellite = view.findViewById(R.id.layoutSatellite);
        LinearLayout terrain = view.findViewById(R.id.layoutTerrain);

        normal.setOnClickListener(v -> {mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);dialog.dismiss();
        });

        satellite.setOnClickListener(v -> {mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);dialog.dismiss();
        });

        terrain.setOnClickListener(v -> {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}