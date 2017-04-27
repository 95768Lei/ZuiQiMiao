package com.zl.project.fisrt_project.UI.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qq.e.ads.banner.BannerView;
import com.zl.project.fisrt_project.Base.BaseActivity;
import com.zl.project.fisrt_project.Mode.JokeBean;
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
 * @author zhanglei
 * @date 16/12/21
 */
public class XHActivity extends BaseActivity {

    private ImageView black;
    private TextView title_name;
    private ListView listView;
    private ProgressBar progress;
    private List<JokeBean> mList = new ArrayList<>();
    private UniversalAdapter<JokeBean> adapter;
    private SwipeRefreshLayout srl;
    private int page = 1;
    private String times;
    //广告的附着位
    private RelativeLayout layout;
    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xh);
        initView();
        initData();
        initListener();
    }

    public void initView() {

        times = getTiems();

        View title = findViewById(R.id.xh_title);
        black = (ImageView) title.findViewById(R.id.base_title_black);
        title_name = (TextView) title.findViewById(R.id.base_title_name);
        title_name.setText(R.string.xh_dq);

        listView = (ListView) findViewById(R.id.xh_list);
        progress = (ProgressBar) findViewById(R.id.xh_progress);
        srl = (SwipeRefreshLayout) findViewById(R.id.xh_srl);
        layout = (RelativeLayout) findViewById(R.id.xh_layout);

        srl.setColorSchemeResources(R.color.main_text_blue);

        adapter = new UniversalAdapter<JokeBean>(mActivity, mList, R.layout.xh_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, JokeBean jokeBean) {
                holder.setText(R.id.xh_item_content, jokeBean.getContent());
                holder.setText(R.id.xh_item_date, jokeBean.getUpdatetime());
            }
        };
        listView.setAdapter(adapter);

        //加载广告
        bannerView = AdvertManager.LoadBanner(mActivity, layout);

    }


    public void initData() {
        page = 1;
        if (mList != null) {
            mList.clear();
        }
        getNetWorkData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取网络数据
     */
    private void getNetWorkData() {
        srl.setRefreshing(true);
        Log.e("times:", "" + times);
        HttpUtils.getInstance(mActivity).GET(API.JOKE + "&page=" + page + "&pagesize=10&sort=desc&time=" + times, new HttpUtils.OnOkHttpCallback() {
            @Override
            public void onSuccess(String body) {
                JSONObject object = null;
                try {
                    object = new JSONObject(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject result = object.optJSONObject("result");
                final List<JokeBean> jokeBeen = JSON.parseArray(result.optString("data"), JokeBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                        mList.addAll(jokeBeen);
                        adapter.notifyDataSetChanged();
                        progress.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onError(Request error, Exception e) {
                srl.setRefreshing(false);
            }
        });
    }

    public void initListener() {
        black.setOnClickListener(this);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            progress.setVisibility(View.VISIBLE);
                            getNetWorkData();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_black:
                setBlackAnimation();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerView.destroy();
    }
}
