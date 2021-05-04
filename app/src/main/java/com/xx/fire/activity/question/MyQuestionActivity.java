package com.xx.fire.activity.question;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.xx.fire.R;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.fragment.question.QuestionFragment;


public class MyQuestionActivity extends BaseActivity {
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
        Fragment fragment = QuestionFragment.newInstance(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment).commit();
    }

    @Override
    public void doBusiness(Context context) {

    }
}
