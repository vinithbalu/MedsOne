package com.gopher.MedsOne;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ReportFragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new PrescriptionFragment());
        adapter.addFragment(new ReminderFragment());
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        System.out.println("positoon: "+ position);
                        switch (position) {
                            case 0:
                                tab.setIcon(R.drawable.tab1);
                                break;
                            case 1:
                                tab.setIcon(R.drawable.tab2);
                                break;
                            case 2:
                                tab.setIcon(R.drawable.tab3);
                                break;
                            default:
                                tab.setIcon(R.drawable.tab1);
                        }
                    }
                }).attach();;
        // Set up a listener for when a tab is selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Do something when a tab is selected
                System.out.println("Tab selected: " + position);
                // Set the ViewPager to display the selected fragment
                viewPager.setCurrentItem(position);
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

