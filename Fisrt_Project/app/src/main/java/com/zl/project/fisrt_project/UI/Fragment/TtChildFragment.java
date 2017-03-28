package com.zl.project.fisrt_project.UI.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.project.fisrt_project.Base.BaseFragment;
import com.zl.project.fisrt_project.Mode.TtBean;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.UI.MyAdapter.TtChildAdapter;
import com.zl.project.fisrt_project.Utils.API;
import com.zl.project.fisrt_project.Utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class TtChildFragment extends BaseFragment {

    private String type;
    private List<TtBean> mList = new ArrayList<>();
    private Handler mHandler = new Handler();
    private TtChildAdapter mAdapter;
    private ListView listView;
    private SwipeRefreshLayout srl;
    private ProgressBar child_pb;
    private List<TtBean> dataList;
    private AutoRelativeLayout layout;

    @Override
    public void onClick(View v) {

    }

    /**
     * item项的回调接口
     */
    public interface TtChildOnClickListener {
        void OnItemClick(String url);
    }

    private TtChildOnClickListener listener;

    public void setChildClick(TtChildOnClickListener listener) {
        this.listener = listener;
    }

    public static TtChildFragment newInstance(String type) {
        TtChildFragment fragment = new TtChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tt_child, container, false);
        initView(view);
        initListener();
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listener != null) {
                    listener.OnItemClick(mList.get(i).getUrl());
                }
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
                            child_pb.setVisibility(View.VISIBLE);
                            setData_8();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNetWorkData();
            }
        });


    }

    /**
     * 初始化View
     *
     * @param view
     */
    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.tt_child_list);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.tt_child_srl);
        child_pb = (ProgressBar) view.findViewById(R.id.child_pb);
        layout = (AutoRelativeLayout) view.findViewById(R.id.tt_child_layout);
        srl.setColorSchemeResources(R.color.colorPrimaryDark);
        //初始化适配器
        mAdapter = new TtChildAdapter(mActivity, this, mList);
        listView.setAdapter(mAdapter);
    }

    private void initData() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        getNetWorkData();
    }

    /**
     * 联网获取数据
     */
    private void getNetWorkData() {
        srl.setRefreshing(true);
        HttpUtils.getInstance(mActivity.getApplicationContext()).GET(API.TOUTIAO + "&type=" + type, new HttpUtils.OnOkHttpCallback() {
            @Override
            public void onSuccess(String body) {
                Log.e("body==", body);
                try {
                    JSONObject object = new JSONObject(body);
                    JSONObject object1 = object.optJSONObject("result");
                    dataList = JSON.parseArray(object1.optString("data"), TtBean.class);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (dataList != null && dataList.size() > 0) {
                                if (mList != null) {
                                    mList.clear();
                                }
                                layout.setVisibility(View.GONE);
                                setData8();
                            } else {
                                srl.setRefreshing(false);
                                layout.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Request error, Exception e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                    }
                });
            }

        });
    }

    /**
     * 对数据进行分页加载 延时加载
     * 每次添加8条数据
     */
    private void setData_8() {
        //数据源的数据长度
        final int mList_size = mList.size();
        //数据源数据长度加8后的长度
        final int mList_size_8 = mList_size + 8;
        //请求返回的数据的长度
        final int list_size = dataList.size();
        Log.e("size==", "mList_size:" + mList_size + "\nlist_size:" + list_size);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //分页加载的逻辑判断
                if (mList_size == list_size) {
                    show("没有更多数据了..");
                } else {
                    //每次添加八条数据到数据源
                    for (int i = mList_size; i < mList_size_8; i++) {
                        try {
                            TtBean ttBean = dataList.get(i);
                            mList.add(ttBean);
                        } catch (IndexOutOfBoundsException e) {
                            //在这里捕获数组下表越界异常进行处理，表示已经没有更多数据可加载
                            show("没有更多数据了..");
                            //刷新适配器
                            mAdapter.notifyDataSetChanged();
                            child_pb.setVisibility(View.GONE);
                            return;
                        }
                    }
                }
                //刷新适配器
                mAdapter.notifyDataSetChanged();
                child_pb.setVisibility(View.GONE);
            }
        }, 1200);
    }

    /**
     * 添加8条数据到界面数据源
     */
    private void setData8() {
        if (dataList.size() < 8) {
            mList.addAll(dataList);
        } else {
            for (int i = 0; i < 8; i++) {
                mList.add(dataList.get(i));
            }
        }

        //刷新适配器
        mAdapter.notifyDataSetChanged();
        child_pb.setVisibility(View.GONE);
        srl.setRefreshing(false);
    }
}
