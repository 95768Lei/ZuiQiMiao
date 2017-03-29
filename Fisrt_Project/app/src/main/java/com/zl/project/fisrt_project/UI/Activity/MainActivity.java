package com.zl.project.fisrt_project.UI.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zl.project.fisrt_project.Base.BaseActivity;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.UI.Fragment.ChatFragment;
import com.zl.project.fisrt_project.UI.Fragment.PhoneCheckFragment;
import com.zl.project.fisrt_project.UI.Fragment.TtChildFragment;
import com.zl.project.fisrt_project.UI.Fragment.WebFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements TtChildFragment.TtChildOnClickListener {

    private NavigationView nv;
    private DrawerLayout draw;
    private Handler handler = new Handler();
    private TabLayout tt_tab;
    private final String[] name = {"头条", "社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"};
    private final String[] type = {"top", "shehui", "guonei", "guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang"};
    private ViewPager tt_pager;
    private FragmentStatePagerAdapter adapter;
    private List<Fragment> mList = new ArrayList<>();
    private FloatingActionButton fab_share;
    private Toolbar main_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkConnected()) {
            show("大兄弟你把网打开好不..");
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        main_toolbar.setNavigationIcon(R.mipmap.app_icon);
        main_toolbar.setTitle(R.string.app_name);
        main_toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(main_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setDarkColor(R.color.colorPrimaryDark);
        nv = (NavigationView) findViewById(R.id.main_nv);
        draw = (DrawerLayout) findViewById(R.id.main_draw);
        fab_share = (FloatingActionButton) findViewById(R.id.main_rl_share);
        nv.setItemIconTintList(null);

        tt_tab = (TabLayout) findViewById(R.id.main_tab);
        tt_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        tt_pager = (ViewPager) findViewById(R.id.main_pager);
        //初始化适配器
        adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }

            @Override
            public int getCount() {
                return mList == null ? 0 : mList.size();
            }
        };
        //添加适配器
        tt_pager.setAdapter(adapter);
    }

    @Override
    public void initData() {
        int length = name.length;
        for (int i = 0; i < length; i++) {
            TabLayout.Tab tab = tt_tab.newTab();
            tab.setText(name[i]);
            tt_tab.addTab(tab);
            TtChildFragment ttChildFragment = TtChildFragment.newInstance(type[i]);
            ttChildFragment.setChildClick(this);
            mList.add(ttChildFragment);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {

        tt_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tt_tab));
        tt_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tt_pager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //头条
                    case R.id.main_draw_top:
                        popFragment();
                        draw.closeDrawers();
                        break;
                    //笑话大全
                    case R.id.xh_all:
                        openXhActivity();
                        break;
                    //手机归属地查询
                    case R.id.phone_check:
                        startFragment(new PhoneCheckFragment(), R.id.main_parent_rl);
                        break;
                    //邮票查询
                    case R.id.chat_robot:
                        startFragment(new ChatFragment(), R.id.main_parent_rl);
                        break;
                    //星座运势
                    case R.id.luck:
                        openLuckActivity();
                        break;
                    //周公解梦
                    case R.id.zg_jm:
                        openZGActivity();
                        break;

                }
                return true;
            }
        });

        main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.openDrawer(Gravity.LEFT);
            }
        });
        fab_share.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //分享文本
            case R.id.main_rl_share:
                share();
                break;
        }
    }

    /**
     * 打开一个新的Fragment
     *
     * @param fragment
     * @param layoutId
     */
    private void startFragment(final Fragment fragment, final int layoutId) {
        draw.closeDrawers();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popFragment();
                addFragment(fragment, layoutId);
            }
        }, 200);
    }

    /**
     * 打开笑话大全Activity
     */
    private void openXhActivity() {
        draw.closeDrawers();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mActivity, XHActivity.class));
            }
        }, 200);
    }

    /**
     * 打开星座运势Activity
     */
    private void openLuckActivity() {
        draw.closeDrawers();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mActivity, LuckActivity.class));
            }
        }, 200);
    }

    /**
     * 打开周公解梦Activity
     */
    private void openZGActivity() {
        draw.closeDrawers();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mActivity, ZGActivity.class));
            }
        }, 200);
    }

    /**
     * 打开资讯详情页的回调接口
     *
     * @param url
     */
    @Override
    public void OnItemClick(String url) {
        if (url != null) {
            WebFragment webFragment = new WebFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            webFragment.setArguments(bundle);
            addFragment(webFragment, R.id.main_parent_rl);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
