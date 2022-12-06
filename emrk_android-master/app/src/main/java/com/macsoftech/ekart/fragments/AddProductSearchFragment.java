package com.macsoftech.ekart.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.activities.DashboardActivity;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.FragmentMyEntityBinding;
import com.macsoftech.ekart.model.search.ListOfVendorsData;
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
public class AddProductSearchFragment extends BaseFragment {

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;

    ImageView iv_search;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.ll_search)
    LinearLayout ll_search;

    @BindView(R.id.progressBar)
    View progressBar;
    @BindView(R.id.txt_product_creation)
    View txt_product_creation;


    private FragmentMyEntityBinding binding;

    List<ListOfVendorsData> list = new ArrayList<>();

    public AddProductSearchFragment() {
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
        return inflater.inflate(R.layout.fragment_addproduct_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // binding = FragmentMyEntityBinding.bind(view);
        txt_product_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashboardActivity activity = (DashboardActivity) getActivity();
                activity.getNavigation().setSelectedItemId(R.id.menu_help);
            }
        });

        iv_search = view.findViewById(R.id.iv_search);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                callSearchApi();
                String str = editable.toString().trim();
                if (str.length() > 0) {
                    chipGroup.setVisibility(View.VISIBLE);
                } else {
                    chipGroup.setVisibility(View.GONE);
                }
            }
        });
        //
//        SettingsPreferences.save
        chipGroup.setVisibility(View.GONE);
//        loadGroup();
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.setVisibility(chipGroup.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

    }

    private void callSearchApi() {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("productName", et_search.getText().toString().trim());
        RestApi.getInstance().getService().searchProducts(map)
                .enqueue(new Callback<SearchRootResponse>() {
                    @Override
                    public void onResponse(Call<SearchRootResponse> call, Response<SearchRootResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            loadGroup(response.body().getUserProdResponse());
                        }
                        if (chipGroup.getChildCount() == 0) {
                            ll_search.setVisibility(View.VISIBLE);
                        } else {
                            ll_search.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchRootResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void loadGroup(List<UserProdResponse> userProdResponse) {
        if (getActivity() == null) {
            return;
        }
        //
        chipGroup.clearCheck();
        chipGroup.removeAllViews();
        chipGroup.setSelectionRequired(false);
        for (int i = 0; i < userProdResponse.size(); i++) {
            Chip chip1 = (Chip) LayoutInflater.from(getActivity()).inflate(R.layout.tag_cloud, chipGroup, false);
            chip1.setText(userProdResponse.get(i).getProductName() + " - " + userProdResponse.get(i).getProductCode());
            chip1.setTag(userProdResponse.get(i));
            chip1.setId(i + 1);
            chipGroup.addView(chip1);
        }
        chipGroup.setOnCheckedChangeListener(null);
        chipGroup.setOnCheckedChangeListener((chipGroup, id) -> {
            @SuppressLint("ResourceType") Chip chip = ((Chip) chipGroup.getChildAt(chipGroup.getCheckedChipId() - 1));
            if (chip != null) {
                // showPopUp(id, String.valueOf(chip.getTag()));
                chip.setChecked(false);
                chipAlertDialog(id, (UserProdResponse) chip.getTag());
            }
        });
    }


    /**
     * length dialog
     *
     * @param id
     * @param item
     */
    private void chipAlertDialog(int id, UserProdResponse item) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertdialog_chip, null);
        ImageView iv_product = alertLayout.findViewById(R.id.iv_product);
        TextView txt_title = alertLayout.findViewById(R.id.txt_title);
        txt_title.setText(item.getProductName());
        if (!item.getProductImage().isEmpty()) {
            Glide.with(getActivity())
                    .load(RestApi.BASE_URL + item.getProductImage().get(0))
                    .into(iv_product);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(alertLayout);
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
//                displayDetails(getActivity(), item);
                // loadDetails(item);
                DashboardActivity activity = (DashboardActivity) getActivity();
                AddProdictDetailFragment fragment = new AddProdictDetailFragment();
                Bundle args = new Bundle();
                args.putParcelable("data", item);
                fragment.setArguments(args);
                activity.replaceBackStackFragment(fragment);
                chipGroup.setVisibility(View.GONE);
            }
        });

        alertLayout.findViewById(R.id.ivdelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}