package com.wei.mobileoffice.opportunity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.OpportunityModel;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.platform.util.StringUtil;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/26.
 */
public class OpportunityDetailActivity extends BaseActivity implements View.OnClickListener {

    private OpportunityModel opportunityModel;
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
        ((TextView) findViewById(R.id.title_bar_title)).setText("商机详情");

        title = (EditText) findViewById(R.id.title);
        customer = (EditText) findViewById(R.id.customer);
        estimatedamount = (EditText) findViewById(R.id.estimatedamount);
        opportunitystatus = (EditText) findViewById(R.id.opportunitystatus);
        successrate = (EditText) findViewById(R.id.successrate);
        channel = (EditText) findViewById(R.id.channel);
        businesstype = (EditText) findViewById(R.id.businesstype);
        opportunitiessource = (EditText) findViewById(R.id.opportunitiessource);
        remarks = (EditText) findViewById(R.id.remarks);

        getInfo();

        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.operation).setOnClickListener(this);
    }

    private void setInfo() {
//        opportunityModel = (OpportunityModel) getIntent().getSerializableExtra("opportunity");
        if (opportunityModel != null) {
            title.setText(opportunityModel.getOpportunitytitle());
            estimatedamount.setText(opportunityModel.getEstimatedamount());
            opportunitystatus.setText(opportunityModel.getOpportunitystatus());
            successrate.setText(opportunityModel.getSuccessrate());
            channel.setText(opportunityModel.getChannel());
            businesstype.setText(opportunityModel.getBusinesstype());
            opportunitiessource.setText(opportunityModel.getOpportunitiessource());
            remarks.setText(opportunityModel.getOpportunityremarks());
            if (StringUtil.isNotBlank(opportunityModel.getCustomerid())){
                customer.setText(opportunityModel.getCustomerid());
            } else {
                customer.setText("无");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.operation:
                modify();
                break;
        }
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
                        UIHelper.showToastAsCenter(OpportunityDetailActivity.this, "连接失败，请重试！");
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
                                UIHelper.showToastAsCenter(OpportunityDetailActivity.this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                            }
                        } catch (JSONException e) {
                            UIHelper.showToastAsCenter(OpportunityDetailActivity.this, "服务器出错，请原谅！");
                        }
                    }
                });
    }

    private void modify() {
        String url = AppConstants.SERVICE_URL + "opportunity_modify_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("opportunityid", opportunityModel.getOpportunityid())
                .addParams("customerid", opportunityModel.getCustomerid())
                .addParams("staffid", opportunityModel.getStaffid())
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
                        UIHelper.showToastAsCenter(OpportunityDetailActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(OpportunityDetailActivity.this, "修改成功");
                        finish();
                    }
                });
    }

}
