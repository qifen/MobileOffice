package com.wei.mobileoffice.business;

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

import com.wei.mobileoffice.BaseFragment;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.constants.AppConstants;
import com.wei.mobileoffice.model.FollowupModel;
import com.wei.mobileoffice.platform.constants.PlatformConstants;
import com.wei.mobileoffice.platform.util.StringUtil;
import com.wei.mobileoffice.ui.CircleImageView;
import com.wei.mobileoffice.ui.pulltorefresh.PullToRefreshBase;
import com.wei.mobileoffice.ui.pulltorefresh.PullToRefreshListView;
import com.wei.mobileoffice.util.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

/**
 * Created by weiyilin on 16/6/26.
 */
public class FollowupFragment extends BaseFragment {

    private String result;
    private PullToRefreshListView listview;
    private FollowupListAdapter adapter;
    private List<FollowupModel> list = new ArrayList<>();
    private int curPages = 1;//当前页
    private int pageSize;//每页条数
    private int pageCount;//页数
    private boolean isStopRefresh = false;//停止刷新

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.followup_fragment, container, false);
        initView();
        return fragmentView;
    }

    @Override
    protected void doActivityCreated(Bundle savedInstanceState) {
        this.createPage(this.getClass().getSimpleName().toLowerCase(Locale.getDefault()));
    }

    private void initView() {
        new Thread(new TaskRunnable()).start();
        fragmentView.findViewById(R.id.refresh_page).setOnClickListener(new View.OnClickListener() {
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
                String url = AppConstants.SERVICE_URL + "common_followup_json";
                OkHttpUtils
                        .post()
                        .url(url)
                        .addParams("currentpage", curPages + "")
                        .addParams("sourcetype", (getArguments().getInt("index") + 1) + "")
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
                                        if (pageSize == 0) {
                                            isStopRefresh = true;
                                        }
                                        if (curPages == 1) {
                                            list.clear();
                                        }
                                        if (curPages < pageCount) {
                                            isStopRefresh = false;
                                        } else {
                                            isStopRefresh = true;
                                        }
                                        for (int i = 0, length = pageSize; i < length; i++) {
                                            JSONObject item = jsonResponse.optJSONObject(i + "");
                                            if (item != null) {
                                                FollowupModel followupModel = new FollowupModel(item);
                                                list.add(followupModel);
                                            }
                                        }
                                    } else {
                                        UIHelper.showToastAsCenter(context, jsonResponse.optString(PlatformConstants.RespProtocal.RPF_MSG));
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
                fragmentView.findViewById(R.id.rl_followup_list).setVisibility(View.GONE);
                fragmentView.findViewById(R.id.progress).setVisibility(View.GONE);
                fragmentView.findViewById(R.id.loading_page_error).setVisibility(View.VISIBLE);
                fragmentView.findViewById(R.id.loading_page_empty).setVisibility(View.GONE);
            }
        });
    }

    private void showEmpty() {
        UIHelper.runInMainThread(new Runnable() {
            @Override
            public void run() {
                fragmentView.findViewById(R.id.rl_followup_list).setVisibility(View.GONE);
                fragmentView.findViewById(R.id.progress).setVisibility(View.GONE);
                fragmentView.findViewById(R.id.loading_page_error).setVisibility(View.GONE);
                fragmentView.findViewById(R.id.loading_page_empty).setVisibility(View.VISIBLE);
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
                fragmentView.findViewById(R.id.rl_followup_list).setVisibility(View.VISIBLE);
                fragmentView.findViewById(R.id.progress).setVisibility(View.GONE);
                fragmentView.findViewById(R.id.loading_page_error).setVisibility(View.GONE);
                fragmentView.findViewById(R.id.loading_page_empty).setVisibility(View.GONE);
                if (listview == null) {
                    listview = (PullToRefreshListView) fragmentView.findViewById(R.id.followup_list);
                    listview.setMode(PullToRefreshBase.Mode.BOTH);
                    adapter = new FollowupListAdapter(context);
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
            FollowupModel followupModel = list.get(pos);
            Intent intent = new Intent(getActivity(), FollowupDetailActivity.class);
            intent.putExtra("followupid", followupModel.getFollowupid());
            startActivity(intent);
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

    class FollowupListAdapter extends BaseAdapter {
        private Context context;

        public FollowupListAdapter(Context c) {
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
            final FollowupItemView itemView;
            if (convertView == null) {
                itemView = new FollowupItemView();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.followup_item, null);
                itemView.time = (TextView) convertView.findViewById(R.id.time);
                itemView.content = (TextView) convertView.findViewById(R.id.content);
                itemView.head_icon = (CircleImageView) convertView.findViewById(R.id.head_icon);
                convertView.setTag(itemView);
            } else {
                itemView = (FollowupItemView) convertView.getTag();
            }

            FollowupModel model = list.get(position);
            if (model == null) {
                return convertView;
            }

            switch (model.getSourcetype()){
                case "1":
                    itemView.head_icon.setImageResource(R.drawable.followup_kehu);
                    break;
                case "2":
                    itemView.head_icon.setImageResource(R.drawable.followup_shangji);
                    break;
                case "3":
                    itemView.head_icon.setImageResource(R.drawable.followup_hetong);
                    break;
            }

            itemView.time.setText(model.getCreatetime());
            itemView.content.setText(model.getContent());

            return convertView;
        }

        private class FollowupItemView {
            TextView time;
            TextView content;
            CircleImageView head_icon;
        }
    }
}
