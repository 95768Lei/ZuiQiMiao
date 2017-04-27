package com.zl.project.fisrt_project.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.project.fisrt_project.Base.BaseActivity;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.Utils.AdvertManager;

/**
 * Created by zhanglei on 2017/4/27.
 */

public class WebActivity extends BaseActivity {

    private WebView webView;
    private ImageView back;
    private TextView title_name;
    private ProgressBar pb;
    private RelativeLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_web);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        //加载插屏广告
        AdvertManager.LoadInsertAdvert(mActivity);
    }

    private void initListener() {
        back.setOnClickListener(this);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    pb.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pb.setProgress(newProgress);//设置进度值
                }
            }
        });

    }

    private void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (url != null) {
            Log.e("url==", url);
            webView.loadUrl(url);
        }
    }

    private void initView() {

        webView = new WebView(mActivity.getApplicationContext());
        pb = (ProgressBar) findViewById(R.id.web_pb);
        layout = (RelativeLayout) findViewById(R.id.web_layout);
        layout.addView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        View title = findViewById(R.id.web_title);
        back = (ImageView) title.findViewById(R.id.base_title_black);
        title_name = (TextView) title.findViewById(R.id.base_title_name);
        title_name.setText("资讯");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_black:
                getFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
        webView.removeAllViews();
    }
}
