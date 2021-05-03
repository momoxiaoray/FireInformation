package com.xx.fire.activity.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.activity.LoginActivity;
import com.xx.fire.fragment.manager.ItemMangerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ManagerMainActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private String[] tabs;
    private List<ItemMangerFragment> itemFragments = new ArrayList<>();
    private long exitTime;

    @Override
    public int bindLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected String initTitle() {
        return "管理员首页";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleHelper.hideLeftButton();
        tabs = new String[]{getString(R.string.tab_text_1), getString(R.string.tab_text_2)};
        mTitleHelper.setOnRightTxClickListener("退出", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUtil.exit();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void doBusiness(Context context) {

        for (int i = 0; i < 2; i++) {
            itemFragments.add(ItemMangerFragment.newInstance(i));
        }
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return itemFragments.get(position);
            }

            @Override
            public int getCount() {
                return itemFragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabs[position];
            }
        });
        //设置TabLayout和ViewPager联动
        tabLayout.setupWithViewPager(viewPager, false);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() < 2000 + exitTime) {
            ActivityUtils.finishAllActivities();
            finish();
        } else {
            exitTime = System.currentTimeMillis();
            ToastUtils.showShort("再按一次退出程序");
        }
    }
}