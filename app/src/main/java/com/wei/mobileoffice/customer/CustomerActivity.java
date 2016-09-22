package com.wei.mobileoffice.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wei.mobileoffice.BaseFragment;
import com.wei.mobileoffice.BaseFragmentActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.ui.PagerTab;
import com.wei.mobileoffice.util.UIHelper;

import java.util.HashMap;

/**
 * Created by weiyilin on 16/5/9.
 */
public class CustomerActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private HashMap<Integer, Fragment> mFragments = new HashMap<Integer, Fragment>();
    private CustomerPagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("客户");
        // 初始化横着滚动的title
        PagerTab tabs = (PagerTab) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new CustomerPagerAdapter(
                getSupportFragmentManager());
        // 设置数据
        viewPager.setAdapter(adapter);
        //绑定viewpager和横着滚动的title
        tabs.setViewPager(viewPager);
        //设置左右滑动的监听
        tabs.setOnPageChangeListener(this);

        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.add:
                startActivity(new Intent(CustomerActivity.this, CustomerAddActivity.class));
                break;
        }
    }

    private class CustomerPagerAdapter extends FragmentStatePagerAdapter {

        private String[] customer_names;

        public CustomerPagerAdapter(FragmentManager fm) {
            super(fm);
            customer_names = UIHelper.getStringArray(R.array.customer_names);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return customer_names[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragments.get(position);

            if (fragment == null) {
                switch (position) {
                    case 0:
                        fragment = new CustomerFragment();
                        break;
                    default:
                        fragment = new CustomerFragment();
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
            return customer_names.length;
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
