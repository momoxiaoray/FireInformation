package com.xx.fire.activity.dynamic;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.xx.fire.R;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.fragment.dynamic.DynamicFragment;
import com.xx.fire.fragment.question.QuestionFragment;


public class MyDynamicActivity extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_question_list;
    }

    @Override
    protected String initTitle() {
        return "我的问答";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Fragment fragment = DynamicFragment.newInstance(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment).commit();
    }

    @Override
    public void doBusiness(Context context) {

    }
}
