package com.liurui.bookshelf;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;


import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class BatchScanFragment extends Fragment {

    private ZXingScannerView mScannerView;
    private ZXingScannerView.ResultHandler mResultHandler = new ZXingScannerView.ResultHandler() {
        @Override
        public void handleResult(Result result) {
            //这里做获取isbn的相关操作，isbn的获取方式为result.getText();
            initData(result.getText());
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            //获取AlertDialog对象
            dialog.setTitle("提示");//设置标题
            dialog.setMessage("是否扫下一本书");//设置信息具体内容
            dialog.setCancelable(true);//设置是否可取消
            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mScannerView.resumeCameraPreview(mResultHandler); //完成之后重新开始扫码，实现连续扫码功能。
                }
            });
            dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();

        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getContext());
        mScannerView.setResultHandler(mResultHandler);

        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
    private void initData(final String ISBN) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 写子线程中的操作
                String information = "", name, isbn;
                Scanning scanning = new Scanning();
                try {
                    information = scanning.main(ISBN);

                    GetString getString = new GetString(information);
                    Log.i("test", "run: " + getString.getName() + " " + getString.getISBN() + " " + getString.getAuthor() + " " + getString.getPublisher()
                            + " " + getString.getPubTime() + " " + getString.getImgPath());
                    if (getString.getName() != "" && getString.getISBN() != "" && getString.getAuthor() != "" && getString.getPublisher() != ""
                            && getString.getPubTime() != "" && getString.getImgPath() != "") {
                        DownLoadImg downLoadImg = new DownLoadImg();
                        Book book = new Book();
                        book.setBitmap(Book.getBytes(downLoadImg.DownloadImg(getString.getImgPath())));
                        book.setAuthor(getString.getAuthor());
                        book.setIsbn(getString.getISBN());
                        book.setName(getString.getName());
                        book.setPublishing_house(getString.getPublisher());
                        book.setPublishing_time(getString.getPubTime());

                        EventBus.getDefault().postSticky(book);

                        Looper.prepare();


                        //日后可删掉
                        Toast.makeText(getActivity(), book.getName() + " " + book.getIsbn(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(getActivity(), "扫描信息错误", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
