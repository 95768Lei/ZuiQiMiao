package com.zl.project.fisrt_project.UI.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qq.e.ads.banner.BannerView;
import com.zl.project.fisrt_project.Base.BaseActivity;
import com.zl.project.fisrt_project.Mode.ZgBean;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.Utils.API;
import com.zl.project.fisrt_project.Utils.AdvertManager;
import com.zl.project.fisrt_project.Utils.HttpUtils;
import com.zl.project.fisrt_project.Utils.UniversalAdapter;
import com.zl.project.fisrt_project.Utils.UniversalViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @author zhagnlei
 * @date 16/12/21
 * 周公解梦
 */
public class ZGActivity extends BaseActivity {

    private ImageView black;
    private TextView title_name, text_rigth;
    private ListView listView;
    private UniversalAdapter<ZgBean> adapter;
    private List<ZgBean> mList = new ArrayList<>();
    private EditText edit;
    private RelativeLayout layout;
    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_zg);

        View title = findViewById(R.id.zg_title);
        black = (ImageView) title.findViewById(R.id.base_title_black);
        title_name = (TextView) title.findViewById(R.id.base_title_name);
        text_rigth = (TextView) title.findViewById(R.id.base_title_text_rigth);
        text_rigth.setText(R.string.cx);
        title_name.setText(R.string.zg_jm);


        listView = (ListView) findViewById(R.id.zg_list);
        edit = (EditText) findViewById(R.id.zg_edit);
        layout = (RelativeLayout) findViewById(R.id.zg_layout);
        adapter = new UniversalAdapter<ZgBean>(mActivity, mList, R.layout.zg_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, ZgBean zgBean) {
                holder.setText(R.id.zg_item_dex, zgBean.getDes());
                holder.setText(R.id.zg_item_title, zgBean.getTitle());
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bannerView = AdvertManager.LoadBanner(mActivity, layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerView.destroy();
    }

    @Override
    public void initData() {
        if (mList != null) {
            mList.clear();
        }
    }

    @Override
    public void initListener() {
        black.setOnClickListener(this);
        text_rigth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_black:
                setBlackAnimation();
                break;
            case R.id.base_title_text_rigth:
                startQuery();
                break;
        }
    }

    /**
     * 开始进行查询
     */
    private void startQuery() {
        showDialog();
        String key = edit.getText().toString();
        HttpUtils.getInstance(mActivity).GET(API.ZG + key, new HttpUtils.OnOkHttpCallback() {
            @Override
            public void onSuccess(String body) {
                Log.e("result==", body);
                try {
                    JSONObject object = new JSONObject(body);
                    final List<ZgBean> result = JSON.parseArray(object.optString("result"), ZgBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog();
                            if (mList != null) {
                                mList.clear();
                            }
                            if (result != null) {
                                if (result.size() > 0) {
                                    mList.addAll(result);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Request error, Exception e) {
                hideDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show("没找到这个解释..");
                    }
                });
            }
        });
    }
}
