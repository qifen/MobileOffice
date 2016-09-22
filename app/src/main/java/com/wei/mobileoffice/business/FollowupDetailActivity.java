package com.wei.mobileoffice.business;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.FollowupModel;
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
public class FollowupDetailActivity extends BaseActivity implements View.OnClickListener {

    private FollowupModel followupModel;
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
        ((TextView) findViewById(R.id.title_bar_title)).setText("跟进详情");

        sourceid = (EditText) findViewById(R.id.sourceid);
        sourcetype = (EditText) findViewById(R.id.sourcetype);
        createtime = (EditText) findViewById(R.id.createtime);
        followuptype = (EditText) findViewById(R.id.followuptype);
        creatorid = (EditText) findViewById(R.id.creatorid);
        content = (EditText) findViewById(R.id.content);
        remarks = (EditText) findViewById(R.id.remarks);

        getInfo();

        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.operation).setOnClickListener(this);
    }

    private void getInfo() {
        String url = AppConstants.SERVICE_URL + "followup_query_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .get()
                .url(url)
                .addParams("followupid", getIntent().getStringExtra("followupid"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(FollowupDetailActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.optInt(PlatformConstants.RespProtocal.RPF_STATUS);
                            if (status == 0) {
                                followupModel = new FollowupModel(jsonResponse.optJSONObject("0"));
                                setInfo();
                            } else {
                                UIHelper.showToastAsCenter(FollowupDetailActivity.this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                            }
                        } catch (JSONException e) {
                            UIHelper.showToastAsCenter(FollowupDetailActivity.this, "服务器出错，请原谅！");
                        }
                    }
                });
    }

    private void setInfo() {
        if (followupModel != null) {
            sourceid.setText(followupModel.getSourceid());
            sourcetype.setText(followupModel.getSourcetype());
            createtime.setText(followupModel.getCreatetime());
            followuptype.setText(followupModel.getFollowuptype());
            creatorid.setText(followupModel.getCreatorid());
            content.setText(followupModel.getContent());
            remarks.setText(followupModel.getFollowupremarks());
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
        String url = AppConstants.SERVICE_URL + "followup_modify_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("followupid", followupModel.getFollowupid())
                .addParams("creatorid", creatorid.getText().toString())
                .addParams("followuptype", followuptype.getText().toString())
                .addParams("content", content.getText().toString())
                .addParams("followupremarks", remarks.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(FollowupDetailActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(FollowupDetailActivity.this, "修改成功");
                        finish();
                    }
                });
    }
}
