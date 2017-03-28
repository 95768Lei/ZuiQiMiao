package com.zl.project.fisrt_project.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.project.fisrt_project.Base.BaseFragment;
import com.zl.project.fisrt_project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecreationFragment extends BaseFragment {


    private ImageView back;
    private WebView web;

    public RecreationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recreation, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initView(View view) {
        final View title = view.findViewById(R.id.recreation_title);
        back = (ImageView) title.findViewById(R.id.base_title_black);
        back.setOnClickListener(this);
        TextView title_name = (TextView) title.findViewById(R.id.base_title_name);
        title_name.setText(getResources().getString(R.string.recreation));

        web = (WebView) view.findViewById(R.id.recreation_web);
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_black:
                popFragment();
                break;
        }
    }
}
