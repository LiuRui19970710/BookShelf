package com.liurui.bookshelf;

import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BookSort {

    public ArrayList<Book> sort_by_title( ArrayList<Book> itemView)
    {
        Book temp;

        for(int i=0;i<itemView.size();i++)
        {
            for(int j=i;j<itemView.size();j++)
            {
                    if (itemView.get(i).getName().compareTo(itemView.get(j).getName()) > 0) {
                        temp = itemView.get(i);
                        itemView.set(i, itemView.get(j));
                        itemView.set(j, temp);

                }
            }
        }

        return itemView;
    }
    public ArrayList<Book> sort_by_author( ArrayList<Book> itemView)
    {
        Book temp;

        for(int i=0;i<itemView.size();i++)
        {
            for(int j=i;j<itemView.size();j++)
            {
                if (itemView.get(i).getAuthor().compareTo(itemView.get(j).getAuthor()) > 0) {
                    temp = itemView.get(i);
                    itemView.set(i, itemView.get(j));
                    itemView.set(j, temp);

                }
            }
        }

        return itemView;
    }
    public ArrayList<Book> sort_by_publish( ArrayList<Book> itemView)
    {
        Book temp;

        for(int i=0;i<itemView.size();i++)
        {
            for(int j=i;j<itemView.size();j++)
            {
                if (itemView.get(i).getPublishing_house().compareTo(itemView.get(j).getPublishing_house()) > 0) {
                    temp = itemView.get(i);
                    itemView.set(i, itemView.get(j));
                    itemView.set(j, temp);

                }
            }
        }

        return itemView;
    }
    public ArrayList<Book> sort_by_time( ArrayList<Book> itemView)
    {
        Book temp;

        for(int i=0;i<itemView.size();i++)
        {
            for(int j=i;j<itemView.size();j++)
            {
                if (itemView.get(i).getPublishing_time().compareTo(itemView.get(j).getPublishing_time()) > 0) {
                    temp = itemView.get(i);
                    itemView.set(i, itemView.get(j));
                    itemView.set(j, temp);

                }
            }
        }

        return itemView;
    }
}
