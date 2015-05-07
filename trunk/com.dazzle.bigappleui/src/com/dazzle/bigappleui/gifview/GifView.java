package com.dazzle.bigappleui.gifview;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 显示GIF图片的控件，注意如果图片太大，会出现OOM
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-28 下午2:27:41 $
 */
public class GifView extends View implements GifAction {
    private GifDecoder gifDecoder = null;// gif解码器
    private Bitmap currentImage = null;// 当前要画的帧的图

    private final boolean isRun = true;

    private boolean pause = false;

    private int showWidth = -1;
    private int showHeight = -1;
    private Rect rect = null;

    private DrawThread drawThread = null;

    private GifImageType animationType = GifImageType.SYNC_DECODER;

    public GifView(Context context) {
        super(context);

    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    /**
     * 设置图片，并开始解码
     * 
     * @param gif
     *            要设置的图片
     */
    private void setGifDecoderImage(byte[] gif) {
        if (gifDecoder != null) {
            gifDecoder.free();
            gifDecoder = null;
        }
        gifDecoder = new GifDecoder(gif, this);
        gifDecoder.start();
    }

    /**
     * 设置图片，开始解码
     * 
     * @param is
     *            要设置的图片
     */
    private void setGifDecoderImage(InputStream is) {
        if (gifDecoder != null) {
            gifDecoder.free();
            gifDecoder = null;
        }
        gifDecoder = new GifDecoder(is, this);
        gifDecoder.start();
    }

    /**
     * 以字节数据形式设置gif图片
     * 
     * @param gif
     *            图片
     */
    public void setGifImage(byte[] gif) {
        setGifDecoderImage(gif);
    }

    /**
     * 以字节流形式设置gif图片
     * 
     * @param is
     *            图片
     */
    public void setGifImage(InputStream is) {
        setGifDecoderImage(is);
    }

    /**
     * 以资源形式设置gif图片
     * 
     * @param resId
     *            gif图片的资源ID
     */
    public void setGifImage(int resId) {
        Resources r = this.getResources();
        InputStream is = r.openRawResource(resId);
        setGifDecoderImage(is);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gifDecoder == null) {
            return;
        }
        if (currentImage == null) {
            currentImage = gifDecoder.getImage();
        }
        if (currentImage == null) {
            return;
        }
        int saveCount = canvas.getSaveCount();
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        if (showWidth == -1) {
            canvas.drawBitmap(currentImage, 0, 0, null);
        }
        else {
            canvas.drawBitmap(currentImage, null, rect, null);
        }
        canvas.restoreToCount(saveCount);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();

        int widthSize;
        int heightSize;

        int w;
        int h;

        if (gifDecoder == null) {
            w = 1;
            h = 1;
        }
        else {
            w = gifDecoder.width;
            h = gifDecoder.height;
        }

        w += pleft + pright;
        h += ptop + pbottom;

        w = Math.max(w, getSuggestedMinimumWidth());
        h = Math.max(h, getSuggestedMinimumHeight());

        widthSize = resolveSize(w, widthMeasureSpec);
        heightSize = resolveSize(h, heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 只显示第一帧图片<br>
     * 调用本方法后，gif不会显示动画，只会显示gif的第一帧图
     */
    public void showCover() {
        if (gifDecoder == null) {
            return;
        }
        pause = true;
        currentImage = gifDecoder.getImage();
        invalidate();
    }

    /**
     * 继续显示动画<br>
     * 本方法在调用showCover后，会让动画继续显示，如果没有调用showCover方法，则没有任何效果
     */
    public void showAnimation() {
        if (pause) {
            pause = false;
        }
    }

    /**
     * 设置gif在解码过程中的显示方式<br>
     * 本方法只能在setGifImage方法之前设置，否则设置无效
     * 
     * @param type
     *            显示方式
     */
    public void setGifImageType(GifImageType type) {
        if (gifDecoder == null) {
            animationType = type;
        }
    }

    /**
     * 设置要显示的图片的大小<br>
     * 当设置了图片大小 之后，会按照设置的大小来显示gif（按设置后的大小来进行拉伸或压缩）
     * 
     * @param width
     * @param height
     */
    public void setShowDimension(int width, int height) {
        if (width > 0 && height > 0) {
            showWidth = width;
            showHeight = height;
            rect = new Rect();
            rect.left = 0;
            rect.top = 0;
            rect.right = width;
            rect.bottom = height;
        }
    }

    @Override
    public void parseOk(boolean parseStatus, int frameIndex) {
        if (parseStatus) {
            if (gifDecoder != null) {
                switch (animationType) {
                case WAIT_FINISH:
                    if (frameIndex == -1) {
                        if (gifDecoder.getFrameCount() > 1) { // 当帧数大于1时，启动动画线程
                            DrawThread dt = new DrawThread();
                            dt.start();
                        }
                        else {
                            reDraw();
                        }
                    }
                    break;
                case COVER:
                    if (frameIndex == 1) {
                        currentImage = gifDecoder.getImage();
                        reDraw();
                    }
                    else if (frameIndex == -1) {
                        if (gifDecoder.getFrameCount() > 1) {
                            if (drawThread == null) {
                                drawThread = new DrawThread();
                                drawThread.start();
                            }
                        }
                        else {
                            reDraw();
                        }
                    }
                    break;
                case SYNC_DECODER:
                    if (frameIndex == 1) {
                        currentImage = gifDecoder.getImage();
                        reDraw();
                    }
                    else if (frameIndex == -1) {
                        reDraw();
                    }
                    else {
                        if (drawThread == null) {
                            drawThread = new DrawThread();
                            drawThread.start();
                        }
                    }
                    break;
                }
            }
            else {
                Log.e("gif", "parse error");
            }
        }
    }

    private void reDraw() {
        if (redrawHandler != null) {
            Message msg = redrawHandler.obtainMessage();
            redrawHandler.sendMessage(msg);
        }
    }

    private final Handler redrawHandler = new Handler() {
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    // ///////////////////////////////////////内部类部分////////////////////////////////////////////////////////////////
    /**
     * 动画线程
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2013-9-28 下午2:33:21 $
     */
    private class DrawThread extends Thread {
        public void run() {
            if (gifDecoder == null) {
                return;
            }

            while (isRun) {
                if (pause == false) {
                    // if(gifDecoder.parseOk()){
                    GifFrame frame = gifDecoder.next();
                    currentImage = frame.bitmap;
                    long sp = frame.delay;
                    if (redrawHandler != null) {
                        Message msg = redrawHandler.obtainMessage();
                        redrawHandler.sendMessage(msg);
                        SystemClock.sleep(sp);
                    }
                    else {
                        break;
                    }
                    // }else{
                    // currentImage = gifDecoder.getImage();
                    // break;
                    // }
                }
                else {
                    SystemClock.sleep(10);
                }
            }
        }
    }

    /**
     * 解码GIF图片的方式
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2013-9-28 下午2:37:30 $
     */
    public enum GifImageType {
        // 在解码过程中，不显示图片，直到解码全部成功后，再显示
        WAIT_FINISH(0),
        // 和解码过程同步，解码进行到哪里，图片显示到哪里
        SYNC_DECODER(1),
        // 在解码过程中，只显示第一帧图片
        COVER(2);

        GifImageType(int i) {
            nativeInt = i;
        }

        final int nativeInt;
    }

}
