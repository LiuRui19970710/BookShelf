package com.liurui.bookshelf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.klinker.android.sliding.SlidingActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Book_Detail_info_Activity extends SlidingActivity {
    private TextView author_name, publisher, data, isbn, reading_status, item_bookshelf, item_notes, item_labels, item_website, item_addDate;
    private android.widget.ImageView picture;
    private int index;
    private ArrayList<Label> labels;
    private Book book;
    private Button delBtn;
    //private Button edit;


    @Override
    public void init( Bundle savedInstanceState) {
        setPrimaryColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        setContent(R.layout.book_info);
        setHeaderContent(R.layout.book_info_header);
        setFab(
                getResources().getColor(R.color.colorAccent),
                R.drawable.pen,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1=new Intent();
                        intent1.setClass(Book_Detail_info_Activity.this,EditBookActivity.class);
                        Bundle bundle =new Bundle();
                        bundle.putSerializable("label",labels);
                        intent1.putExtra("index",index);
                        intent1.putExtras(bundle);
                        intent1.putExtra("method_start","detail");
                        startActivity(intent1);
                    }
                }
        );
        //绑定组件
//        book_name = (TextView) findViewById(R.id.book_name);
        author_name = (TextView) findViewById(R.id.book_info_item_author_name);
        publisher = (TextView) findViewById(R.id.book_info_item_publisher_name);
        data = (TextView) findViewById(R.id.book_info_item_date_number);
        isbn = (TextView) findViewById(R.id.book_info_item_isbn_number);
        reading_status = (TextView) findViewById(R.id.book_detail_item_reading_status_text);
        item_bookshelf = (TextView) findViewById(R.id.book_detail_item_bookshelf_name);
        item_notes = (TextView) findViewById(R.id.book_detail_item_notes_text);
        item_labels = (TextView) findViewById(R.id.book_detail_item_labels_text);
        item_website = (TextView) findViewById(R.id.book_detail_item_website_text);
        picture = (ImageView)findViewById(R.id.picture);
        delBtn = (Button)findViewById(R.id.del_btn);
        item_addDate = (TextView)findViewById(R.id.book_info_addDate);
    //    edit = (Button)findViewById(R.id.edit);

        //刷新数据
        final Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        labels = (ArrayList<Label>) intent.getSerializableExtra("label");
        book = (Book) intent.getSerializableExtra("book");
        setTitle(MainActivity.itemViews.get(index).getName());
//        book_name.setText(MainActivity.itemViews.get(index).getName());
        author_name.setText(MainActivity.itemViews.get(index).getAuthor());
        publisher.setText(MainActivity.itemViews.get(index).getPublishing_house());
        data.setText(MainActivity.itemViews.get(index).getPublishing_time());
        isbn.setText(MainActivity.itemViews.get(index).getIsbn());
        reading_status.setText(this.getResources().getStringArray(R.array.readstatue)[MainActivity.itemViews.get(index).getReading_status()]);
        item_website.setText(MainActivity.itemViews.get(index).getItem_website());

        String txt = "";
        for(int i=0;i<MainActivity.itemViews.get(index).getItem_labels().size();i++)
        {
            txt+=(MainActivity.itemViews.get(index).getItem_labels().get(i)+"  ");
        }
        item_labels.setText(txt);
        item_bookshelf.setText(MainActivity.itemViews.get(index).getItem_bookshelf());
        item_notes.setText(MainActivity.itemViews.get(index).getItem_notes());
        picture.setImageBitmap(MainActivity.itemViews.get(index).getBitmap());
        //彩蛋
        SimpleDateFormat Fmt=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");
        String book_addDate = Fmt.format(MainActivity.itemViews.get(index).getItem_date());
        item_addDate.setText(book_addDate);


        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Book_Detail_info_Activity.this);
                builder.setTitle("确认删除此书？").setNeutralButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.itemViews.remove(index);
                        onBackPressed();
                    }
                });
                builder.show();
            }
        });

     /*   edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent();
                intent1.setClass(Book_Detail_info_Activity.this,EditBookActivity.class);
                Bundle bundle =new Bundle();
                bundle.putSerializable("label",labels);
                intent1.putExtra("index",index);
                intent1.putExtras(bundle);
                intent1.putExtra("method_start","detail");
                startActivity(intent1);
            }
        });*/


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setTitle(MainActivity.itemViews.get(index).getName());
 //       book_name.setText(MainActivity.itemViews.get(index).getName());
        author_name.setText(MainActivity.itemViews.get(index).getAuthor());
        publisher.setText(MainActivity.itemViews.get(index).getPublishing_house());
        data.setText(MainActivity.itemViews.get(index).getPublishing_time());
        isbn.setText(MainActivity.itemViews.get(index).getIsbn());
        reading_status.setText(this.getResources().getStringArray(R.array.readstatue)[MainActivity.itemViews.get(index).getReading_status()]);
        item_website.setText(MainActivity.itemViews.get(index).getItem_website());
        String txt = "";
        for(int i=0;i<MainActivity.itemViews.get(index).getItem_labels().size();i++)
        {
            txt+=(MainActivity.itemViews.get(index).getItem_labels().get(i)+"  ");
        }
        item_labels.setText(txt);
        item_bookshelf.setText(MainActivity.itemViews.get(index).getItem_bookshelf());
        item_notes.setText(MainActivity.itemViews.get(index).getItem_notes());
    }
}
