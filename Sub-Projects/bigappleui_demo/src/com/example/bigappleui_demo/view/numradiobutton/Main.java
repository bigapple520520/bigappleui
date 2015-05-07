package com.example.bigappleui_demo.view.numradiobutton;

import android.app.Activity;
import android.os.Bundle;

import com.dazzle.bigappleui.view.NumRadioButton;
import com.example.bigappleui_demo.R;

/**
 * 设置圆角图片控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-28 下午3:57:15 $
 */
public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_numradiobutton);

        NumRadioButton numRadioButton = (NumRadioButton) findViewById(R.id.numradiobutton);
        numRadioButton.setNum(7, R.drawable.msg_new);
    }

}
