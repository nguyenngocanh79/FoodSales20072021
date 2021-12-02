package com.example.foodsales20072021.utils;

import com.example.foodsales20072021.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavBadge {
    public static void updateBadge(BottomNavigationView bottomNavigationView,
                                   int menuItemId, int badgeNumber){
        if(badgeNumber>0){
            BadgeDrawable navBadge = bottomNavigationView.getOrCreateBadge(menuItemId);
            navBadge.setVisible(true);
            navBadge.setNumber(badgeNumber);
        } else {
            BadgeDrawable navBadge = bottomNavigationView.getBadge(menuItemId);
            if(navBadge!=null){
                navBadge.setVisible(false);
                navBadge.clearNumber();
            }
        }
    }
}
