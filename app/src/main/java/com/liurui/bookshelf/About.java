package com.liurui.bookshelf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class About extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbarabout);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
