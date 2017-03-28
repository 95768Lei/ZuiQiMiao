package com.zl.project.fisrt_project.UI.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.project.fisrt_project.Base.BaseFragment;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.Utils.AdvertManager;

/**
 * @author zhanglei
 * @date 16/12/7
 * 加载网页的Fragment
 */
public class WebFragment extends BaseFragment implements View.OnClickListener {


    private WebView webView;
    private ImageView back;
    private TextView title_name;
    private ProgressBar pb;
    private AutoRelativeLayout arl;
    private RelativeLayout layout;

    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        initView(view);
        initData();
        initListener();
        return view;
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
        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        if (url != null) {
            Log.e("url==", url);
            webView.loadUrl(url);
        }
    }

    private void initView(View view) {

        webView = new WebView(mActivity.getApplicationContext());
        pb = (ProgressBar) view.findViewById(R.id.web_pb);
        layout = (RelativeLayout) view.findViewById(R.id.web_layout);
        layout.addView(webView);

        arl = (AutoRelativeLayout) view.findViewById(R.id.web_arl);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        View title = view.findViewById(R.id.web_title);
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
