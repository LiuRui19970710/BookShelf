package com.liurui.bookshelf;

import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BookSort {

    public void sort_by_title( )
    {
        Book temp;

        for(int i=0;i<MainActivity.itemViews.size();i++)
        {
            for(int j=i;j<MainActivity.itemViews.size();j++)
            {
                    if (MainActivity.itemViews.get(i).getName().compareTo(MainActivity.itemViews.get(j).getName()) > 0) {
                        temp = MainActivity.itemViews.get(i);
                        MainActivity.itemViews.set(i, MainActivity.itemViews.get(j));
                        MainActivity.itemViews.set(j, temp);

                }
            }
        }

    }
    public void sort_by_author( )
    {
        Book temp;

        for(int i=0;i<MainActivity.itemViews.size();i++)
        {
            for(int j=i;j<MainActivity.itemViews.size();j++)
            {
                if (MainActivity.itemViews.get(i).getAuthor().compareTo(MainActivity.itemViews.get(j).getAuthor()) > 0) {
                    temp = MainActivity.itemViews.get(i);
                    MainActivity.itemViews.set(i, MainActivity.itemViews.get(j));
                    MainActivity.itemViews.set(j, temp);

                }
            }
        }

    }
    public void sort_by_publish( )
    {
        Book temp;

        for(int i=0;i<MainActivity.itemViews.size();i++)
        {
            for(int j=i;j<MainActivity.itemViews.size();j++)
            {
                if (MainActivity.itemViews.get(i).getPublishing_house().compareTo(MainActivity.itemViews.get(j).getPublishing_house()) > 0) {
                    temp = MainActivity.itemViews.get(i);
                    MainActivity.itemViews.set(i, MainActivity.itemViews.get(j));
                    MainActivity.itemViews.set(j, temp);

                }
            }
        }
    }
    public void sort_by_time( )
    {
        Book temp;

        for(int i=0;i<MainActivity.itemViews.size();i++)
        {
            for(int j=i;j<MainActivity.itemViews.size();j++)
            {
                if (MainActivity.itemViews.get(i).getPublishing_time().compareTo(MainActivity.itemViews.get(j).getPublishing_time()) > 0) {
                    temp = MainActivity.itemViews.get(i);
                    MainActivity.itemViews.set(i, MainActivity.itemViews.get(j));
                    MainActivity.itemViews.set(j, temp);

                }
            }
        }

    }
}
