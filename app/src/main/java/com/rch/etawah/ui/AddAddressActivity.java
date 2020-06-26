package com.rch.etawah.ui;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.rch.etawah.Adapters.SearchAdapter;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.Constans.RecyclerTouchListener;
import com.rch.etawah.ModelClass.SearchModel;
import com.rch.etawah.R;
import com.rch.etawah.activity.SelectAddress;
import com.rch.etawah.app.ApiClient;
import com.rch.etawah.app.BaseActivity;
import com.rch.etawah.app.MyLocationManager;
import com.rch.etawah.app.Utils;
import com.rch.etawah.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.CityListUrl;
import static com.rch.etawah.Config.BaseURL.SocietyListUrl;

public class AddAddressActivity extends BaseActivity implements
        OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, Runnable {


    private Marker marker;
    private GoogleMap mGoogleMap;
    private MyLocationManager mLocation;
    private Handler mHandler = new Handler();

    private double latitude = 0.0;
    private double longitude = 0.0;
    private final static int MAX_RESULT = 1;
    private final static int GET_ADDRESS = 0;
    private final static int LOCATION_SETTINGS = 3;

    @Override
    public void run() {
        try {
            if (mGoogleMap.getMyLocation() != null) {
                Utils.showLog("Runnable Called");
                onMapLoaded();
            } else {
                if (mLocation.isProviderEnabled()) {

                    showToast("fetching locations");
                    mHandler.postDelayed(this, DELAY);
                    Utils.showLog("Address Manager Called");
                } else {
                    mLocation.openSettings(mHandler, this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showLog(e);
        }
    }

    @Override
    public void onMapLoaded() {
        try {
            Location location = mGoogleMap.getMyLocation();
            Utils.showLog("onMapLoaded Called");

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            BitmapDescriptor icon = getMarkerIcon(R.drawable.ic_location);
            marker = mGoogleMap.addMarker(new MarkerOptions()
                    .title("Live Address")
                    .position(latLng)
                    .icon(icon));

            marker.showInfoWindow();
            mGoogleMap.setOnCameraChangeListener(camPosition -> {
                latitude = camPosition.target.latitude;
                longitude = camPosition.target.longitude;
                setNewLocationAddress(latitude, longitude);
                marker.setPosition(new LatLng(latitude, longitude));
            });
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

        } catch (NullPointerException e) {
            mHandler.postDelayed(this, DELAY);
            Utils.showLog("onMapLoaded Null");
            displayLocationSettingsRequest();
            e.printStackTrace();
            Utils.showLog(e);
        }
    }

    @Override
    protected int getResourcesID() {

        return R.layout.activity_add_address2;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        /*String message = "Required permission for searching location";
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        Permissions.check(getActivity(), permissions, message, null, new PermissionHandler() {
            @Override
            public void onGranted() {*/
        displayLocationSettingsRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapLoadedCallback(AddAddressActivity.this);

        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
          /*  }
        });*/
    }

    private void displayLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Utils.showLog("All location settings are satisfied.");
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Utils.showLog("Address settings are not satisfied. Show the user a dialog to upgrade location settings ");

                    try {
                        status.startResolutionForResult(getActivity(), LOCATION_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Utils.showLog("PendingIntent unable to execute request.");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Utils.showLog("Address settings are inadequate, and cannot be fixed here. Dialog not created.");
                    break;
            }
        });
    }

    private BitmapDescriptor getMarkerIcon(int id) {
        Drawable drawable = getResources().getDrawable(id);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();


        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mLocation = new MyLocationManager(getActivity());

        assert mFragment != null;
        mFragment.getMapAsync(this);


        init();
    }

    private void setNewLocationAddress(double latitude, double longitude) {

        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, MAX_RESULT);

            Address mAddress = addresses.get(GET_ADDRESS);
            Area.setText(mAddress.getSubAdminArea());
            pinCode.setText(mAddress.getPostalCode());
            city.setText(mAddress.getLocality());
            state.setText(mAddress.getAdminArea());

        } catch (IOException | NullPointerException | IndexOutOfBoundsException e) {
            showToast("Geo coder not working");
            e.printStackTrace();
            Utils.showLog(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == LOCATION_SETTINGS && resultCode == Activity.RESULT_CANCELED) {
            displayLocationSettingsRequest();
        }

    }


    Session_management session_management;
    LinearLayout back;
    Button Save, EditBtn;
    EditText pinCode, houseNo, Area, city, state, landmaark, name, mobNo, alterMob;
    TextInputLayout tpinCode, thouseNo, tArea, tcity, tstate, tlandmaark, tname, tmobNo, talterMob;

    CardView currentLoc;
    String user_id;
    String city_id;
    RecyclerView recyclerViewCity, recyclerViewSociety;
    String cityId, cityName, socetyId, SocetyName, landmaarkkkk, updtae, addressId;
    ProgressDialog progressDialog;
    SearchAdapter cityAdapter, societyAdapter;
    List<SearchModel> citylist = new ArrayList<>();
    List<SearchModel> societylist = new ArrayList<>();

    private void init() {
        session_management = new Session_management(AddAddressActivity.this);

        back = findViewById(R.id.back);
        Save = findViewById(R.id.SaveBtn);
        EditBtn = findViewById(R.id.EditBtn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        if (updtae != "") {
            updtae = getIntent().getStringExtra("update");
            addressId = getIntent().getStringExtra("addId");
            //     Log.d("fgh",addressId);

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        Session_management sessionManagement = new Session_management(getApplicationContext());
        user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);


        currentLoc = findViewById(R.id.currentLoc);

        recyclerViewCity = findViewById(R.id.recyclerCity);
        recyclerViewSociety = findViewById(R.id.recyclerSociety);

        tpinCode = findViewById(R.id.input_layout_pinCode);
        thouseNo = findViewById(R.id.input_layout_HOuseNo);
        tArea = findViewById(R.id.input_layout_area);
        tcity = findViewById(R.id.input_layout_CIty);
        tstate = findViewById(R.id.input_layout_state);
        tlandmaark = findViewById(R.id.input_layout_landmark);
        tname = findViewById(R.id.input_layout_NAme);
        tmobNo = findViewById(R.id.input_layout_mobNo);
        talterMob = findViewById(R.id.input_layout_AltermobileNO);
        pinCode = (EditText) findViewById(R.id.input_pinCode);
        houseNo = (EditText) findViewById(R.id.input_HouseNO);
        Area = (EditText) findViewById(R.id.input_area);
        city = (EditText) findViewById(R.id.input_city);
        state = (EditText) findViewById(R.id.input_state);
        landmaark = (EditText) findViewById(R.id.input_landmark);
        name = (EditText) findViewById(R.id.input_NAme);
        mobNo = (EditText) findViewById(R.id.input_mobNO);
        alterMob = (EditText) findViewById(R.id.input_AltermobileNO);


        if (isOnline()) {


            Area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    societyURl(cityId);
                    progressDialog.dismiss();

                }
            });

            city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    cityUrl();
                    progressDialog.dismiss();
                }
            });
            recyclerViewCity.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewCity, new RecyclerTouchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    cityId = citylist.get(position).getId();
                    cityName = citylist.get(position).getpNAme();
                    city.setText(cityName);
                    recyclerViewCity.setVisibility(View.GONE);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
            recyclerViewSociety.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewSociety, new RecyclerTouchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    socetyId = societylist.get(position).getId();
                    SocetyName = societylist.get(position).getpNAme();
                    Area.setText(SocetyName);
                    recyclerViewSociety.setVisibility(View.GONE);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
        }
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pinCode.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter Pincode", Toast.LENGTH_SHORT).show();
                }
                //
                else if (houseNo.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter House No., Building Name", Toast.LENGTH_SHORT).show();
                }
                //
                else if (Area.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter Area, Colony", Toast.LENGTH_SHORT).show();
                }
                //
                else if (city.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_SHORT).show();
                }
                //
                else if (state.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter State", Toast.LENGTH_SHORT).show();
                }
                //
                else if (name.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter your Name", Toast.LENGTH_SHORT).show();
                }
                //
                else if (mobNo.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter Mobile No.", Toast.LENGTH_SHORT).show();
                }
                //
                else if (!isOnline()) {
                    Toast.makeText(getApplicationContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();

                } else {
                    if (TextUtils.isEmpty(landmaarkkkk)) {
                        landmaarkkkk = "NA";
                    } else {
                        landmaarkkkk = landmaark.getText().toString();

                    }

                    HashMap<String, String> param = new HashMap<>();
                    param.put("user_id", session_management.getUserDetails().get(BaseURL.KEY_ID));
                    param.put("receiver_name", name.getText().toString());
                    param.put("receiver_phone", mobNo.getText().toString());
                    param.put("city_name", city.getText().toString());
                    param.put("society_name", Area.getText().toString());
                    param.put("house_no", houseNo.getText().toString());
                    param.put("landmark", landmaarkkkk);
                    param.put("state", state.getText().toString());
                    param.put("pin", pinCode.getText().toString());
                    param.put("lat", String.valueOf(latitude));
                    param.put("lng", String.valueOf(longitude));

                    ApiClient.getInstance(getActivity(), response -> {
                        try {
                            JSONObject jsonObject = new JSONObject((String) response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("1")) {


                                Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), SelectAddress.class);
                                startActivity(intent);
                                finish();

                            } else {
                                showToast(msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }).getApiResponse(ADD_ADDRESS, param);
                }

            }
        });

    }

    private void cityUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CityListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cityyyyyyyy", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        citylist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            city_id = jsonObject1.getString("city_id");
                            String city_name = jsonObject1.getString("city_name");

                            recyclerViewCity.setVisibility(View.VISIBLE);
                            SearchModel cs = new SearchModel(city_id, city_name);
                            citylist.add(cs);
                        }
                        cityAdapter = new SearchAdapter(citylist);
                        recyclerViewCity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerViewCity.setAdapter(cityAdapter);
                        cityAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddressActivity.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void societyURl(String cityId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SocietyListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("socirrty", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        societylist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String society_id = jsonObject1.getString("society_id");
                            String society_name = jsonObject1.getString("society_name");
                            recyclerViewSociety.setVisibility(View.VISIBLE);
                            SearchModel ss = new SearchModel(society_id, society_name);
                            societylist.add(ss);
                        }
                        societyAdapter = new SearchAdapter(societylist);
                        recyclerViewSociety.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerViewSociety.setAdapter(societyAdapter);
                        societyAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("city_id", cityId);
                Log.d("ddd", cityId);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddAddressActivity.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

}
