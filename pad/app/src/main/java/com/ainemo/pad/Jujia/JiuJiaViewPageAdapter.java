package com.ainemo.pad.Jujia;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by victor on 17-5-3.
 */

public class JiuJiaViewPageAdapter extends FragmentPagerAdapter {
    public JiuJiaViewPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            Fragment fragment=new TemperatureFragment();
            return fragment;
        } else if (position == 1) {
            return new TemperatureFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
