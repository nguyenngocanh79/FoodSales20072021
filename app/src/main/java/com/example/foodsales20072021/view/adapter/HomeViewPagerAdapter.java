package com.example.foodsales20072021.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class HomeViewPagerAdapter extends FragmentStateAdapter {

    List<Fragment> mListFragment;

    public HomeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> mListFragment) {
        super(fragmentActivity);
        this.mListFragment = mListFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position){
            case 1 :
                fragment = mListFragment.get(1);
                break;
            case 2 :
                fragment = mListFragment.get(2);
                break;
            default:
                fragment = mListFragment.get(0);
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return mListFragment.size();
    }
}