package com.macsoftech.ekart.activities;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.macsoftech.ekart.R;
import com.macsoftech.ekart.adapter.NotificationsAdapter;
import com.macsoftech.ekart.api.RestApi;
import com.macsoftech.ekart.helper.RecyclerViewEmptySupport;
import com.macsoftech.ekart.helper.SettingsPreferences;
import com.macsoftech.ekart.model.LoginResponse;
import com.macsoftech.ekart.model.NotificationsRoot;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends BaseActivity {

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.emptyView)
    View emptyView;

    @BindView(R.id.recyclerView)
    RecyclerViewEmptySupport recyclerView;
    private NotificationsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //==
        adapter = new NotificationsAdapter(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        loadNotifications();

    }

    void loadNotifications() {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> body = new HashMap<>();
        LoginResponse user = SettingsPreferences.getObject(this, SettingsPreferences.USER, LoginResponse.class);
        body.put("userId", user.getUserId());
        RestApi.getInstance().getService().getPushNotificationByUserId(body)
                .enqueue(new Callback<NotificationsRoot>() {
                    @Override
                    public void onResponse(Call<NotificationsRoot> call, Response<NotificationsRoot> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            adapter.addItems(response.body().getData());
                        }
                        checkEmpty();
                    }

                    @Override
                    public void onFailure(Call<NotificationsRoot> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        checkEmpty();
                    }
                });
    }

    public void checkEmpty() {
        if (adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }


}