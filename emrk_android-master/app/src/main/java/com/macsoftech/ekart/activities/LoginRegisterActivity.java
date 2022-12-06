package com.macsoftech.ekart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.macsoftech.ekart.R;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;

import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class LoginRegisterActivity extends BaseActivity {

    @BindView(R.id.squareField)
    SquarePinField squareField;

    @BindView(R.id.iv_bio)
    ImageView ivBio;

    @BindView(R.id.ll_bio)
    View ll_bio;

    @BindView(R.id.txt_or)
    TextView txt_or;

    private int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register2);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        System.out.println("Dadi setting preferer" + SettingsPreferences.getString(this, "pin"));
       /* if (SettingsPreferences.getString(this, "pin") == null) {
            squareField.setVisibility(View.VISIBLE);
            txt_or.setVisibility(View.GONE);
           // if (isLoginSucces) {
                findViewById(R.id.ll_login).setVisibility(View.GONE);
           // } else {
                findViewById(R.id.ll_login_bio).setVisibility(View.VISIBLE);
           // }

        }*/
        squareField.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete(String str) {
                if (str.equalsIgnoreCase(SettingsPreferences.getString(LoginRegisterActivity.this, "pin"))) {
                    startActivity(new Intent(LoginRegisterActivity.this, DashboardActivity.class));
                    finish();
                    return true;
                } else {
                    showToast("Invalid Pin");
                }
                return false;
            }
        });
//        ivBio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                biometricPrompt.authenticate(promptInfo);
//            }
//        });

        ll_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                authenticateBioMetric();
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                boolean isLoginSucces = SettingsPreferences.getBoolean(this, "LOGIN");
                if (isLoginSucces) {
                    startActivity(new Intent(LoginRegisterActivity.this, DashboardActivity.class));
                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                if (SettingsPreferences.getString(this, "pin") == null) {
                    startActivity(new Intent(LoginRegisterActivity.this, DashboardActivity.class));
                    finish();
                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                if (SettingsPreferences.getString(this, "pin") == null) {
                    startActivity(new Intent(LoginRegisterActivity.this, DashboardActivity.class));
                    finish();
                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
            default:
                isLoginSucces = SettingsPreferences.getBoolean(this, "LOGIN");
                if (isLoginSucces) {
                    startActivity(new Intent(LoginRegisterActivity.this, DashboardActivity.class));
                }
                break;
        }
        boolean isLoginSucces = SettingsPreferences.getBoolean(this, "LOGIN");
        if (isLoginSucces) {
            findViewById(R.id.ll_login).setVisibility(View.GONE);
        } else {
            findViewById(R.id.ll_login_bio).setVisibility(View.GONE);
        }
    }

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    void authenticateBioMetric() {

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
//                Toast.makeText(getApplicationContext(),
//                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginRegisterActivity.this, DashboardActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        startActivity(new Intent(this, LoginActivity.class));
//        finish();
    }

    @OnClick(R.id.btn_register)
    public void onRegisterContinueClick() {
        startActivity(new Intent(this, RegistrationActivity.class));
//        finish();
    }
}