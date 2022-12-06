package com.macsoftech.ekart.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.gson.Gson;
import com.macsoftech.ekart.model.LoginResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class SettingsPreferences {

    private static final String ACT_PREF = "act_pref";
    public static final String PROFILE = "profile";
    public static final String USER = "user";
    public static final String GCM_TOKEN = "GCM_TOKEN";
    public static final String NEAR_BY = "neary_by";
    public static final String IS_RECENTS_REFRESH = "is_recents_refresh";
    static String masterKeyAlias;

    static {
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static SharedPreferences getSharedPreferences(Context context, int i) {
        SharedPreferences sharedPreferences = null;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return context.getSharedPreferences(ACT_PREF, 0);
            }
            sharedPreferences = EncryptedSharedPreferences.create(
                    ACT_PREF,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sharedPreferences;
    }

    public static void setGCMRegID(Context context, String token) {
        Editor prefEditor = getSharedPreferences(context, 0).edit();
        prefEditor.putString("gcm_reg_id", token);
        prefEditor.apply();
    }

    public static void saveString(Context context, String key, String value) {
        Editor prefEditor = getSharedPreferences(context, 0).edit();
        prefEditor.putString(key, value);
        prefEditor.apply();
    }

    public static void saveLong(Context context, String key, Long value) {
        Editor prefEditor = getSharedPreferences(context, 0).edit();
        prefEditor.putLong(key, value);
        prefEditor.apply();
    }

    public static <T> void saveObject(Context context, String key, T value) {
        if (context == null) {
            return;
        }
        Editor prefEditor = getSharedPreferences(context, 0).edit();
        String s= new Gson().toJson(value);
        // Log.e("Ramesh", "saveObject: " + s);
        prefEditor.putString(key, s);
        prefEditor.apply();
    }

    public static <T> T getObject(Context context, String key, Class<T> val) {
        String s = getString(context, key);
        if (s == null) {
            return null;
        }

        return new Gson().fromJson(s, val);
    }

    public static LoginResponse getUser(Context context){
        LoginResponse user = SettingsPreferences.getObject(context, SettingsPreferences.USER, LoginResponse.class);
        return user;
    }


    public static void saveBoolean(Context context, String key, boolean value) {
        Editor prefEditor = getSharedPreferences(context, 0).edit();
        prefEditor.putBoolean(key, value);
        prefEditor.apply();
    }

    public static void setFirstTimeAskedPermission(Context context, String key) {
        Editor prefEditor = getSharedPreferences(context, 0).edit();
        prefEditor.putBoolean(key, true);
        prefEditor.apply();
    }

    public static boolean getBoolean(Context context, String key) {
        return getSharedPreferences(context, 0).getBoolean(key, false);
    }

    public static double getDouble(Context context, String key) {
        return getSharedPreferences(context, 0).getLong(key, 0);
    }

    public static String getString(Context context, String key) {
        String s = getSharedPreferences(context, 0).getString(key, null);
        // Log.e("Ramesh", "getString: " + s);
        return s;
    }

    public static String getRegId(Context context) {
        return getSharedPreferences(context, 0).getString("gcm_reg_id", null);
    }


}
