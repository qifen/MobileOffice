package com.wei.mobileoffice;

import android.os.Bundle;

import com.wei.mobileoffice.ui.swipebacklayout.SwipeBackFragmentActivity;
import com.wei.mobileoffice.ui.swipebacklayout.SwipeBackLayout;
import com.wei.mobileoffice.util.UIHelper;

/**
 * Created by lx on 15/12/28.
 */
public class BaseFragmentActivity extends SwipeBackFragmentActivity {

    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        //设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setEdgeSize(UIHelper.dip2Pixel(20));//设置侧滑返回生效宽度
//        mSwipeBackLayout.setSensitivity(this, 0.1f);//设置灵敏度
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
