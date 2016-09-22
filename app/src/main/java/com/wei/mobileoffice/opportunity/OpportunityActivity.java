package com.wei.mobileoffice.opportunity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wei.mobileoffice.BaseFragment;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.ui.PagerTab;
import com.wei.mobileoffice.util.UIHelper;

import java.util.HashMap;

/**
 * Created by weiyilin on 16/5/9.
 */
public class OpportunityActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private HashMap<Integer, Fragment> mFragments = new HashMap<Integer, Fragment>();
    private VideoPagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opportunity_activity);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("商机");
        // 初始化横着滚动的title
        PagerTab tabs = (PagerTab) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new VideoPagerAdapter(
                getSupportFragmentManager());
        // 设置数据
        viewPager.setAdapter(adapter);
        //绑定viewpager和横着滚动的title
        tabs.setViewPager(viewPager);
        //设置左右滑动的监听
        tabs.setOnPageChangeListener(this);

        findViewById(R.id.title_bar_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    private class VideoPagerAdapter extends FragmentStatePagerAdapter {

        private String[] opportunity_names;

        public VideoPagerAdapter(FragmentManager fm) {
            super(fm);
            opportunity_names = UIHelper.getStringArray(R.array.opportunity_names);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return opportunity_names[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragments.get(position);

            if (fragment == null) {
                switch (position) {
                    case 0:
                        fragment = new OpportunityFragment();
                        break;
                    default:
                        fragment = new OpportunityFragment();
                        break;

                }
                mFragments.put(position, fragment);
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                fragment.setArguments(bundle);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return opportunity_names.length;
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        BaseFragment fragment = (BaseFragment) adapter.getItem(position);
        fragment.load();
    }
}
