package com.liurui.bookshelf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.ArrayList;

public class LeftListItem {
    public ArrayList<Book> LeftListSearch(ArrayList<Book> all_bookL_ist, String searchStr){
        Book temp;
        ArrayList<Book> res_List = new ArrayList<>();
        for(int i=0;i<all_bookL_ist.size();i++){
            temp = all_bookL_ist.get(i);
            if(temp.getName().contains(searchStr))
                res_List.add(temp);
        }
        return res_List;
    }


    public void donate(final Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("捐赠");
        builder.setMessage("您的捐赠是我们继续努力的动力所在，我的支付宝xxxxxxxxxx，感谢你的支持");
        builder.setPositiveButton("复制账号", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context,"复制成功",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    public void set(){

    }
    public void about(Context context){

    }
}
