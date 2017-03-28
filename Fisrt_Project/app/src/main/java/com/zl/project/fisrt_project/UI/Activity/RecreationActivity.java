package com.zl.project.fisrt_project.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zl.project.fisrt_project.R;

/**
 * @author zhanglei
 * @date 16/12/28
 */
public class RecreationActivity extends Activity implements View.OnClickListener {

    private WebView web;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recreation);
        initView();
    }

    private void initView() {
        final View title = findViewById(R.id.recreation_title);
        ImageView back = (ImageView) title.findViewById(R.id.base_title_black);
        back.setOnClickListener(this);
        TextView title_name = (TextView) title.findViewById(R.id.base_title_name);
        title_name.setText(getResources().getString(R.string.recreation));

        web = (WebView) findViewById(R.id.recreation_web);
        pb = (ProgressBar) findViewById(R.id.recreation_pb);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        web.setWebChromeClient(new WebChromeClient() {
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_black:
                if (web.canGoBack()) {
                    web.goBack();
                } else {
                    close();
                }
                break;
        }
    }

    /**
     * 关闭界面
     */
    private void close() {
        finish();
        overridePendingTransition(R.anim.fixation, R.anim.rigth_in_anim);
    }

    /**
     * 监听返回键，浏览器回退
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web.canGoBack()) {
                web.goBack();
            } else {
                close();
            }
        }
        return true;
    }
}
