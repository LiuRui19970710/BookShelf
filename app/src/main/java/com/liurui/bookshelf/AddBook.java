package com.liurui.bookshelf;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddBook {
    Context context;
    private int sid;
    Collection collection_sid = new Collection("SID");

    public AddBook(Context context){
        this.context = context;
        ArrayList<Integer> tmp_sid = collection_sid.read(this.context);
        if(tmp_sid.size()==0)
            sid = 0;
        else
            sid = tmp_sid.get(0);
    }

    public Book add_book(String name) {
        Book book = new Book();
        book.setId(sid++);
        ArrayList<Integer> list_sid = new ArrayList<>();
        list_sid.add(sid);
        collection_sid.save(context,list_sid);
        Log.d("fuck",String.valueOf(sid));

        book.setName(name);
        book.setAuthor("testAuthor");
        book.setPublishing_house("testpublisher");
        book.setPublishing_time("testtime");
        return book;
    }

}
