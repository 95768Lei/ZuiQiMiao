package com.zl.project.fisrt_project.UI.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qq.e.ads.banner.BannerView;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.project.fisrt_project.Base.BaseActivity;
import com.zl.project.fisrt_project.Mode.LuckBean;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.Utils.API;
import com.zl.project.fisrt_project.Utils.AdvertManager;
import com.zl.project.fisrt_project.Utils.HttpUtils;
import com.zl.project.fisrt_project.Utils.UniversalAdapter;
import com.zl.project.fisrt_project.Utils.UniversalViewHolder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @author zhagnlei
 * @date 16/12/21
 * 星座运势
 */
public class LuckActivity extends BaseActivity {

    private ImageView black;
    private TextView title_name, title_right_text;
    private EditText edit;
    private RadioGroup rg;
    private TextView text;
    private String type = "";
    private ListView luck_list;
    private UniversalAdapter<String> adapter;
    private List<String> list = new ArrayList<>();
    private RelativeLayout layout;
    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_luck);
        View title = findViewById(R.id.luck_title);
        black = (ImageView) title.findViewById(R.id.base_title_black);
        title_name = (TextView) title.findViewById(R.id.base_title_name);
        title_right_text = (TextView) title.findViewById(R.id.base_title_text_rigth);
        title_right_text.setText(R.string.ok);
        title_name.setText(R.string.xz_ys);

        edit = (EditText) findViewById(R.id.luck_edit);
        rg = (RadioGroup) findViewById(R.id.luck_rg);
        text = (TextView) findViewById(R.id.luck_text);
        luck_list = (ListView) findViewById(R.id.luck_list);

        adapter = new UniversalAdapter<String>(mActivity, list, R.layout.luck_list_item) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                holder.setText(R.id.luck_item_text, s);
            }
        };

        luck_list.setAdapter(adapter);
        layout = (RelativeLayout) findViewById(R.id.activity_luck);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //加载广告
        bannerView = AdvertManager.LoadBanner(mActivity, layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁广告
        bannerView.destroy();
    }

    @Override
    public void initData() {
        if (list != null) {
            list.clear();
        }
        list.addAll(getFirstData());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        black.setOnClickListener(this);
        title_right_text.setOnClickListener(this);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.luck_rb_today:
                        type = "today";
                        break;
                    case R.id.luck_rb_tomorrow:
                        type = "tomorrow";
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.base_title_black:
                setBlackAnimation();
                break;
            //查询
            case R.id.base_title_text_rigth:
                commitData();
                break;
        }
    }

    /**
     * 提交查询数据
     */
    private void commitData() {

        String consName = edit.getText().toString();
        if (!TextUtils.isEmpty(consName) && !TextUtils.isEmpty(type)) {
            showDialog();
            HttpUtils.getInstance(mActivity).GET(API.LUCK + "&consName=" + consName + "&type=" + type, new HttpUtils.OnOkHttpCallback() {
                @Override
                public void onSuccess(String body) {
                    final LuckBean luckBean = JSON.parseObject(body, LuckBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog();
                            if (list != null) {
                                list.clear();
                            }
                            list.addAll(getData(luckBean));
                            adapter.notifyDataSetChanged();
//                            text.setText(getData(luckBean));
                        }
                    });
                }

                @Override
                public void onError(Request error, Exception e) {
                    hideDialog();
                }
            });
        } else {
            if (TextUtils.isEmpty(consName)) {
                show("请输入星座");
            } else if (TextUtils.isEmpty(type)) {
                show("请选择日期");
            }

        }
    }

    /**
     * 拼接返回的数据
     *
     * @param luckBean
     * @return
     */
    public List<String> getData(LuckBean luckBean) {
        List<String> list = new ArrayList<>();
        list.add("星座：" + luckBean.getName());
        list.add("综合指数：" + luckBean.getAll());
        list.add("幸运色：" + luckBean.getColor());
        list.add("健康指数：" + luckBean.getHealth());
        list.add("财运指数：" + luckBean.getMoney());
        list.add("工作指数：" + luckBean.getWork());
        list.add("幸运数字：" + luckBean.getNumber());
        list.add("速配星座：" + luckBean.getQFriend());
        list.add("运气概述：" + luckBean.getSummary());;
        return list;
    }

    /**
     * 获取初始化的数据
     *
     * @return
     */
    private List<String> getFirstData() {
        List<String> list = new ArrayList<>();
        list.add("星座：");
        list.add("综合指数：");
        list.add("幸运色：");
        list.add("健康指数：");
        list.add("财运指数：");
        list.add("工作指数：");
        list.add("幸运数字：");
        list.add("速配星座：");
        list.add("运气概述：");

        return list;
    }

}
