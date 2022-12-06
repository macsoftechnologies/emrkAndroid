package com.macsoftech.ekart.activities;

import android.os.Bundle;
import android.view.View;

import com.macsoftech.ekart.databinding.ActivityAddChangePinBinding;
import com.macsoftech.ekart.helper.SettingsPreferences;

public class AddChangePinActivity extends BaseActivity {

    private ActivityAddChangePinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Pin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding = ActivityAddChangePinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (SettingsPreferences.getString(this, "pin") != null) {
            binding.txtTitle1.setText("Enter Old Pin");
            binding.txtTitle2.setText("Enter New Pin");
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void submit() {
        BaseActivity.hideKeyboard(this);
        if (SettingsPreferences.getString(this, "pin") == null) {
            if (binding.squareField1.getText().toString().equals(binding.squareField2.getText().toString())) {
                SettingsPreferences.saveString(this, "pin", binding.squareField2.getText().toString());
                showToast("PIN Set Successfully.");
                finish();
            } else {
                BaseActivity.hideKeyboard(this);
                showToast("PIN Mismatch.");
            }
        } else {
            if (binding.squareField1.getText().toString().equals(SettingsPreferences.getString(this, "pin"))) {
                SettingsPreferences.saveString(this, "pin", binding.squareField2.getText().toString());
                showToast("PIN Updated Successfully.");
                finish();
            } else {
                showToast("Enter Correct PIN.");
            }
        }
    }
}