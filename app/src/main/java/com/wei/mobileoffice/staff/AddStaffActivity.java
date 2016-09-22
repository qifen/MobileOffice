package com.wei.mobileoffice.staff;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.platform.util.StringUtil;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/19.
 */
public class AddStaffActivity extends BaseActivity implements View.OnClickListener {

    private String name;
    private String address;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_staff);

        ((TextView) findViewById(R.id.title_bar_title)).setText("新建员工");
        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.add_staff).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.add_staff:
                add_staff();
                break;
        }
    }

    private void add_staff() {
        name = ((EditText) findViewById(R.id.staff_name)).getText().toString();
        address = ((EditText) findViewById(R.id.staff_address)).getText().toString();
        phone = ((EditText) findViewById(R.id.staff_phone)).getText().toString();
        if (StringUtil.isBlank(name)) {
            UIHelper.showToastAsCenter(this, "姓名不能为空！");
            return;
        }

        String url = AppConstants.SERVICE_URL + "staff_create_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("name", name)
                .addParams("position", address)
                .addParams("mobile", phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(AddStaffActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        doSuccess(response);
                    }
                });
    }


    private void doSuccess(String result) {
        try {
            JSONObject jsonResponse = new JSONObject(result);
            int status = jsonResponse.optInt(PlatformConstants.RespProtocal.RPF_STATUS);
            if (status == 0) {
                UIHelper.showToastAsCenter(this, "新建成功");
                finish();
            } else {
                UIHelper.showToastAsCenter(this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
            }
        } catch (JSONException e) {
            UIHelper.showToastAsCenter(this, "服务器出错，请原谅！");
        }
    }
}
