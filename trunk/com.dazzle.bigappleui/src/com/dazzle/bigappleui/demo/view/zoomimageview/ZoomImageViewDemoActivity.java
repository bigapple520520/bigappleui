package com.dazzle.bigappleui.demo.view.zoomimageview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.view.ZoomImageView;
import com.dazzle.bigappleui.view.photoview.PhotoView;
import com.dazzle.bigappleui.view.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * 图片显示，支持两点缩放，及旋转
 * 
 * @author xuan
 */
public class ZoomImageViewDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_zoomimageview_main);
        //init();
        init2();
    }
    
    private void init2(){
    	PhotoView photoView = (PhotoView) findViewById(R.id.photoView);
    	photoView.setVisibility(View.VISIBLE);
    	photoView.setImageResource(R.drawable.demo_zoomimageview_pic);
    	
    	photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				ZoomImageViewDemoActivity.this.finish();
			}
		});
    }
    
    private void init(){
    	ZoomImageView zoomImageView = (ZoomImageView) findViewById(R.id.zoomImageView);
    	zoomImageView.setVisibility(View.VISIBLE);

        zoomImageView.setLongClickTime(1000);
        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ZoomImageViewDemoActivity.this, "单击效果", Toast.LENGTH_SHORT).show();
            }
        });

        zoomImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ZoomImageViewDemoActivity.this, "长按效果", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        zoomImageView.setImageResource(R.drawable.demo_zoomimageview_pic);// 也可以直接在布局文件的src属性中设置
    }

}
