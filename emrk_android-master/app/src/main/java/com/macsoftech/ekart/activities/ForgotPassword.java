package com.macsoftech.ekart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.CommonErrorResponse;
import com.macsoftech.ekart.model.LoginRootResponse;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends BaseActivity {

    @BindView(R.id.etmail)
    EditText etmail;

    @BindView(R.id.etpwd)
    EditText etpwd;

    @BindView(R.id.btn_login)
    Button btn_login;

    TextView forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        forgot = findViewById(R.id.forgot);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        boolean isLoginSucces = SettingsPreferences.getBoolean(ForgotPassword.this, "LOGIN");
        if (isLoginSucces) {
            btn_login.setVisibility(View.GONE);
        } else {
            btn_login.setVisibility(View.VISIBLE);
        }
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {

//        Intent intent = new Intent(this, DashboardActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();

        loginProcess();

    }

    private void loginProcess() {

        String emailId = etmail.getText().toString();
        String pwd = etpwd.getText().toString();
        Map<String, String> map = new HashMap<>();
//        map.put("emailId", emailId);
        map.put("mobileNum", emailId);
        map.put("password", pwd);
        //"emailId":"gowthami@gmail.com",
        //     "password":"gfdsdf"
        showProgress();
        RestApi.getInstance().getService().login(map).enqueue(new Callback<LoginRootResponse>() {

            @Override
            public void onResponse(Call<LoginRootResponse> call, Response<LoginRootResponse> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    SettingsPreferences.saveBoolean(ForgotPassword.this, "LOGIN", true);
                    SettingsPreferences.saveObject(ForgotPassword.this, SettingsPreferences.USER, response.body().loginRes);
                    Intent intent = new Intent(ForgotPassword.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    CommonErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), CommonErrorResponse.class);
                    showToast(errorResponse.getMessage());
//                    showToast("Failed to Login");
                }

            }

            @Override
            public void onFailure(Call<LoginRootResponse> call, Throwable t) {
                hideDialog();
            }
        });

    }

    @OnClick(R.id.btn_register)
    public void onRegisterClick() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}