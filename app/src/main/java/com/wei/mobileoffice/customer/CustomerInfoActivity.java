package com.wei.mobileoffice.customer;

import android.app.ProgressDialog;
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
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.CustomerModel;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.ui.CircleImageView;
import com.wei.mobileoffice.ui.PagerTab;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/23.
 */
public class CustomerInfoActivity extends BaseFragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private HashMap<Integer, Fragment> mFragments = new HashMap<Integer, Fragment>();
    private CustomerPagerAdapter adapter;
    private ViewPager viewPager;
    private CustomerModel customerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_info);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("客户信息");

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
        findViewById(R.id.detail).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);

        getInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.detail:
                Intent intent = new Intent(CustomerInfoActivity.this, CustomerDetailActivity.class);
                intent.putExtra("customerid", customerModel.getCustomerid());
                startActivity(intent);
                break;
            case R.id.add:
                Intent intent1 = new Intent(CustomerInfoActivity.this, CustomerChooseActivity.class);
                intent1.putExtra("customerid", customerModel.getCustomerid());
                startActivity(intent1);
                break;
        }
    }

    private class CustomerPagerAdapter extends FragmentStatePagerAdapter {

        private String[] customer_names;

        public CustomerPagerAdapter(FragmentManager fm) {
            super(fm);
            customer_names = UIHelper.getStringArray(R.array.customer_info_names);
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
                        fragment = new CustomerFollowupFragment();
                        break;
                    case 1:
                        fragment = new CustomerContactsFragment();
                        break;
                    case 2:
                        fragment = new CustomerOpportunityFragment();
                        break;
                    case 3:
                        fragment = new CustomerContractFragment();
                        break;
                }
                mFragments.put(position, fragment);
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                bundle.putString("customerid", getIntent().getStringExtra("customerid"));
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

    private void getInfo() {
        String url = AppConstants.SERVICE_URL + "customer_query_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .get()
                .url(url)
                .addParams("customerid", getIntent().getStringExtra("customerid"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(CustomerInfoActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.optInt(PlatformConstants.RespProtocal.RPF_STATUS);
                            if (status == 0) {
                                customerModel = new CustomerModel(jsonResponse.optJSONObject("0"));
                                setInfo();
                            } else {
                                UIHelper.showToastAsCenter(CustomerInfoActivity.this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                            }
                        } catch (JSONException e) {
                            UIHelper.showToastAsCenter(CustomerInfoActivity.this, "服务器出错，请原谅！");
                        }
                    }
                });
    }

    private void setInfo() {
        if (customerModel != null) {
            UIHelper.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) findViewById(R.id.name)).setText(customerModel.getCustomername());
                    switch (customerModel.getCustomertype()){
                        case "1":
                            ((TextView) findViewById(R.id.type)).setText("重要客户");
                            ((TextView) findViewById(R.id.type)).setTextColor(getResources().getColor(R.color.orange));
                            break;
                        case "2":
                            ((TextView) findViewById(R.id.type)).setText("一般客户");
                            ((TextView) findViewById(R.id.type)).setTextColor(getResources().getColor(R.color.purple));
                            break;
                        case "3":
                            ((TextView) findViewById(R.id.type)).setText("低价值客户");
                            ((TextView) findViewById(R.id.type)).setTextColor(getResources().getColor(R.color.lightblue));
                            break;
                    }
                    switch (customerModel.getCustomerstatus()){
                        case "1":
                            ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.customer_chufang);
                            break;
                        case "2":
                            ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.customer_yixiang);
                            break;
                        case "3":
                            ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.customer_baojia);
                            break;
                        case "4":
                            ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.customer_chengjiao);
                            break;
                        case "5":
                            ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.customer_gezhi);
                            break;
                    }
                }
            });
        }
    }
}
