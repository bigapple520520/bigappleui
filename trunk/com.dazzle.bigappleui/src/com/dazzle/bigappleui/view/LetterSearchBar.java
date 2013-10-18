package com.dazzle.bigappleui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 字母索引
 * 
 * @author xuan
 */
public class LetterSearchBar extends View {
    private String[] letterArray = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
    private String lastLetter;

    private int width;// 单个字母的宽
    private int height;// 单个字母的高

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private OnLetterChange onLetterChange;
    private OutLetterSeacherBar outLetterSeacherBar;

    public LetterSearchBar(Context context) {
        super(context);
    }

    public LetterSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LetterSearchBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h / letterArray.length;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            int tempY = (int) (event.getY() / height);// 字母位置
            if (tempY < letterArray.length && event.getY() > 0) {
                setBackgroundColor(Color.BLACK);
                getBackground().setAlpha(50);// 0-255，0为全透明

                String nowLetter = letterArray[tempY];
                if (null != onLetterChange && !nowLetter.equals(lastLetter)) {
                    onLetterChange.letterChange(nowLetter);
                }

                lastLetter = nowLetter;
            }
        }
        else {
            setBackgroundColor(Color.TRANSPARENT);

            if (null != outLetterSeacherBar) {
                outLetterSeacherBar.outBar(lastLetter);
            }
        }

        return true;// 防止事件往父容器传递
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setFakeBoldText(true);
        paint.setColor(Color.GRAY);
        paint.setStyle(Style.FILL);
        paint.setTextSize(height * 0.75f);
        paint.setTextAlign(Paint.Align.CENTER);
        FontMetrics fm = paint.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fm.ascent + fm.descent) / 2;

        for (int i = 0; i < letterArray.length; i++) {
            canvas.drawText(letterArray[i], x, i * height + y, paint);
        }
    }

    public String[] getLetterArray() {
        return letterArray;
    }

    public void setLetterArray(String[] letterArray) {
        this.letterArray = letterArray;
        invalidate();
    }

    // ////////////////////////////////////////////事件监听/////////////////////////////////////////////////////////////
    /**
     * 搜索字母改变事件
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2012-11-1 下午6:10:00 $
     */
    public interface OnLetterChange {
        void letterChange(String letter);
    }

    /**
     * 手指移除控件等，可用来设置影藏字母提示
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2013-3-14 上午10:22:56 $
     */
    public interface OutLetterSeacherBar {
        void outBar(String lastLetter);
    }

    public void setOnLetterChange(OnLetterChange onLetterChange) {
        this.onLetterChange = onLetterChange;
    }

    public void setOutLetterSeacherBar(OutLetterSeacherBar outLetterSeacherBar) {
        this.outLetterSeacherBar = outLetterSeacherBar;
    }

}
