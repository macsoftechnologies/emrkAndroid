package com.macsoftech.ekart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.macsoftech.ekart.R;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.language.LanguageRootResponse;
import com.macsoftech.ekart.model.language.SelectedLanguages;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageSelectionActivity extends BaseActivity {

    @BindView(R.id.rg_languages)
    RadioGroup rg_languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        loadLanguages();
    }

    private void loadLanguages() {
        showProgress();
        RestApi.getInstance().getService()
                .getLanguages()
                .enqueue(new Callback<LanguageRootResponse>() {
                    @Override
                    public void onResponse(Call<LanguageRootResponse> call, Response<LanguageRootResponse> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            populateLanguages(response.body().getData().getSelectedLanguages());
                        }
                    }

                    @Override
                    public void onFailure(Call<LanguageRootResponse> call, Throwable t) {
                        hideDialog();
                    }
                });
    }

    private void populateLanguages(List<SelectedLanguages> selectedLanguages) {
        for (SelectedLanguages languages : selectedLanguages) {
            //rg_languages
            RadioButton view = (RadioButton) LayoutInflater.from(this).inflate(R.layout.row_language, rg_languages, false);
            view.setText(languages.getLanguage().toUpperCase());
            rg_languages.addView(view);
        }
    }

    @OnClick(R.id.btn_continue)
    public void onContinueClick() {

        boolean isLoginSucces = SettingsPreferences.getBoolean(LanguageSelectionActivity.this, "LOGIN");
        System.out.println("Dadi this is isLoginSucces"+isLoginSucces);
        if (isLoginSucces) {
            startActivity(new Intent(this, OtpVerificationActivity.class));
            finish();
        } else {
            RadioButton rb = rg_languages.findViewById(rg_languages.getCheckedRadioButtonId());
            if (rb == null) {
                showToast("Please select language");
                return;
            }
            SettingsPreferences.saveString(this, "lang", rb.getText().toString());
            startActivity(new Intent(this, LoginRegisterActivity.class));
            finish();
        }

    }
}