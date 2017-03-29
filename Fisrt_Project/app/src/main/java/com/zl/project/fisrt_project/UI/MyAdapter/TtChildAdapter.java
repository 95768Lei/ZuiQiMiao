package com.zl.project.fisrt_project.UI.MyAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.project.fisrt_project.Mode.TtBean;
import com.zl.project.fisrt_project.R;
import com.zl.project.fisrt_project.Utils.ImageUtils;
import com.zl.project.fisrt_project.Utils.OnRecyclerItemClickListener;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class TtChildAdapter extends RecyclerView.Adapter<TtChildAdapter.TtChildHolder> {

    private Context context;
    private List<TtBean> list;
    private Fragment fragment;
    private OnRecyclerItemClickListener listener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public TtChildAdapter(Context context, List<TtBean> list, Fragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    @Override
    public TtChildHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tt_child_item, parent, false);
        return new TtChildHolder(view);
    }

    @Override
    public void onBindViewHolder(TtChildHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
        holder.title.setText(list.get(position).getTitle());
        holder.name.setText(list.get(position).getAuthor_name());
        ImageUtils.LoadImageUrl(list.get(position).getThumbnail_pic_s03(), fragment, holder.icon);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class TtChildHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView title;
        ImageView icon;

        public TtChildHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tt_child_name);
            title = (TextView) itemView.findViewById(R.id.tt_child_title);
            icon = (ImageView) itemView.findViewById(R.id.tt_child_icon);
        }
    }


}
