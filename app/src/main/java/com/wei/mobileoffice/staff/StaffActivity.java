package com.wei.mobileoffice.staff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.OfficeAppContext;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.StaffModel;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.platform.util.StringUtil;
import com.wei.mobileoffice.ui.CircleImageView;
import com.wei.mobileoffice.ui.pulltorefresh.PullToRefreshBase;
import com.wei.mobileoffice.ui.pulltorefresh.PullToRefreshListView;
import com.wei.mobileoffice.util.CacheUtils;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/19.
 */
public class StaffActivity extends BaseActivity {

    private String result;
    private PullToRefreshListView listview;
    private StaffListAdapter adapter;
    private List<StaffModel> list = new ArrayList<>();
    private int curPages = 1;//当前页
    private int pageSize;//每页条数
    private int pageCount;//页数
    private boolean isStopRefresh = false;//停止刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_activity);
        initView();
    }

    private void initView() {
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.add)
                    startActivity(new Intent(StaffActivity.this, AddStaffActivity.class));
            }
        });

        new Thread(new TaskRunnable()).start();
        findViewById(R.id.refresh_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new TaskRunnable()).start();
            }
        });
    }

    class TaskRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (String.class) {
                String url = AppConstants.SERVICE_URL + "common_staff_json";
                OkHttpUtils
                        .post()
                        .url(url)
                        .addParams("currentpage", curPages + "")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                showError();
                            }

                            @Override
                            public void onResponse(String response) {
                                result = response;
                                try {
                                    JSONObject jsonResponse = new JSONObject(result);
                                    int status = jsonResponse.optInt(PlatformConstants.RespProtocal.RPF_STATUS);
                                    if (status == 0) {
                                        pageSize = jsonResponse.optInt("pagesize");
                                        curPages = jsonResponse.optInt("currentpage");
                                        pageCount = jsonResponse.optInt("pagecount");
                                        if (pageSize == 0){
                                            isStopRefresh = true;
                                        }
                                        if (curPages == 1){
                                            list.clear();
                                        }
                                        if (curPages < pageCount) {
                                            isStopRefresh = false;
                                        } else {
                                            isStopRefresh = true;
                                        }
                                        for (int i = 0, length = pageSize; i < length; i++) {
                                            JSONObject item = jsonResponse.optJSONObject(i+"");
                                            if (item != null) {
                                                StaffModel staffModel= new StaffModel(item);
                                                list.add(staffModel);
                                            }
                                        }
                                    } else {
                                        UIHelper.showToastAsCenter(StaffActivity.this, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
                                        showError();
                                        return;
                                    }
                                    showSucceed();
                                    return;
                                } catch (JSONException e) {
                                    showError();
                                    return;
                                }
                            }
                        });
            }
        }
    }

    private void showError() {
        UIHelper.runInMainThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.rl_staff_list).setVisibility(View.GONE);
                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.loading_page_error).setVisibility(View.VISIBLE);
                findViewById(R.id.loading_page_empty).setVisibility(View.GONE);
            }
        });
    }

    private void showEmpty() {
        UIHelper.runInMainThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.rl_staff_list).setVisibility(View.GONE);
                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.loading_page_error).setVisibility(View.GONE);
                findViewById(R.id.loading_page_empty).setVisibility(View.VISIBLE);
            }
        });
    }

    private void showSucceed() {
        UIHelper.runInMainThread(new Runnable() {
            @Override
            public void run() {
                if (StringUtil.isBlank(result)) {
                    return;
                }
                if (list == null || list.size() < 1) {
                    showEmpty();
                    return;
                }
                findViewById(R.id.rl_staff_list).setVisibility(View.VISIBLE);
                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.loading_page_error).setVisibility(View.GONE);
                findViewById(R.id.loading_page_empty).setVisibility(View.GONE);
                if (listview == null) {
                    listview = (PullToRefreshListView) findViewById(R.id.staff_list);
                    listview.setMode(PullToRefreshBase.Mode.BOTH);
                    adapter = new StaffListAdapter(StaffActivity.this);
                    listview.setAdapter(adapter);
                    listview.setOnRefreshListener(listRefreshListener);
                    listview.setOnItemClickListener(listViewItemClickListener);
                } else {
                    adapter.notifyDataSetChanged();
                    listview.onRefreshComplete();
                    if (isStopRefresh) {
                        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        listview.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                }
            }
        });
    }

    private final PullToRefreshBase.OnRefreshListener2<ListView> listRefreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            isStopRefresh = false;
            curPages = 1;
            doSearch();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (!isStopRefresh) {
                curPages++;
            }
            doSearch();
        }
    };

    private AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final int pos = position - 1;
            StaffModel staffModel = list.get(pos);
            CacheUtils.putString(StaffActivity.this, "staffid", staffModel.getStaffid());
            finish();
        }
    };

    private void doSearch() {
        if (curPages < 1) {
            curPages = 1;
        }
        //限定查询范围
        if (list.size() < 500) {
            new Thread(new TaskRunnable()).start();
        }
    }

    class StaffListAdapter extends BaseAdapter {
        private Context context;

        public StaffListAdapter(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final StaffItemView itemView;
            if (convertView == null) {
                itemView = new StaffItemView();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.staff_item, null);
                itemView.name = (TextView) convertView.findViewById(R.id.name);
                itemView.id = (TextView) convertView.findViewById(R.id.id);
                itemView.gender = (TextView) convertView.findViewById(R.id.gender);
                itemView.department = (TextView) convertView.findViewById(R.id.department);
                itemView.head_icon = (CircleImageView) convertView.findViewById(R.id.head_icon);
                convertView.setTag(itemView);
            } else {
                itemView = (StaffItemView) convertView.getTag();
            }

            StaffModel model = list.get(position);
            if (model == null) {
                return convertView;
            }

            if (StringUtil.isNotBlank(model.getAvatar())) {
                itemView.head_icon.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(model.getAvatar(), itemView.head_icon, OfficeAppContext.getOptions());
            }

            itemView.name.setText(model.getName());
            itemView.id.setText(model.getStaffid());
            if (model.getGender().equals("1")) {
                itemView.gender.setText("男");
            } else {
                itemView.gender.setText("女");
            }
            itemView.department.setText(model.getDepartmentname());

            return convertView;
        }

        private class StaffItemView {
            TextView name;
            TextView id;
            TextView gender;
            TextView department;
            CircleImageView head_icon;
        }
    }
}
