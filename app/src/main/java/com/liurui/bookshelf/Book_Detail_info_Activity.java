package com.liurui.bookshelf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;


public class Book_Detail_info_Activity extends Activity{
    private TextView book_name,author_name,publisher,data,isbn,reading_status,item_bookshelf,item_notes,item_labels,item_website;
    private int index;
    private Book book;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info);
        //绑定组件
        book_name=(TextView)findViewById(R.id.book_name);
        author_name=(TextView)findViewById(R.id.book_info_item_author_name);
        publisher=(TextView)findViewById(R.id.book_info_item_publisher_name);
        data=(TextView)findViewById(R.id.book_info_item_date_number);
        isbn=(TextView)findViewById(R.id.book_info_item_isbn_number);
        reading_status=(TextView)findViewById(R.id.book_detail_item_reading_status_text);
        item_bookshelf=(TextView)findViewById(R.id.book_detail_item_bookshelf_name);
        item_notes=(TextView)findViewById(R.id.book_detail_item_notes_text);
        item_labels=(TextView)findViewById(R.id.book_detail_item_labels_text);
        item_website=(TextView)findViewById(R.id.book_detail_item_website_text);

        final Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        book = (Book) intent.getSerializableExtra("book");
        book_name.setText(book.getName());
        author_name.setText(book.getAuthor());
        publisher.setText(book.getPublishing_house());
        data.setText(book.getPublishing_time());
        isbn.setText(book.getIsbn());
        reading_status.setText(book.getReading_status());
        item_website.setText(book.getItem_website());
        item_labels.setText(book.getItem_labels());
        item_bookshelf.setText(book.getItem_bookshelf());
        item_notes.setText(book.getItem_notes());

    }
}
