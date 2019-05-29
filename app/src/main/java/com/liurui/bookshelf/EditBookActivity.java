package com.liurui.bookshelf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;


public class EditBookActivity extends Activity {
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 本地
    private static final int PHOTO_RESOULT = 3;// 结果
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private String change_path = "/peoplechanged";
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
        //imageView.setImageResource(MainActivity.itemViews.get(index).getPictureid());
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

        //解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //图书图片编辑
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(EditBookActivity.this);
                //获取AlertDialog对象
                dialog.setTitle("更换封面");//设置标题
                dialog.setMessage("请选择从本地相册读取或者拍照");//设置信息具体内容
                dialog.setCancelable(true);//设置是否可取消
                dialog.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override//设置拍照事件的事件
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String filePath = Environment.getExternalStorageDirectory()+change_path;
                        File localFile = new File(filePath);
                        if (!localFile.exists()) {
                            localFile.mkdir();
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory()+change_path
                                ,"temp.jpg")));
                        startActivityForResult(intent, PHOTO_GRAPH);
                    }
                });
                dialog.setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override//设置从本地读取图片事件
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                        startActivityForResult(intent, PHOTO_ZOOM);
                    }
                }).show();
            }
        });

    }
}
