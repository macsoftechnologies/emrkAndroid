package com.macsoftech.ekart.fragments;

import static com.macsoftech.ekart.activities.DashboardActivity.mydrawer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.activities.BaseActivity;
import com.macsoftech.ekart.activities.DashboardActivity;
import com.macsoftech.ekart.activities.NotificationsActivity;
import com.macsoftech.ekart.adapter.ComapnyNameAdapter;
import com.macsoftech.ekart.adapter.ComapnyNameAdapter2;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.databinding.FragmentHomeSearchBinding;
import com.macsoftech.ekart.model.LocationData;
import com.macsoftech.ekart.model.LocationResponseRoot;
import com.macsoftech.ekart.model.ProductDetailsRoot;
import com.macsoftech.ekart.model.Vendors;
import com.macsoftech.ekart.model.search.ListOfVendorsData;
import com.macsoftech.ekart.model.search.ListOfVendorsData2;
import com.macsoftech.ekart.model.search.ListOfVendorsResponse;
import com.macsoftech.ekart.model.search.ListOfVendorsResponse2;
import com.macsoftech.ekart.model.search.SearchRootResponse;
import com.macsoftech.ekart.model.search.UserProdResponse;
import com.macsoftech.ekart.model.sizes.LengthModel;
import com.macsoftech.ekart.model.sizes.SizeModel;
import com.macsoftech.ekart.model.sizes.SizeModelRootResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeSearchFragment extends BaseFragment {
    Context context;
    @BindView(R.id.chip_group)
    ChipGroup chipGroup;

    ImageView iv_search;
    EditText et_qty;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.lengthlayout)
    LinearLayout lengthlayout;

    @BindView(R.id.locationlayout)
    LinearLayout locationlayout;

    @BindView(R.id.emptyProductsView)
    View emptyProductsView;

    @BindView(R.id.emptyVendorsView)
    View emptyVendorsView;

    @BindView(R.id.txt_location)
    TextView txt_location;

    @BindView(R.id.txt_length)
    TextView txt_length;

    Boolean vendorslist = false;
    ImageView menu_img;
    List<ListOfVendorsData> list = new ArrayList<>();
    List<Vendors> list2 = new ArrayList<>();
    private FragmentHomeSearchBinding binding;

    public HomeSearchFragment() {
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
        return inflater.inflate(R.layout.fragment_home_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        ButterKnife.bind(this, view);
        binding = FragmentHomeSearchBinding.bind(view);
        et_qty = view.findViewById(R.id.et_qty);
        et_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadgetVendorProductBySize(et_qty.getText().toString(),"", "quantity");
            }
        });
         iv_search = view.findViewById(R.id.iv_search);
        menu_img = view.findViewById(R.id.menu_img);
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydrawer.openDrawer(Gravity.LEFT);
            }
        });
        //sizelayout = view.findViewById(R.id.sizelayout);
        // recyclerView = view.findViewById(R.id.recyclerView);
        if (selectedProduct != null) {
            loadDetails(selectedProduct);
        }
        binding.sizelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSizePopup();
            }
        });


        lengthlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lengthAlertDialog();
//                showLength();
            }
        });

        locationlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationAlertDialog();
            }
        });

        //
//        SettingsPreferences.save
        chipGroup.setVisibility(View.GONE);
        binding.ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificationsActivity.class));
            }
        });
        binding.txtProductCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashboardActivity activity = (DashboardActivity) getActivity();
                activity.getNavigation().setSelectedItemId(R.id.menu_help);
            }
        });
        loadLength();

    }


    SizeModelRootResponse lengthResponse;

    void loadLength() {
        RestApi.getInstance().getService().getProductLength()
                .enqueue(new Callback<SizeModelRootResponse>() {
                    @Override
                    public void onResponse(Call<SizeModelRootResponse> call, Response<SizeModelRootResponse> response) {
                        if (response.isSuccessful()) {
                            lengthResponse = response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<SizeModelRootResponse> call, Throwable t) {

                    }
                });
    }

    private final TextWatcher qtyListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            findByQty(editable.toString().trim());
        }
    };
    private final TextWatcher searchListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String str = editable.toString().trim();
            if (str.length() > 0) {
                callSearchApi();
                chipGroup.setVisibility(View.VISIBLE);
            } else {
                loadGroup(new ArrayList<>());
                chipGroup.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        binding.etQty.removeTextChangedListener(qtyListener);
        et_search.removeTextChangedListener(searchListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.etQty.addTextChangedListener(qtyListener);
        et_search.addTextChangedListener(searchListener);
    }


    String mSize;
    String mMinLength;
    String mMaxLength;
    String mQty;
    String mLocation;

    //TODO: Need to fix this.
    void filterByAll(String size, String minLength, String maxLength, String qty, String location) {
        List<ListOfVendorsData> temp = new ArrayList<>();
        if (!TextUtils.isEmpty(qty)) {
            //
            // value = 20
            // 20*1.30 = 26
            //
            double val1 = Double.parseDouble(qty) * 1.30;
            List<ListOfVendorsData> topMost = new ArrayList<>();
            List<ListOfVendorsData> bottomList = new ArrayList<>();
            for (ListOfVendorsData item : originalList) {
                double val2 = Double.parseDouble(item.getQuantity());
                if (val1 <= val2) {
                    topMost.add(item);
                } else {
                    bottomList.add(item);
                }
            }

            Collections.sort(topMost, (listOfVendorsData, listOfVendorsData2) -> {
                try {
                    double val11 = Double.parseDouble(listOfVendorsData.getQuantity());
                    double val2 = Double.parseDouble(listOfVendorsData2.getQuantity());
                    return (int) (val2 - val11);
                } catch (Exception e) {
                    return 0;
                }
            });
            Collections.sort(bottomList, (listOfVendorsData, listOfVendorsData2) -> {
                try {
                    double val112 = Double.parseDouble(listOfVendorsData.getQuantity());
                    double val2 = Double.parseDouble(listOfVendorsData2.getQuantity());
                    return (int) (val2 - val112);
                } catch (Exception e) {
                    return 0;
                }
            });
            temp.addAll(topMost);
            temp.addAll(bottomList);
        } else {
            temp.addAll(originalList);
        }

        for (Iterator<ListOfVendorsData> it = temp.iterator(); it.hasNext(); ) {
            ListOfVendorsData item = it.next();
            //Dont remove if no filter is applied.
            if (TextUtils.isEmpty(size)
                    && TextUtils.isEmpty(minLength)
                    && TextUtils.isEmpty(maxLength)
                    && TextUtils.isEmpty(location)) {
                continue;
            }
            //1. Check Length Condition
            try {
                double minValue = Double.parseDouble(minLength);
                double maxValue = Double.parseDouble(maxLength);
                double length = Double.parseDouble(item.getLength());
                if (maxValue == minValue && minValue == 0.0) {

                } else if (maxValue == 0.0 && minValue != 0.0) {
                    if (length < minValue) {
                        //5.4>2.5, 2.5<5.4
                        it.remove();
                        continue;
                    }
                } else if (!(length <= maxValue) || !(length >= minValue)) {
                    it.remove();
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //2. Check Size Condition
            if (!TextUtils.isEmpty(size) && item.getSize() != null && !item.getSize().equals(size)) {
                it.remove();
                continue;
            }
            //3. Check Location Condition
            if (!TextUtils.isEmpty(location) && item.getLocation() != null && !item.getLocation().equals(location)) {
                it.remove();
            }

        }
        if (listAdapter != null) {
            list.clear();
            list.addAll(temp);
            listAdapter.notifyDataSetChanged();
            if (list.isEmpty()) {
                emptyVendorsView.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(emptyProductsView, list.size() + " Records Found.", Snackbar.LENGTH_SHORT).show();
                emptyVendorsView.setVisibility(View.GONE);
            }
        }
    }


    private void filterBySizes(String size) {
        mSize = size;
        filterByAll(mSize, mMinLength, mMaxLength, mQty, mLocation);
    }

    private void filterProductsByLength(String max, String min) {
        mMaxLength = max;
        mMinLength = min;
        txt_length.setText(min + " - " + max);
        filterByAll(mSize, mMinLength, mMaxLength, mQty, mLocation);
    }

    void findByLocation(String state, String district, String mandal, String village) {
        mLocation = village;
        filterByAll(mSize, mMinLength, mMaxLength, mQty, mLocation);
    }


    private void findByQty(String value) {
        mQty = value;
        filterByAll(mSize, mMinLength, mMaxLength, mQty, mLocation);
    }

    @Override
    public void showProgress(String title) {
//        super.showProgress(title);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDialog() {
        super.hideDialog();
        binding.progressBar.setVisibility(View.GONE);
    }

    private static List<SizeModel> sizesList = new ArrayList<>();

    private void showSizePopup() {
        if (sizesList != null && !sizesList.isEmpty()) {
            showSizesPopup(sizesList);
            return;
        }
        RestApi.getInstance().getService().getProductSizes()
                .enqueue(new Callback<SizeModelRootResponse>() {
                    @Override
                    public void onResponse(Call<SizeModelRootResponse> call, Response<SizeModelRootResponse> response) {
                        if (response.isSuccessful()) {
                            sizesList = response.body().getData().getSizesList();
                            showSizesPopup(sizesList);
                        }
                    }

                    @Override
                    public void onFailure(Call<SizeModelRootResponse> call, Throwable t) {

                    }
                });
    }

    public void showSizesPopup(List<SizeModel> sizesList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ArrayAdapter<SizeModel> adapter = new ArrayAdapter<SizeModel>(getActivity(), android.R.layout.simple_list_item_1,
                sizesList);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                binding.txtSize.setText(sizesList.get(position).toString());
                loadgetVendorProductBySize(sizesList.get(position).toString(),"", "size");
                // filterBySizes(sizesList.get(position).toString());
            }
        });
        builder.setTitle("Select Size");
        builder.setPositiveButton("Cancel", null);
        builder.create().show();
    }


    private void callSearchApi() {
        showProgress();
        Map<String, String> map = new HashMap<>();
        map.put("productName", et_search.getText().toString().trim());
        RestApi.getInstance().getService().searchProducts(map)
                .enqueue(new Callback<SearchRootResponse>() {
                    @Override
                    public void onResponse(Call<SearchRootResponse> call, Response<SearchRootResponse> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getUserProdResponse().size() > 0) {
                                vendorslist = true;
                            } else {
                                vendorslist = false;
                            }
                            loadGroup(response.body().getUserProdResponse());
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchRootResponse> call, Throwable t) {
                        hideDialog();
                    }
                });
    }

    static List<ListOfVendorsData> originalList = new ArrayList<>();
    static List<Vendors> originalList2 = new ArrayList<>();

    public void loadGroup(List<UserProdResponse> userProdResponse) {
        originalList.clear();
        if (listAdapter != null) {
            listAdapter.clear();
            listAdapter.notifyDataSetChanged();
        }
        chipGroup.setVisibility(View.VISIBLE);
        chipGroup.clearCheck();
        chipGroup.removeAllViews();
        chipGroup.setSelectionRequired(false);
//        System.out.println("Dadi this is th calass " + recyclerView.getAdapter().getClass().getName());
        if (recyclerView.getAdapter() != null){
            if (recyclerView.getAdapter().getClass().getName().equals("com.macsoftech.ekart.adapter.ComapnyNameAdapter2")) {
                ComapnyNameAdapter2 adapter = (ComapnyNameAdapter2) recyclerView.getAdapter();
                if (adapter != null) {
                    adapter.clear();
                }
            } else {
                ComapnyNameAdapter adapter = (ComapnyNameAdapter) recyclerView.getAdapter();
                if (adapter != null) {
                    adapter.clear();
                }
            }
        }

        if (getActivity() == null) {
            return;
        }
        if (userProdResponse.isEmpty()) {
            emptyProductsView.setVisibility(View.VISIBLE);
        } else {
            emptyProductsView.setVisibility(View.GONE);
        }
        for (int i = 0; i < userProdResponse.size(); i++) {
            Chip chip1 = (Chip) LayoutInflater.from(getActivity()).inflate(R.layout.tag_cloud, chipGroup, false);
            String productCode = userProdResponse.get(i).getProductCode();
            if (productCode == null) {
                productCode = "";
            }
            chip1.setText(userProdResponse.get(i).getProductName() + " - " + productCode);
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


    ComapnyNameAdapter listAdapter;
    ComapnyNameAdapter2 listAdapter2;
    private String selectedProduct;

    private void loadDetails(String productId) {
        //
        selectedProduct = productId;
        showProgress();
        list = new ArrayList<>();
        listAdapter = new ComapnyNameAdapter(list, getActivity());
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(listAdapter);
        listAdapter.onItemClickListener(clickListener);

        Map<String, String> body = new HashMap<>();
        body.put("productId", productId);
        RestApi.getInstance().getService()
                .getVendorProduct(productId)
                .enqueue(new Callback<ListOfVendorsResponse>() {
                    @Override
                    public void onResponse(Call<ListOfVendorsResponse> call, Response<ListOfVendorsResponse> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            System.out.println("Dadi getProductAndVendorById " + response.body().getData().toString());
                            originalList = new ArrayList<>(response.body().getData());
                            list.addAll(response.body().getData());
                            listAdapter.notifyDataSetChanged();

                            //  System.out.println("Original List "+originalList.get(0).getMobileNum());
                        }
                        if (list.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            // emptyVendorsView.setVisibility(View.GONE);
                        } else {
                            // emptyVendorsView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ListOfVendorsResponse> call, Throwable t) {
                        hideDialog();
                        System.out.println("Dadi getProductAndVendorById " + t.getMessage().toString());
                    }
                });
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            if (recyclerView.getAdapter().getClass().getName().equals("com.macsoftech.ekart.adapter.ComapnyNameAdapter2")) {
                Vendors item = list2.get(position);
                Fragment fragment = new SearchEntityDetailFragment();
                Bundle args = new Bundle();
                args.putString("type", "2");
                args.putParcelable("data", item);
                fragment.setArguments(args);
                DashboardActivity activity = (DashboardActivity) getActivity();
                activity.replaceBackStackFragment(fragment);
            } else {
                ListOfVendorsData item = list.get(position);
                Fragment fragment = new SearchEntityDetailFragment();
                Bundle args = new Bundle();
                args.putString("type", "1");
                args.putParcelable("data", item);
                fragment.setArguments(args);
                DashboardActivity activity = (DashboardActivity) getActivity();
                activity.replaceBackStackFragment(fragment);
            }
        }
    };


    private void loadgetVendorProductBySize(String size,String size1, String type) {
        list2 = new ArrayList<>();
        listAdapter2 = new ComapnyNameAdapter2(list2, getActivity());
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(listAdapter2);
        listAdapter2.onItemClickListener(clickListener);
        String methodname = "";
        System.out.println("Type selections "+type);
        Map<String, String> body = new HashMap<>();
        if (type.equals("size")) {
            body.put("size", size);
            RestApi.getInstance().getService()
                    .getVendorProductBySize(body)
                    .enqueue(new Callback<ListOfVendorsResponse2>() {
                        @Override
                        public void onResponse(Call<ListOfVendorsResponse2> call, Response<ListOfVendorsResponse2> response) {
                            hideDialog();
                            System.out.println("Dadi getProductAndVendorById " + response.body().toString());
                            if (response.isSuccessful()) {
                                vendorslist = true;
                                originalList2 = new ArrayList<>(response.body().getData());
                                list2.addAll(response.body().getData());
                                listAdapter2.notifyDataSetChanged();

                                //  System.out.println("Original List "+originalList.get(0).getMobileNum());
                                // System.out.println("Original List " + list.get(0).getSize());
                            }
                            if (list.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                // emptyVendorsView.setVisibility(View.GONE);
                            } else {
                                // emptyVendorsView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ListOfVendorsResponse2> call, Throwable t) {
                            hideDialog();
                            vendorslist = false;
                            System.out.println("Dadi getProductAndVendorById " + t.getMessage().toString());
                        }
                    });

        } else if (type.equals("location")) {
            body.put("location", size);
            RestApi.getInstance().getService()
                    .getVendorProductByLocation(body)
                    .enqueue(new Callback<ListOfVendorsResponse2>() {
                        @Override
                        public void onResponse(Call<ListOfVendorsResponse2> call, Response<ListOfVendorsResponse2> response) {
                            hideDialog();
                            System.out.println("Dadi getProductAndVendorById " + response.body().toString());
                            if (response.isSuccessful()) {
                                vendorslist = true;
                                originalList2 = new ArrayList<>(response.body().getData());
                                list2.addAll(response.body().getData());
                                listAdapter2.notifyDataSetChanged();

                                //  System.out.println("Original List "+originalList.get(0).getMobileNum());
                                // System.out.println("Original List " + list.get(0).getSize());
                            }
                            if (list.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                // emptyVendorsView.setVisibility(View.GONE);
                            } else {
                                // emptyVendorsView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ListOfVendorsResponse2> call, Throwable t) {
                            hideDialog();
                            vendorslist = false;
                            System.out.println("Dadi getProductAndVendorById " + t.getMessage().toString());
                        }
                    });
        } else if (type.equals("quantity")) {
            body.put("quantity", size);
            RestApi.getInstance().getService()
                    .getVendorProductByQuantity(body)
                    .enqueue(new Callback<ListOfVendorsResponse2>() {
                        @Override
                        public void onResponse(Call<ListOfVendorsResponse2> call, Response<ListOfVendorsResponse2> response) {
                            hideDialog();
                            System.out.println("Dadi getProductAndVendorById " + response.body().toString());
                            if (response.isSuccessful()) {
                                vendorslist = true;
                                originalList2 = new ArrayList<>(response.body().getData());
                                list2.addAll(response.body().getData());
                                listAdapter2.notifyDataSetChanged();

                                //  System.out.println("Original List "+originalList.get(0).getMobileNum());
                                // System.out.println("Original List " + list.get(0).getSize());
                            }
                            if (list.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                // emptyVendorsView.setVisibility(View.GONE);
                            } else {
                                // emptyVendorsView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ListOfVendorsResponse2> call, Throwable t) {
                            hideDialog();
                            vendorslist = false;
                            System.out.println("Dadi getProductAndVendorById " + t.getMessage().toString());
                        }
                    });
        }else {

            RestApi.getInstance().getService()
                    .getVendorProductByLength(size,size1)
                    .enqueue(new Callback<ListOfVendorsResponse2>() {
                        @Override
                        public void onResponse(Call<ListOfVendorsResponse2> call, Response<ListOfVendorsResponse2> response) {
                            hideDialog();
//                            System.out.println("Dadi getProductAndVendorById " + response.body().toString());
                            if (response.isSuccessful()) {
                                vendorslist = true;
                                originalList2 = new ArrayList<>(response.body().getData());
                                list2.addAll(response.body().getData());
                                listAdapter2.notifyDataSetChanged();

                                //  System.out.println("Original List "+originalList.get(0).getMobileNum());
                                // System.out.println("Original List " + list.get(0).getSize());
                            }
                            if (list.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                // emptyVendorsView.setVisibility(View.GONE);
                            } else {
                                // emptyVendorsView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ListOfVendorsResponse2> call, Throwable t) {
                            hideDialog();
                            vendorslist = false;
                            System.out.println("Dadi getProductAndVendorById " + t.getMessage().toString());
                        }
                    });
        }
    }

    /**
     * length dialog
     */
    private void lengthAlertDialog() {
        if (lengthResponse == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lengthAlertDialog();
                }
            }, 500);
            return;
        }
        LayoutInflater inflater = getLayoutInflater();
        View alertView = inflater.inflate(R.layout.alertdialog_size, null);
        //
        EditText txt_max_num = alertView.findViewById(R.id.txt_max_num);
        EditText txt_max_dec = alertView.findViewById(R.id.txt_max_dec);
        EditText txt_min_num = alertView.findViewById(R.id.txt_min_num);
        EditText txt_min_dec = alertView.findViewById(R.id.txt_min_dec);
        //lengthResponse
        Spinner sp_min = alertView.findViewById(R.id.sp_min);
        Spinner sp_max = alertView.findViewById(R.id.sp_max);
        Spinner sp_min_dec = alertView.findViewById(R.id.sp_min_dec);
        Spinner sp_max_dec = alertView.findViewById(R.id.sp_max_dec);
        //lengthResponse

        ArrayAdapter<LengthModel> adapter = new ArrayAdapter<LengthModel>(getActivity(),
                android.R.layout.simple_list_item_1,
                lengthResponse.getData().getLengthList());

        List<String> decList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            decList.add(String.valueOf(i));
        }
        ArrayAdapter<String> decAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                decList);

        sp_min.setAdapter(adapter);
        sp_max.setAdapter(adapter);
        sp_min_dec.setAdapter(decAdapter);
        sp_max_dec.setAdapter(decAdapter);

        //
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(alertView);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();

        alertView.findViewById(R.id.linearcancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        alertView.findViewById(R.id.linearok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                BaseActivity.hideKeyboard(getActivity());
//                String max_num = txt_max_num.getText().toString().trim();
//                String max_dec = txt_max_dec.getText().toString().trim();
//                String min_num = txt_min_num.getText().toString().trim();
//                String min_dec = txt_min_dec.getText().toString().trim();

                String max_num = sp_max.getSelectedItem().toString();
                String max_dec = sp_max_dec.getSelectedItem().toString();
                String min_num = sp_min.getSelectedItem().toString();
                String min_dec = sp_min_dec.getSelectedItem().toString();
                if (TextUtils.isEmpty(max_num)) {
                    max_num = "0";
                }
                if (TextUtils.isEmpty(max_dec)) {
                    max_dec = "0";
                }
                if (TextUtils.isEmpty(min_num)) {
                    min_num = "0";
                }
                if (TextUtils.isEmpty(min_dec)) {
                    min_dec = "0";
                }

               // filterProductsByLength(max_num + "." + max_dec, min_num + "." + min_dec);
                loadgetVendorProductBySize(min_num,max_num, "lenght");
            }
        });

        alertView.findViewById(R.id.ivdelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    List<LocationData> mLocationData = new ArrayList<>();

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
                String state = spState.getSelectedItem() == null ? "" : spState.getSelectedItem().toString();
                String district = spDistrict.getSelectedItem().toString().equalsIgnoreCase("--Select--")
                        ? "" : spDistrict.getSelectedItem().toString();
                String mandal = sp_mandal.getSelectedItem().toString().equalsIgnoreCase("--Select--") ? ""
                        : sp_mandal.getSelectedItem().toString();
                String village = sp_village.getSelectedItem().toString().equalsIgnoreCase("--Select--") ? ""
                        : sp_village.getSelectedItem().toString();
                txt_location.setText(village);
                loadgetVendorProductBySize(village,"", "location");
                //findByLocation(state, district, mandal, village);

            }
        });
        alertLayout.findViewById(R.id.ivdelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
        BaseActivity.hideKeyboard(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertdialog_chip, null);
        ImageView iv_product = alertLayout.findViewById(R.id.iv_product);
        TextView txt_title = alertLayout.findViewById(R.id.txt_title);
        txt_title.setText(item.getProductName());
        if (!item.getProductImage().isEmpty()) {
            String image = item.getProductImage().get(0);
            if (image.contains(",")) {
                image = image.split(",")[0];
            }
            Glide.with(getActivity())
                    .load(RestApi.BASE_URL + image)
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
                loadDetails(item.getProductId());
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