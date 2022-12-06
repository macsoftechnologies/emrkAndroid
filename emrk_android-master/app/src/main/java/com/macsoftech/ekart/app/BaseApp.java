package com.macsoftech.ekart.app;

import androidx.multidex.MultiDexApplication;

import com.macsoftech.ekart.helper.Helper;


/**
 * Created by Ramesh on 06/10/17.
 */

public class BaseApp extends MultiDexApplication {

    private static BaseApp instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        ViewPump.init(ViewPump.builder()
//                .addInterceptor(new CalligraphyInterceptor(
//                        new CalligraphyConfig.Builder()
//                                .setDefaultFontPath("fonts/Lato-Regular.ttf")
//                                .setFontAttrId(R.attr.fontPath)
//                                .build()))
//                .build());

    }

    public static BaseApp getInstance() {
        return instance;
    }

    public static boolean hasNetwork() {
        return Helper.isNetworkAvailable(instance);
    }
}
