package com.macsoftech.ekart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.macsoftech.ekart.databinding.ActivityRegistrationBinding;
import com.macsoftech.ekart.helper.SettingsPreferences;

public class RegistrationActivity extends BaseActivity {
    ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClick();
            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterClick();
            }
        });

    }


    public void onLoginClick() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    public void onRegisterClick() {
        if (TextUtils.isEmpty(binding.etFirstName.getText().toString())) {
            showToast("Enter First name");
            return;
        }
        if (TextUtils.isEmpty(binding.etLastName.getText().toString())) {
            showToast("Enter Last name");
            return;
        }
        if (TextUtils.isEmpty(binding.etMobile.getText().toString())) {
            showToast("Enter Mobile Number");
            return;
        }
//        if (TextUtils.isEmpty(binding.etAltMobile.getText().toString())) {
//            showToast("Enter Alternative Mobile Number");
//            return;
//        }
        if (TextUtils.isEmpty(binding.etPwd.getText().toString())) {
            showToast("Enter Password");
            return;
        }
        if (!binding.etPwd.getText().toString()
                .equals(binding.etConfirmPwd.getText().toString())) {
            showToast("Password and Confirm password both are different");
            return;
        }
       /* if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
            showToast("Enter Email Address");
            return;
        } */
        if (binding.etMobile.getText().toString().length()!=10) {
            showToast("Mobile number must be 10 digits");
            return;
        }
        String m1 = binding.etMobile.getText().toString();
        String m2 = binding.etAltMobile.getText().toString();

        if (binding.etAltMobile.getText().toString().length()>0){
            if (binding.etAltMobile.getText().toString().length()!=10) {
                showToast("Alternative Mobile number must be 10 digits "+m1+" m- "+
                        m2);
                return;
            }
        }

        if (m1.equals(m2)) {
            showToast("Mobile Numbers should not be the same.");
            return;
        }
        if (binding.etEmail.getText().toString().length() >1){
            if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches())
            {
                showToast("Invalid Email Address");
                return;
            }
        }


        Intent intent = new Intent(this, RegistrationContinuousActivity.class);
        intent.putExtra("firstName", binding.etFirstName.getText().toString());
        intent.putExtra("lastName", binding.etLastName.getText().toString());
        intent.putExtra("mobileNum", binding.etMobile.getText().toString());
        intent.putExtra("altNumber", binding.etAltMobile.getText().toString());
        intent.putExtra("password", binding.etPwd.getText().toString());
        intent.putExtra("emailId", binding.etEmail.getText().toString());
        intent.putExtra("chooseLanguage", SettingsPreferences.getString(this, "lang"));
        startActivity(intent);
    }
}