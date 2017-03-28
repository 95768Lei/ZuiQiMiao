package com.zl.project.fisrt_project.Utils;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;

/**
 * Created by zhanglei on 2016/12/29.
 * 广告的管理类
 */

public class AdvertManager {

    /**
     * 加载横幅广告
     *
     * @param activity
     * @param group
     * @return
     */
    public static BannerView LoadBanner(Activity activity, ViewGroup group) {

        BannerView bv = new BannerView(activity, ADSize.BANNER, API.APP_ID, API.BANNER);
        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(int arg0) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + arg0);
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        group.addView(bv);

        bv.loadAD();

        return bv;
    }

    /**
     * 加载插屏广告
     *
     * @param activity
     */
    public static void LoadInsertAdvert(Activity activity) {
        final InterstitialAD interstitialAD = new InterstitialAD(activity, API.APP_ID, API.INSERT);
        interstitialAD.setADListener(new AbstractInterstitialADListener() {
            @Override
            public void onADReceive() {
                interstitialAD.show();
            }

            @Override
            public void onNoAD(int i) {

            }
        });
        interstitialAD.loadAD();
    }

}
