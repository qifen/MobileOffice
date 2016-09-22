package com.wei.mobileoffice.opportunity;

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
import com.wei.mobileoffice.model.OpportunityModel;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.platform.util.StringUtil;
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
 * Created by weiyilin on 16/6/26.
 */
public class OpportunityInfoActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private HashMap<Integer, Fragment> mFragments = new HashMap<Integer, Fragment>();
    private OpportunityPagerAdapter adapter;
    private ViewPager viewPager;
    private OpportunityModel opportunityModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opportunity_info);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("商机信息");

        // 初始化横着滚动的title
        PagerTab tabs = (PagerTab) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new OpportunityPagerAdapter(
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
                Intent intent = new Intent(OpportunityInfoActivity.this, OpportunityDetailActivity.class);
                intent.putExtra("opportunityid", opportunityModel.getOpportunityid());
                startActivity(intent);
                break;
            case R.id.add:
                Intent intent1 = new Intent(OpportunityInfoActivity.this, OpportunityChooseActivity.class);
                intent1.putExtra("opportunityid", opportunityModel.getOpportunityid());
                startActivity(intent1);
                break;
        }
    }

    private class OpportunityPagerAdapter extends FragmentStatePagerAdapter {

        private String[] opportunity_names;

        public OpportunityPagerAdapter(FragmentManager fm) {
            super(fm);
            opportunity_names = UIHelper.getStringArray(R.array.opportunity_info_names);
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
                        fragment = new OpportunityFollowupFragment();
                        break;
                    case 1:
                        fragment = new OpportunityProductFragment();
                        break;
                    case 2:
                        fragment = new OpportunityContractFragment();
                        break;
                }
                mFragments.put(position, fragment);
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                bundle.putString("opportunityid", getIntent().getStringExtra("opportunityid"));
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

    private void getInfo() {
        String url = AppConstants.SERVICE_URL + "opportunity_query_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .get()
                .url(url)
                .addParams("opportunityid", getIntent().getStringExtra("opportunityid"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(OpportunityInfoActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.optInt(PlatformConstants.RespProtocal.RPF_STATUS);
                            if (status == 0) {
                                opportunityModel = new OpportunityModel(jsonResponse.optJSONObject("0"));
                                setInfo();
                            } else {
                                UIHelper.showToastAsCenter(OpportunityInfoActivity.this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                            }
                        } catch (JSONException e) {
                            UIHelper.showToastAsCenter(OpportunityInfoActivity.this, "服务器出错，请原谅！");
                        }
                    }
                });
    }

    private void setInfo() {
//        opportunityModel = (OpportunityModel) getIntent().getSerializableExtra("opportunity");
        if (opportunityModel != null) {
            ((TextView) findViewById(R.id.name)).setText(opportunityModel.getOpportunitytitle());
            switch (opportunityModel.getBusinesstype()){
                case "2":
                    ((TextView) findViewById(R.id.type)).setText("重要商机");
                    ((TextView) findViewById(R.id.type)).setTextColor(getResources().getColor(R.color.orange));
                    break;
                case "1":
                    ((TextView) findViewById(R.id.type)).setText("一般商机");
                    ((TextView) findViewById(R.id.type)).setTextColor(getResources().getColor(R.color.purple));
                    break;
                case "0":
                    ((TextView) findViewById(R.id.type)).setText("普通商机");
                    ((TextView) findViewById(R.id.type)).setTextColor(getResources().getColor(R.color.lightblue));
                    break;
            }
            switch (opportunityModel.getOpportunitystatus()){
                case "1":
                    ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.opportunity_chubu);
                    break;
                case "2":
                    ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.opportunity_xuqiu);
                    break;
                case "3":
                    ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.opportunity_fangan);
                    break;
                case "4":
                    ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.opportunity_tanpan);
                    break;
                case "5":
                    ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.opportunity_ying);
                    break;
                case "6":
                    ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.opportunity_shu);
                    break;
            }

            if (StringUtil.isNotBlank(opportunityModel.getCustomerid())){
                ((TextView) findViewById(R.id.customer)).setText("来自客户[" + opportunityModel.getCustomerid() + "]");
            } else {
                ((TextView) findViewById(R.id.customer)).setText("来自客户[无]");
            }
        }
    }
}
