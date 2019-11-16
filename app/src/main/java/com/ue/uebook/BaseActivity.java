package com.ue.uebook;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static Runnable mOnWritePermissionGranted;
    private Dialog dialog;
    private Double lattt;

    FusedLocationProviderClient mFusedLocationClient;
    SettingsClient mSettingsClient;
    LocationRequest mLocationRequest;
    LocationSettingsRequest mLocationSettingsRequest;
    LocationCallback mLocationCallback;
    Location mCurrentLocation;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    public static final int REQUEST_GPS_CHECK_SETTINGS = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    protected void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * This method is called to display a loading progress indicator in the screen.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showLoadingIndicator() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_loading_indicator);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * This method is called to hide a progress indicator from the screen.
     */

    public void hideLoadingIndicator() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
            dialog = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This method is invoked to display a snackBar in the screen.
     *
     * @param view    layout in which we want to display snack bar.
     * @param message message that we want to display in snack bar.
     */
    protected void showSnackBar(final View view, final String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    @SuppressLint({"MissingPermission", "RestrictedApi"})
    public void getCurrentLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                updateLocationUI();
                locationlatitude();
                locationlongitude();

            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        startLocationUpdates();


    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        hideLoadingIndicator();
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            enableLocationSettings();

        }
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, null);

                        BaseActivity.this.updateLocationUI();
                        BaseActivity.this.locationlatitude();
                        BaseActivity.this.locationlongitude();
                    }
                });
    }

    /**
     * passing lat and lan data into address funtion
     */
    public String updateLocationUI() {
        String add = "";
        if (mCurrentLocation != null) {

            add = CommonUtils.getAddressFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), this);
            lattt = mCurrentLocation.getLatitude();
            //setStringToText(text_Address, add);
            locationlongitude();
            locationlatitude();
            Log.i(TAG, "Location" + CommonUtils.getAddressFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), this));
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            hideLoadingIndicator();
        }

        return add;

    }

    public Double locationlatitude() {
        Double lat = 0.0;
        if (mCurrentLocation != null) {
            lat = mCurrentLocation.getLatitude();

        }
        return lat;
    }
    public Double locationlongitude() {
        Double longi = 0.0;
        if (mCurrentLocation != null) {
            longi = mCurrentLocation.getLongitude();
        }
        return longi;
    }

    /**
     * Enbling the GPS  setting as runtime
     */
    protected void enableLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse response) {
                        // startUpdatingLocation(...);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception ex) {
                        if (ex instanceof ResolvableApiException) {

                            // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) ex;
                                resolvable.startResolutionForResult(BaseActivity.this, REQUEST_GPS_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                        }
                    }
                });
    }


    /**
     * this method for permission request
     */
    public void PermissionRequest(int request_code) {

        if (request_code == REQUEST_PERMISSIONS_REQUEST_CODE) {
            int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if (readPermission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            callOnWritePermissionGranted();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "User interaction PERMISSION_GRANTED.");
                // Permission granted.
//                getCurrentLocation();
            } else {
                // Permission denied.
                PermissionRequest(REQUEST_PERMISSIONS_REQUEST_CODE);

            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GPS_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
//                getCurrentLocation();

            }
        }
    }

    private void callOnWritePermissionGranted() {
        if (mOnWritePermissionGranted != null) {
            mOnWritePermissionGranted.run();
            mOnWritePermissionGranted = null; //reset to prevent future calls issues...
        }
    }

//    protected void showErrorSnackbar(@StringRes int resId, Exception e, View.OnClickListener clickListener) {
//        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//        if (rootView != null) {
//            ErrorUtils.showSnackbar(rootView, resId, e,
//                    R.string.dialog_retry, clickListener).show();
//        }
//    }


}