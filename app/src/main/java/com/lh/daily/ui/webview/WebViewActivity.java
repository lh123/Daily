package com.lh.daily.ui.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lh.daily.R;
import com.lh.daily.base.BaseActivity;
import com.lh.daily.databinding.ActivityWebviewBinding;

/**
 * Created by home on 2017/2/10.
 */

public class WebViewActivity extends BaseActivity<ActivityWebviewBinding> {

    private static final String EXTRA_URL = "url";

    public static void startActivity(Context context,String url){
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra(EXTRA_URL,url);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        final String url = getIntent().getStringExtra(EXTRA_URL);
        mDataBinding.views.toolbar.setTitle(R.string.loading);
        mDataBinding.views.toolbar.inflateMenu(R.menu.web_view_menu);
        mDataBinding.views.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.open){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(Intent.createChooser(intent,"选择浏览器"));
                }
                return false;
            }
        });
        mDataBinding.views.toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        mDataBinding.views.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDataBinding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
        mDataBinding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mDataBinding.views.toolbar.setTitle(title);
            }
        });
        mDataBinding.webView.getSettings().setJavaScriptEnabled(true);
        mDataBinding.webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (mDataBinding.webView.canGoBack()){
            mDataBinding.webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBinding.webView.removeAllViews();
        mDataBinding.webView.destroy();
    }
}
