package com.appublisher.huaxiaotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Test1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        ActivityManager.instance().registActivity(this);
    }
}
