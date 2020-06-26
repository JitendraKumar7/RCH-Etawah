package com.rch.etawah.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rch.etawah.activity.CategoryPage;
import com.rch.etawah.activity.MainActivity;
import com.rch.etawah.Adapters.DealsAdapter;
import com.rch.etawah.Adapters.HomeCategoryAdapter;
import com.rch.etawah.Adapters.Home_adapter;
import com.rch.etawah.Adapters.PageAdapter;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.Constans.RecyclerTouchListener;
import com.rch.etawah.ModelClass.CategoryGrid;
import com.rch.etawah.ModelClass.Category_model;
import com.rch.etawah.ModelClass.HomeCate;
import com.rch.etawah.R;
import com.rch.etawah.app.AppController;
import com.rch.etawah.util.CustomSlider;
import com.rch.etawah.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;
import static com.rch.etawah.Config.BaseURL.ADDRESS;
import static com.rch.etawah.Config.BaseURL.BANNER;
import static com.rch.etawah.Config.BaseURL.BANN_IMG_URL;
import static com.rch.etawah.Config.BaseURL.CITY;
import static com.rch.etawah.Config.BaseURL.LAT;
import static com.rch.etawah.Config.BaseURL.LONG;
import static com.rch.etawah.Config.BaseURL.MyPrefreance;
import static com.rch.etawah.Config.BaseURL.secondary_banner;

public class HomeeeFragment extends Fragment implements View.OnClickListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    PageAdapter pageAdapter;

    TabItem tab1, tab2, tab3, tab4;
    Float translationY = 100f;
    FloatingActionButton fabMain, fabOne, fabTwo, fabThree, fabfour;

    LinearLayout Search_layout;
    ScrollView scrollView;
    RecyclerView rv_items;
    SliderLayout banner_slider, featuredslider, home_list_banner;

    OvershootInterpolator interpolator = new OvershootInterpolator();

    private static final String TAG1 = "MainActivity";

    Boolean isMenuOpen = false;

    RecyclerView recyclerViewCate, recyclerViewDEal;
    HomeCategoryAdapter cateListAdapter;
    private List<HomeCate> cateList = new ArrayList<>();
    DealsAdapter dealAdapter;
    private List<CategoryGrid> dealList = new ArrayList<>();
    private List<Category_model> category_modelList = new ArrayList<>();
    private Home_adapter adapter;
    private boolean isSubcat = false;
    String productId;


    TextView loc;
    List<Address> addresses = new ArrayList<>();
    String latitude, longitude, address, city, state, country, postalCode;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;

    public HomeeeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));

        sharedPreferences = getActivity().getSharedPreferences(MyPrefreance, MODE_PRIVATE);
        latitude = sharedPreferences.getString(LAT, null);
        longitude = sharedPreferences.getString(LONG, null);
        address = sharedPreferences.getString(ADDRESS, null);
        city = sharedPreferences.getString(CITY, null);

        // loc.setText(address+", "+city+", "+postalCode);
        rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        home_list_banner = view.findViewById(R.id.home_img_slider);
        // home_list.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false));

        banner_slider = (SliderLayout) view.findViewById(R.id.relative_banner);
        featuredslider = (SliderLayout) view.findViewById(R.id.featured_img_slider);
        rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_items.setLayoutManager(gridLayoutManager);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setNestedScrollingEnabled(false);
        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String getid = category_modelList.get(position).getCat_id();

                Intent intent = new Intent(getActivity(), CategoryPage.class);
                intent.putExtra("cat_id", getid);
                intent.putExtra("title", category_modelList.get(position).getTitle());
                intent.putExtra("image", category_modelList.get(position).getImage());
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
//        loc=view.findViewById(R.id.loc);
        Search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        scrollView.setSmoothScrollingEnabled(true);
        if (isOnline()) {
            makeGetSliderRequest();
            second_banner();
            makeGetCategoryRequest();
        }


        Search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchFragment trending_fragment = new SearchFragment();
                FragmentManager m = getFragmentManager();
                FragmentTransaction fragmentTransaction = m.beginTransaction();
                fragmentTransaction.replace(R.id.contentPanel, trending_fragment);
                fragmentTransaction.commit();
            }
        });

        fabMain = view.findViewById(R.id.fabMain);
        fabOne = view.findViewById(R.id.fabOne);
        fabTwo = view.findViewById(R.id.fabTwo);
        fabThree = view.findViewById(R.id.fabThree);
        fabfour = view.findViewById(R.id.fabfour);


        fabOne.setAlpha(0f);
        fabTwo.setAlpha(0f);
        fabThree.setAlpha(0f);
        fabfour.setAlpha(0f);

        fabOne.setTranslationY(translationY);
        fabTwo.setTranslationY(translationY);
        fabThree.setTranslationY(translationY);
        fabfour.setTranslationY(translationY);

        fabMain.setOnClickListener(this);
        fabOne.setOnClickListener(this);
        fabTwo.setOnClickListener(this);
        fabThree.setOnClickListener(this);
        fabfour.setOnClickListener(this);

        tab1 = view.findViewById(R.id.top_selling_item);
        tab2 = view.findViewById(R.id.recent_item);
        tab3 = view.findViewById(R.id.deals_item);
        tab4 = view.findViewById(R.id.whtsnewitem);

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.pager_product);

        pageAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        //Check Internet Connection
/*
        if (ConnectivityReceiver.isConnected()) {
//            makeGetSliderRequest();

        */
/*    home_banner();
            makeGetBannerSliderRequest();
            makeGetCategoryRequest();
            makeGetFeaturedSlider();
*//*

            Search_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fm = new SearchFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();

                }
            });

        home_list.addOnItemTouchListener(new RecyclerTouchListener(getContext(), home_list, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

              */
/*  Bundle args = new Bundle();
//                Product_fragment fm = new Product_fragment();
//                args.putString("id", sub_cat);
//                Log.d("dfdd", sub_cat);
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();*//*

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

//        //Slider
////        imgSlider = (SliderLayout) view.findViewById(R.id.home_img_slider);

//
//        //Catogary Icons
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            rv_items.setLayoutManager(gridLayoutManager);
            rv_items.setItemAnimator(new DefaultItemAnimator());
            rv_items.setNestedScrollingEnabled(false);

            rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                   */
/* getid = category_modelList.get(position).getId();
                    getcat_title = category_modelList.get(position).getTitle();
                    Bundle args = new Bundle();
                    Fragment fm = new Product_fragment();
                    args.putString("cat_id", getid);
                    args.putString("cat_title", getcat_title);
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();*//*


                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
        }
*/
        return view;
    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fabOne.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();


    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabOne.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();


    }

    private void handleFabOne() {
        Log.i(TAG, "handleFabOne: ");
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabMain:

                Log.i(TAG, "onClick: fab main");
                if (isMenuOpen) {
                    fabOne.setVisibility(View.GONE);
                    fabTwo.setVisibility(View.GONE);
                    fabThree.setVisibility(View.GONE);
                    fabfour.setVisibility(View.GONE);
                    closeMenu();
                } else {
                    fabOne.setVisibility(View.VISIBLE);
                    fabTwo.setVisibility(View.VISIBLE);
                    fabThree.setVisibility(View.VISIBLE);
                    fabfour.setVisibility(View.VISIBLE);
                    openMenu();
                }
                break;
            case R.id.fabOne:
                Intent sendIntent1 = new Intent();
                sendIntent1.setAction(Intent.ACTION_SEND);
                sendIntent1.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + " APP");
                sendIntent1.setType("text/plain");
                startActivity(sendIntent1);

                Log.i(TAG, "onClick: fab one");
                handleFabOne();
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
            case R.id.fabTwo:
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                Log.i(TAG, "onClick: fab two");
                break;
            case R.id.fabThree:
                String smsNumber = "9889887711";
//                Intent sendIntent = new Intent("android.intent.action.MAIN");
//                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
//                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
//                startActivity(sendIntent);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This app delivers all my grocery needs without any hassles. It's contactless & safe. Highly recommended! Download using this in link. https://#");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(sendIntent);
                } else {
                    Toast.makeText(getActivity(), "Whatsapp isn't install please install whatsapp", Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, "onClick: fab three");
                break;


            case R.id.fabfour:

                if (isPermissionGranted()) {
                    call_action();
                }

                Log.i(TAG, "onClick: fab four");
                break;
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void call_action() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "919889887711"));
        startActivity(callIntent);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void makeGetSliderRequest() {
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, BANNER, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fghgh", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("banner_name", jsonObject.getString("banner_name"));
                                url_maps.put("banner_id", jsonObject.getString("banner_id"));
                                url_maps.put("banner_image", BANN_IMG_URL + jsonObject.getString("banner_image"));
                                listarray.add(url_maps);
                            }
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("banner_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("banner_name"));
                                textSliderView.getBundle().putString("extra", name.get("banner_id"));
                                home_list_banner.addSlider(textSliderView);
                                //   banner_slider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
//                                        Bundle args = new Bundle();
//                                        android.app.Fragment fm = new Product_fragment();
//                                        args.putString("id", sub_cat);
//                                        fm.setArguments(args);
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                                .addToBackStack(null).commit();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);


    }

    private void second_banner() {
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, secondary_banner, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fghgh", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("banner_name", jsonObject.getString("banner_name"));
                                url_maps.put("banner_id", jsonObject.getString("sec_banner_id"));
                                url_maps.put("banner_image", BANN_IMG_URL + jsonObject.getString("banner_image"));
                                listarray.add(url_maps);
                            }
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("banner_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("banner_name"));
                                textSliderView.getBundle().putString("extra", name.get("banner_id"));
                                banner_slider.addSlider(textSliderView);
                                //   banner_slider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
//                                        Bundle args = new Bundle();
//                                        android.app.Fragment fm = new Product_fragment();
//                                        args.putString("id", sub_cat);
//                                        fm.setArguments(args);
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                                .addToBackStack(null).commit();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);


    }

    private void makeGetCategoryRequest() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.topsix, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Category_model>>() {
                            }.getType();
                            category_modelList = gson.fromJson(response.getString("data"), listType);
                            adapter = new Home_adapter(category_modelList);
                            rv_items.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}