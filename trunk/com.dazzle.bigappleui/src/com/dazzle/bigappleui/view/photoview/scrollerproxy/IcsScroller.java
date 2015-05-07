package com.dazzle.bigappleui.view.photoview.scrollerproxy;

import android.content.Context;

//@TargetApi(14)
public class IcsScroller extends GingerScroller {

    public IcsScroller(Context context) {
        super(context);
    }

    @Override
    public boolean computeScrollOffset() {
        return mScroller.computeScrollOffset();
    }

}
