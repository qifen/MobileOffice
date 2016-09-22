package com.wei.mobileoffice.contacts;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.ContactsModel;
import com.wei.mobileoffice.platform.util.StringUtil;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/23.
 */
public class ContactsDetailActivity extends BaseActivity implements View.OnClickListener{

    private ContactsModel contactsModel;
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
        ((TextView) findViewById(R.id.title_bar_title)).setText("联系人详情");

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

        setInfo();

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
                modify();
                break;
        }
    }

    private void setInfo() {
        contactsModel = (ContactsModel) getIntent().getSerializableExtra("contacts");
        if (contactsModel != null) {
            UIHelper.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    name.setText(contactsModel.getContactsname());
                    mobile.setText(contactsModel.getContactsmobile());
                    telephone.setText(contactsModel.getContactstelephone());
                    age.setText(contactsModel.getContactsage());
                    gender.setText(contactsModel.getContactsgender());
                    email.setText(contactsModel.getContactsemail());
                    address.setText(contactsModel.getContactsaddress());
                    zipcode.setText(contactsModel.getContactszipcode());
                    qq.setText(contactsModel.getContactsqq());
                    wechat.setText(contactsModel.getContactswechat());
                    remarks.setText(contactsModel.getContactsremarks());
                    if (StringUtil.isNotBlank(contactsModel.getCustomerid())){
                        customer.setText(contactsModel.getCustomerid());
                    } else {
                        customer.setText("无");
                    }
                }
            });
        }
    }

    private void modify() {
        String url = AppConstants.SERVICE_URL + "contact_modify_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("contactsid", contactsModel.getContactsid())
                .addParams("customerid", contactsModel.getCustomerid())
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
                        UIHelper.showToastAsCenter(ContactsDetailActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ContactsDetailActivity.this, "修改成功");
                        finish();
                    }
                });
    }
}
