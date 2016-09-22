package com.wei.mobileoffice.business;

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
import com.wei.mobileoffice.util.CacheUtils;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/27.
 */
public class FollowupAddActivity extends BaseActivity implements View.OnClickListener {

    private EditText sourceid;
    private EditText sourcetype;
    private EditText createtime;
    private EditText followuptype;
    private EditText creatorid;
    private EditText content;
    private EditText remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followup_detail);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_title)).setText("添加跟进");
        ((Button) findViewById(R.id.operation)).setText("添加");

        sourceid = (EditText) findViewById(R.id.sourceid);
        sourcetype = (EditText) findViewById(R.id.sourcetype);
        createtime = (EditText) findViewById(R.id.createtime);
        followuptype = (EditText) findViewById(R.id.followuptype);
        creatorid = (EditText) findViewById(R.id.creatorid);
        content = (EditText) findViewById(R.id.content);
        remarks = (EditText) findViewById(R.id.remarks);

        sourceid.setText(getIntent().getStringExtra("sourceid"));
        sourcetype.setText(getIntent().getStringExtra("sourcetype"));
        creatorid.setText(CacheUtils.getString(FollowupAddActivity.this, "staffid", ""));

        findViewById(R.id.ll_createtime).setVisibility(View.GONE);

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
        if (StringUtil.isBlank(content.getText().toString())) {
            UIHelper.showToastAsCenter(this, "跟进内容不能为空！");
            return;
        }
        String url = AppConstants.SERVICE_URL + "followup_create_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("sourcetype", getIntent().getStringExtra("sourcetype"))
                .addParams("sourceid", getIntent().getStringExtra("sourceid"))
                .addParams("creatorid", CacheUtils.getString(FollowupAddActivity.this, "staffid", ""))
                .addParams("followuptype", followuptype.getText().toString())
                .addParams("content", content.getText().toString())
                .addParams("followupremarks", remarks.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(FollowupAddActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(FollowupAddActivity.this, "添加成功");
                        finish();
                    }
                });
    }
}
