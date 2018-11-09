package com.sample.mdialog;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.poping520.open.mdialog.MDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MDialog mDialog = new MDialog.Builder(this)
                .setHeaderBgColor(Color.BLACK)
                .setHeaderPic(R.drawable.ic_android_black_48dp)
                .setTitle("Hello Android")
                .setMessage("This's a sample for MDialog")
                .setCancelable(false)
                .setPositiveButton("OK", true, null)
                .setNegativeButton("NO", null)
                .setNeutralButton("NEVER", null)
                .create();

        mDialog.setMessage("New Message");
        mDialog.show();

    }
}
