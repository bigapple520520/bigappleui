package com.dazzle.bigappleui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.dazzle.bigappleui.view.SlipButton;
import com.dazzle.bigappleui.view.SlipButton.OnChangedListener;

public class Main extends Activity {

    private SlipButton slipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
