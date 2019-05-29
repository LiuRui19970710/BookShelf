package com.liurui.bookshelf;

import android.content.Context;

import java.util.ArrayList;

public class AddBook {
    //BookCollection bookCollection = new BookCollection();
    public Book add_book(String name)
    {
        Book book = new Book();
        book.setName(name);
        book.setAuthor("testAuthor");
        book.setPublishing_house("testpublisher");
        book.setPublishing_time("testtime");

        return book;
    }

}
