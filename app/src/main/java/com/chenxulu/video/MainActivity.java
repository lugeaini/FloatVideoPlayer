package com.chenxulu.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chenxulu.video.demo1.Demo1Activity;
import com.chenxulu.video.demo2.Demo2Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void demo1(View view) {
        startActivity(new Intent(this, Demo1Activity.class));
    }

    public void demo2(View view) {
        startActivity(new Intent(this, Demo2Activity.class));
    }
}
