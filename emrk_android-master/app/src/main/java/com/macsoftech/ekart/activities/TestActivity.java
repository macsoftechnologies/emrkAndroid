package com.macsoftech.ekart.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.macsoftech.ekart.R;
import com.macsoftech.ekart.fragments.SearchEntityDetailFragment;
import com.macsoftech.ekart.fragments.SearchEntityProductNameFragment;

public class TestActivity extends  BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragemt_search);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
       // fragmentTransaction.replace(R.id.fragment,new SearchEntityDetailFragment());
        fragmentTransaction.replace(R.id.fragment,new SearchEntityProductNameFragment());
        fragmentTransaction.commit();
    }
}
