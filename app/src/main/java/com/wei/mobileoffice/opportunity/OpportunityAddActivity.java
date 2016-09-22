package com.wei.mobileoffice.opportunity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.platform.util.StringUtil;
import com.wei.mobileoffice.util.CacheUtils;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/27.
 */
public class OpportunityAddActivity extends BaseActivity implements View.OnClickListener{

    private EditText title;
    private EditText customer;
    private EditText estimatedamount;
    private EditText opportunitystatus;
    private EditText successrate;
    private EditText channel;
    private EditText businesstype;
    private EditText opportunitiessource;
    private EditText remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opportunity_detail);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("添加商机");
        ((Button) findViewById(R.id.operation)).setText("添加");

        title = (EditText) findViewById(R.id.title);
        customer = (EditText) findViewById(R.id.customer);
        estimatedamount = (EditText) findViewById(R.id.estimatedamount);
        opportunitystatus = (EditText) findViewById(R.id.opportunitystatus);
        successrate = (EditText) findViewById(R.id.successrate);
        channel = (EditText) findViewById(R.id.channel);
        businesstype = (EditText) findViewById(R.id.businesstype);
        opportunitiessource = (EditText) findViewById(R.id.opportunitiessource);
        remarks = (EditText) findViewById(R.id.remarks);

        findViewById(R.id.ll_customer).setVisibility(View.GONE);

        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.operation).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.operation:
                add();
                break;
        }
    }

    private void add() {
        if (StringUtil.isBlank(title.getText().toString())) {
            UIHelper.showToastAsCenter(this, "商机标题不能为空！");
            return;
        }
        if (StringUtil.isBlank(estimatedamount.getText().toString())) {
            UIHelper.showToastAsCenter(this, "预计销售金额不能为空！");
            return;
        }
        if (StringUtil.isBlank(opportunitystatus.getText().toString())) {
            UIHelper.showToastAsCenter(this, "状态不能为空！");
            return;
        }
        if (StringUtil.isBlank(businesstype.getText().toString())) {
            UIHelper.showToastAsCenter(this, "商机类型不能为空！");
            return;
        }
        String url = AppConstants.SERVICE_URL + "opportunity_create_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("customerid", getIntent().getStringExtra("customerid"))
                .addParams("staffid", CacheUtils.getString(OpportunityAddActivity.this, "staffid", ""))
                .addParams("opportunitystatus", opportunitystatus.getText().toString())
                .addParams("opportunitytitle", title.getText().toString())
                .addParams("estimatedamount", estimatedamount.getText().toString())
                .addParams("opportunitystatus", opportunitystatus.getText().toString())
                .addParams("successrate", successrate.getText().toString())
                .addParams("channel", channel.getText().toString())
                .addParams("businesstype", businesstype.getText().toString())
                .addParams("opportunitiessource", opportunitiessource.getText().toString())
                .addParams("opportunityremarks", remarks.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(OpportunityAddActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(OpportunityAddActivity.this, "添加成功");
                        finish();
                    }
                });
    }
}
