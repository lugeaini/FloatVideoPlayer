package com.chenxulu.video.demo2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chenxulu.video.R;

import java.util.ArrayList;

/**
 * Created by xulu on 16/5/12.
 */
public class MyAdapter2 extends BaseAdapter {
    private ArrayList<String> list;
    private View.OnClickListener onClickListener;

    public MyAdapter2(ArrayList<String> list, View.OnClickListener onClickListener) {
        this.list = list;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.hideView = view.findViewById(R.id.hide_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(list.get(i));
        viewHolder.hideView.setTag(i);
        viewHolder.hideView.setOnClickListener(onClickListener);
        return view;
    }

    class ViewHolder {
        TextView title;
        View hideView;
    }
}
