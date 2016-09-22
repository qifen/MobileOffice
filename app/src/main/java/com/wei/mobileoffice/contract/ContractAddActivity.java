package com.wei.mobileoffice.contract;

import android.app.ProgressDialog;
import android.content.Intent;
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
public class ContractAddActivity extends BaseActivity implements View.OnClickListener {

    private static final int CHOOSE_OP = 0;

    private EditText title;
    private EditText opportunityid;
    private EditText customerid;
    private EditText totalamount;
    private EditText contractstatus;
    private EditText contractnumber;
    private EditText contracttype;
    private EditText paymethod;
    private EditText clientcontractor;
    private EditText ourcontractor;
    private EditText remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract_detail);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("添加合同");
        ((Button) findViewById(R.id.operation)).setText("添加");

        title = (EditText) findViewById(R.id.title);
        opportunityid = (EditText) findViewById(R.id.opportunityid);
        customerid = (EditText) findViewById(R.id.customerid);
        totalamount = (EditText) findViewById(R.id.totalamount);
        contractstatus = (EditText) findViewById(R.id.contractstatus);
        contractnumber = (EditText) findViewById(R.id.contractnumber);
        contracttype = (EditText) findViewById(R.id.contracttype);
        paymethod = (EditText) findViewById(R.id.paymethod);
        clientcontractor = (EditText) findViewById(R.id.clientcontractor);
        ourcontractor = (EditText) findViewById(R.id.ourcontractor);
        remarks = (EditText) findViewById(R.id.remarks);

        customerid.setText(getIntent().getStringExtra("customerid"));

        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.operation).setOnClickListener(this);
        opportunityid.setOnClickListener(this);
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
            case R.id.opportunityid:
                Intent intent = new Intent(ContractAddActivity.this, ContractChooseOpActivity.class);
                intent.putExtra("customerid", getIntent().getStringExtra("customerid"));
                startActivityForResult(intent, CHOOSE_OP);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_OP){
            if (resultCode == RESULT_OK){
                opportunityid.setText(data.getStringExtra("opportunityid"));
            }
        }
    }

    private void add() {
        if (StringUtil.isBlank(title.getText().toString())) {
            UIHelper.showToastAsCenter(this, "合同标题不能为空！");
            return;
        }
        if (StringUtil.isBlank(opportunityid.getText().toString())) {
            UIHelper.showToastAsCenter(this, "商机编号不能为空！");
            return;
        }
        if (StringUtil.isBlank(totalamount.getText().toString())) {
            UIHelper.showToastAsCenter(this, "合同总金额不能为空！");
            return;
        }
        if (StringUtil.isBlank(contractstatus.getText().toString())) {
            UIHelper.showToastAsCenter(this, "合同状态不能为空！");
            return;
        }
        String url = AppConstants.SERVICE_URL + "contract_create_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("opportunityid", opportunityid.getText().toString())
                .addParams("customerid", customerid.getText().toString())
                .addParams("staffid", CacheUtils.getString(ContractAddActivity.this, "staffid", ""))
                .addParams("contracttitle", title.getText().toString())
                .addParams("totalamount", totalamount.getText().toString())
                .addParams("contractstatus", contractstatus.getText().toString())
                .addParams("contractnumber", contractnumber.getText().toString())
                .addParams("contracttype", contracttype.getText().toString())
                .addParams("paymethod", paymethod.getText().toString())
                .addParams("clientcontractor", clientcontractor.getText().toString())
                .addParams("ourcontractor", ourcontractor.getText().toString())
                .addParams("contractremarks", remarks.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ContractAddActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ContractAddActivity.this, "添加成功");
                        finish();
                    }
                });
    }
}
