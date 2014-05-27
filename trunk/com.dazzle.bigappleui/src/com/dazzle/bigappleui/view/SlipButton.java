package com.dazzle.bigappleui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dazzle.bigappleui.utils.M;

/**
 * 模仿ios的是否滑块控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-24 下午7:54:55 $
 */
public class SlipButton extends View {
    private final Context context;

    private static final int OFFSET = 5;// 滑块距离背景最左和最右的距离

    private boolean nowChoose = false;// 是否选中状态
    private boolean onSlip = false;// 是否滑动状态

    private float nowX;// 当前的x坐标

    private OnChangedListener onChangedListener;// 是否选中状态改变事件

    private Bitmap slipBgOn;// 开启状态背景
    private Bitmap slipBgOff;// 关闭状态背景

    private Bitmap slipOn;// 开启滑块
    private Bitmap slipOff;// 关闭滑块
    private Bitmap slipBtn;

    private final Matrix matrix = new Matrix();
    private final Paint paint = new Paint();

    private int heightOffset;// 滑块距离背景上下偏移

    public SlipButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SlipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        slipBgOn = BitmapFactory.decodeResource(getResources(), M.drawable(context, "slip_bg_on"));
        slipBgOff = BitmapFactory.decodeResource(getResources(), M.drawable(context, "slip_bg_off"));
        slipOn = BitmapFactory.decodeResource(getResources(), M.drawable(context, "slip_btn_on"));
        slipOff = BitmapFactory.decodeResource(getResources(), M.drawable(context, "slip_btn_off"));

        heightOffset = (slipBgOn.getHeight() - slipOn.getHeight()) / 2;
        nowX = slipBgOn.getWidth() - slipOn.getWidth();// 默认滑块在右边
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x;

        // 滑块背景
        if (nowChoose) {
            canvas.drawBitmap(slipBgOn, matrix, paint);
        }
        else {
            canvas.drawBitmap(slipBgOff, matrix, paint);
        }

        Log.d("", "------------------------------------" + nowChoose + "-------" + nowX);

        if (onSlip) {// 滑动状态
            if (nowX >= slipBgOn.getWidth()) {
                x = slipBgOn.getWidth() - slipOn.getWidth();
                slipBtn = slipOff;
            }
            else {
                x = nowX - slipOn.getWidth() / 2;
                slipBtn = slipOn;
            }
        }
        else {// 非滑动状态
            if (nowChoose) {
                x = OFFSET;
                slipBtn = slipOn;
            }
            else {
                x = slipBgOn.getWidth() - slipOn.getWidth();
                slipBtn = slipOff;
            }
        }

        if (x <= 0) {
            x = OFFSET;
        }
        else if (x > slipBgOn.getWidth() - slipOn.getWidth() - OFFSET) {
            x = slipBgOn.getWidth() - slipOn.getWidth() - OFFSET;
        }

        canvas.drawBitmap(slipBtn, x, heightOffset, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_MOVE:
            nowX = event.getX();
            break;
        case MotionEvent.ACTION_DOWN:
            if (event.getX() > slipBgOn.getWidth() || event.getY() > slipBgOn.getHeight()) {
                return false;
            }
            onSlip = true;
            nowX = event.getX();
            break;
        case MotionEvent.ACTION_CANCEL:// 同up一起处理
        case MotionEvent.ACTION_UP:
            onSlip = false;

            // 松开时判断是否选中
            boolean lastChoose = nowChoose;
            nowChoose = (nowX < (slipBgOn.getWidth() / 2));
            Log.e("", "-----------------------------" + event.getX());

            if (null != onChangedListener && (lastChoose != nowChoose)) {
                onChangedListener.OnChanged(nowChoose);
            }
            break;
        }
        invalidate();
        return true;
    }

    /**
     * 设置事件改变监听
     * 
     * @param onChangedListener
     */
    public void SetOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    /**
     * 判断当前开或者关状态
     * 
     * @return
     */
    public boolean isChecked() {
        return nowChoose;
    }

    /**
     * 外部手动设置开或者关
     * 
     * @param check
     */
    public void setChecked(boolean check) {
        if (check != nowChoose) {
            nowChoose = check;
            if (nowChoose) {
                nowX = OFFSET;
            }
            else {
                nowX = slipBgOn.getWidth() - slipOn.getWidth();
            }

            invalidate();

            if (null != onChangedListener) {
                onChangedListener.OnChanged(nowChoose);
            }
        }
    }

    /**
     * 滑块改变事件
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2013-7-24 下午7:50:50 $
     */
    public interface OnChangedListener {

        /**
         * 事件执行
         * 
         * @param checkState
         */
        public void OnChanged(boolean checkState);
    }

}
