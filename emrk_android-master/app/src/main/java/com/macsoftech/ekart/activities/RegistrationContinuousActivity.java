package com.macsoftech.ekart.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.ActivityRegistrationContinuousBinding;
import com.macsoftech.ekart.model.CommonErrorResponse;
import com.macsoftech.ekart.model.LocationData;
import com.macsoftech.ekart.model.LocationResponseRoot;
import com.macsoftech.ekart.model.register.RegistrationRootResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationContinuousActivity extends BaseActivity {

    private ActivityRegistrationContinuousBinding binding;
    private boolean isProfile;
    private File profile;
    private File entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration_continuous);
        binding = ActivityRegistrationContinuousBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ButterKnife.bind(this, binding.getRoot());
        getSupportActionBar().hide();

        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProfile = true;
                choosePhoto();
            }
        });

        binding.ivEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProfile = false;
                choosePhoto();
            }
        });
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
        binding.etEntityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (binding.etEntityName.getText().toString().length()>0){
                    binding.etPrimary.setEnabled(true);
                    binding.etPrimary.setFocusable(true);
                    binding.etPrimary.setAlpha(1.0f);
                    binding.txtAddAnother.setEnabled(true);
                    binding.txtAddAnother.setFocusable(true);
                    binding.txtAddAnother.setAlpha(1.0f);
                    binding.ivEntity.setEnabled(true);
                    binding.ivEntity.setFocusable(true);
                    binding.ivEntity.setAlpha(1.0f);
                    binding.ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.add,null));
                    binding.ivEntity.setImageDrawable(getResources().getDrawable(R.drawable.add,null));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.etEntityName.getText().toString().length()==0){
                    binding.etPrimary.setEnabled(false);
                    binding.etPrimary.setFocusable(false);
                    binding.etPrimary.setAlpha(.5f);
                    binding.txtAddAnother.setEnabled(false);
                    binding.txtAddAnother.setFocusable(false);
                    binding.txtAddAnother.setAlpha(.5f);
                    binding.ivEntity.setEnabled(false);
                    binding.ivEntity.setFocusable(false);
                    binding.ivEntity.setAlpha(.5f);

                }
            }
        });
        binding.etPrimary.setEnabled(false);
        binding.etPrimary.setFocusable(false);
        binding.etPrimary.setAlpha(.5f);
        binding.txtAddAnother.setEnabled(false);
         binding.txtAddAnother.setFocusable(false);
        binding.txtAddAnother.setAlpha(.5f);
        binding.ivEntity.setEnabled(false);
        binding.ivEntity.setFocusable(false);
        binding.ivEntity.setAlpha(.5f);

        SharedPreferences entitty= getSharedPreferences("Entitiy",MODE_PRIVATE);
        if (entitty.getString("name", "").toString().length()>0){
            binding.etEntityName.setText(entitty.getString("name", ""));;
            binding.txtAddAnother.setEnabled(true);
            binding.txtAddAnother.setFocusable(true);
            binding.txtAddAnother.setAlpha(1.0f);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                Glide.with(RegistrationContinuousActivity.this)
                        .load(photoFile)
                        .into(binding.ivProfile);
            } else {
                entity = photoFile;
                Glide.with(RegistrationContinuousActivity.this)
                        .load(photoFile)
                        .into(binding.ivEntity);
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
                    Glide.with(RegistrationContinuousActivity.this)
                            .load(file)
                            .into(binding.ivProfile);
                } else {
                    entity = file;
                    Glide.with(RegistrationContinuousActivity.this)
                            .load(file)
                            .into(binding.ivEntity);
                }
            }
        });
    }


    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.btn_register)
    public void onRegisterClick() {
        Bundle extras = getIntent().getExtras();
        Map<String, String> map = new HashMap<>();
        for (String key : extras.keySet()) {
            map.put(key, extras.getString(key));
        }
//        if (TextUtils.isEmpty(binding.etEntityName.getText().toString())) {
//            showToast("Enter entity name");
//            return;
//        }
       /*   if (TextUtils.isEmpty(binding.etPrimary.getText().toString())) {
            showToast("Select Primary Location");
            return;
        }

      if (profile == null) {
            showToast("Add User Image");
            return;
        }
        if (entity == null) {
            showToast("Add Entity Image");
            return;
        } */

        map.put("entityName", binding.etEntityName.getText().toString());
        map.put("primaryLocation", binding.etPrimary.getText().toString());
        if (binding.llAnotherLocations.getChildCount() > 0) {
            for (int i = 0; i < binding.llAnotherLocations.getChildCount(); i++) {
                EditText location = (EditText) binding.llAnotherLocations.getChildAt(i).findViewById(R.id.et_location);
                map.put("availableLocation[" + i + "]", location.getText().toString());
            }
        }
        showProgress();

       if (photoFile == null){
           System.out.println("Dadi this is the photofile url null values");
           Bitmap bitmap= BitmapFactory.decodeResource(RegistrationContinuousActivity.this.getResources(),
                   R.drawable.check);



           profile = new File(String.valueOf(bitmap));
           entity = new File(String.valueOf(bitmap));
       }
        RestApi.getInstance().getService().register(
                RestApi.prepareFilePart("userImage", profile.getAbsolutePath(), null),
                RestApi.prepareFilePart("entityImage", entity.getAbsolutePath(), null),
                RestApi.prepareBodyPart(map)
        ).enqueue(new Callback<RegistrationRootResponse>() {
            @Override
            public void onResponse(Call<RegistrationRootResponse> call, Response<RegistrationRootResponse> response) {
                hideDialog();
                System.out.println("dadi login response "+response.body().getStatusCode());
                if (response.isSuccessful()) {
                    Intent intent = new Intent(RegistrationContinuousActivity.this, OtpVerificationActivity.class);
                    intent.putExtra("mobileNum", getIntent().getStringExtra("mobileNum"));
                    startActivity(intent);
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
                System.out.println("dadi login response "+t.getMessage().toString());

            }
        });


        //=====
//        startActivity(new Intent(this, OtpVerificationActivity.class));
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        SharedPreferences.Editor entitty= getSharedPreferences("Entitiy",MODE_PRIVATE).edit();
        entitty.putString("name", binding.etEntityName.getText().toString());
        entitty.commit();
        finish();
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationContinuousActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(states));
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
                districts.add("--District--");
                for (LocationData locationData : mLocationData) {
                    if (spState.getSelectedItem().toString().equalsIgnoreCase(locationData.getState())) {
                        if (locationData.getDistrict() != null) {
                            districts.add(locationData.getDistrict());
                        }
                    }

                }
                //
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(RegistrationContinuousActivity.this,
                        android.R.layout.simple_list_item_1, new ArrayList<>());
                sp_mandal.setAdapter(emptyAdapter);
                sp_village.setAdapter(emptyAdapter);
                //
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationContinuousActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(districts));
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
                districts.add("--Mandal--");
                for (LocationData locationData : mLocationData) {
                    if (spDistrict.getAdapter().getItem(i).toString().equalsIgnoreCase(
                            locationData.getDistrict())) {
                        if (locationData.getMandal() != null) {
                            districts.add(locationData.getMandal());
                        }

                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationContinuousActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(districts));
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
                districts.add("--Village--");
                for (LocationData locationData : mLocationData) {
                    if (sp_mandal.getAdapter().getItem(i).toString().equalsIgnoreCase(
                            locationData.getMandal())) {
                        if (locationData.getVillage() != null) {
                            districts.add(locationData.getVillage());
                        }
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationContinuousActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(districts));
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
//        EditText editText = (EditText) LayoutInflater.from(this)
//                .inflate(R.layout.row_other_locations, binding.llAnotherLocations, false);
//        editText.setText(village);
//        editText.setEnabled(false);
//        binding.llAnotherLocations.addView(editText);
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
}