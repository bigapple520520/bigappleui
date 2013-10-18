package com.example.bigappleui_demo.view.slipbutton;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.dazzle.bigappleui.view.SlipButton;
import com.dazzle.bigappleui.view.SlipButton.OnChangedListener;
import com.example.bigappleui_demo.R;

/**
 * 滑块控件测试，如要图片如下：<br>
 * slip_bg_off.png<br>
 * slip_bg_on.png<br>
 * slip_btn_off.png<br>
 * slip_btn_on.png
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-14 下午2:13:17 $
 */
public class Main extends Activity {

    private SlipButton slipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_slipbutton);

        SlipButton slipButton = (SlipButton) findViewById(R.id.slipButton);

        slipButton.SetOnChangedListener(new OnChangedListener() {
            @Override
            public void OnChanged(boolean state) {
                if (state) {
                    Toast.makeText(Main.this, "我被选中了YES", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Main.this, "我被取消了NO", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
