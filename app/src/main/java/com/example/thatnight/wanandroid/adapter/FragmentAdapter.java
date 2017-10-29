package com.example.thatnight.wanandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.thatnight.wanandroid.base.BaseFragment;

import java.util.List;

/**
 * Created by thatnight on 2017.10.27.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mFragments;
    private List<String> mTitle;

    public FragmentAdapter(FragmentManager fm, List<BaseFragment> fragments, List<String> title) {
        super(fm);
        mFragments = fragments;
        mTitle = title;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }
}
