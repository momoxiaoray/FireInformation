package com.xx.fire.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xx.fire.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] tabs ;
    private List<ItemFragment> itemFragments =new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tabs = new String[]{getString(R.string.tab_text_1), getString(R.string.tab_text_2)};
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager = root.findViewById(R.id.viewpager);

        initView();
        return root;
    }


    private void initView(){
        for (int i = 0; i < 2; i++) {
            itemFragments.add(ItemFragment.newInstance(i));
        }
        viewPager.setAdapter(new FragmentStatePagerAdapter(getParentFragmentManager(),
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
        //??????TabLayout???ViewPager??????
        tabLayout.setupWithViewPager(viewPager,false);
    }
}