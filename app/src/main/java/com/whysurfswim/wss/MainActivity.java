package com.whysurfswim.wss;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String ShowOrHideWebViewInitialUse = "show";
    private WebView webview;
    private ProgressBar spinner;
    private Toolbar toolbar;
    private boolean flag,doubleBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webview = (WebView) findViewById(R.id.webView);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        webview.setWebViewClient(new CustomWebViewClient());

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl("https://whysurfswim.com/");
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            doubleBack=false;
            webview.goBack();
        } else if(doubleBack){
            super.onBackPressed();
        } else {
            doubleBack=true;
            Toast.makeText(this, "Click BACK button again to exit", Toast.LENGTH_SHORT).show();
            webview.reload();
        }
    }


    // This allows for a splash screen
    // (and hide elements once the page loads)
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {

            if(url.startsWith("market://details")){
                webview.loadUrl("https://www.flipkart.com/?cmpid=fkrt_affiliate_network_ravichand32&referrer=mat_click_id%3Ddbad0e023ca71d5f3aa21bd92cea8081-20170710-189358");
            }

            if(url.equals("https://www.g2g.com/r/pbkn")){
                webview.loadUrl("https://www.g2g.com/clash-royale/top-up-gems-23420-23443");
            }

            if(url.equals("https://chat.whatsapp.com/invite/1ACYKZvKAcP7GJqhgG0NBk")){
                Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().contains("com.whatsapp")) {
                        shareIntent.setPackage(info.activityInfo.packageName);
                        flag = true;
                    }
                }
                if (flag) {
                    startActivity(shareIntent);
                    webview.reload();
                } else {
                    webview.loadUrl("https://play.google.com/store/apps/details?id=com.whatsapp&hl=en");
                }
            }

            if (url.startsWith("https://www.facebook")) {
                Intent shareIntent;
                if(url.equals("https://www.facebook.com/whysurfswim/")){
                    shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/410812639284252"));
                } else {
                    shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
                    shareIntent.setType("text/plain");
                }
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().contains("com.facebook.kata") || info.activityInfo.packageName.toLowerCase().contains("com.facebook.li")) {
                        shareIntent.setPackage(info.activityInfo.packageName);
                        flag = true;
                    }
                }
                if (flag) {
                    startActivity(shareIntent);
                    webview.reload();
                } else {
                    webview.loadUrl("https://play.google.com/store/apps/details?id=com.facebook.katana&hl=en");
                }
            }

            if (url.startsWith("whatsapp://")) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
                shareIntent.setType("text/plain");

                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().contains("com.whatsapp")) {
                        shareIntent.setPackage(info.activityInfo.packageName);
                        flag = true;
                    }
                }
                if (flag) {
                    startActivity(shareIntent);
                    webview.reload();
                } else {
                    webview.loadUrl("https://play.google.com/store/apps/details?id=com.whatsapp&hl=en");
                }
            }

            if (url.startsWith("https://twitter")) {
                Intent shareIntent;
                if(url.equals("https://twitter.com/@whysurfswim/")){
                    shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/whysurfswim"));
                } else {
                    shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
                    shareIntent.setType("text/plain");
                }
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().contains("com.twitter")) {
                        shareIntent.setPackage(info.activityInfo.packageName);
                        flag = true;
                    }
                }
                if (flag) {
                    startActivity(shareIntent);
                    webview.reload();
                } else {
                    webview.loadUrl("https://play.google.com/store/apps/details?id=com.twitter.android&hl=en");
                }
            }

            if (url.startsWith("https://platform")) {
                Intent shareIntent;
                if(webview.getUrl().equals("https://www.linkedin.com/company-beta/13269149/")){
                    shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company-beta/13269149/"));
                } else {
                    shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
                    shareIntent.setType("text/plain");
                }
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().contains("com.linkedin")) {
                        shareIntent.setPackage(info.activityInfo.packageName);
                        flag = true;
                    }
                }
                if (flag) {
                    startActivity(shareIntent);
                    webview.reload();
                } else {
                    webview.loadUrl("https://play.google.com/store/apps/details?id=com.linkedin.android&hl=en");
                }
            }

            if (url.startsWith("tg:")) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
                shareIntent.setType("text/plain");
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().contains("telegram")) {
                        shareIntent.setPackage(info.activityInfo.packageName);
                        flag = true;
                    }
                }
                if (flag) {
                    startActivity(shareIntent);
                    webview.reload();
                } else {
                    webview.loadUrl("https://telegram.me/share/url?url=" + url);
                    webview.stopLoading();
                }
            }

            // only make it invisible the FIRST time the app is run
            if (ShowOrHideWebViewInitialUse.equals("show")) {
                webview.setVisibility(webview.INVISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            ShowOrHideWebViewInitialUse = "hide";
            spinner.setVisibility(View.GONE);

            view.setVisibility(webview.VISIBLE);
            super.onPageFinished(view, url);

        }
    }
}
