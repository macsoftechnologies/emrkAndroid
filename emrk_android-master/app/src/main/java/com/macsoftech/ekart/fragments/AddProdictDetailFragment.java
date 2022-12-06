package com.macsoftech.ekart.fragments;

import static com.macsoftech.ekart.activities.BaseActivity.CAMERA_CAPTURE_IMAGE_REQUEST_CODE_300;
import static com.macsoftech.ekart.activities.BaseActivity.GALLERY_PICK_REQUEST_CODE_400;
import static com.macsoftech.ekart.activities.BaseActivity.IMAGE_DIRECTORY_NAME;
import static com.macsoftech.ekart.activities.BaseActivity.copy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.activities.BaseActivity;
import com.macsoftech.ekart.activities.DashboardActivity;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.FragmentAddproductdetailsBinding;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.LoginResponse;
import com.macsoftech.ekart.model.register.RegistrationRootResponse;
import com.macsoftech.ekart.model.search.GetUserResponseRoot;
import com.macsoftech.ekart.model.search.UserProdResponse;
import com.macsoftech.ekart.model.sizes.SizeModel;
import com.macsoftech.ekart.model.sizes.SizeModelRootResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddProdictDetailFragment extends BaseFragment {

    UserProdResponse data;
    @BindView(R.id.sprsize)
    Spinner sprsize;

    @BindView(R.id.sprlocation)
    TextView sprlocation;
    private FragmentAddproductdetailsBinding binding;

    public AddProdictDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addproductdetails, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        binding = FragmentAddproductdetailsBinding.bind(view);
        data = getArguments().getParcelable("data");
        binding.txtProduct.setText(data.getProductName());
        if (!data.getProductImage().isEmpty())
            Glide.with(getActivity())
                    .load(RestApi.BASE_URL + data.getProductImage().get(0))
                    .into(binding.ivProduct);
        loadSizes();
        loadLocations();
        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashboardActivity) getActivity()).choosePhoto();
            }
        });
        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        binding.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        sprlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocationsPopup();
            }
        });
    }

    private void saveData() {
        Map<String, String> map = new HashMap<>();

        map.put("productId", data.getProductId());
        map.put("productName", data.getProductName());
        map.put("entityName", SettingsPreferences.getUser(getActivity()).getEntityName());
        map.put("mobileNum", SettingsPreferences.getUser(getActivity()).getMobileNum());
        map.put("quantity", binding.etQty.getText().toString());
        map.put("size", sprsize.getSelectedItem().toString());
        map.put("length", binding.etFeet.getText().toString() + "." + binding.etInch.getText().toString());
//        map.put("location", sprlocation.getSelectedItem().toString());
        map.put("location", sprlocation.getText().toString());
        map.put("userId", SettingsPreferences.getUser(getActivity()).getUserId());

        showProgress();
        int index = 0;
        MultipartBody.Part[] images = new MultipartBody.Part[files.size()];
        for (String path : files) {
            MultipartBody.Part part = RestApi.prepareFilePart("productImage[" + index + "]", path, null);
            images[index] = part;
            index++;
        }
        RestApi.getInstance().getService()
                .addProduct(RestApi.prepareBodyPart(map),
                        images).enqueue(new Callback<RegistrationRootResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationRootResponse> call, Response<RegistrationRootResponse> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationRootResponse> call, Throwable t) {
                        hideDialog();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
//                File file = new File(Environment.getExternalStorageDirectory(),
//                        IMAGE_DIRECTORY_NAME);
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
                    addImage(photoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                UCrop.of(Uri.parse(photoFile.getAbsolutePath()), Uri.parse(photoFile.getAbsolutePath()))
////                        .withAspectRatio(16, 9)
//                        .start(this);
            }
        }
    }

    private void compressFile() throws IOException {
        Compressor compressor = new Compressor(this.getActivity());
        final File file = compressor.compressToFile(((BaseActivity) getActivity()).photoFile);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addImage(file);
            }
        });
    }

    List<String> files = new ArrayList<>();

    private void addImage(File file) {
        ImageView view = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.row_add_product_image, binding.ivPlaceHolder, false);
        Glide.with(getActivity())
                .load(file)
                .into(view);
        files.add(file.getAbsolutePath());
        binding.ivPlaceHolder.addView(view);
    }

    private void loadLocations() {
        showProgress();
        Map<String, String> body = new HashMap<>();
        LoginResponse user = SettingsPreferences.getObject(getActivity(), SettingsPreferences.USER, LoginResponse.class);
        body.put("userId", user.getUserId());
        RestApi.getInstance().getService().getUser(body).enqueue(new Callback<GetUserResponseRoot>() {
            @Override
            public void onResponse(Call<GetUserResponseRoot> call, Response<GetUserResponseRoot> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    try {
                        LoginResponse user = response.body().getUserFeedbackResponse().get(0);
                        locatinos.clear();
                        locatinos.add(user.getPrimaryLocation());
                        locatinos.addAll(user.getAvailableLocation());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserResponseRoot> call, Throwable t) {
                hideDialog();
            }
        });


    }

    private void loadSizes() {

        RestApi.getInstance().getService().getProductSizes()
                .enqueue(new Callback<SizeModelRootResponse>() {
                    @Override
                    public void onResponse(Call<SizeModelRootResponse> call, Response<SizeModelRootResponse> response) {
                        if (response.isSuccessful()) {
                            ArrayAdapter<SizeModel> ad = new ArrayAdapter<SizeModel>(getActivity(), android.R.layout.simple_spinner_item,
                                    response.body().getData().getSizesList());
                            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sprsize.setAdapter(ad);
                            sprsize.setPrompt("Size");
                        }
                    }

                    @Override
                    public void onFailure(Call<SizeModelRootResponse> call, Throwable t) {

                    }
                });
    }

    ArrayList<Integer> langList = new ArrayList<>();
    ArrayList<String> locatinos = new ArrayList<>();
    String[] langArray = {};
    boolean[] selectedLanguage = null;

    void showLocationsPopup() {
        langArray = locatinos.toArray(new String[0]);
        if (selectedLanguage == null || selectedLanguage.length != langArray.length) {
            selectedLanguage = new boolean[langArray.length];
        }

        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // set title
        builder.setTitle("Select Locations");

        // set dialog non cancelable
        builder.setCancelable(false);

        builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    langList.add(i);
                    // Sort array list
                    Collections.sort(langList);
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    langList.remove(Integer.valueOf(i));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Initialize string builder
                StringBuilder stringBuilder = new StringBuilder();
                // use for loop
                for (int j = 0; j < langList.size(); j++) {
                    // concat array value
                    selectedLanguage[langList.get(j)] = true;
                    stringBuilder.append(langArray[langList.get(j)]);
                    // check condition
                    if (j != langList.size() - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ");
                    }
                }
                if (stringBuilder.toString().trim().length() == 0) {
                    sprlocation.setText("Select Locations");
                }
                // set text on textView
                sprlocation.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
//        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // use for loop
//                for (int j = 0; j < selectedLanguage.length; j++) {
//                    // remove all selection
//                    selectedLanguage[j] = false;
//                    // clear language list
//                    langList.clear();
//                    // clear text view value
//                    sprlocation.setText("");
//                }
//            }
//        });
        // show dialog
        builder.show();
    }


}