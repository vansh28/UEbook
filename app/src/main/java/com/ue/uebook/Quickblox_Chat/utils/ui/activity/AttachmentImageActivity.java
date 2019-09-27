package com.ue.uebook.Quickblox_Chat.utils.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ue.uebook.Quickblox_Chat.utils.ResourceUtils;
import com.ue.uebook.R;


public class AttachmentImageActivity extends BaseActivity {

    private static final String EXTRA_URL = "url";
    private static final int PREFERRED_IMAGE_SIZE_FULL = ResourceUtils.dpToPx(320);

    private ImageView imageView;
    private ProgressBar progressBar;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, AttachmentImageActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        initUI();
        loadImage();
    }

    private void initUI() {
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);
        imageView = findViewById(R.id.image_full_view);
        progressBar = findViewById(R.id.progress_bar_show_image);
    }

    private void loadImage() {
        String url = getIntent().getStringExtra(EXTRA_URL);
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.ic_error_white);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
//        Glide.with(this)
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .listener(new RequestListenerImpl())
//                .error(R.drawable.ic_error_white)
//                .dontTransform()
//                .override(PREFERRED_IMAGE_SIZE_FULL, PREFERRED_IMAGE_SIZE_FULL)
//                .into(imageView);
    }

//    private class RequestListenerImpl implements RequestListener<String, GlideDrawable> {
//
//        @Override
//        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//            progressBar.setVisibility(View.GONE);
//            return false;
//        }
//
//        @Override
//        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//            progressBar.setVisibility(View.GONE);
//            return false;
//        }
//    }
}