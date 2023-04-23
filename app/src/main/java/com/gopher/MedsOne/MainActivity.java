package com.gopher.MedsOne;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createTabLayout();
    }

    private void createTabLayout() {
        // Initialize the TabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        // Create a list of icon resource IDs
        List<Integer> tabIcons = new ArrayList<>();
        tabIcons.add(R.drawable.tab1);
        tabIcons.add(R.drawable.tab2);
        tabIcons.add(R.drawable.tab3);

        // Loop through the tab items and add them to the TabLayout
        for (int i = 0; i < tabIcons.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setIcon(tabIcons.get(i));
            tabLayout.addTab(tab);
        }

        // Set up a listener for when a tab is selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Do something when a tab is selected
                System.out.println("Tab selected: " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do something when a tab is unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do something when a tab is reselected
            }
        });
    }
}