package com.dazzle.bigappleui.demo.gifview;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.gifview.GifView;
import com.dazzle.bigappleui.view.imageview.RotationImageView;
import com.dazzle.bigappleui.view.roundedimageview.RoundedImageView;
import com.winupon.andframe.bigapple.bitmap.AnBitmapUtilsFace;

/**
 * 显示gifview控件demo
 * 
 * @author xuan
 */
public class GifViewDemoActivity extends Activity {
    private boolean pause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_gifview_main);
        AnBitmapUtilsFace.init(this.getApplicationContext());

        final GifView gif = (GifView) findViewById(R.id.gif);
        gif.setGifImage(R.drawable.demo_gifview_pic);

        final Button button = (Button) findViewById(R.id.button);
        button.setText("点击暂停");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pause) {
                    gif.showAnimation();
                    pause = false;
                    button.setText("点击暂定");
                }
                else {
                    gif.showCover();
                    pause = true;
                    button.setText("点击继续");
                }
            }
        });

        // 正常图
        testImageView();

        // 圆角图
        testRoundImageView();

        // 旋转图
        testRotationImageView();
    }

    private void testImageView() {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        imageView.setPadding(20, 20, 20, 20);
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic1));
    }

    /** 圆角图片控件测试 */
    private void testRoundImageView() {
        RoundedImageView roundedImageView = (RoundedImageView) findViewById(R.id.roundedImageView);
        // roundedImageView.setCornerRadius(500f);
        // roundedImageView.setBorderWidth(10f);
        // roundedImageView.setOval(true);
        // roundedImageView.setCornerRadius(R.dimen.dp40);
        // roundedImageView.setCornerRadius(getResources().getDimension(R.dimen.dp40));
        // roundedImageView.setCircle(true);
        roundedImageView.setScaleType(ScaleType.CENTER_CROP);
        roundedImageView.setPadding(20, 20, 20, 20);
        // roundedImageView.setOval(true);
        // roundedImageView.setMutateBackground(true);
        // Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.demo_viewpage_pic1);
        // roundedImageView.setImageBitmap(bm);
        // AnBitmapUtilsFace.getInstance().display(roundedImageView,
        // "http://weikefile.wanpeng.net/upload/photo/20140930/17/FF8080813A3EEE1C013A43152AD009F6/s.jpg?43");
        // roundedImageView.setBackgroundResource(R.drawable.slip_bg_off);

        roundedImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic1_90));
        // roundedImageView.setImageBitmap(BitmapFactory.decodeFile(ContextUtils.getSdCardPath() +
        // "/wpchat/test111.png"));
    }

    /** 旋转图片控件测试 */
    private void testRotationImageView() {
        final RotationImageView rotationImageView = (RotationImageView) findViewById(R.id.rotationImageView);
        rotationImageView.setRotationDegree(90);
        rotationImageView.setScaleType(ScaleType.CENTER_CROP);
        rotationImageView.setPadding(20, 20, 20, 20);
        // rotationImageView.setScaleType(ScaleType.FIT_CENTER);
        // rotationImageView.setScaleType(ScaleType.FIT_START);
        // rotationImageView.setScaleType(ScaleType.FIT_END);
        // rotationImageView.setScaleType(ScaleType.FIT_XY);
        // rotationImageView.setScaleType(ScaleType.CENTER);
        // rotationImageView.setScaleType(ScaleType.CENTER_INSIDE);
        // rotationImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic1));
        // rotationImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic1));
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        rotationImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic1));
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rotationImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic1));
                    }
                }, 1000);
            }
        }).start();
    }
}
