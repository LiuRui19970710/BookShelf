package com.liurui.bookshelf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


public class EditBookActivity extends Activity {
    private int index;
    private ImageView imageView;
    private EditText bookname,author,publish,year,moon,isbn,notes,weburl,label;
    private Spinner readstatue,bookshelf;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book);
        final Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        //组件绑定
        imageView = (ImageView)findViewById(R.id.edit_picture);
        bookname = (EditText)findViewById(R.id.edit_bookname);
        author = (EditText)findViewById(R.id.edit_author);
        publish = (EditText)findViewById(R.id.edit_publisher);
        year = (EditText)findViewById(R.id.edit_year);
        moon = (EditText)findViewById(R.id.edit_moon);
        isbn = (EditText)findViewById(R.id.edit_isbn);
        readstatue = (Spinner)findViewById(R.id.edit_readstatue);
        bookshelf = (Spinner)findViewById(R.id.edit_bookshelf);
        notes = (EditText)findViewById(R.id.edit_notes);
        weburl = (EditText)findViewById(R.id.edit_weburl);
        label = (EditText)findViewById(R.id.edit_label);
        //设置内容
        imageView.setImageResource(MainActivity.itemViews.get(index).getPictureid());
        bookname.setText(MainActivity.itemViews.get(index).getName());
        author.setText(MainActivity.itemViews.get(index).getAuthor());
        publish.setText(MainActivity.itemViews.get(index).getPublishing_house());
        //year.setText(MainActivity.itemViews.get(index).getYear());
        isbn.setText(MainActivity.itemViews.get(index).getIsbn());
        readstatue.setSelection(MainActivity.itemViews.get(index).getReading_status());
        //书架内容暂未设置
        notes.setText(MainActivity.itemViews.get(index).getItem_notes());
        weburl.setText(MainActivity.itemViews.get(index).getItem_website());
        label.setText(MainActivity.itemViews.get(index).getItem_labels());

        //图书图片本地查找
        //imageView.setImageResource();

    }
}
