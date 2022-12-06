package com.macsoftech.ekart.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.macsoftech.ekart.R;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.LoginResponse;
import com.macsoftech.ekart.model.search.GetUserResponseRoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditContactsListActivity extends BaseActivity {

    LinearLayout ll_contacts_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts_list);
        getSupportActionBar().setTitle("EditContacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ll_contacts_container = findViewById(R.id.ll_contacts_container);
        loadData();

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactsPopup();
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContacts();
            }
        });
    }

    private void saveContacts() {
        showProgress();
        Map<String, Object> body = new HashMap<>();
        LoginResponse user = SettingsPreferences.getObject(this, SettingsPreferences.USER, LoginResponse.class);
        body.put("userId", user.getUserId());
        List<String> list = new ArrayList<>();
        if (ll_contacts_container.getChildCount() > 0) {
            for (int i = 0; i < ll_contacts_container.getChildCount(); i++) {
                TextView mobile = (TextView) ll_contacts_container.getChildAt(i).findViewById(R.id.txt_mobile);
                list.add(mobile.getText().toString());
            }
        }
        body.put("altNumber", list.toArray(new String[0]));
        RestApi.getInstance().getService().updateUser(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            showToast("Updated Successfully");
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                    }
                });
    }

    private void showContactsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Contact");
        EditText editText = new EditText(this);
        editText.setSingleLine(true);
        editText.setHint("Enter Contact No");
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(editText);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addContact(editText.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    LoginResponse currentUser;

    private void loadData() {
        showProgress();
        Map<String, String> body = new HashMap<>();
        LoginResponse user = SettingsPreferences.getObject(this, SettingsPreferences.USER, LoginResponse.class);
        body.put("userId", user.getUserId());
        RestApi.getInstance().getService().getUser(body).enqueue(new Callback<GetUserResponseRoot>() {
            @Override
            public void onResponse(Call<GetUserResponseRoot> call, Response<GetUserResponseRoot> response) {
                hideDialog();
//                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        currentUser = response.body().getUserFeedbackResponse().get(0);
                        loadContacts();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserResponseRoot> call, Throwable t) {
                hideDialog();
//                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }


    private void loadContacts() {
        List<String> contacts = new ArrayList<>();
        if (currentUser.getAltNumber() instanceof String) {
            contacts.add(currentUser.getAltNumber().toString());
        }
        if (currentUser.getAltNumber() instanceof List && !((List<?>) currentUser.getAltNumber()).isEmpty()) {
            for (String v : ((List<String>) currentUser.getAltNumber())) {
                contacts.add(v);
            }
        }

        for (int i = 1; i <= contacts.size(); i++) {
//            View view = LayoutInflater.from(this).inflate(R.layout.row_contacts, null);
//            TextView tv_name = view.findViewById(R.id.tv_name);
//            TextView txt_mobile = view.findViewById(R.id.txt_mobile);
//            txt_mobile.setText(contacts.get(i - 1));
//            tv_name.setText(i + ". " + currentUser.getFirstName() + " " + currentUser.getLastName());
//            txt_mobile.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_delete_forever_24,0);
//            ll_contacts_container.addView(view);
            addContact(contacts.get(i - 1));
        }

    }

    void addContact(String contacts) {
        int i = ll_contacts_container.getChildCount() + 1;
        View view = LayoutInflater.from(this).inflate(R.layout.row_contacts_no_linkify, null);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView txt_mobile = view.findViewById(R.id.txt_mobile);
        txt_mobile.setText(contacts);
        txt_mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_delete_forever_24, 0);
        tv_name.setText(i + ". " + currentUser.getFirstName() + " " + currentUser.getLastName());
//        txt_mobile.setAutoLinkMask(Linkify.WEB_URLS);
        ll_contacts_container.addView(view);
        txt_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_contacts_container.removeView(view);
            }
        });
    }
}