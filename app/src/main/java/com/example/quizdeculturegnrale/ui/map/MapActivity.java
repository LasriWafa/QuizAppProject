package com.example.quizdeculturegnrale.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.quizdeculturegnrale.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleMap mMap;
    private TextView addressTextView;
    private LatLng selectedLocation;
    private ImageButton backButton;
    private Button saveButton;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        try {
            // Check if Google Play Services is available
            if (!isGooglePlayServicesAvailable()) {
                showToast("Google Play Services n'est pas disponible");
                finish();
                return;
            }

            // Initialize views
            addressTextView = findViewById(R.id.addressText);
            backButton = findViewById(R.id.btn_back);
            saveButton = findViewById(R.id.btn_save);

            // Set up click listeners
            backButton.setOnClickListener(v -> finish());
            saveButton.setOnClickListener(v -> saveLocation());

            // Initialize map
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            } else {
                showToast("Erreur lors de l'initialisation de la carte");
                finish();
            }
        } catch (Exception e) {
            showToast("Une erreur est survenue: " + e.getMessage());
            finish();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            
            // Configure map settings
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            
            // Set default location (Paris)
            LatLng paris = new LatLng(48.8566, 2.3522);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 12));

            // Enable location if permission is granted
            if (checkLocationPermission()) {
                enableMyLocation();
            } else {
                requestLocationPermission();
            }

            // Set up map click listener
            mMap.setOnMapClickListener(latLng -> {
                selectedLocation = latLng;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                updateAddressText(latLng);
            });
        } catch (Exception e) {
            showToast("Erreur lors de la configuration de la carte: " + e.getMessage());
            finish();
        }
    }

    private void saveLocation() {
        if (selectedLocation != null) {
            try {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selected_address", addressTextView.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            } catch (Exception e) {
                showToast("Erreur lors de la sauvegarde: " + e.getMessage());
            }
        } else {
            showToast("Veuillez sélectionner une adresse sur la carte");
        }
    }

    private void updateAddressText(LatLng location) {
        if (location == null) return;
        
        executor.execute(() -> {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                mainHandler.post(() -> {
                    try {
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            StringBuilder addressText = new StringBuilder();
                            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                                addressText.append(address.getAddressLine(i));
                                if (i < address.getMaxAddressLineIndex()) {
                                    addressText.append(", ");
                                }
                            }
                            addressTextView.setText(addressText.toString());
                        } else {
                            addressTextView.setText("Adresse non trouvée");
                        }
                    } catch (Exception e) {
                        addressTextView.setText("Erreur lors de l'affichage de l'adresse");
                    }
                });
            } catch (IOException e) {
                mainHandler.post(() -> addressTextView.setText("Erreur lors de la recherche de l'adresse"));
            } catch (Exception e) {
                mainHandler.post(() -> addressTextView.setText("Une erreur est survenue"));
            }
        });
    }

    private void showToast(String message) {
        mainHandler.post(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void enableMyLocation() {
        if (mMap != null && checkLocationPermission()) {
            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                showToast("Permission de localisation révoquée");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                showToast("Permission de localisation refusée");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
} 