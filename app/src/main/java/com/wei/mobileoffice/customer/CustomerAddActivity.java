package com.wei.mobileoffice.customer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.util.CacheUtils;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/27.
 */
public class CustomerAddActivity extends BaseActivity implements View.OnClickListener {

    private EditText name;
    private EditText type;
    private EditText status;
    private EditText parentcustomerid;
    private EditText telephone;
    private EditText email;
    private EditText address;
    private EditText zipcode;
    private EditText website;
    private EditText remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_detail);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("添加客户");
        ((Button) findViewById(R.id.operation)).setText("添加");

        name = (EditText) findViewById(R.id.name);
        type = (EditText) findViewById(R.id.type);
        status = (EditText) findViewById(R.id.status);
        parentcustomerid = (EditText) findViewById(R.id.parentcustomerid);
        telephone = (EditText) findViewById(R.id.telephone);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);
        zipcode = (EditText) findViewById(R.id.zipcode);
        website = (EditText) findViewById(R.id.website);
        remarks = (EditText) findViewById(R.id.remarks);

        findViewById(R.id.ll_parentcustomerid).setVisibility(View.GONE);

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
        String url = AppConstants.SERVICE_URL + "customer_create_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("staffid", CacheUtils.getString(CustomerAddActivity.this, "staffid", ""))
                .addParams("customername", name.getText().toString())
                .addParams("customertype", type.getText().toString())
                .addParams("customerstatus", status.getText().toString())
                .addParams("telephone", telephone.getText().toString())
                .addParams("email", email.getText().toString())
                .addParams("address", address.getText().toString())
                .addParams("zipcode", zipcode.getText().toString())
                .addParams("website", website.getText().toString())
                .addParams("customerremarks", remarks.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(CustomerAddActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(CustomerAddActivity.this, "添加成功");
                        finish();
                    }
                });
    }
}
