package com.macsoftech.ekart.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.macsoftech.ekart.BuildConfig;

public class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.e(getClass().getSimpleName(), "onCreate: called");
        }
    }

    public void showProgress() {
        showProgress("Please wait..");
    }

    public void showProgress(String title) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage(title);
        progressDialog.setCanceledOnTouchOutside(false);
        if (!getActivity().isFinishing()) {
            progressDialog.show();
        } else {
            progressDialog = null;
        }
    }

    public void hideDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
