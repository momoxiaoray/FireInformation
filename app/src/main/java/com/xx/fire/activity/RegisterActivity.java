package com.xx.fire.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.xx.fire.R;
import com.xx.fire.model.User;
import com.xx.fire.util.T;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tid_name)
    TextInputEditText name;
    @BindView(R.id.tid_password)
    TextInputEditText password;
    @BindView(R.id.tid_nickname)
    TextInputEditText nickName;

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected String initTitle() {
        return "用户注册";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        name.addTextChangedListener(getTextWatcher());
        nickName.addTextChangedListener(getTextWatcher());
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = name.getText().toString();
                if (StringUtils.isEmpty(nameStr)) {
                    T.showToast("请输入账号");
                    return;
                }
                String passWordStr = password.getText().toString();
                if (StringUtils.isEmpty(passWordStr)) {
                    T.showToast("请输入密码");
                    return;
                }
                String nickNameStr = nickName.getText().toString();
                if (StringUtils.isEmpty(nickNameStr)) {
                    T.showToast("请输入昵称");
                    return;
                }
                if (nickNameStr.length() > 10) {
                    T.showToast("昵称太长了");
                    return;
                }
                List<User> users = LitePal.select("account").where("account = ?", nameStr).find(User.class);
                if (users != null && users.size() > 0) {
                    T.showToast("该用户已注册，请登录");
                    return;
                }
                User user = new User();
                user.setAccount(nameStr);
                user.setPassword(passWordStr);
                user.setNickname(nickNameStr);
                user.save();
                T.showToast("注册成功");
                finish();
            }
        });
    }

    private TextWatcher getTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>10){
                    editable.delete(editable.length()-1,editable.length());
                    T.showToast("内容太长了");
                }
            }
        };
        return textWatcher;
    }


    @Override
    public void doBusiness(Context context) {

    }
}
