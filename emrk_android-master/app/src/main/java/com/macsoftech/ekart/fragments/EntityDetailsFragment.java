package com.macsoftech.ekart.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.activities.DashboardActivity;
import com.macsoftech.ekart.activities.EditContactsListActivity;
import com.macsoftech.ekart.activities.ImagePreviewActivity;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.FragmentEntityProductDetailBinding;
import com.macsoftech.ekart.model.LoginResponse;
import com.macsoftech.ekart.model.ProductDetailsRoot;
import com.macsoftech.ekart.model.search.GetUserResponseRoot;
import com.macsoftech.ekart.model.search.SearchRootResponse;
import com.macsoftech.ekart.model.search.UserProdResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EntityDetailsFragment extends BaseFragment {

    UserProdResponse data;
    private FragmentEntityProductDetailBinding binding;

    public EntityDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entity_product_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        binding = FragmentEntityProductDetailBinding.bind(view);
        data = getArguments().getParcelable("data");
        view.findViewById(R.id.txtviewentity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DashboardActivity activity = (DashboardActivity) getActivity();
//                activity.replaceBackStackFragment(new SearchEntityProductNameFragment());
            }
        });

        view.findViewById(R.id.txtviewentity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardActivity activity = (DashboardActivity) getActivity();
                activity.replaceBackStackFragment(new SearchEntityProductNameFragment());

            }
        });
        view.findViewById(R.id.txt_view_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactAlertDialog();
            }
        });

        view.findViewById(R.id.txt_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    String phone = currentUser.getMobileNum();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            }
        });


        binding.txtProduct.setText(data.getProductName());
        binding.txtQty.setText(": " + data.getQuantity());
        binding.txtSize.setText(": " + data.getSize());
        binding.txtLocation.setText(": " + data.getLocation());
        binding.txtLength.setText(": " + data.getLength());

        if (!data.getProductImage().isEmpty()) {
            Glide.with(getActivity())
                    .load(RestApi.BASE_URL + data.getProductImage().get(0))
                    .into(binding.ivProduct);
        }

        loadEntityDetails();
        binding.incls.ivEntity.setOnClickListener(v -> {
            if (currentUser != null) {
                String entityImageUrl = RestApi.BASE_URL + currentUser.getEntityImage();
                Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                intent.putExtra("url", entityImageUrl);
                startActivity(intent);
            }

        });
        loadProductDetailProduct();
    }

    void loadProductDetailProduct() {
        RestApi.getInstance().getService()
                .getProductAndVendorById(data.getProductId())
                .enqueue(new Callback<ProductDetailsRoot>() {
                    @Override
                    public void onResponse(Call<ProductDetailsRoot> call, Response<ProductDetailsRoot> response) {
                        System.out.println("Dadi getProductAndVendorById " + response.body().toString());

                        if (response.isSuccessful()) {
                            try {
                                binding.txtProductName2.setText(response.body().getResp().getProductName1());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductDetailsRoot> call, Throwable t) {
                        System.out.println("Dadi getProductAndVendorById " + t.getMessage().toString());
                    }
                });
    }


    LoginResponse currentUser;

    private void loadEntityDetails() {
        Map<String, String> body = new HashMap<>();
        body.put("userId", data.getUserId());
        RestApi.getInstance().getService().getUser(body).enqueue(new Callback<GetUserResponseRoot>() {
            @Override
            public void onResponse(Call<GetUserResponseRoot> call, Response<GetUserResponseRoot> response) {
                if (response.isSuccessful()) {
                    try {
                        currentUser = response.body().getUserFeedbackResponse().get(0);
                        binding.incls.txtEntity.setText(currentUser.getEntityName());
                        binding.incls.txtVendorName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
                        binding.incls.txtMobile.setText(currentUser.getMobileNum());
                        Glide.with(getActivity())
                                .load(RestApi.BASE_URL + currentUser.getEntityImage())
                                .error(R.drawable.entity_profile)
                                .into(binding.incls.ivEntity);
                    } catch (Exception e) {
                        e.printStackTrace();
                        binding.incls.txtEntity.setText("");
                        binding.incls.txtVendorName.setText("");
                        binding.incls.txtMobile.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserResponseRoot> call, Throwable t) {

            }
        });
    }

    private void addContactAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertdialog_entity_contact, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LinearLayout ll_contacts = alertLayout.findViewById(R.id.ll_contacts);

        if (currentUser != null) {
            List<String> contacts = new ArrayList<>();
            contacts.add(currentUser.getMobileNum());

            if (currentUser.getAltNumber() instanceof String) {
                contacts.add(currentUser.getAltNumber().toString());
            }
            if (currentUser.getAltNumber() instanceof List && !((List<?>) currentUser.getAltNumber()).isEmpty()) {
                for (String v : ((List<String>) currentUser.getAltNumber())) {
                    contacts.add(v);
                }
            }
            for (int i = 1; i <= contacts.size(); i++) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_contacts, null);
                TextView tv_name = view.findViewById(R.id.tv_name);
                TextView txt_mobile = view.findViewById(R.id.txt_mobile);
                txt_mobile.setText(contacts.get(i - 1));
                tv_name.setText(i + ". " + currentUser.getFirstName() + " " + currentUser.getLastName());
                ll_contacts.addView(view);
            }
        }
        alert.setView(alertLayout);
        alert.setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }


}