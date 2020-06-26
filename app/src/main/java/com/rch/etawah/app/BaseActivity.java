package com.rch.etawah.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

import static android.util.Patterns.EMAIL_ADDRESS;
import static android.view.Gravity.CENTER;

@SuppressWarnings("All")
public abstract class BaseActivity extends AppCompatActivity
        implements AppConstants {

    private Toast toast = null;
    private Activity activity = null;

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    protected Activity getActivity() {
        if (activity == null) activity = this;
        return activity;
    }

    protected abstract int getResourcesID();

    protected String getValue(EditText e) {

        return e.getText().toString().trim();
    }

    public void showToast(String message) {
        if (toast == null) toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(CENTER, 0, 0);

        if (!toast.getView().isShown()) {
            toast.setText(message);
            toast.show();
        }
    }

    protected boolean isRequired(EditText e) {
        if (TextUtils.isEmpty(getValue(e))) {
            e.setError("Required");
            e.requestFocus();
            return false;
        }
        return true;
    }

    protected boolean isValidEmail(EditText e) {
        if (!EMAIL_ADDRESS.matcher(getValue(e)).matches()) {
            e.setError("Invalid");
            e.requestFocus();
            return false;
        }
        return true;
    }

    protected boolean isRequired(EditText e, int l) {
        String txtValue = getValue(e);
        boolean b = txtValue.length() > l;
        if (TextUtils.isEmpty(txtValue)) {
            e.setError("Required");
            e.requestFocus();
            return false;
        } else if (!b) {
            e.setError("Invalid");
            e.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourcesID());
        ButterKnife.bind(this);

        setRequestedOrientation(SCREEN_ORIENTATION);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
    }

}
