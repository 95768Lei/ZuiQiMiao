package com.zl.project.fisrt_project.Mode;

/**
 * Created by zhanglei on 2016/12/22.
 */

public class ChatBean {
    /**
     * type == 1 表示是机器人说的
     * type == 2 表示是你说的
     */
    private int type;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
