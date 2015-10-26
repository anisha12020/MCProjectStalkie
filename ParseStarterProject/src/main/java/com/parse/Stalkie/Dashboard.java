package com.parse.Stalkie;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    private Toolbar toolbar;
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

//    ViewPager pager;
//    ViewPagerAdapter adapter;
//    SlidingTabLayout tabs;
//    CharSequence Titles[]={"Notifications","Meetings"};
//    int Numboftabs =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //ActionBar actionBar = getSupportActionBar();
//        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
//
//        // Assigning ViewPager View and setting the adapter
//        pager = (ViewPager) findViewById(R.id.pager);
//        pager.setAdapter(adapter);
//
//        // Assiging the Sliding Tab Layout View
//        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
//        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
//
//        // Setting Custom Color for the Scroll bar indicator of the Tab View
//        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
//            @Override
//            public int getIndicatorColor(int position) {
//                return getResources().getColor(R.color.tabsScrollColor);
//            }
//        });
//
//        // Setting the ViewPager For the SlidingTabsLayout
//        tabs.setViewPager(pager);
//
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Settings icon",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_add) {
//            Toast.makeText(getApplicationContext(), "Add icon",
//                    Toast.LENGTH_SHORT).show();
            supportInvalidateOptionsMenu();

            Intent intent = new Intent(Dashboard.this, CreateMeetingActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
//        menu.removeItem(R.id.action_add);
//        menu.removeItem(R.id.action_settings);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if(i == 0) // if the position is 0 we are returning the First tab
            {
                Notifications tab1 = new Notifications();
                return tab1;
            }
            else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {

                Meetings tab2 = new Meetings();
                return tab2;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.title_section1).toUpperCase();
                case 1: return getString(R.string.title_section2).toUpperCase();

            }
            return null;
        }
    }
//
//    /**
//     * A dummy fragment representing a section of the app, but that simply displays dummy text.
//     */
//    public static class DummySectionFragment extends Fragment {
//        public DummySectionFragment() {
//        }
//
//        public static final String ARG_SECTION_NUMBER = "section_number";
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.fragment_notifications, container, false);
//        }
//    }
}


