package com.macsoftech.ekart.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.ActivityEditProfileBinding;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.CommonErrorResponse;
import com.macsoftech.ekart.model.LocationData;
import com.macsoftech.ekart.model.LocationResponseRoot;
import com.macsoftech.ekart.model.LoginResponse;
import com.macsoftech.ekart.model.LoginRootResponse;
import com.macsoftech.ekart.model.register.RegistrationRootResponse;
import com.macsoftech.ekart.model.search.GetUserResponseRoot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import id.zelory.compressor.Compressor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding binding;
    private boolean isEdit = false;
    private boolean isProfile;
    TextView removeentity, removeprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(binding.getRoot());
        SharedPreferences.Editor editclicked = getSharedPreferences("editclicked", MODE_PRIVATE).edit();
        editclicked.putString("editclicked", "no");
        editclicked.commit();

        removeentity = findViewById(R.id.removeentity);
        removeprofile = findViewById(R.id.removeprofile);
        removeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        removeentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        SharedPreferences ld = getSharedPreferences("Logindetails", MODE_PRIVATE);
        binding.etEmail.setText(ld.getString("email", ""));
        binding.etAltMobile.setText(ld.getString("altmobileNum", ""));
        binding.etEntityName.setText(ld.getString("entityName", ""));
        binding.etFirstName.setText(ld.getString("firstName", ""));
        binding.etLastName.setText(ld.getString("lastName", ""));
        binding.etPrimary.setText(ld.getString("primaryLocation", ""));
        binding.etMobile.setText(ld.getString("mobileNum", ""));
        Glide.with(EditProfileActivity.this)
                .load(RestApi.BASE_URL + ld.getString("userImage", ""))
                .error(R.drawable.entity_profile)
                .into(binding.ivProfile);
        Glide.with(EditProfileActivity.this)
                .load(RestApi.BASE_URL + ld.getString("entityImage", ""))
                .error(R.drawable.entity_profile)
                .into(binding.ivEntity);

        //loadData();
        loginProcess();
        binding.etPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationAlertDialog(binding.etPrimary);
            }
        });
        binding.txtAddAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationAlertDialog(null);
            }
        });
        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProfile = true;
                openCamera();
//                showToast("Profile pic updating is restricted.");
            }
        });

        binding.ivEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProfile = false;
                openCamera();
//                showToast("Entity pic updating is restricted.");
            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    updateProfile();
                    isEdit = false;
                    binding.btnUpdate.setText("EDIT");
                    enableUIComponents();
                } else {
                    isEdit = true;
                    binding.btnUpdate.setText("SUBMIT");
                    enableUIComponents();
                }

            }
        });
        enableUIComponents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_300
                    && resultCode == Activity.RESULT_OK
            ) {
                new Thread(() -> {
                    try {
                        compressFile();

//                    runOnUiThread(() -> setData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            } else if (requestCode == GALLERY_PICK_REQUEST_CODE_400
                    && resultCode == Activity.RESULT_OK) {
                if (isProfile) {
                    profile = photoFile;
                    Glide.with(this)
                            .load(photoFile)
                            .into(binding.ivProfile);
                } else {
                    entity = photoFile;
                    Glide.with(this)
                            .load(photoFile)
                            .into(binding.ivEntity);
                }
                updatePics();
            }

        }
    }

    private void compressFile() throws IOException {
        Compressor compressor = new Compressor(this.getApplicationContext());
        final File file = compressor.compressToFile(((BaseActivity) this).photoFile);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isProfile) {
                    profile = file;
                    Glide.with(EditProfileActivity.this)
                            .load(file)
                            .into(binding.ivProfile);
                } else {
                    entity = file;
                    Glide.with(EditProfileActivity.this)
                            .load(file)
                            .into(binding.ivEntity);
                }
                updatePics();
            }
        });
    }


    private void enableUIComponents() {
        binding.etEntityName.setEnabled(isEdit);
        binding.etFirstName.setEnabled(isEdit);
        binding.etLastName.setEnabled(isEdit);
        binding.etPrimary.setEnabled(isEdit);
        binding.etMobile.setEnabled(isEdit);
        binding.etAltMobile.setEnabled(isEdit);
        binding.etEmail.setEnabled(isEdit);
        binding.txtAddAnother.setEnabled(isEdit);
    }


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
                       /* LoginResponse user = response.body().getUserFeedbackResponse().get(0);
                        binding.etEntityName.setText(user.getEntityName());
                        binding.etFirstName.setText(user.getFirstName());
                        binding.etLastName.setText(user.getLastName());
                        binding.etPrimary.setText(user.getPrimaryLocation());
                        binding.etMobile.setText(user.getMobileNum());
                        if (user.getAltNumber() instanceof String) {
                            binding.etAltMobile.setText(user.getAltNumber().toString());
                        }else if(user.getAltNumber() instanceof List){
                            if (user.getAltNumber() instanceof List && !((List<?>) user.getAltNumber()).isEmpty()) {
                                String value =(String) ((List<?>) user.getAltNumber()).get(0);
                                binding.etAltMobile.setText(value);
                            }
                        }*/
//                        binding.txtMobile.setText(user.getMobileNum());
                     /*   for (String location : user.getAvailableLocation()) {
                            addNewLocation(location);
                        }*/


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

    private void loginProcess() {
        SharedPreferences logincred = getSharedPreferences("logincred", MODE_PRIVATE);
        String emailId = logincred.getString("mobile", "");
        String pwd = logincred.getString("password", "");
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
                    SettingsPreferences.saveBoolean(EditProfileActivity.this, "LOGIN", true);
                    SettingsPreferences.saveObject(EditProfileActivity.this, SettingsPreferences.USER, response.body().loginRes);
                    SharedPreferences.Editor ld = getSharedPreferences("Logindetails", MODE_PRIVATE).edit();
                    ld.putString("firstName", response.body().loginRes.getFirstName());
                    ld.putString("lastName", response.body().loginRes.getLastName());
                    ld.putString("email", response.body().loginRes.getEmailId());
                    ld.putString("mobileNum", response.body().loginRes.getMobileNum());
                    ld.putString("altmobileNum", response.body().loginRes.getAltNumber().toString().replace("[", "").replace("]", ""));
                    ld.putString("entityName", response.body().loginRes.getEntityName());
                    ld.putString("primaryLocation", response.body().loginRes.getPrimaryLocation());
                    ld.putString("userImage", response.body().loginRes.getUserImage());
                    ld.putString("entityImage", response.body().loginRes.getEntityImage().replace("[", "").replace("]", ""));
                    ld.commit();

                    SharedPreferences ldd = getSharedPreferences("Logindetails", MODE_PRIVATE);
                    binding.etEmail.setText(ldd.getString("email", ""));
                    binding.etAltMobile.setText(ldd.getString("altmobileNum", ""));
                    binding.etEntityName.setText(ldd.getString("entityName", ""));
                    binding.etFirstName.setText(ldd.getString("firstName", ""));
                    binding.etLastName.setText(ldd.getString("lastName", ""));
                    binding.etPrimary.setText(ldd.getString("primaryLocation", ""));
                    binding.etMobile.setText(ldd.getString("mobileNum", ""));
                    Glide.with(EditProfileActivity.this)
                            .load(RestApi.BASE_URL + ldd.getString("userImage", ""))
                            .error(R.drawable.entity_profile)
                            .into(binding.ivProfile);
                    Glide.with(EditProfileActivity.this)
                            .load(RestApi.BASE_URL + ldd.getString("entityImage", ""))
                            .error(R.drawable.entity_profile)
                            .into(binding.ivEntity);

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


    public void onRegisterClick() {


//        Intent intent = new Intent(this, EditProfileActivity.class);
//        //
//        intent.putExtra("firstName", binding.etFirstName.getText().toString());
//        intent.putExtra("lastName", binding.etLastName.getText().toString());
//        intent.putExtra("mobileNum", binding.etMobile.getText().toString());
//        intent.putExtra("altNumber", binding.etAltMobile.getText().toString());
//        intent.putExtra("password", binding.etPwd.getText().toString());
//        intent.putExtra("emailId", binding.etEmail.getText().toString());
//        intent.putExtra("chooseLanguage", SettingsPreferences.getString(this, "lang"));
//        startActivity(intent);
    }

    private void updateProfile() {
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
        if (TextUtils.isEmpty(binding.etAltMobile.getText().toString())) {
            showToast("Enter Alternative Mobile Number");
            return;
        }

        if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
            showToast("Enter Email Address");
            return;
        }
        if (TextUtils.isEmpty(binding.etEntityName.getText().toString())) {
            showToast("Enter entity name");
            return;
        }
        if (TextUtils.isEmpty(binding.etPrimary.getText().toString())) {
            showToast("Select Primary Location");
            return;
        }
        showProgress();
        Map<String, Object> body = new HashMap<>();
        LoginResponse user = SettingsPreferences.getObject(this, SettingsPreferences.USER, LoginResponse.class);
        body.put("userId", user.getUserId());
        body.put("firstName", binding.etFirstName.getText().toString());
        body.put("lastName", binding.etLastName.getText().toString());
        body.put("mobileNum", binding.etMobile.getText().toString());
        body.put("altNumber", new String[]{binding.etAltMobile.getText().toString()});
        body.put("emailId", binding.etEmail.getText().toString());
        body.put("entityName", binding.etEntityName.getText().toString());
        body.put("primaryLocation", binding.etPrimary.getText().toString());
        String[] availableLocation = new String[binding.llAnotherLocations.getChildCount()];
        if (binding.llAnotherLocations.getChildCount() > 0) {
            for (int i = 0; i < binding.llAnotherLocations.getChildCount(); i++) {
                EditText location = (EditText) binding.llAnotherLocations.getChildAt(i).findViewById(R.id.et_location);
                availableLocation[i] = location.getText().toString();
//                body.put("availableLocation[" + i + "]", location.getText().toString());
            }
        }
        if (availableLocation.length != 0) {
            body.put("availableLocation", availableLocation);
        }
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

    List<LocationData> mLocationData = new ArrayList<>();

    /**
     * location dialog
     */
    private void locationAlertDialog(EditText editText) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertdialog_location, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        //
        Spinner spState = alertLayout.findViewById(R.id.sp_state);
        Spinner spDistrict = alertLayout.findViewById(R.id.sp_district);
        Spinner sp_mandal = alertLayout.findViewById(R.id.sp_mandal);
        Spinner sp_village = alertLayout.findViewById(R.id.sp_village);
        //
        RestApi.getInstance().getService().getLocations().enqueue(new Callback<LocationResponseRoot>() {
            @Override
            public void onResponse(Call<LocationResponseRoot> call, Response<LocationResponseRoot> response) {
                if (response.isSuccessful()) {
                    LocationResponseRoot res = response.body();
                    TreeSet<String> states = new TreeSet<>();
                    mLocationData = res.getData();
                    for (LocationData locationData : mLocationData) {
                        if (locationData.getState() != null) {
                            states.add(locationData.getState());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(states));
                    spState.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<LocationResponseRoot> call, Throwable t) {

            }
        });
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TreeSet<String> districts = new TreeSet<>();
                districts.add("--Select--");
                for (LocationData locationData : mLocationData) {
                    if (spState.getSelectedItem().toString().equalsIgnoreCase(locationData.getState())) {
                        if (locationData.getDistrict() != null) {
                            districts.add(locationData.getDistrict());
                        }
                    }

                }
                //
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(EditProfileActivity.this,
                        android.R.layout.simple_list_item_1, new ArrayList<>());
                sp_mandal.setAdapter(emptyAdapter);
                sp_village.setAdapter(emptyAdapter);
                //
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(districts));
                spDistrict.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TreeSet<String> districts = new TreeSet<>();
                districts.add("--Select--");
                for (LocationData locationData : mLocationData) {
                    if (spDistrict.getAdapter().getItem(i).toString().equalsIgnoreCase(
                            locationData.getDistrict())) {
                        if (locationData.getMandal() != null) {
                            districts.add(locationData.getMandal());
                        }

                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(districts));
                sp_mandal.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_mandal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TreeSet<String> districts = new TreeSet<>();
                districts.add("--Select--");
                for (LocationData locationData : mLocationData) {
                    if (sp_mandal.getAdapter().getItem(i).toString().equalsIgnoreCase(
                            locationData.getMandal())) {
                        if (locationData.getVillage() != null) {
                            districts.add(locationData.getVillage());
                        }
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(districts));
                sp_village.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //

        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();

        alertLayout.findViewById(R.id.linearcancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        alertLayout.findViewById(R.id.linearok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String village = sp_village.getSelectedItem().toString().equalsIgnoreCase("--Select--") ? ""
                        : sp_village.getSelectedItem().toString();
                if (!TextUtils.isEmpty(village)) {
                    if (editText != null) {
                        editText.setText(village);
                    } else {
                        addNewLocation(village);
                    }
                }

            }
        });
        alertLayout.findViewById(R.id.ivdelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void addNewLocation(String village) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.row_other_locations, binding.llAnotherLocations, false);

        EditText editText = view.findViewById(R.id.et_location);
        ImageView iv_delete = view.findViewById(R.id.iv_delete);
        editText.setText(village);
        editText.setEnabled(false);
        binding.llAnotherLocations.addView(view);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.llAnotherLocations.removeView(view);
            }
        });
    }

    private File profile;
    private File entity;

    void updatePics() {
        showProgress();
        Map<String, String> map = new HashMap<>();
        LoginResponse user = SettingsPreferences.getObject(this, SettingsPreferences.USER, LoginResponse.class);
        map.put("userId", user.getUserId());
        RestApi.getInstance().getService().updateUser(
                profile == null ? null : RestApi.prepareFilePart("userImage", profile.getAbsolutePath(), null),
                entity == null ? null : RestApi.prepareFilePart("entityImage", entity.getAbsolutePath(), null),
                RestApi.prepareBodyPart(map)
        ).enqueue(new Callback<RegistrationRootResponse>() {
            @Override
            public void onResponse(Call<RegistrationRootResponse> call, Response<RegistrationRootResponse> response) {
                hideDialog();

                if (response.isSuccessful()) {
                    setResult(Activity.RESULT_OK);
                    showToast("Updated Successfully");
                    finish();
                } else {
                    CommonErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), CommonErrorResponse.class);
                    showToast(errorResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RegistrationRootResponse> call, Throwable t) {
                hideDialog();
                showToast("Failed to register.");
            }
        });
    }
}