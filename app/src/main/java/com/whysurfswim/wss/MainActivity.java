package com.whysurfswim.wss;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String ShowOrHideWebViewInitialUse = "show";
    private WebView webview;
    private ProgressBar spinner;
    private Toolbar toolbar;
    private boolean flag,doubleBack,fabExpanded;
    private AdView mAdView;
    private FloatingActionButton fabInfo,fabAbout,fabShare,fabFacebook,fabLinkedin,fabWhatsapp,fabTwitter;
    private LinearLayoutCompat layoutAbout,layoutShare,layoutLinkedin,layoutWhatsapp,layoutTwitter,layoutFacebook;

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

        if(isOnline()) {
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            webview.loadUrl("https://whysurfswim.com/");
        }else {
            finish();
        }

        MobileAds.initialize(this, "ca-app-pub-6059528612565667/8468844477");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        closeSubMenusFab();
        fabInfo = (FloatingActionButton) this.findViewById(R.id.fabInfo);
        fabAbout = (FloatingActionButton) this.findViewById(R.id.fabAbout);
        fabShare = (FloatingActionButton) this.findViewById(R.id.fabShare);
        fabLinkedin = (FloatingActionButton) this.findViewById(R.id.fabLinkedin);
        fabTwitter = (FloatingActionButton) this.findViewById(R.id.fabTwitter);
        fabWhatsapp = (FloatingActionButton) this.findViewById(R.id.fabWhatsapp);
        fabFacebook = (FloatingActionButton) this.findViewById(R.id.fabFacebook);

        //When main Fab (Settings) is clicked, it expands if not expanded already.
        //Collapses if main FAB was open already.
        //This gives FAB (Settings) open/close behavior
        fabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        fabAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.loadUrl("https://play.google.com/store/apps/details?id=com.whysurfswim.wss&hl=en");
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });

        fabWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.loadUrl("https://chat.whatsapp.com/invite/1ACYKZvKAcP7GJqhgG0NBk");
            }
        });

        fabLinkedin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                webview.loadUrl("https://www.linkedin.com/company-beta/13269149/");
            }
        });

        fabTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent shareIntent = new Intent(Intent.ACTION_VIEW,
                       Uri.parse("https://twitter.com/intent/follow?user_id=839208853103312896"));
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().contains("com.twitter.android")) {
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
        });

        fabFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.loadUrl("https://www.facebook.com/whysurfswim/");
            }
        });
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


    //closes FAB submenus
    private void closeSubMenusFab(){
        fabInfo = (FloatingActionButton) this.findViewById(R.id.fabInfo);
        layoutAbout = (LinearLayoutCompat) this.findViewById(R.id.layoutFabAbout);
        layoutShare = (LinearLayoutCompat) this.findViewById(R.id.layoutFabShare);
        layoutLinkedin = (LinearLayoutCompat) this.findViewById(R.id.layoutFabLinkedin);
        layoutTwitter = (LinearLayoutCompat) this.findViewById(R.id.layoutFabTwitter);
        layoutWhatsapp = (LinearLayoutCompat) this.findViewById(R.id.layoutFabWhatsapp);
        layoutFacebook = (LinearLayoutCompat) this.findViewById(R.id.layoutFabFacebook);

        layoutLinkedin.setVisibility(View.INVISIBLE);
        layoutWhatsapp.setVisibility(View.INVISIBLE);
        layoutTwitter.setVisibility(View.INVISIBLE);
        layoutShare.setVisibility(View.INVISIBLE);
        layoutAbout.setVisibility(View.INVISIBLE);
        layoutFacebook.setVisibility(View.INVISIBLE);
        fabInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        fabInfo = (FloatingActionButton) this.findViewById(R.id.fabInfo);
        layoutAbout = (LinearLayoutCompat) this.findViewById(R.id.layoutFabAbout);
        layoutShare = (LinearLayoutCompat) this.findViewById(R.id.layoutFabShare);
        layoutLinkedin = (LinearLayoutCompat) this.findViewById(R.id.layoutFabLinkedin);
        layoutTwitter = (LinearLayoutCompat) this.findViewById(R.id.layoutFabTwitter);
        layoutWhatsapp = (LinearLayoutCompat) this.findViewById(R.id.layoutFabWhatsapp);
        layoutFacebook = (LinearLayoutCompat) this.findViewById(R.id.layoutFabFacebook);

        layoutLinkedin.setVisibility(View.VISIBLE);
        layoutWhatsapp.setVisibility(View.VISIBLE);
        layoutTwitter.setVisibility(View.VISIBLE);
        layoutAbout.setVisibility(View.VISIBLE);
        layoutShare.setVisibility(View.VISIBLE);
        layoutFacebook.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fabInfo.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // This allows for a splash screen
    // (and hide elements once the page loads)
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {

            if(url.startsWith("https://play.google.com")){
                Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().contains("com.android.vending")) {
                        shareIntent.setPackage(info.activityInfo.packageName);
                        flag = true;
                    }
                }
                if (flag) {
                    startActivity(shareIntent);
                    webview.reload();
                } else {
                    webview.loadUrl(url);
                }
            }

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
                    shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
                    shareIntent.setType("text/plain");
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

            if(url.equals("https://www.linkedin.com/company-beta/13269149/")){
                Intent shareIntent;
                shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company-beta/13269149/"));
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

            if (url.startsWith("https://platform")) {
                Intent shareIntent;
                    shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
                    shareIntent.setType("text/plain");
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
