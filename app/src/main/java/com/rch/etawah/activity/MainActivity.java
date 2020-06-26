package com.rch.etawah.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.app.SharedPreference;
import com.rch.etawah.fragments.CartFragment;
import com.rch.etawah.fragments.CategoryFragment;
import com.rch.etawah.fragments.Contact_Us_fragment;
import com.rch.etawah.fragments.Edit_profile_fragment;
import com.rch.etawah.fragments.HomeeeFragment;
import com.rch.etawah.fragments.NotificationFragment;
import com.rch.etawah.fragments.Reward_fragment;
import com.rch.etawah.fragments.SearchFragment;
import com.rch.etawah.fragments.Terms_and_Condition_fragment;
import com.rch.etawah.fragments.Wallet_fragment;
import com.rch.etawah.R;
import com.rch.etawah.ui.LoginActivity;
import com.rch.etawah.util.DatabaseHandler;
import com.rch.etawah.util.GPSTracker;
import com.rch.etawah.util.Session_management;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.rch.etawah.Config.BaseURL.ADDRESS;
import static com.rch.etawah.Config.BaseURL.CITY;
import static com.rch.etawah.Config.BaseURL.COUNTRY;
import static com.rch.etawah.Config.BaseURL.IS_LOGIN;
import static com.rch.etawah.Config.BaseURL.KEY_PINCODE;
import static com.rch.etawah.Config.BaseURL.LAT;
import static com.rch.etawah.Config.BaseURL.LONG;
import static com.rch.etawah.Config.BaseURL.MyPrefreance;
import static com.rch.etawah.Config.BaseURL.STATE;
import static com.rch.etawah.Config.BaseURL.SupportUrl;
import static com.rch.etawah.Config.BaseURL.TermsUrl;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static BottomNavigationView navigation;
    int padding = 0;
    private DatabaseHandler dbcart;
    private Session_management sessionManagement;
    private AppBarConfiguration mAppBarConfiguration;
    LinearLayout My_Order, My_Reward, My_Walllet, My_Cart;
    RelativeLayout loginSignUp, profile, aboutUs, TermsPolicy, ContactUs, share, logout;
    private Menu nav_menu;
    NavigationView navigationView;
    LinearLayout viewpa;
    TextView mTitle, username;
    Button login, signup;
    private ImageView iv_profile;
    TextView totalBudgetCount;
    Toolbar toolbar;
    ImageView sliderr;

    List<Address> addresses = new ArrayList<>();
    String latitude, longitude, address, city, state, country, postalCode;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sessionManagement = new Session_management(MainActivity.this);
        dbcart = new DatabaseHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        /*toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());

        setSupportActionBar(toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {

            View view = toolbar.getChildAt(i);


            if (view instanceof TextView) {
                TextView textView = (TextView) view;

            }
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
//                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
//            applyFontToMenuItem(mi);
        }

        View headerView = navigationView.getHeaderView(0);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        viewpa = (LinearLayout) header.findViewById(R.id.viewpa);
        if (sessionManagement.isLoggedIn()) {
            viewpa.setVisibility(View.VISIBLE);
        }


        My_Order = (LinearLayout) header.findViewById(R.id.my_orders);
        My_Reward = (LinearLayout) header.findViewById(R.id.my_reward);
        My_Walllet = (LinearLayout) header.findViewById(R.id.my_wallet);
        My_Cart = (LinearLayout) header.findViewById(R.id.my_cart);
        iv_profile = (ImageView) header.findViewById(R.id.iv_header_img);
        username = (TextView) header.findViewById(R.id.tv_header_name);


       /* login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.sign);
        username=findViewById(R.id.tv_header_name);*/

        My_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sessionManagement.isLoggedIn()) {
                    Intent intent = new Intent(MainActivity.this, My_Order_activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        My_Reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManagement.isLoggedIn()) {

                    drawer.closeDrawer(GravityCompat.START);

                    Reward_fragment fm = new Reward_fragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.contentPanel, fm);
                    transaction.commit();

//                    android.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        My_Walllet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManagement.isLoggedIn()) {

                    drawer.closeDrawer(GravityCompat.START);

                    Wallet_fragment fm = new Wallet_fragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.contentPanel, fm);
                    transaction.commit();


//                    Wallet_fragment fm = new Wallet_fragment();
//                    android.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        My_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbcart.getCartCount() > 0) {
                    CartFragment favourite_fragment = new CartFragment();
                    FragmentManager manager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = manager1.beginTransaction();
                    transaction1.replace(R.id.contentPanel, favourite_fragment);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                } else {
                    Toast.makeText(MainActivity.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    Edit_profile_fragment fm = new Edit_profile_fragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
            }
        });

        sideMenu();

        // checkConnection();

        if (savedInstanceState == null) {
            HomeeeFragment fm = new HomeeeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fm, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
        getSupportFragmentManager().

                addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        try {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            Fragment fr = getSupportFragmentManager().findFragmentById(R.id.contentPanel);

                            final String fm_name = fr.getClass().getSimpleName();
                            Log.e("backstack: ", ": " + fm_name);
                            if (fm_name.contentEquals("Home_fragment")) {
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                toggle.setDrawerIndicatorEnabled(true);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                toggle.syncState();

                            } else if (fm_name.contentEquals("My_order_fragment") ||
                                    fm_name.contentEquals("Thanks_fragment")) {
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();

                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        HomeeeFragment fm = new HomeeeFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });
                            } else {

                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();

                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        onBackPressed();
                                    }
                                });
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
        GPSTracker mGPS = new GPSTracker(getApplicationContext());
        if (mGPS.canGetLocation) {
            mGPS.getLocation();

            latitude = String.valueOf(mGPS.getLatitude());
            longitude = String.valueOf(mGPS.getLongitude());

            Log.d("lat", latitude);
            Log.d("long", longitude);

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(mGPS.getLatitude(), mGPS.getLongitude(), 1); //1 num of possible location returned
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (address != null) {
                address = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();                // commented on 27 sept 19 bcoz of screen crash
                // create your custom title
                String title = address + "-" + city + "-" + state;
                Log.d("addresss", title + country + "-" + postalCode);

                SharedPreferences.Editor editor = getSharedPreferences(MyPrefreance, MODE_PRIVATE).edit();
                editor.putString(ADDRESS, address);
                editor.putString(CITY, city);
                editor.putString(STATE, state);
                editor.putString(KEY_PINCODE, postalCode);
                editor.putString(COUNTRY, country);
                editor.putString(LAT, latitude);
                editor.putString(LONG, longitude);
                editor.apply();
                editor.commit();
            }
        }

        initComponent();
        loadFragment(new HomeeeFragment());
        // updateHeader();
//sideMenu();
    }

    @Override
    public boolean onSupportNavigateUp() {
       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();*/
        return false;
    }

    private void loadFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentPanel, fragment)
                .commitAllowingStateLoss();
    }

    private void initComponent() {
        navigation = (BottomNavigationView) findViewById(R.id.nav_view12);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(new HomeeeFragment());

                        HomeeeFragment appNewsHome1Fragment = new HomeeeFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentPanel, appNewsHome1Fragment);
                        transaction.commit();
                        return true;
                    case R.id.navigation_dashboard:

                        CategoryFragment category_fragment = new CategoryFragment();
                        FragmentManager manager2 = getSupportFragmentManager();
                        FragmentTransaction transaction2 = manager2.beginTransaction();
                        transaction2.replace(R.id.contentPanel, category_fragment);
                        transaction2.commit();
                        return true;

                    case R.id.navigation_notifications1:

                        SearchFragment trending_fragment = new SearchFragment();
                        FragmentManager m = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = m.beginTransaction();
                        fragmentTransaction.replace(R.id.contentPanel, trending_fragment);
                        fragmentTransaction.commit();
                        return true;


                    case R.id.navigation_notifications12:

                        NotificationFragment nf = new NotificationFragment();
                        FragmentManager m4 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransactionnn = m4.beginTransaction();
                        fragmentTransactionnn.replace(R.id.contentPanel, nf);
                        fragmentTransactionnn.commit();
                        return true;
//                    case R.id.navigation_newsstand:
////                        mTextMessage.setText(item.getTitle());
//                        navigation.setBackgroundColor(getResources().getColor(R.color.teal_800));
//                        window.setStatusBarColor(getResources().getColor(R.color.teal_800));
//                        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.teal_800));
//
//                        EditorPicked_Fragment editorPicked_fragment = new EditorPicked_Fragment();
//                        FragmentManager manager3 = getSupportFragmentManager();
//                        FragmentTransaction transaction3 = manager3.beginTransaction();
//                        transaction3.replace(R.id.contentPanel, editorPicked_fragment);
//                        transaction3.commit();
//                        return true;
                    case R.id.navigation_notifications123:

                        CartFragment favourite_fragment = new CartFragment();
                        FragmentManager manager1 = getSupportFragmentManager();
                        FragmentTransaction transaction1 = manager1.beginTransaction();
                        transaction1.replace(R.id.contentPanel, favourite_fragment);
                        transaction1.commit();
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.sign) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.sign) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }

       /* else if (id == R.id.nav_shop_now) {
            fm = new Shop_Now_fragment();
        } */
        else if (id == R.id.nav_my_profile) {
            fm = new Edit_profile_fragment();
//        } else if (id == R.id.nav_support) {
//            String smsNumber = "9889887711";
//            Intent sendIntent = new Intent("android.intent.action.MAIN");
//            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
//            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
//            startActivity(sendIntent);

        } else if (id == R.id.nav_aboutus) {
//            toolbar.setTitle("About");
            startActivity(new Intent(getApplicationContext(), About_us.class));
        } else if (id == R.id.nav_policy) {
            fm = new Terms_and_Condition_fragment();
            args.putString("url", TermsUrl);
            args.putString("title", getResources().getString(R.string.nav_terms));
            fm.setArguments(args);
        }
//        else if (id == R.id.nav_review) {
//            //reviewOnApp();
//        }
        else if (id == R.id.nav_contact) {
            fm = new Contact_Us_fragment();
            args.putString("url", SupportUrl);
            args.putString("title", getResources().getString(R.string.nav_terms));
            fm.setArguments(args);

        }
//        else if (id == R.id.nav_review) {
//            reviewOnApp();
//        }
        else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_logout) {
            sessionManagement.logoutSession();
            SharedPreference.putBoolean(IS_LOGIN, false);
//            login.setVisibility(View.VISIBLE);
            finish();

        }
//        else if (id == R.id.nav_powerd) {
//            // stripUnderlines(textView);
//            String url = "http://sameciti.com";
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//            finish();
//        }

        if (fm != null) {


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP"); //getPackageName()
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void sideMenu() {

        if (sessionManagement.isLoggedIn()) {
            //  tv_number.setVisibility(View.VISIBLE);
            nav_menu.findItem(R.id.nav_logout).setVisible(true);
            nav_menu.findItem(R.id.nav_my_profile).setVisible(true);
            //   nav_menu.findItem(R.id.login).setVisible(true);
            nav_menu.findItem(R.id.sign).setVisible(false);

            username.setText("Welcome! " +
                    "" + sessionManagement.getUserDetails().get(BaseURL.KEY_NAME));

//            nav_menu.findItem(R.id.signup).setVisible(false);

//            nav_menu.findItem(R.id.nav_user).setVisible(true);
        } else {

            //tv_number.setVisibility(View.GONE);
//            tv_name.setText(getResources().getString(R.string.btn_login));
//            tv_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(i);
//                }
//            });
            nav_menu.findItem(R.id.login).setVisible(false);
            nav_menu.findItem(R.id.nav_my_profile).setVisible(false);
            nav_menu.findItem(R.id.nav_logout).setVisible(false);
            nav_menu.findItem(R.id.sign).setVisible(true);


            //            nav_menu.findItem(R.id.nav_user).setVisible(false);
        }
    }


//    public void updateHeader() {
//        if (sessionManagement.isLoggedIn()) {
//            String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
//            String getimage = sessionManagement.getUserDetails().get(BaseURL.KEY_IMAGE);
//            String getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
//            SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
//            String previouslyEncodedImage = shre.getString("image_data", "");
//            if (!previouslyEncodedImage.equalsIgnoreCase("")) {
//                byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//                iv_profile.setImageBitmap(bitmap);
//            }
//            Glide.with(this)
//                    .load(BaseURL.IMG_PROFILE_URL + getimage)
//                    .placeholder(R.drawable.icon)
//                    .crossFade()
//                    .into(iv_profile);
    //  tv_name.setText(getname);

//        }
//    }


   /* public void setCartCounter(String totalitem) {
        try {
            totalBudgetCount.setText(totalitem);
        }catch (Exception e){

        }
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_cart);
        item.setVisible(true);

       View count = item.getActionView();
        totalBudgetCount = (TextView) count.findViewById(R.id.actionbar_notifcation_textview);
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.performIdentifierAction(item.getItemId(), 0);
            }
        });


        totalBudgetCount.setText("" + dbcart.getCartCount());
        return true;
    }*/
}
