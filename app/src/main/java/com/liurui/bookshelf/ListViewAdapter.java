package com.liurui.bookshelf;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szekinfung on 2019/4/13.
 * Update by LiuRui on 2019/4/16
 */

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Book> itemViews;
    Context context;
    private OnShowItemClickListener onShowItemClickListener;

    public ListViewAdapter(Context context,ArrayList<Book> itemViews){
        this.context = context;
        this.itemViews = itemViews;
    }

    public void delAll(){
        itemViews.clear();
    }
    public int getCount() {
        return itemViews.size();
    }
    public long getItemId(int position) {
        return position;
    }
    public Object getItem(int position) {
        return itemViews.get(position);
    }

    public class ViewHolder{
        ImageView BookCover;
        TextView BookName;
        TextView BookAuthorAndPublishingHouse;
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent ) {
        final Book book = itemViews.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.book_list, null);
            viewHolder.BookCover = (ImageView) convertView.findViewById(R.id.BookCover);
            viewHolder.BookName = (TextView) convertView.findViewById(R.id.BookName);
            viewHolder.BookAuthorAndPublishingHouse = (TextView) convertView.findViewById(R.id.BookAuthor);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.listview_select_cb);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        if (book.isShow()) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }

        viewHolder.BookCover.setImageResource(R.drawable.bookcover);
        viewHolder.BookName.setText(book.getName());
        viewHolder.BookAuthorAndPublishingHouse.setText(book.getAuthor() + "," + book.getPublishing_house()+"    "+book.getPublishing_time());

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    book.setChecked(true);
                } else {
                    book.setChecked(false);
                }

                onShowItemClickListener.onShowItemClick(book);
            }
        });

        viewHolder.checkBox.setChecked(book.isChecked());

        return convertView;
    }

    public interface OnShowItemClickListener {
        void onShowItemClick(Book book);
    }

    public void setOnShowItemClickListener(OnShowItemClickListener onShowItemClickListener) {
        this.onShowItemClickListener = onShowItemClickListener;
    }
}
