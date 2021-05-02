package com.xx.fire.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.util.T;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tid_name)
    TextInputEditText name;
    @BindView(R.id.tid_password)
    TextInputEditText password;

    @BindView(R.id.tl_name)
    TextInputLayout tlName;
    @BindView(R.id.tl_password)
    TextInputLayout tlPassword;

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected String initTitle() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setStatusBar(true);
    }

    @Override
    public void doBusiness(Context context) {
        name.setFocusable(true);
        name.requestFocus();
        name.setText("123456");
        password.setText("123456");

    }


    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String account = name.getText().toString();
                String passwordStr = password.getText().toString();
                if (StringUtils.isEmpty(account)) {
                    T.showToast("请输入账号");
                    return;
                }
                if (StringUtils.isEmpty(passwordStr)) {
                    T.showToast("请输入密码");
                    return;
                }
                if (!UserUtil.isExit(account)) {
                    T.showToast("账号不存在，请注册后再登录");
                    return;
                }
                if (!UserUtil.checkPassword(account, passwordStr)) {
                    T.showToast("密码错误，请重新输入");
                    return;
                }
                UserUtil.saveCurrentUser(account);
                ActivityUtils.startActivity(MainActivity.class);
                finish();
                break;
            case R.id.btn_register:
                ActivityUtils.startActivity(RegisterActivity.class);
                break;
        }
    }
}
