package com.bzh.helper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 20;
            }

            @Override
            public Fragment getItem(int position) {
                return MyFragment.newInstance(position);
            }
        };
        mViewPager.setAdapter(adapter);
        ViewPagerHelper viewPagerHelper = new ViewPagerHelper();
        viewPagerHelper.setViewPager(mViewPager, adapter);
        viewPagerHelper.addListener(new ViewPagerHelper.IViewPagerTrendListener() {
            @Override
            public void onFullPageSelected(int selectedPosition) {
                Log.d(TAG, "onFullPageSelected() called with: selectedPosition = [" + selectedPosition + "]");
            }

            @Override
            public void onDirectSelected(int direct, int selectedPosition, int nextPosition) {
                Log.d(TAG, "onDirectSelected() called with: direct = [" + direct + "], selectedPosition = [" + selectedPosition + "], nextPosition = [" + nextPosition + "]");
            }

            @Override
            public void onPrePageSelected(int position) {
                Log.d(TAG, "onPrePageSelected() called with: position = [" + position + "]");
            }

            @Override
            public void onFractionPage(int direct, int selectedPosition, int nextPosition, float positionOffset) {
                Log.d(TAG, "onFractionPage() called with: direct = [" + direct + "], selectedPosition = [" + selectedPosition + "], nextPosition = [" + nextPosition + "], positionOffset = [" + positionOffset + "]");
            }
        });
    }


    public static class MyFragment extends Fragment {
        public static MyFragment newInstance(int position) {

            Bundle args = new Bundle();
            args.putInt("KEY", position);
            MyFragment fragment = new MyFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.my_fragment, container, false);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText("biezhihua " + getArguments().getInt("KEY"));
            return view;
        }
    }

}