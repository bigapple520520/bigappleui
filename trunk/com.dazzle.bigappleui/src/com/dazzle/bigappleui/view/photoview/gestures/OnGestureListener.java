package com.dazzle.bigappleui.view.photoview.gestures;

/**
 * 手势发生接口
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-4 上午11:12:47 $
 */
public interface OnGestureListener {

    /**
     * 手势拖动触发
     * 
     * @param dx
     * @param dy
     */
    public void onDrag(float dx, float dy);

    /**
     * 手势快滑触发
     * 
     * @param startX
     * @param startY
     * @param velocityX
     * @param velocityY
     */
    public void onFling(float startX, float startY, float velocityX, float velocityY);

    /**
     * 手势缩放触发
     * 
     * @param scaleFactor
     * @param focusX
     * @param focusY
     */
    public void onScale(float scaleFactor, float focusX, float focusY);

}
