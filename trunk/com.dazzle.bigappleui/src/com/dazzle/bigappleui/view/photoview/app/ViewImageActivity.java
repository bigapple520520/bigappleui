package com.dazzle.bigappleui.view.photoview.app;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import com.dazzle.bigappleui.view.photoview.app.handler.ResidViewImageHandler;
import com.dazzle.bigappleui.view.photoview.app.handler.UrlViewImageHandler;
import com.dazzle.bigappleui.view.photoview.app.handler.ViewImageBaseHandler;
import com.dazzle.bigappleui.view.photoview.app.viewholder.CreateViewHelper;
import com.dazzle.bigappleui.view.photoview.app.viewholder.WraperActivityView;
import com.winupon.andframe.bigapple.utils.Validators;

/**
 * 查看大图Activity组件<br>
 * 注意：查看大图控件需要在AndroidManifest.xml注册Activity，或者注册继承他的自定义的Activity
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-10-16 下午5:04:15 $
 */
public class ViewImageActivity extends FragmentActivity {
    private static final String TAG = "ViewImageActivity";
    private static final String STATE_POSITION = "STATE_POSITION";

    public static final String PARAM_IMAGE_INDEX = "param.image.position";// 指定首先显示第几张图片
    public static final String PARAM_IMAGE_URLS = "param.image.urls";// 图片地址数组
    public static final String PARAM_LOADTYPE = "param.loadtype";// 图片加载方式
    public static final String PARAM_DATAS = "param.datas";// 额外数据

    private int position;// 指定首先显示第几张图片
    private String[] urls;
    private String loadType;
    private Object[] datas;

    private HashMap<String, ViewImageBaseHandler> handlerMap = new HashMap<String, ViewImageBaseHandler>();
    private ViewImageBaseHandler viewImageHandler;

    private WraperActivityView wraperActivityView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wraperActivityView = CreateViewHelper.getWraperActivityView(this);
        setContentView(wraperActivityView.root);

        // 初始化处理器
        initViewImageHandler();

        // 接受参数
        position = getIntent().getIntExtra(PARAM_IMAGE_INDEX, 0);
        urls = getIntent().getStringArrayExtra(PARAM_IMAGE_URLS);
        Object temp = getIntent().getSerializableExtra(PARAM_DATAS);
        if (null != temp) {
            datas = (Object[]) temp;
        }

        loadType = getIntent().getStringExtra(PARAM_LOADTYPE);
        if (Validators.isEmpty(loadType)) {
            Log.e(TAG, "没有对应的处理逻辑块，请指定loadType值。");
            this.finish();
        }

        viewImageHandler = handlerMap.get(loadType);
        if (null == viewImageHandler) {
            Log.e(TAG, "没有对应的处理逻辑块，请确认loadType值。");
            this.finish();
        }

        // Viewpage初始化
        wraperActivityView.hackyViewPager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager()));
        int current = position + 1;
        final int total = wraperActivityView.hackyViewPager.getAdapter().getCount();
        wraperActivityView.textView.setText(current + "/" + total);
        wraperActivityView.hackyViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int postion) {
                wraperActivityView.textView.setText((postion + 1) + "/" + total);
            }
        });

        // 如果savedInstanceState不为空，说明这次启动是因为上次被系统回收，所以恢复上次当前页数
        if (null != savedInstanceState) {
            position = savedInstanceState.getInt(STATE_POSITION);
        }
        wraperActivityView.hackyViewPager.setCurrentItem(position);
    }

    // 初始化加载器
    private void initViewImageHandler() {
        handlerMap.put(ViewImageBaseHandler.LOADTYPE_BY_RESIID, new ResidViewImageHandler());
        handlerMap.put(ViewImageBaseHandler.LOADTYPE_BY_URL, new UrlViewImageHandler());
        onInitViewImageHandler(handlerMap);
    }

    // 由子类复写，可以添加自定义的图片处理器
    protected void onInitViewImageHandler(HashMap<String, ViewImageBaseHandler> handlerMap) {
    }

    /**
     * 子类调用添加处理器
     * 
     * @param myHandler
     */
    public void addViewImageHandler(ViewImageBaseHandler myHandler) {
        if (handlerMap.containsKey(myHandler.getLoadType())) {
            throw new IllegalArgumentException("处理器的loadType跟系统自带的冲突，请重新命名这个常量");
        }
        else {
            handlerMap.put(myHandler.getLoadType(), myHandler);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, wraperActivityView.hackyViewPager.getCurrentItem());
    }

    /**
     * ViewPager设配器
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-10-16 下午7:00:18 $
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return null == urls ? 0 : urls.length;
        }

        @Override
        public Fragment getItem(int position) {
            return ViewImageFragment.newInstance(urls[position], ViewImageActivity.this, viewImageHandler, datas);
        }
    }

}
