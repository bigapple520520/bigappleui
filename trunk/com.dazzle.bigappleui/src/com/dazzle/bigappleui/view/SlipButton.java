package com.dazzle.bigappleui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dazzle.bigappleui.utils.M;

/**
 * 拖动滑块控件，如果需要自定义图片，请在项目里放入如下命名的图片即可：<br>
 * slip_bg_on，slip_bg_off，slip_btn_on，slip_btn_off<br>
 * 如果没有下面命名的图片，那么系统会自动绘制以一张
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-24 下午7:54:55 $
 */
public class SlipButton extends View {
    private static int OFFSET = 5;// 滑块距离背景最左和最右的距离

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
        init();
    }

    public SlipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        slipBgOn = BitmapFactory.decodeResource(getResources(), M.drawable(getContext(), "slip_bg_on"));
        slipBgOff = BitmapFactory.decodeResource(getResources(), M.drawable(getContext(), "slip_bg_off"));
        slipOn = BitmapFactory.decodeResource(getResources(), M.drawable(getContext(), "slip_btn_on"));
        slipOff = BitmapFactory.decodeResource(getResources(), M.drawable(getContext(), "slip_btn_off"));

        if (null == slipBgOn) {
            slipBgOn = Bitmap.createBitmap(150, 50, Config.ARGB_8888);
            Canvas canvas = new Canvas(slipBgOn);
            canvas.drawColor(Color.parseColor("#999999"));
        }

        if (null == slipBgOff) {
            slipBgOff = Bitmap.createBitmap(150, 50, Config.ARGB_8888);
            Canvas canvas = new Canvas(slipBgOff);
            canvas.drawColor(Color.parseColor("#999999"));
        }

        if (null == slipOn) {
            slipOn = Bitmap.createBitmap(75, 40, Config.ARGB_8888);
            Canvas canvas = new Canvas(slipOn);
            canvas.drawColor(Color.parseColor("#99CCFF"));
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(24);
            canvas.drawText("打开", 10, 30, paint);
        }

        if (null == slipOff) {
            slipOff = Bitmap.createBitmap(75, 40, Config.ARGB_8888);
            Canvas canvas = new Canvas(slipOff);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            canvas.drawColor(Color.parseColor("#CCCCCC"));
            paint.setTextSize(24);
            canvas.drawText("关闭", 10, 30, paint);
        }

        heightOffset = (slipBgOn.getHeight() - slipOn.getHeight()) / 2;
        nowX = slipBgOn.getWidth() - slipOn.getWidth();// 默认滑块在右边
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        // 计算控件的宽
        if (MeasureSpec.EXACTLY == widthMode) {
            width = widthSize;
        }
        else {
            // MeasureSpec.AT_MOST和MeasureSpec.EXACTLY类型的都按照资源的宽
            width = slipBgOn.getWidth();
        }

        // 计算控件的高
        if (MeasureSpec.EXACTLY == heightMode) {
            height = heightSize;
        }
        else {
            // MeasureSpec.AT_MOST和MeasureSpec.EXACTLY类型的都按照资源的高
            height = slipBgOn.getHeight();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置滑块背景，根据nowChoose判断是否选中来确定不同的背景
        if (nowChoose) {
            canvas.drawBitmap(slipBgOn, matrix, paint);
        }
        else {
            canvas.drawBitmap(slipBgOff, matrix, paint);
        }

        float x;
        if (onSlip) {// 如果正在滑动状态，设置滑块的位置
            if (nowX >= slipBgOn.getWidth()) {
                x = slipBgOn.getWidth() - slipOn.getWidth();
                slipBtn = slipOff;
            }
            else {
                x = nowX - slipOn.getWidth() / 2;
                slipBtn = slipOn;
            }
        }
        else {// 如果手抬起不再滑动状态，根据滑块是否选中来设置滑块的位置
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

        // 绘制滑块
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
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            onSlip = false;

            // 松开时判断是否选中
            boolean lastChoose = nowChoose;
            nowChoose = (nowX < (slipBgOn.getWidth() / 2));

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
         *            是否选中
         */
        public void OnChanged(boolean checkState);
    }

}
