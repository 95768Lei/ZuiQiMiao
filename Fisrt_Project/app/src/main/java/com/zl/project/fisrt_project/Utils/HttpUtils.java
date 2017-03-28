package com.zl.project.fisrt_project.Utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ��
 * 网络请求工具类
 *
 * @author zhanglei
 */
public class HttpUtils {

    private static HttpUtils mHttpUtils;
    private static OkHttpClient mOkClient;
    private static Context context;
    private static Handler handler = new Handler();

    private HttpUtils(Context context) {
        mOkClient = new OkHttpClient();
        this.context = context;
    }

    /**
     * 获取一个本类的对象
     *
     * @return
     */
    public static HttpUtils getInstance(Context context) {
        if (mHttpUtils == null) {
            synchronized (HttpUtils.class) {
                if (mHttpUtils == null) {
                    mHttpUtils = new HttpUtils(context);
                }
            }
        }
        return mHttpUtils;
    }

    /**
     * get形式的请求方式
     *
     * @param url
     * @param callback
     */
    public void GET(String url, final OnOkHttpCallback callback) {

        Request request = new Request.Builder().url(url).build();

        final Call mCall = mOkClient.newCall(request);
        mCall.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onError(null, e);
                }
                mCall.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bodyString = response.body().string();
                response.body().close();
                if (bodyString != null) {
                    if (callback != null) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(bodyString);
                            Log.d("body==", bodyString);
                            int error_code = object.optInt("error_code");
                            int resultcode = object.optInt("resultcode");
                            if (error_code == 0 || resultcode == 200) {
                                callback.onSuccess(bodyString);
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError(null, e);
                        }
                    }
                }
                mCall.cancel();
            }
        });
    }

    /**
     * POST形式的网络请求
     *
     * @param params
     * @param url
     * @param callback
     */
    public void POST(Map<String, String> params, String url, final OnOkHttpCallback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //添加参数
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        //创建请求体
        final Request request = new Request.Builder().url(url).post(builder.build()).build();
        //加入任务调度
        mOkClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (callback != null) {
                    callback.onError(null, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bodyString = response.body().string();
                if (callback != null) {
                    callback.onSuccess(bodyString);
                }
                response.body().close();
            }

        });
    }

    /**
     * 获取数据的接口
     *
     * @author zhanglei
     */
    public interface OnOkHttpCallback {
        void onSuccess(String body);

        void onError(Request error, Exception e);
    }


}
