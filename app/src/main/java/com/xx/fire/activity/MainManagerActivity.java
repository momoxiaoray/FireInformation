package com.xx.fire.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.xx.fire.R;
import com.xx.fire.activity.BaseActivity;

public class MainManagerActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_main_manager;
    }

    @Override
    protected String initTitle() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void doBusiness(Context context) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}