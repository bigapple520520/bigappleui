package com.dazzle.bigappleui.view.roundedimageview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.dazzle.bigappleui.utils.LogUtils;

/**
 * 这个是基于ImageView实现的可设置圆角图片控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-9-22 下午3:25:01 $
 */
public class RoundedImageView extends ImageView {
    public static final String TAG = "RoundedImageView";

    public static final float DEFAULT_RADIUS = 0f;
    public static final float DEFAULT_BORDER_WIDTH = 0f;

    /** 圆角的幅度，越大圆角就越圆，默认0 */
    private float cornerRadius = DEFAULT_RADIUS;

    /** 边宽的宽度，默认0 */
    private float borderWidth = DEFAULT_BORDER_WIDTH;

    /** 边框颜色，默认黑色 */
    private ColorStateList borderColor = ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);

    /** 是否画椭圆，如果设置了这个true，那么就会忽略cornerRadius这个圆角参数 */
    private boolean isOval = false;

    /** 背景是否可变，这个参数好像暂时没起作用 */
    private boolean mutateBackground = false;

    /** ImageView中的src资源resid */
    private int mResource;
    /** ImageView中的src资源对象 */
    private Drawable mDrawable;
    /** ImageView中的背景资源对象 */
    private Drawable mBackgroundDrawable;

    /** ImageView的图片显示类型，这里都默认会调整成ScaleType.FIT_XY */
    private ScaleType mScaleType;

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.FIT_CENTER);
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(true);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        assert scaleType != null;

        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
            case CENTER:
            case CENTER_CROP:
            case CENTER_INSIDE:
            case FIT_CENTER:
            case FIT_START:
            case FIT_END:
            case FIT_XY:
                super.setScaleType(ScaleType.FIT_XY);
                break;
            default:
                super.setScaleType(scaleType);
                break;
            }

            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mResource = 0;
        mDrawable = RoundedDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mResource = 0;
        mDrawable = RoundedDrawable.fromBitmap(bm);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageResource(int resId) {
        if (mResource != resId) {
            mResource = resId;
            mDrawable = resolveResource();
            updateDrawableAttrs();
            super.setImageDrawable(mDrawable);
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    // ///////////////////////////////////////////设置圆角度数///////////////////////////////////////////
    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int resId) {
        setCornerRadius(getResources().getDimension(resId));
    }

    public void setCornerRadius(float radius) {
        if (cornerRadius == radius) {
            return;
        }

        cornerRadius = radius;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
    }

    // ////////////////////////////////////////////////////设置边框的宽度和颜色//////////////////////////////////////
    /**
     * 获取边界宽度
     * 
     * @return
     */
    public float getBorderWidth() {
        return borderWidth;
    }

    /**
     * 设置边界宽度。注意：使用资源文件中的resid，会根据不同手机分辨率进行调整
     * 
     * @param resId
     */
    public void setBorderWidth(int resId) {
        setBorderWidth(getResources().getDimension(resId));
    }

    /**
     * 设置边界宽度。单位是px
     * 
     * @param width
     */
    public void setBorderWidth(float width) {
        if (borderWidth == width) {
            return;
        }

        borderWidth = width;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    /**
     * 获取边界的默认颜色
     * 
     * @return
     */
    public int getBorderColor() {
        return borderColor.getDefaultColor();
    }

    /**
     * 设置边界颜色
     * 
     * @param color
     */
    public void setBorderColor(int color) {
        setBorderColor(ColorStateList.valueOf(color));
    }

    /**
     * 获取边界颜色列表
     * 
     * @return
     */
    public ColorStateList getBorderColors() {
        return borderColor;
    }

    /**
     * 设置边界颜色列表
     * 
     * @param colors
     */
    public void setBorderColor(ColorStateList colors) {
        if (borderColor.equals(colors)) {
            return;
        }

        borderColor = (colors != null) ? colors : ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        if (borderWidth > 0) {
            invalidate();
        }
    }

    // /////////////////////////////////////////设置是否变成椭圆/////////////////////////////////////////////
    /**
     * 获取是否绘制成椭圆
     * 
     * @return
     */
    public boolean isOval() {
        return isOval;
    }

    /**
     * 设置是否绘制成椭圆，如果设置成true，就会忽略圆角弧度参数
     * 
     * @param oval
     */
    public void setOval(boolean oval) {
        isOval = oval;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    // //////////////////////////////////////////设置背景是否可变///////////////////////////////////////////
    /**
     * 获取背景的可变性
     * 
     * @return
     */
    public boolean isMutateBackground() {
        return mutateBackground;
    }

    /**
     * 设置背景是否可变
     * 
     * @param mutate
     */
    public void setMutateBackground(boolean mutate) {
        if (mutateBackground == mutate) {
            return;
        }

        mutateBackground = mutate;
        updateBackgroundDrawableAttrs(true);
        invalidate();
    }

    // ////////////////////////////////////////////////////////内部方法///////////////////////////////////////////
    /** 根据资源resid解析出资源对象 */
    private Drawable resolveResource() {
        Resources res = getResources();
        if (null == res) {
            return null;
        }

        Drawable drawable = null;
        if (mResource != 0) {
            try {
                drawable = res.getDrawable(mResource);
            }
            catch (Exception e) {
                LogUtils.e(TAG, "Can not find resource:" + mResource, e);
                mResource = 0;
            }
        }
        return RoundedDrawable.fromDrawable(drawable);
    }

    /** 更新src指定的资源属性，主要把该控件中的一些参数设置到RoundedDrawable中去 */
    private void updateDrawableAttrs() {
        updateAttrs(mDrawable);
    }

    /** 更新背景资源属性，主要把该控件中的一些参数设置到RoundedDrawable中去 */
    private void updateBackgroundDrawableAttrs(boolean convert) {
        if (mutateBackground) {
            if (convert) {
                mBackgroundDrawable = RoundedDrawable.fromDrawable(mBackgroundDrawable);
            }
            updateAttrs(mBackgroundDrawable);
        }
    }

    /** 更新资源的属性，主要把该控件中的一些参数设置到RoundedDrawable中去 */
    private void updateAttrs(Drawable drawable) {
        if (null == drawable) {
            return;
        }

        if (drawable instanceof RoundedDrawable) {
            ((RoundedDrawable) drawable).setScaleType(mScaleType).setCornerRadius(cornerRadius)
                    .setBorderWidth(borderWidth).setBorderColor(borderColor).setOval(isOval);
        }
        else if (drawable instanceof LayerDrawable) {
            // 遍历修改所有图层的参数
            LayerDrawable ld = ((LayerDrawable) drawable);
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i));
            }
        }
    }

}
