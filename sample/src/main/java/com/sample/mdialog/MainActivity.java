package com.sample.mdialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.poping520.open.mdialog.MDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(v -> {
            final MDialog mDialog = new MDialog.Builder(MainActivity.this)
                    .setHeaderBgColorRes(R.color.colorPrimary)
                    .setHeaderPic(R.drawable.ic_android_black_48dp)
                    .setTitle("Hello Android")
                    .setHTMLMessage("NORMAL <b>BOLD</b>")
                    .setCancelable(false)
                    .setPositiveButton("OK OK", true, null)
                    .setNegativeButton("NO NO", null)
                    .setNeutralButton("NEVER", null)
                    .create();

            mDialog.show();
        });
    }
}
