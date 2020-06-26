package com.rch.etawah.app;

public interface AppConstants {

    int DELAY = 3000;
    int SCREEN_ORIENTATION = 1;

    String TAG = "RCH";
    String IS_LOGIN = "isLogin";
    String USER_DATA = "userData";

    String SHARE = "com.rch.app.share";
    String SHARE_ADDRESS = "com.rch.app.share.address";

    String BASE_URL = "http://app.rchetawah.com/";

    String LOGIN = BASE_URL + "api/login";
    String HOME = BASE_URL + "api/whatsnew";
    String BANNER = BASE_URL + "api/banner";
    String MIN_MAX = BASE_URL + "api/minmax";
    String VERIFY_OTP = BASE_URL + "api/verify_phone";
    String ADD_ADDRESS = BASE_URL + "api/add_address";
    String SHOW_ADDRESS = BASE_URL + "api/show_address";
    String FORGOT_OTP = BASE_URL + "api/forget_password";
    String DELIVERY_INFO = BASE_URL + "api/delivery_info";
    String MAKE_AN_ORDER = BASE_URL + "api/make_an_order";
    String SELECT_ADDRESS = BASE_URL + "api/select_address";

}
