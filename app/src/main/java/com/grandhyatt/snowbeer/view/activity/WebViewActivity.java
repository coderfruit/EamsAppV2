package com.grandhyatt.snowbeer.view.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.ButterKnife;

/**
 * WebViewActivity
 *
 * @author
 * @email
 * @mobile
 * @create 2018/7/3 15:51
 */
@SuppressLint("NewApi")
public class WebViewActivity extends ActivityBase implements IActivityBase, OnClickListener {

    private ToolBarLayout mToolBar;

    public static final String EXTRA_DATA_TITLE = "EXTRA_DATA_TITLE";
    public static final String EXTRA_DATA_URL = "EXTRA_DATA_URL";
    private String mTitle;
    private String mUrl;
    private WebView mWebView;
    private SmartRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(this);

        mTitle = getIntent().getStringExtra(EXTRA_DATA_TITLE);
        mUrl = getIntent().getStringExtra(EXTRA_DATA_URL);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initView() {
        mToolBar = (ToolBarLayout) findViewById(R.id.mToolBar);
        mWebView = (WebView) findViewById(R.id.mWebView);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.mRefreshLayout);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings webSettings = mWebView.getSettings();


        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= 11) {
            webSettings.setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setTextSize(WebSettings.TextSize.NORMAL);
            try {
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        mWebView.requestFocusFromTouch();


    }

    @Override
    public void bindEvent() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh();
                mWebView.reload();
            }
        });
    }

    @Override
    public void refreshUI() {
        try {
            mToolBar.setTitle(mTitle);
            mWebView.loadUrl(mUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestNetworkData() {

    }
}
