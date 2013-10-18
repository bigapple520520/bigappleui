package com.example.bigappleui_demo.view.lettersearchbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.dazzle.bigappleui.view.LetterSearchBar;
import com.dazzle.bigappleui.view.LetterSearchBar.OnLetterChange;
import com.dazzle.bigappleui.view.LetterSearchBar.OutLetterSeacherBar;
import com.example.bigappleui_demo.R;

public class Main extends Activity {

    private LetterSearchBar letterSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_lettersearchbar);

        LetterSearchBar letterSearchBar = (LetterSearchBar) findViewById(R.id.letterSearchBar);

        letterSearchBar.setLetterArray(new String[] { "hhhhh", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" });

        letterSearchBar.setOnLetterChange(new OnLetterChange() {
            @Override
            public void letterChange(String letter) {
                Log.d("", "----------------------------" + letter);
            }
        });
        letterSearchBar.setOutLetterSeacherBar(new OutLetterSeacherBar() {
            @Override
            public void outBar(String lastLetter) {
                Log.d("", "-----------------------我出来啦:" + lastLetter);
            }
        });
    }

}
