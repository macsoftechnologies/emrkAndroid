package com.macsoftech.ekart.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.macsoftech.ekart.BuildConfig;
import com.macsoftech.ekart.R;
import com.macsoftech.ekart.fragments.HelpFragment;
import com.macsoftech.ekart.fragments.HomeSearchFragment;
import com.macsoftech.ekart.fragments.MyEntityFragment;
import com.macsoftech.ekart.fragments.ProfileFragment;
import com.macsoftech.ekart.helper.SettingsPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardActivity extends BaseActivity {
    public static DrawerLayout mydrawer;
    @Nullable
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    public static boolean isTrailStarted;
    private String TAG = "DashboardActivity";

    CardView editprofile, products, help, request, notifications, logout;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        isTrailStarted = false;
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.menu_search) {
                    fragment = new HomeSearchFragment();
                } else if (item.getItemId() == R.id.menu_entity) {
//                    if (!isTrailStarted) {
//                        fragment = new MyEntityTrailFragment();
//                    } else {
//                        fragment = new MyEntityFragment();
//                    }
                    fragment = new MyEntityFragment();
                } else if (item.getItemId() == R.id.menu_help) {
                    fragment = new HelpFragment();
                } else if (item.getItemId() == R.id.menu_profile) {
                    fragment = new ProfileFragment();
                }
                replaceFragment(fragment);
                return true;
            }
        });
        mydrawer = findViewById(R.id.mydrawer);
        editprofile = findViewById(R.id.editprofile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editclicked = getSharedPreferences("editclicked",MODE_PRIVATE).edit();
                editclicked.putString("editclicked","yes");
                editclicked.commit();
                 mydrawer.closeDrawer(Gravity.LEFT);
                Fragment fragment = new ProfileFragment();
                replaceFragment(fragment);
            }
        });
        products = findViewById(R.id.products);
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydrawer.closeDrawer(Gravity.LEFT);
                 Fragment fragment = new HomeSearchFragment();
                replaceFragment(fragment);
            }
        });
        help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydrawer.closeDrawer(Gravity.LEFT);
                HelpFragment.menuval = "help";
                Fragment fragment = new HelpFragment();
                replaceFragment(fragment);
            }
        });
        request = findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydrawer.closeDrawer(Gravity.LEFT);
                HelpFragment.menuval = "request";
                Fragment  fragment = new HelpFragment();
                replaceFragment(fragment);
            }
        });
        notifications = findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, NotificationsActivity.class));
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
            }
        });
        //request,notifications,logout
        replaceFragment(new HomeSearchFragment());
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, token);
                        }

                        SettingsPreferences.saveString(DashboardActivity.this, SettingsPreferences.GCM_TOKEN,
                                token);
                        SplashScreenActivity.saveGCM(getApplicationContext());
//                        Toast.makeText(DashboardActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public BottomNavigationView getNavigation() {
        return navigation;
    }

    public void replaceFragment(Fragment fragment) {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < count - 1; i++) {
            getSupportFragmentManager().popBackStack();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss();
    }

    public void replaceBackStackFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            showpopup("Would you like to close the eKart?");
            //Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    AlertDialog show;
    public void showpopup(String msg) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = ((Activity) this).getLayoutInflater();
        View alertView = inflater.inflate(R.layout.custom_alert, null);
        alertDialog.setView(alertView);
        show = alertDialog.show();
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textalert = alertView.findViewById(R.id.textalert);
        textalert.setText(msg);
        TextView okay = alertView.findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView close = alertView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });
    }
}