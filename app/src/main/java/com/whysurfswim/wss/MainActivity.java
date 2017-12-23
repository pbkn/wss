package com.whysurfswim.wss;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

@SuppressLint("setJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar spinner;
    private FloatingActionButton fabInfo;
    private LinearLayoutCompat layoutAbout, layoutShare, layoutLinkedin, layoutTwitter, layoutWhatsapp, layoutFacebook;
    private boolean fabExpanded = true;
    private long doubleBack = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        AdView adView = this.findViewById(R.id.adView);
        FloatingActionButton fabAbout = this.findViewById(R.id.fabAbout);
        FloatingActionButton fabShare = this.findViewById(R.id.fabShare);
        FloatingActionButton fabLinkedin = this.findViewById(R.id.fabLinkedin);
        FloatingActionButton fabTwitter = this.findViewById(R.id.fabTwitter);
        FloatingActionButton fabWhatsapp = this.findViewById(R.id.fabWhatsapp);
        FloatingActionButton fabFacebook = this.findViewById(R.id.fabFacebook);
        spinner = this.findViewById(R.id.progressBar);
        webView = this.findViewById(R.id.webView);
        fabInfo = this.findViewById(R.id.fabInfo);
        layoutAbout = this.findViewById(R.id.layoutFabAbout);
        layoutShare = this.findViewById(R.id.layoutFabShare);
        layoutLinkedin = this.findViewById(R.id.layoutFabLinkedin);
        layoutTwitter = this.findViewById(R.id.layoutFabTwitter);
        layoutWhatsapp = this.findViewById(R.id.layoutFabWhatsapp);
        layoutFacebook = this.findViewById(R.id.layoutFabFacebook);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fabInfo.setVisibility(View.INVISIBLE);
        closeSubMenusFab();
        webView.setWebViewClient(new CustomWebViewClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        if (isOnline()) {
            webView.loadUrl("https://whysurfswim.com/");
            MobileAds.initialize(this, "ca-app-pub-6059528612565667/8468844477");
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } else {
            finish();
        }

        fabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        fabAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://play.google.com/store/apps/details?id=com.whysurfswim.wss&hl=en");
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });

        fabWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://chat.whatsapp.com/invite/1ACYKZvKAcP7GJqhgG0NBk");
            }
        });

        fabLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://www.linkedin.com/company-beta/13269149/");
            }
        });

        fabTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/follow?user_id=839208853103312896"));
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (checkPackage(shareIntent, "com.twitter.android")) {
                    startActivity(shareIntent);
                    webView.reload();
                } else {
                    webView.loadUrl("https://play.google.com/store/apps/details?id=com.twitter.android&hl=en");
                }
            }
        });

        fabFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://www.facebook.com/whysurfswim/");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            doubleBack = 0;
            webView.goBack();
        } else if (doubleBack + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            doubleBack = System.currentTimeMillis();
            Toast.makeText(this, "Click BACK button again to exit", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * closeSubMenusFab method used to close all the opened fabs
     */
    private void closeSubMenusFab() {
        layoutLinkedin.setVisibility(View.INVISIBLE);
        layoutWhatsapp.setVisibility(View.INVISIBLE);
        layoutTwitter.setVisibility(View.INVISIBLE);
        layoutShare.setVisibility(View.INVISIBLE);
        layoutAbout.setVisibility(View.INVISIBLE);
        layoutFacebook.setVisibility(View.INVISIBLE);
        fabInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
        fabExpanded = false;
    }

    /**
     * openSubMenusFab method used to open all the closed fabs
     */
    private void openSubMenusFab() {
        layoutLinkedin.setVisibility(View.VISIBLE);
        layoutWhatsapp.setVisibility(View.VISIBLE);
        layoutTwitter.setVisibility(View.VISIBLE);
        layoutAbout.setVisibility(View.VISIBLE);
        layoutShare.setVisibility(View.VISIBLE);
        layoutFacebook.setVisibility(View.VISIBLE);
        fabInfo.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    /**
     * isOnline method is used to check the status of internet connection
     *
     * @return boolean
     */
    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        if (conMgr != null) {
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                Toast.makeText(this, "No Internet connection !", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    /**
     * checkPackage method is used to check for installed apps in phone
     *
     * @param intent     {intent variant to load package}
     * @param appPackage {app folder path}
     * @return boolean
     */
    private boolean checkPackage(Intent intent, String appPackage) {
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().contains(appPackage)) {
                intent.setPackage(info.activityInfo.packageName);
                return true;
            }
        }
        return false;
    }

    /**
     * CustomeWebViewClient used to load our main screen using webview
     */
    private class CustomWebViewClient extends WebViewClient {

        @Override
        @SuppressLint("setJavaScriptEnabled")
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            if (!isOnline()) {
                finish();
            } else {
                spinner.setVisibility(View.VISIBLE);
            }

            if (spinner.getVisibility() == View.VISIBLE) {
                webView.setVisibility(View.INVISIBLE);
            }

            if (url.startsWith("https://play.google.com")) {
                Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (checkPackage(shareIntent, "com.android.vending")) {
                    startActivity(shareIntent);
                    webView.reload();
                } else {
                    webView.loadUrl(url);
                }
            }

            if (url.startsWith("market://details")) {
                webView.loadUrl("https://www.flipkart.com/?cmpid=fkrt_affiliate_network_ravichand32&referrer=mat_click_id%3Ddbad0e023ca71d5f3aa21bd92cea8081-20170710-189358");
            }

            if (url.equals("https://www.g2g.com/r/pbkn")) {
                webView.loadUrl("https://www.g2g.com/clash-royale/top-up-gems-23420-23443");
            }

            if (url.equals("https://chat.whatsapp.com/invite/1ACYKZvKAcP7GJqhgG0NBk")) {
                Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (checkPackage(shareIntent, "com.whatsapp")) {
                    startActivity(shareIntent);
                    webView.reload();
                } else {
                    webView.loadUrl("https://play.google.com/store/apps/details?id=com.whatsapp&hl=en");
                }
            }

            if (url.startsWith("https://www.facebook")) {
                Intent shareIntent;
                if (url.equals("https://www.facebook.com/whysurfswim/")) {
                    shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/410812639284252"));
                } else {
                    shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                    shareIntent.setType("text/plain");
                }
                if (checkPackage(shareIntent, "com.facebook.kata") || checkPackage(shareIntent, "com.facebook.li")) {
                    startActivity(shareIntent);
                    webView.reload();
                } else {
                    webView.loadUrl("https://play.google.com/store/apps/details?id=com.facebook.katana&hl=en");
                }
            }

            if (url.startsWith("whatsapp://")) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                shareIntent.setType("text/plain");
                if (checkPackage(shareIntent, "com.whatsapp")) {
                    startActivity(shareIntent);
                    webView.reload();
                } else {
                    webView.loadUrl("https://play.google.com/store/apps/details?id=com.whatsapp&hl=en");
                }
            }

            if (url.startsWith("https://twitter")) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                shareIntent.setType("text/plain");
                if (checkPackage(shareIntent, "com.twitter")) {
                    startActivity(shareIntent);
                    webView.reload();
                } else {
                    webView.loadUrl("https://play.google.com/store/apps/details?id=com.twitter.android&hl=en");
                }
            }

            if (url.equals("https://www.linkedin.com/company-beta/13269149/")) {
                Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company-beta/13269149/"));
                if (checkPackage(shareIntent, "com.linkedin")) {
                    startActivity(shareIntent);
                    webView.reload();
                } else {
                    webView.loadUrl("https://play.google.com/store/apps/details?id=com.linkedin.android&hl=en");
                }
            }

            if (url.startsWith("https://platform")) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                shareIntent.setType("text/plain");
                if (checkPackage(shareIntent, "com.linkedin")) {
                    startActivity(shareIntent);
                    webView.reload();
                } else {
                    webView.loadUrl("https://play.google.com/store/apps/details?id=com.linkedin.android&hl=en");
                }
            }

            if (url.startsWith("tg:")) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                shareIntent.setType("text/plain");
                if (checkPackage(shareIntent, "telegram")) {
                    startActivity(shareIntent);
                    webView.reload();
                } else {
                    webView.loadUrl("https://telegram.me/share/url?url=" + url);
                    webView.stopLoading();
                }
            }
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            if (!isOnline()) {
                finish();
            }
            spinner.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            fabInfo.setVisibility(View.VISIBLE);
            super.onPageFinished(webView, url);
        }
    }
}
