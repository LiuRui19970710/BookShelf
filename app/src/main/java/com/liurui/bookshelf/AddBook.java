package com.liurui.bookshelf;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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

    public Book add_book(String name, String Author, String Publisher, String Time, Bitmap bitmap,String isbn) {
        Book book = new Book();
        book.setId(sid++);
        ArrayList<Integer> list_sid = new ArrayList<>();
        list_sid.add(sid);
        collection_sid.save(context,list_sid);
        Log.d("fuck",String.valueOf(sid));

        book.setName(name);
        book.setAuthor(Author);
        book.setPublishing_house(Publisher);
        book.setPublishing_time(Time);
        book.setBitmap(getBytes(bitmap));
        book.setItem_bookshelf("默认书架");
        book.setIsbn(isbn);
        return book;
    }

    public static byte[] getBytes(Bitmap bitmap){
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }
}
