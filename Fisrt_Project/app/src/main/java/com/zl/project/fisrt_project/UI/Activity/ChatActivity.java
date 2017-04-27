package com.zl.project.fisrt_project.UI.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zl.project.fisrt_project.Base.BaseActivity;
import com.zl.project.fisrt_project.Mode.ChatBean;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.Utils.API;
import com.zl.project.fisrt_project.Utils.HttpUtils;
import com.zl.project.fisrt_project.Utils.UniversalAdapter;
import com.zl.project.fisrt_project.Utils.UniversalViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by zhanglei on 2017/4/27.
 * 聊天机器人
 */

public class ChatActivity extends BaseActivity {

    private EditText send_edit;
    private TextView send_text, title_name;
    private ListView listView;
    private ImageView black;
    private List<ChatBean> mList = new ArrayList<>();
    private UniversalAdapter<ChatBean> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        initView();
        initListener();
    }

    private void initListener() {
        black.setOnClickListener(this);
        send_text.setOnClickListener(this);
    }

    /**
     * 初始化View
     */
    private void initView() {
        View title = findViewById(R.id.chat_title);
        black = (ImageView) title.findViewById(R.id.base_title_black);
        title_name = (TextView) title.findViewById(R.id.base_title_name);
        title_name.setText("聊天机器人");
        send_edit = (EditText) findViewById(R.id.chat_send_edit);
        send_text = (TextView) findViewById(R.id.send_text);
        listView = (ListView) findViewById(R.id.chat_list);

        final ChatBean bean = new ChatBean();
        bean.setText("欢迎来到聊天室..");
        bean.setType(API.LEFT);
        mList.add(bean);
        adapter = new UniversalAdapter<ChatBean>(mActivity, mList, R.layout.chat_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, ChatBean chatBean) {
                TextView right = holder.getView(R.id.chat_item_right);
                TextView left = holder.getView(R.id.chat_item_left);
                int type = chatBean.getType();
                switch (type) {
                    case 1:
                        left.setText(chatBean.getText());
                        left.setVisibility(View.VISIBLE);
                        right.setVisibility(View.GONE);
                        break;
                    case 2:
                        right.setText(chatBean.getText());
                        right.setVisibility(View.VISIBLE);
                        left.setVisibility(View.GONE);
                        break;
                }
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_black:
                popFragment();
                break;
            case R.id.send_text:
                sendMessage();
                break;
        }
    }

    /**
     * 发送消息给机器人
     */
    private void sendMessage() {
        String message = send_edit.getText().toString();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(mActivity, "内容不能为空...", Toast.LENGTH_SHORT).show();
        } else {
            ChatBean bean = new ChatBean();
            bean.setText(message);
            bean.setType(API.RIGHT);
            mList.add(bean);
            adapter.notifyDataSetChanged();
            send_edit.setText("");
            HttpUtils.getInstance(mActivity).GET(API.ROBOT + message, new HttpUtils.OnOkHttpCallback() {
                @Override
                public void onSuccess(String body) {

                    try {
                        JSONObject object = new JSONObject(body);
                        JSONObject result = object.optJSONObject("result");
                        String text = result.optString("text");
                        ChatBean bean = new ChatBean();
                        bean.setText(text);
                        bean.setType(API.LEFT);
                        mList.add(bean);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
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
                            show("能不能正经点说话..");
                        }
                    });

                }
            });
        }
    }
}
