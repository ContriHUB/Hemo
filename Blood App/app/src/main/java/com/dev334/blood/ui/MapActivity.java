package com.dev334.blood.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dev334.blood.R;
import com.dev334.blood.database.TinyDB;
import com.dev334.blood.databinding.ActivityMapBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.Location;
import com.here.sdk.core.Point2D;
import com.here.sdk.gestures.TapListener;
import com.here.sdk.mapview.MapError;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapScene;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.mapview.MapView;


public class MapActivity extends AppCompatActivity {

    private ActivityMapBinding binding;
    private final String TAG="mapActivityLog";
    private LocationRequest locationRequest;
    private Double longitude=13.384915;
    private Double latitude=52.530932;
    private MapMarker mapMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.mapView.onCreate(savedInstanceState);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


        MapImage mapImage = MapImageFactory.fromResource(getResources(), R.drawable.place_png);

        GeoCoordinates geoCoordinates=new GeoCoordinates(latitude, longitude);
        mapMarker = new MapMarker(geoCoordinates, mapImage);

        binding.myLocationFab.setOnClickListener(v->{
            getCurrentLocation();
        });

        binding.mapView.setOnReadyListener(new MapView.OnReadyListener() {
            @Override
            public void onMapViewReady() {
                Log.i(TAG, "onMapViewReady: Ready to use map");
            }
        });
        loadMapScene();

        binding.mapView.getGestures().setTapListener(new TapListener() {
            @Override
            public void onTap(@NonNull Point2D point2D) {
                Log.i(TAG, "onTap: "+point2D.x+" "+point2D.y);
                GeoCoordinates geo = binding.mapView.viewToGeoCoordinates(point2D);
                longitude = geo.longitude;
                latitude = geo.latitude;
                setMarker();
            }
        });

        binding.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinyDB=new TinyDB(getApplicationContext());
                tinyDB.putDouble("Latitude", latitude);
                tinyDB.putDouble("Longitude", longitude);
                finish();
            }
        });

        //binding.mapView.pinView(new GeoCoordinates(latitude, longitude));

    }

    private void loadMapScene() {
        // Load a scene from the HERE SDK to render the map with a map scheme.
        binding.mapView.getMapScene().loadScene(MapScheme.NORMAL_NIGHT, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapError mapError) {
                if (mapError == null) {
                    setMapLocation();

                } else {
                    Log.d(TAG, "Loading map failed: mapError: " + mapError.name());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MapActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MapActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();
                                        setMapLocation();
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MapActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MapActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    private void setMapLocation(){
        GeoCoordinates geo=new GeoCoordinates(latitude,longitude);
        double distanceInMeters = 1000 * 10;
        binding.mapView.getCamera().lookAt(
                geo, distanceInMeters);
        binding.mapView.getMapScene().removeMapMarker(mapMarker);
        Log.i(TAG, "setMapLocation: "+latitude+" "+longitude);
        mapMarker.setCoordinates(geo);
        binding.mapView.getMapScene().addMapMarker(mapMarker);
    }

    private void setMarker() {
        GeoCoordinates geo=new GeoCoordinates(latitude,longitude);
        binding.mapView.getMapScene().removeMapMarker(mapMarker);
        Log.i(TAG, "setMapLocation: "+latitude+" "+longitude);
        mapMarker.setCoordinates(geo);
        binding.mapView.getMapScene().addMapMarker(mapMarker);
    }


}