package com.chenxulu.video.demo2;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.chenxulu.video.R;
import com.chenxulu.video.widget.MyVideoLayout;

import java.util.ArrayList;
import java.util.List;

public class Demo2Activity extends Activity {
    String[] videoArray = {
            "http://file.ihimee.cn/videoForMobile/0/%e5%b0%91%e5%84%bf%e8%8b%b1%e8%af%ad/%e8%8b%b1%e6%96%87%e8%a7%86%e9%a2%91/%e5%8e%9f%e7%89%88%e5%8a%a8%e7%94%bb/3rd%20&%20Bird%ef%bc%88%e4%b8%89%e5%8f%aa%e5%b0%8f%e9%b8%9f%ef%bc%89/%e7%ac%ac%e4%b8%80%e5%ad%a3/A%20Chorus%20for%20Us.mp4",
            "http://file.ihimee.cn/videoForMobile/0/%e5%b0%91%e5%84%bf%e8%8b%b1%e8%af%ad/%e8%8b%b1%e6%96%87%e8%a7%86%e9%a2%91/%e5%8e%9f%e7%89%88%e5%8a%a8%e7%94%bb/Ben%2010%20alien%20force/Alone%20Together.mp4",
            "http://file.ihimee.cn/videoForMobile/0/%e5%b0%91%e5%84%bf%e8%8b%b1%e8%af%ad/%e8%8b%b1%e6%96%87%e8%a7%86%e9%a2%91/%e5%8e%9f%e7%89%88%e5%8a%a8%e7%94%bb/64%20Zoo%20Lane/%e7%ac%ac%e4%b8%80%e5%ad%a3/The%20Story%20of%20Adam%20the%20Armadillo.mp4"};

    private MyVideoLayout videoLayout;
    private ListView listView;

    private ArrayList<String> list;
    private MyAdapter2 myAdapter2;

    private MyVideoController2 myVideoController2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);
        videoLayout = (MyVideoLayout) findViewById(R.id.video_player);
        listView = (ListView) findViewById(R.id.list_view);

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + "");
        }
        myAdapter2 = new MyAdapter2(list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayClick(v, (int) v.getTag());
            }
        });
        listView.setAdapter(myAdapter2);

        myVideoController2 = new MyVideoController2(videoLayout, listView);
    }

    void onPlayClick(View v, int position) {
        myVideoController2.stop();
        myVideoController2.startPlay(v, videoArray[position % 3], position);
    }


}
