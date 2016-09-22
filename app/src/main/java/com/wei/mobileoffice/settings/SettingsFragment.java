package com.wei.mobileoffice.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wei.mobileoffice.BaseFragment;
import com.wei.mobileoffice.OfficeAppContext;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.StaffModel;
import com.wei.mobileoffice.platform.StringUtil;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.staff.StaffActivity;
import com.wei.mobileoffice.ui.CircleImageView;
import com.wei.mobileoffice.util.CacheUtils;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/5/9.
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    private StaffModel staffModel;


    private TextView userId;
    private TextView nickTitle;
    private TextView nickName;
    private TextView department;
    private TextView address;
    private TextView phone;
    private TextView weixin;

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.settings_fragment, container ,false);

        initView();
        initData();

        return fragmentView;
    }

    @Override
    protected void doActivityCreated(Bundle savedInstanceState) {
        this.createPage(this.getClass().getSimpleName().toLowerCase(Locale.getDefault()));
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        userId = (TextView) fragmentView.findViewById(R.id.userId);
        nickTitle = (TextView) fragmentView.findViewById(R.id.nick_title);
        nickName = (TextView) fragmentView.findViewById(R.id.nickname);
        department = (TextView) fragmentView.findViewById(R.id.department);
        address = (TextView) fragmentView.findViewById(R.id.address);
        phone = (TextView) fragmentView.findViewById(R.id.communication);
        weixin = (TextView) fragmentView.findViewById(R.id.weixin);

        fragmentView.findViewById(R.id.rl_nick).setOnClickListener(this);
        fragmentView.findViewById(R.id.rl_communication).setOnClickListener(this);
        fragmentView.findViewById(R.id.rl_weixin).setOnClickListener(this);
        fragmentView.findViewById(R.id.rl_address).setOnClickListener(this);
        fragmentView.findViewById(R.id.logout_icon).setOnClickListener(this);
    }

    private void initData() {
        if (!StringUtil.isBlank(CacheUtils.getString(context, "staffid", ""))){
            getStaffInfo();
        } else {
            startActivity(new Intent(this.getActivity(), StaffActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_nick:
                startActivity(new Intent(this.getActivity(), ChangeNameActivity.class));
                break;
            case R.id.rl_communication:
                startActivity(new Intent(this.getActivity(), ChangePhoneActivity.class));
                break;
            case R.id.rl_weixin:
                startActivity(new Intent(this.getActivity(), ChangeWeixinActivity.class));
                break;
            case R.id.rl_address:
                startActivity(new Intent(this.getActivity(), ChangeAddressActivity.class));
                break;
            case R.id.logout_icon:
                Intent intent = new Intent(getActivity(), StaffActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getStaffInfo() {
        String url = AppConstants.SERVICE_URL + "staff_query_json";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("staffid", CacheUtils.getString(context, "staffid", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        UIHelper.showToastAsCenter(getActivity(), "连接失败，请重试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.optInt(PlatformConstants.RespProtocal.RPF_STATUS);
                            if (status == 0) {
                                staffModel = new StaffModel(jsonResponse.optJSONObject("0"));
                                setUserInfo();
                            } else {
                                UIHelper.showToastAsCenter(getActivity(), jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                            }
                        } catch (JSONException e) {
                            UIHelper.showToastAsCenter(getActivity(), "服务器出错，请原谅！");
                        }
                    }
                });
    }

    /**
     * 登录页面设置操作
     */
    private void setUserInfo() {
        UIHelper.runInMainThread(new Runnable() {
            @Override
            public void run() {
                if (com.wei.mobileoffice.platform.util.StringUtil.isNotBlank(staffModel.getAvatar())) {
                    ImageLoader.getInstance().displayImage(staffModel.getAvatar(),  (CircleImageView) fragmentView.findViewById(R.id.head_icon), OfficeAppContext.getOptions());
                }
                userId.setText(staffModel.getStaffid());
                nickName.setText(staffModel.getName());
                nickTitle.setText(staffModel.getName());
                department.setText(staffModel.getDepartmentid());
                address.setText(staffModel.getPosition());
                phone.setText(staffModel.getMobile());
                weixin.setText(staffModel.getWeixinid());
            }
        });
    }
}
