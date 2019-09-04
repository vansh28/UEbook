package com.ue.uebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class WebviewScreen extends AppCompatActivity implements View.OnClickListener {
    private WebView mWebView;
    private ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_screen);
        mWebView = findViewById(R.id.mWebView);
        backbtn = findViewById(R.id.backbtn_webview);
        backbtn.setOnClickListener(this);
        mWebView.setWebViewClient(new AppWebViewClients());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.loadUrl("https://www.slideshare.net/briannaglennlk/pdf-download-the-good-son-a-novel-ebook");
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

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }
}
