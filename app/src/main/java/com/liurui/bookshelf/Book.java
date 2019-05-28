package com.liurui.bookshelf;

import java.io.Serializable;

public class Book implements Serializable {
    private String name;
    private String author;
    private String publishing_house;
    private String publishing_time;
    private String isbn;
    private String reading_status;
    private String item_bookshelf;
    private String item_notes;
    private String item_labels;
    private String item_website;

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

    public String getPublishing_time() {
        return publishing_time;
    }

    public void setPublishing_time(String publishing_time) {
        this.publishing_time = publishing_time;
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

    public String getItem_labels() {
        return item_labels;
    }

    public void setItem_labels(String item_labels) {
        this.item_labels = item_labels;
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

    public String getReading_status() {
        return reading_status;
    }

    public void setReading_status(String reading_status) {
        this.reading_status = reading_status;
    }
}
