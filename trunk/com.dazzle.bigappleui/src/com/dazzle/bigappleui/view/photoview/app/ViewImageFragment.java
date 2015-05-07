package com.dazzle.bigappleui.view.photoview.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dazzle.bigappleui.view.photoview.app.handler.ViewImageBaseHandler;
import com.dazzle.bigappleui.view.photoview.app.viewholder.CreateViewHelper;
import com.dazzle.bigappleui.view.photoview.app.viewholder.WraperFragmentView;

/**
 * 显示单个图片Fragment
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-10-16 下午5:06:47 $
 */
public class ViewImageFragment extends Fragment {
    private WraperFragmentView wraperFragmentView;

    private String imageUrl;
    private Activity activity;
    private ViewImageBaseHandler viewImageHandler;
    private Object[] datas;

    public ViewImageFragment(String imageUrl, Activity activity, ViewImageBaseHandler viewImageHandler, Object[] datas) {
        this.imageUrl = imageUrl;
        this.activity = activity;
        this.viewImageHandler = viewImageHandler;
        this.datas = datas;
    }

    /**
     * 获取一个Fragment对象
     * 
     * @param imageUrl
     * @param activity
     * @param viewImageHandler
     * @param datas
     * @return
     */
    public static ViewImageFragment newInstance(String imageUrl, Activity activity,
            ViewImageBaseHandler viewImageHandler, Object[] datas) {
        return new ViewImageFragment(imageUrl, activity, viewImageHandler, datas);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wraperFragmentView = CreateViewHelper.getFragmentView(activity);
        return wraperFragmentView.root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewImageHandler.handler(imageUrl, wraperFragmentView, activity, datas);
    }

}
