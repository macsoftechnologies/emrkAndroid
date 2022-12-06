package com.macsoftech.ekart.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.macsoftech.ekart.R;

public class ImagePreviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ImageView iv_profile = findViewById(R.id.iv_profile);
        Glide.with(ImagePreviewActivity.this)
                .load(getIntent().getStringExtra("url"))
                .error(R.drawable.entity_profile)
                .into(iv_profile);

    }
}