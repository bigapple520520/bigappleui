package com.example.bigappleui_demo.gifview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.dazzle.bigappleui.gifview.GifView;
import com.example.bigappleui_demo.R;

/**
 * 显示gif图片demo
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-28 下午3:03:58 $
 */
public class Main extends Activity {

    private boolean pause = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_gifview);

        final GifView gif = (GifView) findViewById(R.id.gif);
        gif.setShowDimension(300, 300);
        gif.setGifImage(R.drawable.gif_pic);
        gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pause) {
                    gif.showCover();
                    pause = false;
                }
                else {
                    gif.showAnimation();
                    pause = true;
                }
            }
        });
    }

}
