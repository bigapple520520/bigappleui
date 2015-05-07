/* 
 * @(#)MyViewImageActivity.java    Created on 2014-11-7
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.view.zoomimageview;

import java.util.HashMap;

import android.app.Activity;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.view.photoview.app.ViewImageActivity;
import com.dazzle.bigappleui.view.photoview.app.handler.ViewImageBaseHandler;
import com.dazzle.bigappleui.view.photoview.app.viewholder.WraperFragmentView;

/**
 * 这里可以自定义一些图片加载器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 下午1:53:47 $
 */
public class MyViewImageActivity extends ViewImageActivity {
    public static final String LOADTYPE1 = "loadtype1";

    @Override
    protected void onInitViewImageHandler(HashMap<String, ViewImageBaseHandler> handlerMap) {
        addViewImageHandler(new MyViewImageHandler());
    }

    /**
     * 自定义实现一个图片加载处理
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-11-7 下午2:02:14 $
     */
    class MyViewImageHandler extends ViewImageBaseHandler {
        @Override
        public String getLoadType() {
            return LOADTYPE1;
        }

        @Override
        public void onHandler(String url, WraperFragmentView wraperFragmentView, Activity activity, Object[] datas) {
            wraperFragmentView.photoView.setImageResource(R.drawable.pic1);// 这里可以自己实现要加载的图片我这里测试而已
        }
    }

}
