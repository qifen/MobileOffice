package com.wei.mobileoffice.settings;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
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
 * Created by weiyilin on 16/6/19.
 */
public class ChangeWeixinActivity extends BaseActivity implements View.OnClickListener{

    private String weixin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_weixin);

        ((TextView) findViewById(R.id.title_bar_title)).setText("修改微信");
        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.send_weixin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.send_weixin:
                send_weixin();
                break;
        }
    }

    private void send_weixin() {
        weixin = ((EditText) findViewById(R.id.change_weixin)).getText().toString();
        if (StringUtil.isBlank(weixin)) {
            UIHelper.showToastAsCenter(this, "微信号不能为空！");
            return;
        }

        String url = AppConstants.SERVICE_URL + "staff_modify_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("staffid", CacheUtils.getString(ChangeWeixinActivity.this, "staffid", ""))
                .addParams("weixinid", weixin)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ChangeWeixinActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ChangeWeixinActivity.this, "修改成功");
                        finish();
                    }
                });
    }
}
