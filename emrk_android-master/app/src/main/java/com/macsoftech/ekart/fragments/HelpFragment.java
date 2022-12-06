package com.macsoftech.ekart.fragments;

import static com.macsoftech.ekart.activities.BaseActivity.CAMERA_CAPTURE_IMAGE_REQUEST_CODE_300;
import static com.macsoftech.ekart.activities.BaseActivity.GALLERY_PICK_REQUEST_CODE_400;
import static com.macsoftech.ekart.activities.BaseActivity.IMAGE_DIRECTORY_NAME;
import static com.macsoftech.ekart.activities.BaseActivity.copy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.macsoftech.ekart.R;
import com.macsoftech.ekart.activities.BaseActivity;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.FragmentHelpBinding;
import com.macsoftech.ekart.helper.Helper;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.LocationData;
import com.macsoftech.ekart.model.LocationResponseRoot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import id.zelory.compressor.Compressor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HelpFragment extends BaseFragment {


    @Nullable
    @BindView(R.id.txt_header_1)
    TextView txt_header_1;

    @Nullable
    @BindView(R.id.txt_header_2)
    TextView txt_header_2;

    @Nullable
    @BindView(R.id.txt_header_3)
    TextView txt_header_3;

    @Nullable
    @BindView(R.id.txt_header_4)
    TextView txt_header_4;

    @Nullable
    @BindView(R.id.txt_header_5)
    TextView txt_header_5;

    @Nullable
    @BindView(R.id.ll_content_1)
    LinearLayout ll_content_1;

    @Nullable
    @BindView(R.id.ll_content_2)
    LinearLayout ll_content_2;

    @Nullable
    @BindView(R.id.ll_content_3)
    LinearLayout ll_content_3;

    @Nullable
    @BindView(R.id.ll_content_4)
    LinearLayout ll_content_4;

    @Nullable
    @BindView(R.id.ll_content_5)
    LinearLayout ll_content_5;
    private FragmentHelpBinding binding;
    private List<LocationData> mLocationData;
    public static String menuval="main";
    LinearLayout one,two,three,four,five;
    TextView heading;
    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        ButterKnife.bind(getActivity(), view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHelpBinding.bind(view);
        heading = view.findViewById(R.id.heading);
        ll_content_1 = view.findViewById(R.id.ll_content_1);
        ll_content_2 = view.findViewById(R.id.ll_content_2);
        ll_content_3 = view.findViewById(R.id.ll_content_3);
        ll_content_4 = view.findViewById(R.id.ll_content_4);
        ll_content_5 = view.findViewById(R.id.ll_content_5);
        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);
        five = view.findViewById(R.id.five);

        if (menuval.equals("request")){
            heading.setText("REQUEST");
            one.setVisibility(View.VISIBLE);
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.GONE);
            four.setVisibility(View.GONE);
            five.setVisibility(View.GONE);
        }else if (menuval.equals("help")){
            heading.setText("HELP");
            one.setVisibility(View.GONE);
            two.setVisibility(View.GONE);
            three.setVisibility(View.VISIBLE);
            four.setVisibility(View.VISIBLE);
            five.setVisibility(View.VISIBLE);
        }else {
            heading.setText("HELP");
            one.setVisibility(View.VISIBLE);
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.VISIBLE);
            four.setVisibility(View.VISIBLE);
            five.setVisibility(View.VISIBLE);
        }
        view.findViewById(R.id.txt_header_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ontxtHeader1Click();
            }
        });


        view.findViewById(R.id.txt_header_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ontxtHeader2Click();
            }
        });


        view.findViewById(R.id.txt_header_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ontxtHeader3Click();
            }
        });

        view.findViewById(R.id.txt_header_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ontxtHeader4Click();
            }
        });

        view.findViewById(R.id.txt_header_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ontxtHeader5Click();
            }
        });

        binding.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReport();
            }
        });

        binding.btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFeedBack();
            }
        });
        binding.btnMissingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMissingProduct();
            }
        });

        binding.txtAddMissingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).choosePhoto();
            }
        });

        binding.txtLocationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMissingLocation();
            }
        });
    }

    private void submitMissingLocation() {
        if (binding.spState.getText().length() == 0
                || binding.spDistrict.getText().length() == 0
                || binding.spMandal.getText().length() == 0) {
            Helper.showShortToast(getActivity(), "Please Enter State/District/Mandal");
            return;
        } else if (TextUtils.isEmpty(binding.etVillage.getText().toString().trim())) {
            Helper.showShortToast(getActivity(), "Please Enter Area/Village name");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userId", SettingsPreferences.getUser(getActivity()).getUserId());
        map.put("state", binding.spState.getText().toString());
        map.put("district", binding.spDistrict.getText().toString());
        map.put("mandal", binding.spMandal.getText().toString());
        map.put("village", binding.etVillage.getText().toString());
        BaseActivity.hideKeyboard(getActivity());
        showProgress();
        RestApi.getInstance().getService()
                .unavailLocationCreate(map)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            Helper.showShortToast(getActivity(), "Submitted Successfully");
                            binding.etVillage.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                    }
                });
    }

    private void submitMissingProduct() {
        String product = binding.etProduct.getText().toString().trim();
        String desc = binding.etDesc.getText().toString().trim();

        if (TextUtils.isEmpty(product)) {
            Helper.showShortToast(getActivity(), "Please Enter Product");
            return;
        }
        if (TextUtils.isEmpty(product)) {
            Helper.showShortToast(getActivity(), "Please Enter Description");
            return;
        }
//        if (profile == null) {
//            Helper.showShortToast(getActivity(), "Please Add image");
//            return;
//        }
        BaseActivity.hideKeyboard(getActivity());
        Map<String, String> map = new HashMap<>();
        map.put("userId", SettingsPreferences.getUser(getActivity()).getUserId());
        map.put("productName", product);
        map.put("productDescription", desc);
        showProgress();
        Call<ResponseBody> call;
        if (profile == null) {
            call = RestApi.getInstance().getService()
                    .createUnavailbleproduct(RestApi.prepareBodyPart(map)
                    );
        } else {
            call = RestApi.getInstance().getService()
                    .createUnavailbleproduct(RestApi.prepareFilePart("productImage", profile.getAbsolutePath(), null),
                            RestApi.prepareBodyPart(map));
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    binding.etProduct.setText("");
                    binding.etDesc.setText("");
                    binding.txtAddMissingProduct.setText("");
                    profile = null;
                    Helper.showShortToast(getActivity(), "Submitted Successfully");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                && resultCode == Activity.RESULT_OK
        ) {
            if (requestCode == GALLERY_PICK_REQUEST_CODE_400
                    && resultCode == Activity.RESULT_OK
                    && data != null) {
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                if (cursor == null || cursor.getCount() < 1) {
                    return;
                }
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                if (columnIndex < 0) // no column index
                    return; // DO YOUR ERROR HANDLING

                String picturePath = cursor.getString(columnIndex);
                cursor.close(); // close cursor
                File file = new File(getActivity().getExternalFilesDir(null), IMAGE_DIRECTORY_NAME);

                if (!file.isDirectory()) {
                    file.mkdirs();
                }
                File photoFile = new File(file.getPath() + "/"
                        + System.currentTimeMillis() + ".jpg");

                if (!photoFile.getParentFile().exists()) {
                    photoFile.getParentFile().mkdirs();
                }
                copy(new File(picturePath), photoFile);
                Compressor compressor = new Compressor(getActivity().getApplicationContext());
                try {
                    photoFile = compressor.compressToFile(photoFile);
                    profile = photoFile;
                    binding.txtAddMissingProduct.setText(profile.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private File profile;

    private void compressFile() throws IOException {
        Compressor compressor = new Compressor(getActivity());
        final File file = compressor.compressToFile(((BaseActivity) getActivity()).photoFile);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                profile = file;
                binding.txtAddMissingProduct.setText(file.getName());
            }
        });
    }

    private void saveReport() {
        if (binding.etReport.getText().toString().trim().length() == 0) {
            return;
        }
        BaseActivity.hideKeyboard(getActivity());
        showProgress();
        Map<String, String> map = new HashMap<>();
        map.put("userId", SettingsPreferences.getUser(getActivity()).getUserId());
        map.put("report", binding.etReport.getText().toString().trim());
        RestApi.getInstance().getService().addReport(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    binding.etReport.setText("");
                    Helper.showShortToast(getActivity(), "Reported Successfully.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
            }
        });
    }

    private void saveFeedBack() {
        if (binding.etFeedback.getText().toString().trim().length() == 0) {
            return;
        }
        BaseActivity.hideKeyboard(getActivity());
        showProgress();
        Map<String, String> map = new HashMap<>();
        map.put("userId", SettingsPreferences.getUser(getActivity()).getUserId());
        map.put("feedBack", binding.etFeedback.getText().toString().trim());
        RestApi.getInstance().getService().addUserFeedback(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    binding.etFeedback.setText("");
                    Helper.showShortToast(getActivity(), "Feedback Sent Successfully.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
            }
        });
    }

    @Optional
    @OnClick(R.id.txt_header_1)
    public void ontxtHeader1Click() {
        ll_content_1.setVisibility(View.VISIBLE);
        ll_content_2.setVisibility(View.GONE);
        ll_content_3.setVisibility(View.GONE);
        ll_content_4.setVisibility(View.GONE);
        ll_content_5.setVisibility(View.GONE);
    }


    @Optional
    @OnClick(R.id.txt_header_2)
    public void ontxtHeader2Click() {
        ll_content_1.setVisibility(View.GONE);
        ll_content_2.setVisibility(View.VISIBLE);
        ll_content_3.setVisibility(View.GONE);
        ll_content_4.setVisibility(View.GONE);
        ll_content_5.setVisibility(View.GONE);
//        loadLocationsData();
    }


    @Optional
    @OnClick(R.id.txt_header_3)
    public void ontxtHeader3Click() {
        ll_content_1.setVisibility(View.GONE);
        ll_content_2.setVisibility(View.GONE);
        ll_content_3.setVisibility(View.VISIBLE);
        ll_content_4.setVisibility(View.GONE);
        ll_content_5.setVisibility(View.GONE);
    }

    @Optional
    @OnClick(R.id.txt_header_4)
    public void ontxtHeader4Click() {
        ll_content_1.setVisibility(View.GONE);
        ll_content_2.setVisibility(View.GONE);
        ll_content_3.setVisibility(View.GONE);
        ll_content_4.setVisibility(View.VISIBLE);
        ll_content_5.setVisibility(View.GONE);
    }

    @Optional
    @OnClick(R.id.txt_header_5)
    public void ontxtHeader5Click() {
        ll_content_1.setVisibility(View.GONE);
        ll_content_2.setVisibility(View.GONE);
        ll_content_3.setVisibility(View.GONE);
        ll_content_4.setVisibility(View.GONE);
        ll_content_5.setVisibility(View.VISIBLE);
    }

    void loadLocationsData() {
        View alertLayout = getView();
        Spinner spState = alertLayout.findViewById(R.id.sp_state);
        Spinner spDistrict = alertLayout.findViewById(R.id.sp_district);
        Spinner sp_mandal = alertLayout.findViewById(R.id.sp_mandal);
//        Spinner sp_village = alertLayout.findViewById(R.id.sp_village);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}