package com.rch.etawah.ui;

import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.rch.etawah.R;
import com.rch.etawah.app.BaseActivity;
import com.rch.etawah.app.SharedPreference;
import com.rch.etawah.app.Utils;

import java.util.List;

public class SplashActivity extends BaseActivity {

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    @SuppressWarnings("deprecation")
    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            //
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(connectionResult -> {
                        // Print Error
                        Utils.showLog("Location error " + connectionResult.getErrorCode());
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(7 * 1000);  //30 * 1000
            locationRequest.setFastestInterval(5 * 1000); //5 * 1000
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(result1 -> {
                final Status status = result1.getStatus();
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(SplashActivity.this, REQUEST_LOCATION);

                        // getActivity().finish();
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                }
            });
        }
    }

    @Override
    protected int getResourcesID() {

        return R.layout.activity_splash;
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null) return false;
        final List<String> providers = mgr.getAllProviders();
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFinishOnTouchOutside(true);
        if (hasGPSDevice(this)) {
            enableLoc();
        }
        // not Supported
        else {
            showToast("Gps not Supported");
        }


        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(() -> {
            if (SharedPreference.getBoolean(IS_LOGIN)) {
                startActivity(DashboardActivity.class, null);
            }
            // login Activity
            else {
                startActivity(LoginActivity.class, null);
            }

            finish();

        }, SPLASH_DISPLAY_LENGTH);

    }

}

