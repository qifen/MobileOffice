package com.wei.mobileoffice.contract;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.ContractModel;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/26.
 */
public class ContractDetailActivity extends BaseActivity implements View.OnClickListener {

    private ContractModel contractModel;
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
        ((TextView) findViewById(R.id.title_bar_title)).setText("合同详情");

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

        getInfo();

        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.operation).setOnClickListener(this);
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
                        UIHelper.showToastAsCenter(ContractDetailActivity.this, "连接失败，请重试！");
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
                                UIHelper.showToastAsCenter(ContractDetailActivity.this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                            }
                        } catch (JSONException e) {
                            UIHelper.showToastAsCenter(ContractDetailActivity.this, "服务器出错，请原谅！");
                        }
                    }
                });
    }

    private void setInfo() {
//        contractModel = (ContractModel) getIntent().getSerializableExtra("contract");
        if (contractModel != null) {
            title.setText(contractModel.getContracttitle());
            opportunityid.setText(contractModel.getOpportunityid());
            customerid.setText(contractModel.getCustomerid());
            totalamount.setText(contractModel.getTotalamount());
            contractstatus.setText(contractModel.getContractstatus());
            contractnumber.setText(contractModel.getContractnumber());
            contracttype.setText(contractModel.getContracttype());
            paymethod.setText(contractModel.getPaymethod());
            clientcontractor.setText(contractModel.getClientcontractor());
            ourcontractor.setText(contractModel.getOurcontractor());
            remarks.setText(contractModel.getContractremarks());
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

    private void modify() {
        String url = AppConstants.SERVICE_URL + "contract_modify_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("contractid", contractModel.getContractid())
                .addParams("opportunityid", contractModel.getOpportunityid())
                .addParams("customerid", contractModel.getCustomerid())
                .addParams("staffid", contractModel.getStaffid())
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
                        UIHelper.showToastAsCenter(ContractDetailActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ContractDetailActivity.this, "修改成功");
                        finish();
                    }
                });
    }
}
