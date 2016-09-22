package com.wei.mobileoffice.product;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.ProductModel;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/23.
 */
public class ProductDetailActivity extends BaseActivity implements View.OnClickListener{

    private ProductModel productModel;
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
        ((TextView) findViewById(R.id.title_bar_title)).setText("商品详情");

        name = (EditText) findViewById(R.id.name);
        productsn = (EditText) findViewById(R.id.productsn);
        standardprice = (EditText) findViewById(R.id.standardprice);
        salesunit = (EditText) findViewById(R.id.salesunit);
        type = (EditText) findViewById(R.id.type);
        introduction = (EditText) findViewById(R.id.introduction);
        remarks = (EditText) findViewById(R.id.remarks);

        getInfo();

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

    private void getInfo() {
        String url = AppConstants.SERVICE_URL + "product_query_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .get()
                .url(url)
                .addParams("productid", getIntent().getStringExtra("productid"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ProductDetailActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.optInt(PlatformConstants.RespProtocal.RPF_STATUS);
                            if (status == 0) {
                                productModel = new ProductModel(jsonResponse.optJSONObject("0"));
                                setInfo();
                            } else {
                                UIHelper.showToastAsCenter(ProductDetailActivity.this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                            }
                        } catch (JSONException e) {
                            UIHelper.showToastAsCenter(ProductDetailActivity.this, "服务器出错，请原谅！");
                        }
                    }
                });
    }

    private void setInfo() {
        if (productModel != null) {
            UIHelper.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    name.setText(productModel.getProductname());
                    productsn.setText(productModel.getProductsn());
                    standardprice.setText(productModel.getStandardprice());
                    salesunit.setText(productModel.getSalesunit());
                    type.setText(productModel.getClassification());
                    introduction.setText(productModel.getIntroduction());
                    remarks.setText(productModel.getIntroduction());
                }
            });
        }
    }

    private void modify() {
        String url = AppConstants.SERVICE_URL + "product_modify_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("productid", getIntent().getStringExtra("productid"))
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
                        UIHelper.showToastAsCenter(ProductDetailActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ProductDetailActivity.this, "修改成功");
                        finish();
                    }
                });
    }
}
