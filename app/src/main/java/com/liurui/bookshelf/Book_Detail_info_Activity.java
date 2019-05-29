package com.liurui.bookshelf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class Book_Detail_info_Activity extends Activity {
    private TextView book_name, author_name, publisher, data, isbn, reading_status, item_bookshelf, item_notes, item_labels, item_website;
    private android.widget.ImageView picture;
    private int index;
    private Book book;
    private Button edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info);
        //绑定组件
        book_name = (TextView) findViewById(R.id.book_name);
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
        edit = (Button)findViewById(R.id.edit);
        //刷新数据
        final Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        book = (Book) intent.getSerializableExtra("book");
        book_name.setText(book.getName());
        author_name.setText(book.getAuthor());
        publisher.setText(book.getPublishing_house());
        //data.setText(book.getYear()+"-"+book.getMoon());
        isbn.setText(book.getIsbn());
        reading_status.setText(this.getResources().getStringArray(R.array.readstatue)[book.getReading_status()]);
        item_website.setText(book.getItem_website());
        item_labels.setText(book.getItem_labels());
        item_bookshelf.setText(book.getItem_bookshelf());
        item_notes.setText(book.getItem_notes());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent();
                intent1.setClass(Book_Detail_info_Activity.this,EditBookActivity.class);
                intent1.putExtra("index",index);
                startActivity(intent1);
            }
        });


    }
}
