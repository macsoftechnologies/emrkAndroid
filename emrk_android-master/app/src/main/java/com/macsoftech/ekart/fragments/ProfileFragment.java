package com.macsoftech.ekart.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.multidex.BuildConfig;

import com.bumptech.glide.Glide;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.activities.AddChangePinActivity;
import com.macsoftech.ekart.activities.EditContactsListActivity;
import com.macsoftech.ekart.activities.EditProfileActivity;
import com.macsoftech.ekart.activities.LoginActivity;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.FragmentProfileBinding;
import com.macsoftech.ekart.helper.Helper;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.LocationData;
import com.macsoftech.ekart.model.LocationResponseRoot;
import com.macsoftech.ekart.model.LoginResponse;
import com.macsoftech.ekart.model.language.LanguageRootResponse;
import com.macsoftech.ekart.model.language.SelectedLanguages;
import com.macsoftech.ekart.model.search.GetUserResponseRoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {


    @BindView(R.id.tvlogout)
    TextView tvlogout;

    private FragmentProfileBinding binding;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        binding = FragmentProfileBinding.bind(view);
        SharedPreferences editclicked = view.getContext().getSharedPreferences("editclicked", MODE_PRIVATE);
        System.out.println("Dadi editpotion " + editclicked.getString("editclicked", ""));
        if (editclicked.getString("editclicked", "").equals("yes")) {
            callEditProfile();
        }

        try {
            loadEntityDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.txtAddPin.setOnClickListener(view1 -> {
            navigateToNextPinActivity();
        });
        binding.txtChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguagePopup();
            }
        });
        binding.txtChangeContactNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditContactNo();
            }
        });

        binding.txtAddEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationAlertDialog();
            }
        });

        binding.txtSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), EditContactsListActivity.class));
            }
        });
        binding.txtVersion.setText("Version: " + BuildConfig.VERSION_NAME);


        binding.editicon.setOnClickListener(view12 -> callEditProfile());
    }

    private void callEditProfile() {

        startActivityForResult(new Intent(getActivity(), EditProfileActivity.class), 102);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            loadEntityDetails();
        }
    }

    private void navigateToNextPinActivity() {
        startActivity(new Intent(getActivity(), AddChangePinActivity.class));
    }

    private void showEditContactNo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Contact");
        EditText editText = new EditText(getActivity());
        editText.setSingleLine(true);
        editText.setHint("Enter Contact No");
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        editText.setText(SettingsPreferences.getUser(getActivity()).getMobileNum());

        builder.setView(editText);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateContactNo(editText.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void updateContactNo(String value) {
        if (value.trim().length() == 0) {
            Helper.showShortToast(getActivity(), "Enter Contact Number");
            return;
        }
        Map<String, Object> body = new HashMap<>();
        body.put("mobileNum", value);
        showProgress();
        RestApi.getInstance().getService().updateUser(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            LoginResponse user = SettingsPreferences.getUser(getActivity());
                            user.setMobileNum(value);
                            SettingsPreferences.saveObject(getActivity(), SettingsPreferences.USER, user);
                            Helper.showShortToast(getActivity(), "Updated.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                    }
                });
    }

    private void changeLanguagePopup() {
        showProgress();
        RestApi.getInstance().getService()
                .getLanguages()
                .enqueue(new Callback<LanguageRootResponse>() {
                    @Override
                    public void onResponse(Call<LanguageRootResponse> call, Response<LanguageRootResponse> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            showLangPopup(response.body().getData().getSelectedLanguages());
                        }
                    }

                    @Override
                    public void onFailure(Call<LanguageRootResponse> call, Throwable t) {
                        hideDialog();
                    }
                });
    }

    private void showLangPopup(List<SelectedLanguages> selectedLanguages) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] items = new CharSequence[selectedLanguages.size()];
        int index = 0;
        int selectedItem = -1;
        String lang = SettingsPreferences.getString(getActivity(), "lang");
        for (SelectedLanguages languages : selectedLanguages) {
            if (lang.equalsIgnoreCase(languages.getLanguage())) {
                selectedItem = index;
            }
            items[index++] = languages.getLanguage().toUpperCase();
        }
        final int[] position = new int[1];
        builder.setSingleChoiceItems(items, selectedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                position[0] = i;
            }
        });
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callUpdateLanguageApi(selectedLanguages.get(position[0]));
            }
        });
        builder.setTitle("Select Language");
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void callUpdateLanguageApi(SelectedLanguages selectedLanguages) {
//        Helper.showShortToast(getActivity(), selectedLanguages.getLanguage());
        //
        Map<String, Object> body = new HashMap<>();
        body.put("chooseLanguage", selectedLanguages.getLanguage());
        showProgress();
        RestApi.getInstance().getService().updateUser(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            Helper.showShortToast(getActivity(), "Updated.");
                            SettingsPreferences.saveString(getActivity(), "lang", selectedLanguages.getLanguage());
                            LoginResponse user = SettingsPreferences.getUser(getActivity());
                            user.setChooseLanguage(selectedLanguages.getLanguage());
                            SettingsPreferences.saveObject(getActivity(), SettingsPreferences.USER, user);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                    }
                });
        //

    }


    private void loadEntityDetails() {
//        showProgress();

        Map<String, String> body = new HashMap<>();
//        body.put("userId", getArguments().getString("userId"));
        LoginResponse user = SettingsPreferences.getObject(getActivity(), SettingsPreferences.USER, LoginResponse.class);
        body.put("userId", user.getUserId());
        //   body.put("userId", "c2200473-1b1b-4d14-9068-0d617652e450");
        RestApi.getInstance().getService().getUser(body).enqueue(new Callback<GetUserResponseRoot>() {
            @Override
            public void onResponse(Call<GetUserResponseRoot> call, Response<GetUserResponseRoot> response) {
                hideDialog();
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        LoginResponse user = response.body().getUserFeedbackResponse().get(0);
                        binding.ownername.setText(user.getEntityName());
                        binding.edtprofilename.setText(user.getFirstName() + " " + user.getLastName());
                        binding.txtLocation.setText(user.getPrimaryLocation().toUpperCase());
//                        binding.txtMobile.setText(user.getMobileNum());
/*
                        Glide.with(getActivity())
                                .load(RestApi.BASE_URL + user.getUserImage())
                                .error(R.drawable.entity_profile)
                                .into(binding.profileicon);
*/
                        System.out.println("complete Image"+RestApi.BASE_URL+user.getUserImage());
                        SharedPreferences ld = getContext().getSharedPreferences("Logindetails",MODE_PRIVATE);
                        binding.edtprofilename.setText(ld.getString("firstName","") + " " + ld.getString("lastName",""));
                        binding.ownername.setText(ld.getString("entityName",""));
                        binding.txtLocation.setText(ld.getString("primaryLocation",""));
                        Glide.with(getActivity())
                                .load(RestApi.BASE_URL + ld.getString("userImage",""))
                                .error(R.drawable.entity_profile)
                                .into(binding.profileicon);
                        System.out.println("complete Image"+RestApi.BASE_URL+ld.getString("userImage",""));
                        ld.getString("userImage","");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserResponseRoot> call, Throwable t) {
                hideDialog();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.tvlogout)
    public void onLogoutClick() {
        SettingsPreferences.saveBoolean(getActivity(), "LOGIN", false);
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    /**
     * location dialog
     */
    private void locationAlertDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertdialog_location, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>(states));
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
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, new ArrayList<>());
                sp_mandal.setAdapter(emptyAdapter);
                sp_village.setAdapter(emptyAdapter);
                //
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>(districts));
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>(districts));
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>(districts));
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
                    saveLocation(village);
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

    List<LocationData> mLocationData = new ArrayList<>();

    private void saveLocation(String village) {
        Map<String, Object> body = new HashMap<>();
        LoginResponse user = SettingsPreferences.getUser(getActivity());
        int i = 0;
        TreeSet<String> list = new TreeSet<>();
        for (; i < user.getAvailableLocation().size(); i++) {
//            body.put("availableLocation[" + i + "]", user.getAvailableLocation().get(i));
            list.add(user.getAvailableLocation().get(i));
        }
//        body.put("availableLocation[" + i + "]", village);
        list.add(village);
        body.put("availableLocation", list);
        showProgress();
        RestApi.getInstance().getService().updateUser(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            LoginResponse user = SettingsPreferences.getUser(getActivity());
                            //Add new village
                            LocationData data = new LocationData();
                            data.setVillage(village);
                            user.getAvailableLocationObj().add(data);
                            SettingsPreferences.saveObject(getActivity(), SettingsPreferences.USER, user);
                            Helper.showShortToast(getActivity(), "Updated.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                    }
                });


    }


}