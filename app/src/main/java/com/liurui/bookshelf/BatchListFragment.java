package com.liurui.bookshelf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class BatchListFragment extends Fragment {
    private ListView listView;
    private ArrayList<Book> scannerbooks=new ArrayList<>();
    private MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_batch_list, container, false);
        listView = view.findViewById(R.id.batch_list);
        myAdapter = new MyAdapter(getContext());
        listView.setAdapter(myAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int index, long l) {
                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(getContext());
                //获取AlertDialog对象
                dialog.setTitle("提示");//设置标题
                dialog.setMessage("是否删除该书");//设置信息具体内容
                dialog.setCancelable(true);//设置是否可取消
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scannerbooks.remove(index);
                        myAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
                return false;
            }
        });
        EventBus.getDefault().register(this);//粘性事件在注册的时候就会对其响应，所以在相应事件中所用的对象必须都初始化
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(Book book) {
        scannerbooks.add(book);
        Toast.makeText(getContext(),book.getName(),Toast.LENGTH_SHORT).show();
        myAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    class MyAdapter extends BaseAdapter{
        Context context;

        MyAdapter(Context context){
            this.context = context;
        }
        @Override
        public int getCount() {
            return scannerbooks.size();
        }

        @Override
        public Object getItem(int i) {
            return scannerbooks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.batchadd_item, viewGroup,false);
                viewHolder.BookCover = (ImageView) view.findViewById(R.id.batchadd_BookCover);
                viewHolder.BookName = (TextView) view.findViewById(R.id.batchadd_BookName);
                viewHolder.BookAuthorAndPublishingHouse = (TextView) view.findViewById(R.id.batchadd_BookAuthor);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.BookCover.setImageBitmap(scannerbooks.get(i).getBitmap());
            viewHolder.BookName.setText(scannerbooks.get(i).getName());
            viewHolder.BookAuthorAndPublishingHouse.setText(scannerbooks.get(i).getAuthor() + "," + scannerbooks.get(i).getPublishing_house()+"    "+scannerbooks.get(i).getPublishing_time());
            return view;
        }
    }
    public class ViewHolder{
        ImageView BookCover;
        TextView BookName;
        TextView BookAuthorAndPublishingHouse;
    }
}
