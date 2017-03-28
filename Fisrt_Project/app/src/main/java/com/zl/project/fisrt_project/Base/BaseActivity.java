package com.zl.project.fisrt_project.Base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;
import com.zl.project.fisrt_project.R;

/**
 * Created by zhanglei on 2016/12/6.
 */

public abstract class BaseActivity extends AutoLayoutActivity implements View.OnClickListener {

    protected Activity mActivity;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("加载中...");
        initView();
        initData();
        initListener();
    }

    /**
     * 显示Dialog
     */
    protected void showDialog() {
        dialog.show();
    }

    /**
     * 显示Dialog
     */
    protected void hideDialog() {
        dialog.dismiss();
    }

    /**
     * 显示吐司
     *
     * @param message
     */
    protected void show(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化数据
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化监听
     */
    public abstract void initListener();

    /**
     * 加载Fragment
     *
     * @param fragment
     * @param layoutId
     */
    protected void LoadFragment(Fragment fragment, int layoutId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layoutId, fragment);
        transaction.addToBackStack("main");
        transaction.commit();
    }

    /**
     * 加载Fragment并将其添加至回退栈
     *
     * @param fragment
     * @param layoutId
     */
    protected void LoadFragmentToTask(Fragment fragment, int layoutId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layoutId, fragment);
        transaction.addToBackStack("");
        transaction.commit();
    }

    /**
     * 设置状态栏的颜色
     *
     * @param colorId
     */
    protected void setDarkColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorId));
        }
    }

    /**
     * 分享
     *
     * @param appName
     */
    protected void share(String appName) {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "f分享");
        share_intent.putExtra(Intent.EXTRA_TEXT, "亲 我刚做了一款应用 支持一下吧:" + appName);
        share_intent = Intent.createChooser(share_intent, "分享");
        startActivity(share_intent);
    }

    /**
     * 退出并设置界面退出的动画
     */
    protected void setBlackAnimation() {
        finish();
        overridePendingTransition(R.anim.fixation, R.anim.rigth_in_anim);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() > 0) {
                manager.popBackStack();
            } else {
                setBlackAnimation();
            }

        }

        return true;
    }

    /**
     * 添加Fragment将其放入回退栈并提交
     *
     * @param fragment
     * @param layoutId
     */
    protected void addFragment(Fragment fragment, int layoutId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.rigth_out_anim,
                R.anim.rigth_in_anim,
                R.anim.rigth_out_anim,
                R.anim.rigth_in_anim
        );
        transaction.add(layoutId, fragment);
        transaction.addToBackStack("tag");
        transaction.commit();
    }

    /**
     * 向外弹出Fragment
     */
    protected void popFragment() {
        FragmentManager manager = getSupportFragmentManager();
        int backStackEntryCount = manager.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            manager.popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取系统的时间戳
     *
     * @return
     */
    public String getTiems() {
        long time = System.currentTimeMillis() / 1000;
        String times = String.valueOf(time);
        return times;

    }

    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public boolean isNetworkConnected() {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) mActivity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();

        }

        return false;

    }

}
