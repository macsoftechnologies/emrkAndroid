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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.activities.DashboardActivity;
import com.macsoftech.ekart.activities.ImagePreviewActivity;
import com.macsoftech.ekart.adapter.ProductNameAdapter;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.FragmentSearchEntityProductnamelBinding;
import com.macsoftech.ekart.model.LoginResponse;
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
public class SearchEntityProductNameFragment extends BaseFragment {

    RecyclerView recyclerView;

    List<UserProdResponse> list = new ArrayList<>();
    private FragmentSearchEntityProductnamelBinding binding;

    public SearchEntityProductNameFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_entity_productnamel, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        binding = FragmentSearchEntityProductnamelBinding.bind(view);
        recyclerView = view.findViewById(R.id.recycleView);
//        displayProductNames(getActivity());

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
        loadEntityDetails();
        loadEntityProductsDetails();
        binding.incls.ivEntity.setOnClickListener(v -> {
            if (currentUser != null) {
                String entityImageUrl = RestApi.BASE_URL + currentUser.getEntityImage();
                Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                intent.putExtra("url", entityImageUrl);
                startActivity(intent);
            }

        });

    }

    LoginResponse currentUser;

    private void loadEntityDetails() {
        if (getArguments() == null) {
            return;
        }
        Map<String, String> body = new HashMap<>();
        body.put("userId", getArguments().getString("userId"));
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

    private void loadEntityProductsDetails() {
        list.clear();
        ProductNameAdapter listAdapter = new ProductNameAdapter(list, getActivity());
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(listAdapter);
        listAdapter.onItemClickListener(clickListener);

        Map<String, String> body = new HashMap<>();
        body.put("userId", getArguments().getString("userId"));
        RestApi.getInstance().getService().getUserProducts(body).enqueue(new Callback<SearchRootResponse>() {
            @Override
            public void onResponse(Call<SearchRootResponse> call, Response<SearchRootResponse> response) {
                if (response.isSuccessful()) {
                    list.addAll(response.body().getData());
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchRootResponse> call, Throwable t) {

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


//    private void displayProductNames(Context mContext) {
//
//        list = new ArrayList<CompanyName>();
//        CompanyName campanyName = new CompanyName();
//        campanyName.setCompanyName("Product Name");
//        campanyName.setMobileNo("Product Name1");
//        campanyName.setQty("BC*DC");
//        list.add(campanyName);
//
//
//        CompanyName campanyName2 = new CompanyName();
//        campanyName2.setCompanyName("Product Name");
//        campanyName2.setMobileNo("Product Name1");
//        campanyName2.setQty("BC*DC");
//        list.add(campanyName2);
//
//        CompanyName campanyName3 = new CompanyName();
//        campanyName3.setCompanyName("Product Name");
//        campanyName3.setMobileNo("Product Name1");
//        campanyName3.setQty("BC*DC");
//        list.add(campanyName3);
//
//        CompanyName campanyName4 = new CompanyName();
//        campanyName4.setCompanyName("Product Name");
//        campanyName4.setMobileNo("roduct Name1");
//        campanyName4.setQty("BC*DC");
//        list.add(campanyName4);
//
//        CompanyName campanyName5 = new CompanyName();
//        campanyName5.setCompanyName("Product Name1");
//        campanyName5.setMobileNo("Product Name1");
//        campanyName5.setQty("BC*DC");
//        list.add(campanyName5);
//
//        ProductNameAdapter listAdapter = new ProductNameAdapter(list, mContext);
//        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(mContext);
//        recyclerView.setLayoutManager(linearLayout);
//        recyclerView.setAdapter(listAdapter);
//        listAdapter.onItemClickListener(clickListener);
//    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
//            int position = viewHolder.getAdapterPosition();

//            UserProdResponse
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            UserProdResponse item = list.get(position);

            DashboardActivity activity = (DashboardActivity) getActivity();
            Fragment fragment = new EntityDetailsFragment();
            Bundle args = new Bundle();
            args.putParcelable("data", item);
            fragment.setArguments(args);
            activity.replaceBackStackFragment(fragment);
            // CompanyName item = list.get(position);


        }
    };

}