package com.rch.etawah.app;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.view.Gravity.CENTER;

public class Utils implements AppConstants {

    public static void showLog(Object o) {
        //if (AppController.getInstance().getBoolean(IS_SHOWING_LOG))
        Log.d(TAG, "Message: " + o);
    }

    @SuppressLint("SimpleDateFormat")
    public static void showDate(Object o) {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
        showLog(o + " " + df.format(Calendar.getInstance().getTime()));
    }

    public static void showLog(Throwable e) {
        //if (AppController.getInstance().getBoolean(IS_SHOWING_LOG))
        Log.d(TAG, "Exception: " + e.getMessage(), e);
    }

    /*public static void rateApp(Context ctx) {
        try {
            Uri uri = Uri.parse("market://details?id=" + ctx.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            ctx.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(PLAY_STORE_APP_URL));
            ctx.startActivity(intent);
            e.printStackTrace();
            showLog(e);
        } catch (Exception e) {
            e.printStackTrace();
            showLog(e);
        }
    }

    public static void shareApp(Context ctx) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, APP_SHARE_MESSAGE);
        sendIntent.setType("text/plain");
        ctx.startActivity(sendIntent);
    }*/

    public static void feedbackApp(Context ctx) {

        String deviceInfo = "\n\n\n\n\n\n\n";
        deviceInfo += "\n KMB Device Info:- ";
        deviceInfo += "\n Brand: " + Build.BRAND;
        deviceInfo += "\n Device: " + Build.DEVICE;
        deviceInfo += "\n Version: " + getVersion(ctx);
        deviceInfo += "\n Manufacturer: " + Build.MANUFACTURER;
        deviceInfo += "\n OS API Level: " + Build.VERSION.SDK_INT;
        deviceInfo += "\n Model (and Product): " + Build.MODEL + " (" + Build.PRODUCT + ")";


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "jitendra7.meo@gmail.com", null));

        emailIntent.putExtra(Intent.EXTRA_TEXT, deviceInfo);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        ctx.startActivity(Intent.createChooser(emailIntent, "KMB"));
    }

    public static String getVersion(Context ctx) {
        try {
            String packageName = ctx.getPackageName();
            return ctx.getPackageManager()
                    .getPackageInfo(packageName,
                            0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            showLog(e);
        }
        return null;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context ctx) {
        ContentResolver resolver = ctx.getContentResolver();
        return Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
    }

    public static void showToast(Context ctx, String msg) {
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        toast.setGravity(CENTER, 0, 0);
        if (!toast.getView().isShown()) {
            toast.setText(msg);
            toast.show();
        }
    }

    public static void chatWhatsApp(Context ctx, String phone) {

        try {
            String pkgWhatsApp = "com.whatsapp";
            String conversation = "com.whatsapp.Conversation";


            String number = String.format("91%s@s.whatsapp.net", phone);
            Intent sendIntent = new Intent(Intent.ACTION_MAIN);
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra("jid", number);
            sendIntent.setType("text/plain");

            sendIntent.setComponent(new ComponentName(pkgWhatsApp, conversation));
            sendIntent.setPackage(pkgWhatsApp);
            ctx.startActivity(sendIntent);

        } catch (Exception e) {
            e.printStackTrace();
            showLog(e);
        }

    }

}
