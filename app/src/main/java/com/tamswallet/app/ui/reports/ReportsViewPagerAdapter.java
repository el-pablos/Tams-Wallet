package com.tamswallet.app.ui.reports;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReportsViewPagerAdapter extends FragmentStateAdapter {
    
    private static final int NUM_PAGES = 3;
    
    public ReportsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MonthlyReportFragment();
            case 1:
                return new CategoryReportFragment();
            case 2:
                return new TrendsReportFragment();
            default:
                return new MonthlyReportFragment();
        }
    }
    
    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}