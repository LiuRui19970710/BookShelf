package com.liurui.bookshelf;

import java.io.Serializable;

public class Label implements Serializable {
    private String label;
    private int id;
    public void setLabel(String label)
    {
        this.label=label;
    }
    public String getLabel()
    {
        return this.label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
