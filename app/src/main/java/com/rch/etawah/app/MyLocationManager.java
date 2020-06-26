package com.rch.etawah.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import com.rch.etawah.R;

import static com.rch.etawah.app.AppConstants.DELAY;

public final class MyLocationManager extends ContextWrapper {

    private LocationManager service;

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        String provider = getService().getBestProvider(new Criteria(), Boolean.FALSE);
        return getService().getLastKnownLocation(provider);
    }

    public boolean isProviderEnabled() {

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = getService().isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            network_enabled = getService().isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gps_enabled && network_enabled;
    }

    private LocationManager getService() {

        if (service == null) {
            service = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        return service;
    }

    public MyLocationManager(Activity activity) {
        super(activity);
    }

    public void openSettings(final Handler h, final Runnable r) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseContext());
        String message = getResources().getString(R.string.open_location_settings);

        dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(message, (paramDialogInterface, paramInt) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            h.postDelayed(r, DELAY);
        });
        dialog.show();
    }

}
