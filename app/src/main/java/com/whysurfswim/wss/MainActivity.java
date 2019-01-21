package com.whysurfswim.wss;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.util.List;

@SuppressLint("setJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private FloatingActionButton fabShare;
    private SwipeRefreshLayout layoutRefresh;
    private SearchView searchView;
    private Toolbar toolbar;
    private TextView textView;
    private DrawerLayout sidemenuLayout;
    private NavigationView sidemenuView;
    private long doubleBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        fabShare = this.findViewById(R.id.fabShare);
        searchView = this.findViewById(R.id.searchView);
        toolbar = this.findViewById(R.id.toolbar);
        webView = this.findViewById(R.id.webView);
        textView = this.findViewById(R.id.textView);
        layoutRefresh = this.findViewById(R.id.swipeLayout);
        sidemenuLayout = this.findViewById(R.id.sidemenuDrawerLayout);
        sidemenuView = this.findViewById(R.id.sidemenuNavigationView);
        initialLoad();
        eventListeners();
        MobileAds.initialize(this, "ca-app-pub-6059528612565667~1367770570");
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    /**
     * unCheckSidemenu method used to uncheck all sidemenu items selected
     */
    private void unCheckSidemenu() {
        int size = sidemenuView.getMenu().size();
        for (int count = 0; count < size; count++) {
            sidemenuView.getMenu().getItem(count).setChecked(false);
        }
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
     * initialLoad method is used to set default conditions
     */
    private void initialLoad() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        searchView.setActivated(true);
        searchView.setQueryHint(getResources().getString(R.string.search_text));
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.loadUrl("https://whysurfswim.com/");
    }

    /**
     * eventListeners to handle all events in app
     */
    private void eventListeners() {
        // Swipe refresh event
        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (webView.getUrl() != null) {
                    webView.reload();
                }
                layoutRefresh.setRefreshing(false);
            }
        });

        // Sidemenu open/close event
        sidemenuLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                        textView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                        textView.setVisibility(View.INVISIBLE);
                        unCheckSidemenu();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
                        }
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        textView.setVisibility(View.VISIBLE);
                        unCheckSidemenu();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                        }
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        // Sidemenu click event
        sidemenuView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getTitle().toString()) {
                            case "HOME":
                                webView.loadUrl("https://whysurfswim.com/");
                                break;
                            case "ANDROID TUTORIALS":
                                webView.loadUrl("https://whysurfswim.com/category/android-development/");
                                break;
                            case "CONTACT US":
                                webView.loadUrl("https://whysurfswim.com/contact-us/");
                                break;
                            case "OTHER APPS":
                                Intent shareIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/dev?id=7367641307370213032"));
                                if (checkPackage(shareIntent, "com.android.vending")) {
                                    startActivity(shareIntent);
                                } else {
                                    Intent viewIntent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/dev?id=7367641307370213032"));
                                    startActivity(viewIntent);
                                }
                                break;
                            case "ABOUT US":
                                webView.loadUrl("https://whysurfswim.com/about-us/");
                                break;
                            case "REVIEW":
                                Intent reviewIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.whysurfswim.wss"));
                                if (checkPackage(reviewIntent, "com.android.vending")) {
                                    startActivity(reviewIntent);
                                } else {
                                    Intent viewIntent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id=com.whysurfswim.wss"));
                                    startActivity(viewIntent);
                                }
                                break;
                            default:
                                webView.loadUrl("https://whysurfswim.com/");
                                break;
                        }
                        sidemenuLayout.closeDrawers();
                        menuItem.setChecked(false);
                        return true;
                    }
                });


        // Search View Event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {
                webView.loadUrl("https://whysurfswim.com/?s=" + searchText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        // Share click event
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (sidemenuLayout.isDrawerOpen(GravityCompat.START)) {
                    sidemenuLayout.closeDrawers();
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                } else {
                    sidemenuLayout.openDrawer(GravityCompat.START);
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * retryOnline method used to display retry page
     */
    private void retryOnline() {
        setContentView(R.layout.activity_retry);
        Button retryButton = this.findViewById(R.id.retryButton);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    recreate();
                }
            }
        });
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
                Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
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
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            if (!isOnline()) {
                retryOnline();
            } else if (url.equals("https://whysurfswim.com/")) {
                textView.setText(getResources().getString(R.string.home_text));
            } else if (url.equals("https://whysurfswim.com/category/android-development/")) {
                textView.setText(getResources().getString(R.string.android_text));
            } else if (url.equals("https://whysurfswim.com/contact-us/")) {
                textView.setText(getResources().getString(R.string.contact_text));
            } else if (url.equals("https://whysurfswim.com/about-us/")) {
                textView.setText(getResources().getString(R.string.about_text));
            } else if (url.startsWith("https://whysurfswim.com/?s=")) {
                textView.setText(getResources().getString(R.string.search_result_text));
            } else if (url.startsWith("https://whysurfswim.com/")) {
                textView.setText(getResources().getString(R.string.post_text));
            } else {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(viewIntent);
                webView.goBack();
            }
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            if (!isOnline()) {
                retryOnline();
            }
            super.onPageFinished(webView, url);
        }

        @Override
        public void onPageCommitVisible(WebView webView, String url) {
            if (!isOnline()) {
                retryOnline();
            }
            super.onPageCommitVisible(webView, url);
        }
    }
}
