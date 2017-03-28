package com.zl.project.fisrt_project.Mode;

import android.support.v4.app.Fragment;

/**
 * Created by zhanglei on 2016/12/7.
 */

public class FragmentMode {

    private Fragment fragment;
    private boolean isShow;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
