package com.wei.mobileoffice;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by weiyilin on 16/5/9.
 */
public abstract class BaseFragment extends Fragment {

    private String mPageName = "";
    private String H5PageName;
    protected Activity activity;
    protected Context context = OfficeAppContext.getApplication();

    protected View fragmentView;
    private boolean isNeedInit = true;

    public void setH5PageName(String h5PageName) {
        H5PageName = h5PageName;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            isNeedInit = true;
            fragmentView = doCreateView(inflater, container, savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
            isNeedInit = false;
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isNeedInit) {
            doActivityCreated(savedInstanceState);
        }

    }

    protected void createPage(String pageName) {
        mPageName = pageName;
    }

    @Override
    public void onResume() {
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

    protected abstract View doCreateView(LayoutInflater inflater, ViewGroup container,
                                         Bundle savedInstanceState);

    protected abstract void doActivityCreated(Bundle savedInstanceState);

    public void load(){};

    public void dispatchActivityResume(){};
}
