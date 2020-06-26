package com.rch.etawah.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rch.etawah.fragments.Deals_Fragment;
import com.rch.etawah.fragments.Recent_Details_Fragment;
import com.rch.etawah.fragments.Top_Deals_Fragment;
import com.rch.etawah.fragments.Whats_New_Fragment;

public class PageAdapter extends FragmentPagerAdapter {
    private int numsoftabs;

    public PageAdapter(FragmentManager fm, int numsoftabs) {
        super(fm);
        this.numsoftabs = numsoftabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new Whats_New_Fragment();
            case 1:
                return new Recent_Details_Fragment();
            case 2:
                return new Deals_Fragment();
            case 3:
                return new Top_Deals_Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numsoftabs;
    }
}
