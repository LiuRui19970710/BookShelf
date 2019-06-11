package com.liurui.bookshelf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.ArrayList;

public class LeftListItem {
    public boolean[] LeftListSearch(ArrayList<Book> all_bookL_ist, String searchStr){
        boolean[] res_List = new boolean[all_bookL_ist.size()];

        for(int i=0;i<all_bookL_ist.size();i++){
//            if(all_bookL_ist.get(i).getName().contains(searchStr)||all_bookL_ist.get(i).getAuthor().equals(searchStr)||all_bookL_ist.get(i).getPublishing_house().equals(searchStr)||all_bookL_ist.get(i).getItem_notes().equals(searchStr))
            if(all_bookL_ist.get(i).getName().contains(searchStr))
                res_List[i] = true;
            else
                res_List[i] = false;
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
