package com.ijunes.laundrytimer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ijunes.laundrytimer.fragment.CycleFragment;
import com.ijunes.laundrytimer.fragment.MachineFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a MachineFragment (defined as a static inner class below).
        switch(position){
            case 0:
                return CycleFragment.newInstance(0);
            case 1:
                return MachineFragment.newInstance(position + 1);
            default:
                return CycleFragment.newInstance(0);
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Current";
            case 1:
                return "History";
            case 2:
                return "Machines";
        }
        return null;
    }
}