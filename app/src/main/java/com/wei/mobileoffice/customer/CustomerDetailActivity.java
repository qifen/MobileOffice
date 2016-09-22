package com.wei.mobileoffice.customer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.CustomerModel;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/25.
 */
public class CustomerDetailActivity extends BaseActivity implements View.OnClickListener{

    private CustomerModel customerModel;
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
        ((TextView) findViewById(R.id.title_bar_title)).setText("客户详情");

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

        getInfo();

        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.operation).setOnClickListener(this);
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
                        UIHelper.showToastAsCenter(CustomerDetailActivity.this, "连接失败，请重试！");
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
                                UIHelper.showToastAsCenter(CustomerDetailActivity.this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                            }
                        } catch (JSONException e) {
                            UIHelper.showToastAsCenter(CustomerDetailActivity.this, "服务器出错，请原谅！");
                        }
                    }
                });
    }

    private void setInfo() {
//        customerModel = (CustomerModel) getIntent().getSerializableExtra("customer");
        if (customerModel != null) {
            name.setText(customerModel.getCustomername());
            type.setText(customerModel.getCustomertype());
            status.setText(customerModel.getCustomerstatus());
            parentcustomerid.setText(customerModel.getParentcustomerid());
            telephone.setText(customerModel.getTelephone());
            email.setText(customerModel.getEmail());
            address.setText(customerModel.getAddress());
            zipcode.setText(customerModel.getZipcode());
            website.setText(customerModel.getWebsite());
            remarks.setText(customerModel.getCustomerremarks());
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
        String url = AppConstants.SERVICE_URL + "customer_modify_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("customerid", customerModel.getCustomerid())
                .addParams("staffid", customerModel.getStaffid())
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
                        UIHelper.showToastAsCenter(CustomerDetailActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(CustomerDetailActivity.this, "修改成功");
                        finish();
                    }
                });
    }
}
