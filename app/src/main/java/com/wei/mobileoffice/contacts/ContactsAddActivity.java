package com.wei.mobileoffice.contacts;

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
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/27.
 */
public class ContactsAddActivity extends BaseActivity implements View.OnClickListener {

    private EditText name;
    private EditText mobile;
    private EditText telephone;
    private EditText customer;
    private EditText age;
    private EditText gender;
    private EditText email;
    private EditText address;
    private EditText zipcode;
    private EditText qq;
    private EditText wechat;
    private EditText remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_detail);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("添加联系人");
        ((Button) findViewById(R.id.operation)).setText("添加");

        name = (EditText) findViewById(R.id.name);
        mobile = (EditText) findViewById(R.id.mobile);
        telephone = (EditText) findViewById(R.id.telephone);
        customer = (EditText) findViewById(R.id.customer);
        age = (EditText) findViewById(R.id.age);
        gender = (EditText) findViewById(R.id.gender);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);
        zipcode = (EditText) findViewById(R.id.zipcode);
        qq = (EditText) findViewById(R.id.qq);
        wechat = (EditText) findViewById(R.id.wechat);
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
        if (StringUtil.isBlank(name.getText().toString())) {
            UIHelper.showToastAsCenter(this, "姓名不能为空！");
            return;
        }
        if (StringUtil.isBlank(age.getText().toString())) {
            UIHelper.showToastAsCenter(this, "年龄不能为空！");
            return;
        }
        if (StringUtil.isBlank(gender.getText().toString())) {
            UIHelper.showToastAsCenter(this, "性别不能为空！");
            return;
        }
        String url = AppConstants.SERVICE_URL + "contact_create_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("customerid", getIntent().getStringExtra("customerid"))
                .addParams("contactsname", name.getText().toString())
                .addParams("contactsmobile", mobile.getText().toString())
                .addParams("contactstelephone", telephone.getText().toString())
                .addParams("contactsage", age.getText().toString())
                .addParams("contactsgender", gender.getText().toString())
                .addParams("contactsemail", email.getText().toString())
                .addParams("contactsaddress", address.getText().toString())
                .addParams("contactszipcode", zipcode.getText().toString())
                .addParams("contactsqq", qq.getText().toString())
                .addParams("contactswechat", wechat.getText().toString())
                .addParams("contactsremarks", remarks.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ContactsAddActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ContactsAddActivity.this, "添加成功");
                        finish();
                    }
                });
    }
}
