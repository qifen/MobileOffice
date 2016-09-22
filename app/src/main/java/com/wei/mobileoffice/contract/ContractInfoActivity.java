package com.wei.mobileoffice.contract;

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
import com.wei.mobileoffice.business.FollowupAddActivity;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.ContractModel;
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
 * Created by weiyilin on 16/6/29.
 */
public class ContractInfoActivity extends BaseFragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{

    private HashMap<Integer, Fragment> mFragments = new HashMap<Integer, Fragment>();
    private ContractPagerAdapter adapter;
    private ViewPager viewPager;
    private ContractModel contractModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract_info);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("合同信息");

        // 初始化横着滚动的title
        PagerTab tabs = (PagerTab) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ContractPagerAdapter(
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
                Intent intent = new Intent(ContractInfoActivity.this, ContractDetailActivity.class);
                intent.putExtra("contractid", contractModel.getContractid());
                startActivity(intent);
                break;
            case R.id.add:
                Intent intent1 = new Intent(ContractInfoActivity.this, FollowupAddActivity.class);
                intent1.putExtra("sourceid", getIntent().getStringExtra("contractid"));
                intent1.putExtra("sourcetype", "3");
                startActivity(intent1);
                break;
        }
    }

    private class ContractPagerAdapter extends FragmentStatePagerAdapter {

        private String[] contract_names;

        public ContractPagerAdapter(FragmentManager fm) {
            super(fm);
            contract_names = UIHelper.getStringArray(R.array.contract_info_names);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return contract_names[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragments.get(position);

            if (fragment == null) {
                switch (position) {
                    case 0:
                        fragment = new ContractFollowupFragment();
                        break;
                }
                mFragments.put(position, fragment);
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                bundle.putString("contractid", getIntent().getStringExtra("contractid"));
                fragment.setArguments(bundle);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return contract_names.length;
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
        String url = AppConstants.SERVICE_URL + "contract_query_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .get()
                .url(url)
                .addParams("contractid", getIntent().getStringExtra("contractid"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ContractInfoActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.optInt(PlatformConstants.RespProtocal.RPF_STATUS);
                            if (status == 0) {
                                contractModel = new ContractModel(jsonResponse.optJSONObject("0"));
                                setInfo();
                            } else {
                                UIHelper.showToastAsCenter(ContractInfoActivity.this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                            }
                        } catch (JSONException e) {
                            UIHelper.showToastAsCenter(ContractInfoActivity.this, "服务器出错，请原谅！");
                        }
                    }
                });
    }

    private void setInfo() {
        if (contractModel != null) {
            UIHelper.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) findViewById(R.id.name)).setText(contractModel.getContracttitle());
                    switch (contractModel.getContractstatus()){
                        case "1":
                            ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.contract_weikaishi);
                            break;
                        case "2":
                            ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.contract_zhixingz);
                            break;
                        case "3":
                            ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.contract_chenggong);
                            break;
                        case "4":
                            ((CircleImageView) findViewById(R.id.head_icon)).setImageResource(R.drawable.contract_yiwaizhongzhi);
                            break;
                    }
                    if (StringUtil.isNotBlank(contractModel.getCustomerid())){
                        ((TextView) findViewById(R.id.customer)).setText("来自客户[" + contractModel.getCustomerid() + "]");
                    } else {
                        ((TextView) findViewById(R.id.customer)).setText("来自客户[无]");
                    }
                }
            });
        }
    }
}
