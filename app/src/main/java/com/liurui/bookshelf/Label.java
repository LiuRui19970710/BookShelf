package com.liurui.bookshelf;

import java.io.Serializable;

public class Label implements Serializable {
    private String label;
    public void setLabel(String label)
    {
        this.label=label;
    }
    public String getLabel()
    {
        return this.label;
    }
}
