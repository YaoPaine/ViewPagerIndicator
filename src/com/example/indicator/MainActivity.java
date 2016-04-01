package com.example.indicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class MainActivity extends FragmentActivity {

	private ViewPagerIndicator mIndicator;
	private ViewPager mViewPager;
	private List<String> mTitle = Arrays.asList(Cheeses.NAMES);
	// private List<String> mTitle = Arrays.asList("短信1", "收藏1", "推荐1", "短信2",
	// "收藏2", "推荐2", "短信1", "收藏1", "推荐1");
	private List<VpFragment> mContents = new ArrayList<VpFragment>();
	private FragmentPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main2);

		initViews();
		initData();
	}

	@SuppressLint("NewApi")
	private void initData() {
		for (String title : mTitle) {
			VpFragment vpFragment = VpFragment.newInstance(title);
			mContents.add(vpFragment);
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mContents.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mContents.get(position);
			}
		};
		mIndicator.setVisibleCount(3);
		mIndicator.setTabItemTitles(mTitle);
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager, 5);
	}

	private void initViews() {
		mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
	}
}
