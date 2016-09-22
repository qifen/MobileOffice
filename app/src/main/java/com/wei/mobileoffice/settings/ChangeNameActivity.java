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
public class ChangeNameActivity extends BaseActivity implements View.OnClickListener{

    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_nickname);

        ((TextView) findViewById(R.id.title_bar_title)).setText("修改姓名");
        findViewById(R.id.title_bar_back).setOnClickListener(this);
        findViewById(R.id.send_nickname).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.send_nickname:
                send_nickname();
                break;
        }
    }


    private void send_nickname() {
        nickname = ((EditText) findViewById(R.id.change_nickname)).getText().toString();
        if (StringUtil.isBlank(nickname)) {
            UIHelper.showToastAsCenter(this, "姓名不能为空！");
            return;
        }

        String url = AppConstants.SERVICE_URL + "staff_modify_json";
        final ProgressDialog progressDialog = UIHelper.showProDialog(this, "", "加载中", null);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("staffid", CacheUtils.getString(ChangeNameActivity.this, "staffid", ""))
                .addParams("name", nickname)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ChangeNameActivity.this, "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        UIHelper.closeProgress(progressDialog);
                        UIHelper.showToastAsCenter(ChangeNameActivity.this, "修改成功");
                        finish();
                    }
                });
    }
}
