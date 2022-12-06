package com.macsoftech.ekart.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.activities.DashboardActivity;
import com.macsoftech.ekart.activities.EditContactsListActivity;
import com.macsoftech.ekart.activities.ImagePreviewActivity;
import com.macsoftech.ekart.adapter.ProductNameAdapter;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.FragmentMyEntityBinding;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.LoginResponse;
import com.macsoftech.ekart.model.search.GetUserResponseRoot;
import com.macsoftech.ekart.model.search.SearchRootResponse;
import com.macsoftech.ekart.model.search.UserProdResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyEntityFragment extends BaseFragment {

    List<UserProdResponse> list = new ArrayList<>();

    @BindView(R.id.tvcontacts)
    TextView tvcontacts;

    @BindView(R.id.tvlocation)
    TextView tvlocation;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private FragmentMyEntityBinding binding;

    public MyEntityFragment() {
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
        return inflater.inflate(R.layout.fragment_my_entity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        binding = FragmentMyEntityBinding.bind(view);
        binding.tvaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardActivity activity = (DashboardActivity) getActivity();
                AddProductSearchFragment fragment = new AddProductSearchFragment();
                activity.replaceBackStackFragment(fragment);
            }
        });
        tvcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactAlertDialog();
            }
        });

        tvlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationAlertDialog();
            }
        });
        loadEntityDetails();
        loadEntityProductsDetails();
    }

    LoginResponse currentUser;

    private void loadEntityDetails() {

        Map<String, String> body = new HashMap<>();
//       body.put("userId", getArguments().getString("userId"));
        LoginResponse user = SettingsPreferences.getObject(getActivity(), SettingsPreferences.USER, LoginResponse.class);
        body.put("userId", user.getUserId());
        //body.put("userId", "7d415ca3-22f3-421b-9f4e-df261ea0a655");
        RestApi.getInstance().getService().getUser(body).enqueue(new Callback<GetUserResponseRoot>() {
            @Override
            public void onResponse(Call<GetUserResponseRoot> call, Response<GetUserResponseRoot> response) {
                if (response.isSuccessful()) {
                    try {
                        currentUser = response.body().getUserFeedbackResponse().get(0);
                        binding.txtEntity.setText(currentUser.getEntityName());
                        binding.txtVendorName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
//                        binding.txtMobile.setText(user.getMobileNum());
                        Glide.with(getActivity())
                                .load(RestApi.BASE_URL + currentUser.getEntityImage())
                                .error(R.drawable.entity_profile)
                                .into(binding.ivEntity);
                        binding.ivEntity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String entityImageUrl = RestApi.BASE_URL + currentUser.getEntityImage();
                                Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                                intent.putExtra("url", entityImageUrl);
                                startActivity(intent);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserResponseRoot> call, Throwable t) {

            }
        });
    }

    private void loadEntityProductsDetails() {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        ProductNameAdapter listAdapter = new ProductNameAdapter(list, getActivity());
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getActivity());
        binding.recycleView.setLayoutManager(linearLayout);
        binding.recycleView.setAdapter(listAdapter);
        listAdapter.onItemClickListener(clickListener);

        Map<String, String> body = new HashMap<>();
        LoginResponse user = SettingsPreferences.getObject(getActivity(), SettingsPreferences.USER, LoginResponse.class);
        body.put("userId", user.getUserId());
//        body.put("userId", "7d415ca3-22f3-421b-9f4e-df261ea0a655");

        RestApi.getInstance().getService().getUserProducts(body).enqueue(new Callback<SearchRootResponse>() {
            @Override
            public void onResponse(Call<SearchRootResponse> call, Response<SearchRootResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    list.addAll(response.body().getData());
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchRootResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void addContactAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertdialog_entity_contact, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LinearLayout ll_contacts = alertLayout.findViewById(R.id.ll_contacts);
        View txt_edit = alertLayout.findViewById(R.id.txt_edit);
        txt_edit.setVisibility(View.VISIBLE);

        if (currentUser != null) {
//            String[] contacts = new String[]{
//                    currentUser.getMobileNum(),
//
//            };
//            if (currentUser.getAltNumber() instanceof String) {
//                contacts = new String[]{
//                        currentUser.getMobileNum(),
//                        currentUser.getAltNumber().toString()
//                };
//            }
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
        txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(getActivity(), EditContactsListActivity.class));
            }
        });
    }

    private void locationAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertdialog_entity_location, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(alertLayout);
        LinearLayout ll_contacts = alertLayout.findViewById(R.id.ll_contacts);
        if (currentUser != null) {

            List<String> list = new ArrayList<>();
            list.add(currentUser.getPrimaryLocation());
            if (!currentUser.getAvailableLocation().isEmpty()) {
                list.addAll(currentUser.getAvailableLocation());
            }

            for (int i = 1; i <= list.size(); i++) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_contacts, null);
                TextView tv_name = view.findViewById(R.id.tv_name);
                TextView txt_mobile = view.findViewById(R.id.txt_mobile);
                txt_mobile.setText(list.get(i - 1));
                tv_name.setText("");
                txt_mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                ll_contacts.addView(view);
            }
        }
        alert.setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            UserProdResponse item = list.get(position);

            DashboardActivity activity = (DashboardActivity) getActivity();
            Fragment fragment = new EntityDetailsFragment();
            Bundle args = new Bundle();
            args.putParcelable("data", item);
            fragment.setArguments(args);
            activity.replaceBackStackFragment(fragment);


        }
    };
}