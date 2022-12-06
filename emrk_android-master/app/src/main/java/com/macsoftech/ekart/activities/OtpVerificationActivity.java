package com.macsoftech.ekart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.CommonErrorResponse;
import com.macsoftech.ekart.model.LoginRootResponse;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationActivity extends BaseActivity {

    @BindView(R.id.btn_login)
    Button btn_login;


    @BindView(R.id.squareField)
    SquarePinField squareField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        boolean isLoginSucces = SettingsPreferences.getBoolean(OtpVerificationActivity.this, "LOGIN");
        if (isLoginSucces) {
            btn_login.setVisibility(View.GONE);
        } else {
            btn_login.setVisibility(View.VISIBLE);
        }

        squareField.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete(String str) {
                loginProcess();
//                if (str.equalsIgnoreCase("1234")) {
//                    startActivity(new Intent(OtpVerificationActivity.this, DashboardActivity.class));
//                    finish();
//                    return true;
//                }
                return false;
            }
        });

    }


    private void loginProcess() {

        String otp = squareField.getText().toString().trim();
        ;
        Map<String, String> map = new HashMap<>();
        map.put("otp", otp);
        map.put("mobileNum", getIntent().getStringExtra("mobileNum"));
        showProgress();
        RestApi.getInstance().getService()
                .verifyOtp(map)
                .enqueue(new Callback<LoginRootResponse>() {
                    @Override
                    public void onResponse(Call<LoginRootResponse> call, Response<LoginRootResponse> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            SettingsPreferences.saveBoolean(OtpVerificationActivity.this, "LOGIN", true);
                            SettingsPreferences.saveObject(OtpVerificationActivity.this, SettingsPreferences.USER, response.body().loginRes);
                            Intent intent = new Intent(OtpVerificationActivity.this, DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            CommonErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), CommonErrorResponse.class);
                            showToast(errorResponse.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginRootResponse> call, Throwable t) {
                        hideDialog();
                    }
                });

    }


    //btn_login
    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}