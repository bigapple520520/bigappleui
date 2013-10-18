package com.example.bigappleui_demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_);

        ProgressDialog progressDialog = new ProgressDialog(this.getApplicationContext());
        progressDialog.show();
    }

}
