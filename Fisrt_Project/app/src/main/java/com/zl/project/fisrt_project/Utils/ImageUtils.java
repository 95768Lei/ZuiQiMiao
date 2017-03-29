package com.zl.project.fisrt_project.Utils;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zl.project.fisrt_project.R;

/**
 * Created by zhanglei on 2016/12/6.
 * 图片加载工具类
 */

public class ImageUtils {

//    /**
//     * 加载网络图片
//     *
//     * @param url
//     * @param activity
//     * @param imageView
//     */
//    public static void LoadImageUrl(String url, Activity activity, ImageView imageView) {
//        Glide.with(activity).
//                load(url).
//                asBitmap().
//                diskCacheStrategy(DiskCacheStrategy.ALL).
//                into(imageView);
//    }
//

    /**
     * 加载网络图片
     *
     * @param url
     * @param fragment
     * @param imageView
     */
    public static void LoadImageUrl(String url, Fragment fragment, ImageView imageView) {
        Glide.with(fragment).
                load(url).
                placeholder(R.mipmap.loading).
                crossFade().
                centerCrop().
                diskCacheStrategy(DiskCacheStrategy.ALL).
                into(imageView);
    }
}
