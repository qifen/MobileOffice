package com.wei.mobileoffice.product;

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
 * Created by weiyilin on 16/6/23.
 */
public class ProductAddActivity extends BaseActivity implements View.OnClickListener{

    private EditText name;
    private EditText productsn;
    private EditText standardprice;
    private EditText salesunit;
    private EditText type;
    private EditText introduction;
    private EditText remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("添加商品");
        ((Button) findViewById(R.id.operation)).setText("添加");

        name = (EditText) findViewById(R.id.name);
        productsn = (EditText) findViewById(R.id.productsn);
        standardprice = (EditText) findViewById(R.id.standardprice);
        salesunit = (EditText) findViewById(R.id.salesunit);
        type = (EditText) findViewById(R.id.type);
        introduction = (EditText) findViewById(R.id.introduction);
        remarks = (EditText) findViewById(R.id.remarks);

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
            UIHelper.showToastAsCenter(this, "产品名称不能为空！");
            return;
        }
        if (StringUtil.isBlank(productsn.getText().toString())) {
            UIHelper.showToastAsCenter(this, "产品编号不能为空！");
            return;
        }
        if (StringUtil.isBlank(standardprice.getText().toString())) {
            UIHelper.showToastAsCenter(this, "产品单价不能为空！");
            return;
        }
        if (StringUtil.isBlank(salesunit.getText().toString())) {
            UIHelper.showToastAsCenter(this, "销售单位不能为空！");
            return;
        }
        String url = AppConstants.SERVICE_URL + "product_create_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("productname", name.getText().toString())
                .addParams("productsn", productsn.getText().toString())
                .addParams("standardprice", standardprice.getText().toString())
                .addParams("salesunit", salesunit.getText().toString())
                .addParams("classification", type.getText().toString())
                .addParams("introduction", introduction.getText().toString())
                .addParams("productremarks", remarks.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ProductAddActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ProductAddActivity.this, "添加成功");
                        finish();
                    }
                });
    }
}
