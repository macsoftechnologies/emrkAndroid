package com.macsoftech.ekart.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.macsoftech.ekart.R;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.helper.SettingsPreferences;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.macsoftech.ekart.helper.SettingsPreferences.GCM_TOKEN;


public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(() -> {
            moveToNext();
        }, 1000);


//        Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
//        startActivity(intent);


    }

    private void moveToNext() {
        boolean isLoginSucces = SettingsPreferences.getBoolean(this, "LOGIN");
        if (isLoginSucces) {
            Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
//            Intent intent = new Intent(SplashScreenActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, LanguageSelectionActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public static void saveGCM(Context context) {
        String token = SettingsPreferences.getString(context, GCM_TOKEN);
        if (TextUtils.isEmpty(token) || SettingsPreferences.getUser(context) == null) {
            return;
        }
        Map<String, String> body = new HashMap<>();
        body.put("userId", SettingsPreferences.getUser(context).getUserId());
        body.put("token", token);
        body.put("platform", "Android");
        RestApi.getInstance().getService()
                .saveGCM(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            System.out.println("It is in splashscreenactivity response "+response.body().string());
                        } catch (IOException e) {
                            System.out.println("It is in splashscreenactivity response "+e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("It is in splashscreenactivity response "+t.getMessage());
                    }
                });
    }
}