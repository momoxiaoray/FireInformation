package com.xx.fire.activity.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ActivityUtils;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.activity.LoginActivity;
import com.xx.fire.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserFragment extends Fragment {

    private UserViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        TextView userName = root.findViewById(R.id.user_name);
        notificationsViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userName.setText(user.getNickName());
            }
        });
        ButterKnife.bind(root);
        return root;
    }


    @OnClick({R.id.collection, R.id.about,R.id.btn_login_out})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection:

                break;
            case R.id.about:

                break;
            case R.id.btn_login_out:
                UserUtil.exit();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ActivityUtils.finishAllActivities();
                break;
        }
    }
}