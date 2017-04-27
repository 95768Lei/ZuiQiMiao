package com.zl.project.fisrt_project.UI.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qq.e.ads.banner.BannerView;
import com.zl.project.fisrt_project.Base.BaseActivity;
import com.zl.project.fisrt_project.Mode.PhoneBean;
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
 * Created by zhanglei on 2017/4/27.
 * 手机号查询
 */

public class PhoneCheckActivity extends BaseActivity {

    private ImageView black;
    private TextView title_name;
    private TextView text_rigth;
    private EditText edit;
    private ListView list_view;
    private UniversalAdapter<String> adapter;
    private List<String> list = new ArrayList<>();
    private RelativeLayout layout;
    private BannerView bannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_wd);

        initView();
        initData();
        initListener();
    }

    private void initListener() {
        black.setOnClickListener(this);
        text_rigth.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        bannerView = AdvertManager.LoadBanner(mActivity, layout);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        list.addAll(getFirstData());
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        View title = findViewById(R.id.phone_title);
        black = (ImageView) title.findViewById(R.id.base_title_black);
        title_name = (TextView) title.findViewById(R.id.base_title_name);
        title_name.setText("手机号码查询");
        text_rigth = (TextView) title.findViewById(R.id.base_title_text_rigth);
        text_rigth.setText(R.string.cx);

        edit = (EditText) findViewById(R.id.phone_edit);
        list_view = (ListView) findViewById(R.id.phone_list);
        layout = (RelativeLayout) findViewById(R.id.phone_layout);

        adapter = new UniversalAdapter<String>(mActivity, list, R.layout.luck_list_item) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                holder.setText(R.id.luck_item_text, s);
            }
        };
        list_view.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        bannerView.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_black:
                popFragment();
                break;
            case R.id.base_title_text_rigth:
                phoneQuery();
                break;
        }
    }


    /**
     * 手机号归属地查询
     */
    private void phoneQuery() {
        String phone = edit.getText().toString();
        HttpUtils.getInstance(mActivity).GET(API.PHONEQUERY + phone, new HttpUtils.OnOkHttpCallback() {
            @Override
            public void onSuccess(String body) {
                try {
                    JSONObject object = new JSONObject(body);
                    final PhoneBean result = JSON.parseObject(object.optString("result"), PhoneBean.class);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (list != null) {
                                list.clear();
                            }
                            list.addAll(getData(result));
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Request error, Exception e) {

            }
        });
    }

    /**
     * 拼接网络返回的数据
     *
     * @param phoneBean
     * @return
     */
    private List<String> getData(PhoneBean phoneBean) {
        List<String> list = new ArrayList<>();
        list.add("省份：" + phoneBean.getProvince());
        list.add("城市：" + phoneBean.getCity());
        list.add("区号：" + phoneBean.getAreacode());
        list.add("邮编：" + phoneBean.getZip());
        list.add("运营商：" + phoneBean.getCompany());
        list.add("卡类型：" + phoneBean.getCard());

        return list;
    }

    /**
     * 获取初始化的数据
     *
     * @return
     */
    private List<String> getFirstData() {
        List<String> list = new ArrayList<>();
        list.add("省份：");
        list.add("城市：");
        list.add("区号：");
        list.add("邮编：");
        list.add("运营商：");
        list.add("卡类型：");

        return list;
    }
}
