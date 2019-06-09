package com.liurui.bookshelf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
    private int id;
    private String name;
    private String author;
    private String publishing_house;
    private String publishing_time;
    private String isbn;
    private int reading_status;
    private String item_bookshelf;
    private String item_notes;
    private ArrayList<String> item_labels = new ArrayList<String>();
    private String item_website;
    private Bitmap bitmap;
    private byte[] data = new byte[0];

    private boolean isShow = false;
    private boolean isChecked = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishing_house() {
        return publishing_house;
    }

    public void setPublishing_house(String publishing_house) {
        this.publishing_house = publishing_house;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getItem_bookshelf() {
        return item_bookshelf;
    }

    public void setItem_bookshelf(String item_bookshelf) {
        this.item_bookshelf = item_bookshelf;
    }

    public List<String> getItem_labels() {
        return item_labels;
    }

    public void setItem_labels(String item_labels) {
        this.item_labels.add(item_labels);
    }

    public String getItem_notes() {
        return item_notes;
    }

    public void setItem_notes(String item_notes) {
        this.item_notes = item_notes;
    }

    public String getItem_website() {
        return item_website;
    }

    public void setItem_website(String item_website) {
        this.item_website = item_website;
    }

    public int getReading_status() {
        return reading_status;
    }

    public void setReading_status(int reading_status) {
        this.reading_status = reading_status;
    }


    public String getPublishing_time() {
        return publishing_time;
    }

    public void setPublishing_time(String publishing_time) {
        this.publishing_time = publishing_time;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public void setBitmap(byte[] Byte) {
        this.data = Byte;
    }

    public Bitmap getBitmap(){
       return BitmapFactory.decodeByteArray(this.data, 0, this.data.length);//从字节数组解码位图
    }

    public static byte[] getBytes(Bitmap bitmap){
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }
}
