package com.zl.project.fisrt_project.UI.MyAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.project.fisrt_project.Mode.TtBean;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.Utils.ImageUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class TtChildAdapter extends BaseAdapter {

    private Context context;
    private Fragment fragment;
    private List<TtBean> list;

    public TtChildAdapter(Context context, Fragment fragment, List<TtBean> list) {
        this.context = context;
        this.fragment = fragment;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TtChildViewHolder holder;
        if (view == null) {
            holder = new TtChildViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.tt_child_item, viewGroup, false);
            holder.name = (TextView) view.findViewById(R.id.tt_child_name);
            holder.title = (TextView) view.findViewById(R.id.tt_child_title);
            holder.icon = (ImageView) view.findViewById(R.id.tt_child_icon);
            view.setTag(holder);
        } else {
            holder = (TtChildViewHolder) view.getTag();
        }

        //绑定数据
        holder.title.setText(list.get(i).getTitle());
        String author_name = list.get(i).getAuthor_name();
        if (author_name.equals("中央人民广播电台")) {
            author_name = "广播电台";
        }
        holder.name.setText(author_name);
        ImageUtils.LoadImageUrl(list.get(i).getThumbnail_pic_s03(), fragment, holder.icon);
        return view;
    }

    public class TtChildViewHolder {
        TextView name;
        TextView title;
        ImageView icon;
    }
}
