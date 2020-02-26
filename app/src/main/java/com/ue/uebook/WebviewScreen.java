package com.ue.uebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;

public class WebviewScreen extends BaseActivity implements View.OnClickListener {
    private WebView mWebView;
    private ImageButton backbtn;
    private Intent intent;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_screen);
        mWebView = findViewById(R.id.mWebView);
        backbtn = findViewById(R.id.backbtn_webview);
        intent = getIntent();
        String url = intent.getStringExtra("url");
        backbtn.setOnClickListener(this);
        mWebView.setWebViewClient(new AppWebViewClients());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
    }
    @Override
    public void onClick(View view) {
        if (view==backbtn){
            finish();
        }
    }
    public class AppWebViewClients extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView view, String url) {
            hideLoadingIndicator();
            super.onPageFinished(view, url);

        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            showLoadingIndicator();
            super.onPageStarted(view, url, favicon);
        }
}
    @Override
    public void onPause() {
        mWebView.onPause();
        mWebView.pauseTimers();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        mWebView.resumeTimers();
        mWebView.onResume();
    }
    @Override
    protected void onDestroy() {
        mWebView.destroy();
        mWebView = null;
        super.onDestroy();
    }

}
