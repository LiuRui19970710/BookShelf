package com.liurui.bookshelf;

import java.io.Serializable;

/**
 * Created by szekinfung on 2019/5/29.
 */

public class Shelf implements Serializable {
    private String shelf;

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

}
