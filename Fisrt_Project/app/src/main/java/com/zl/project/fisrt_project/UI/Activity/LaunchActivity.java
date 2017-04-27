package com.zl.project.fisrt_project.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.zl.project.fisrt_project.Base.BaseActivity;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.Utils.API;

/**
 * @author zhanglei
 * @date 16/12/23
 * 启动页
 */
public class LaunchActivity extends BaseActivity implements SplashADListener {

    private RelativeLayout arl;
    private TextView launch_text;
    private SplashAD splashAD;
    public boolean canJump = false;
    private static final String SKIP_TEXT = "点击跳过 %d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanch);
        //设置全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        arl = (RelativeLayout) findViewById(R.id.launch_arl);
        launch_text = (TextView) findViewById(R.id.launch_text);

        //初始化开屏广告页
        splashAD = new SplashAD(this, arl, launch_text, API.APP_ID, API.PUBLIC, this, 0);
    }


    /**
     * 跳转到主界面
     */
    private void toMainActivity() {
        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onADDismissed() {
        next();
    }

    @Override
    public void onNoAD(int i) {
        toMainActivity();
    }

    @Override
    public void onADPresent() {

    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADTick(long l) {
        launch_text.setText(String.format(SKIP_TEXT, Math.round(l / 1000f)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            next();
        }
        canJump = true;
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void next() {
        if (canJump) {
            this.startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            canJump = true;
        }
    }

    @Override
    public void onClick(View view) {

    }
}
