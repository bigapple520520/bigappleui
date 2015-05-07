package com.dazzle.bigappleui.demo.view.zoomimageview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.view.ZoomImageView;
import com.dazzle.bigappleui.view.photoview.PhotoView;
import com.dazzle.bigappleui.view.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.dazzle.bigappleui.view.photoview.app.ViewImageUtils;
import com.winupon.andframe.bigapple.bitmap.AnBitmapUtilsFace;
import com.winupon.andframe.bigapple.utils.ContextUtils;

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
        // init();
        init2();
        AnBitmapUtilsFace.init(this);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ViewImageUtils.gotoViewImageActivityForResids(ZoomImageViewDemoActivity.this, new int[] {
                        R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4 }, 1, null);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ViewImageUtils.gotoViewImageActivityForUrls(ZoomImageViewDemoActivity.this, new String[] {
                        "http://c.hiphotos.baidu.com/image/pic/item/00e93901213fb80ea15ff5a935d12f2eb83894c6.jpg",
                        "http://e.hiphotos.baidu.com/image/pic/item/f703738da9773912d761111ffb198618367ae20d.jpg",
                        "http://c.hiphotos.baidu.com/image/pic/item/810a19d8bc3eb135394e23c5a51ea8d3fc1f44c6.jpg",
                        "http://b.hiphotos.baidu.com/image/pic/item/aec379310a55b3193a929fbc41a98226cefc178b.jpg",
                        "http://e.hiphotos.baidu.com/image/pic/item/ac4bd11373f082026ea01c3548fbfbedab641b8d.jpg",
                        ContextUtils.getSdCardPath() + "/xuan/1.jpg", ContextUtils.getSdCardPath() + "/xuan/2.jpg" },
                        1, null);
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ViewImageUtils.gotoViewImageActivity(ZoomImageViewDemoActivity.this, new String[] {
                        "http://c.hiphotos.baidu.com/image/pic/item/00e93901213fb80ea15ff5a935d12f2eb83894c6.jpg",
                        "http://e.hiphotos.baidu.com/image/pic/item/f703738da9773912d761111ffb198618367ae20d.jpg",
                        "http://c.hiphotos.baidu.com/image/pic/item/810a19d8bc3eb135394e23c5a51ea8d3fc1f44c6.jpg",
                        "http://b.hiphotos.baidu.com/image/pic/item/aec379310a55b3193a929fbc41a98226cefc178b.jpg",
                        "http://e.hiphotos.baidu.com/image/pic/item/ac4bd11373f082026ea01c3548fbfbedab641b8d.jpg",
                        ContextUtils.getSdCardPath() + "/xuan/1.jpg", ContextUtils.getSdCardPath() + "/xuan/2.jpg" },
                        1, MyViewImageActivity.LOADTYPE1, null, MyViewImageActivity.class);
            }
        });
    }

    private void init2() {
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

    private void init() {
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
