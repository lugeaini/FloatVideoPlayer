package com.chenxulu.video.demo1;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xulu on 16/5/12.
 */
public class MyAdapter1 extends BaseAdapter {
    private ArrayList<Integer> list;

    public MyAdapter1(ArrayList<Integer> list) {
        this.list = list;
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
        if (view == null) {
            view = new TextView(viewGroup.getContext());
            int width = view.getResources().getDisplayMetrics().widthPixels;
            int height = width / 2;
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, height);
            view.setLayoutParams(params);
        }
        ((TextView) view).setText(list.get(i) + "");
        return view;
    }
}
