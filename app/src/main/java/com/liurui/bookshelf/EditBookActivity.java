package com.liurui.bookshelf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class EditBookActivity extends Activity {
    private int NEW_SHELF = 0;
    private int NEW_SHELF_INDEX = 0;
    private static final int NONE = 0;
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 本地
    private static final int PHOTO_RESOULT = 3;// 结果
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private String change_path = "/peoplechanged";
    private int index;
    private ImageView imageView;
    private EditText bookname, author, publish, year, moon, isbn, notes, weburl, label;
    private Spinner readstatue, bookshelf;
    private Uri imageUri;
    Toolbar toolbar;
    Collection collection_Shelf = new Collection("Shelf");
    ArrayList<String> spinner_list = new ArrayList<>();
    ArrayList<Shelf> shelfs;
    private int nowIndex;
    private ArrayList<Label> labels;
    private Button button_save;
    private String sign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book);
        final Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        sign = intent.getStringExtra("method_start");
        labels = (ArrayList<Label>) intent.getSerializableExtra("label");
        //组件绑定
        imageView = (ImageView) findViewById(R.id.edit_picture);
        bookname = (EditText) findViewById(R.id.edit_bookname);
        author = (EditText) findViewById(R.id.edit_author);
        publish = (EditText) findViewById(R.id.edit_publisher);
        year = (EditText) findViewById(R.id.edit_year);
        moon = (EditText) findViewById(R.id.edit_moon);
        isbn = (EditText) findViewById(R.id.edit_isbn);
        readstatue = (Spinner) findViewById(R.id.edit_readstatue);
        bookshelf = (Spinner) findViewById(R.id.edit_bookshelf);
        notes = (EditText) findViewById(R.id.edit_notes);
        weburl = (EditText) findViewById(R.id.edit_weburl);
        label = (EditText) findViewById(R.id.edit_label);
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        button_save = (Button)findViewById(R.id.button_save);

        final ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_list);  //创建一个数组适配器
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式

        //设置内容
        //imageView.setImageResource(MainActivity.itemViews.get(index).getPictureid());
        bookname.setText(MainActivity.itemViews.get(index).getName());
        author.setText(MainActivity.itemViews.get(index).getAuthor());
        publish.setText(MainActivity.itemViews.get(index).getPublishing_house());
        toolbar.setTitle("编辑书籍详情");
        //year.setText(MainActivity.itemViews.get(index).getYear());
        isbn.setText(MainActivity.itemViews.get(index).getIsbn());
        readstatue.setSelection(MainActivity.itemViews.get(index).getReading_status());
        if(MainActivity.itemViews.get(index).getBitmap()!=null){
            imageView.setImageBitmap(MainActivity.itemViews.get(index).getBitmap());
        }
        //书架内容
        shelfs = collection_Shelf.read(EditBookActivity.this);
        for (int index = 0; index < shelfs.size(); index++)
            spinner_list.add(shelfs.get(index).getShelf());
        bookshelf.setAdapter(spinner_adapter);
        spinner_list.add("添加新书架");


        //其他
        notes.setText(MainActivity.itemViews.get(index).getItem_notes());
        weburl.setText(MainActivity.itemViews.get(index).getItem_website());
        label.setText(MainActivity.itemViews.get(index).getItem_labels());

        //解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //标签的点击事件
        label.setInputType(InputType.TYPE_NULL);
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditBookActivity.this);
                //获取AlertDialog对象
                dialog.setTitle("选择标签");//设置标题
                final String[] labelarray = new String[labels.size()];
                for (int i = 0;i<labels.size();i++){
                    labelarray[i]=labels.get(i).getLabel();
                }
                final boolean[] isChecked = new boolean[labelarray.length];
                dialog.setMultiChoiceItems(labelarray, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                    }
                });
                dialog.setCancelable(true);//设置是否可取消
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder temp =new StringBuilder();
                        for (int x = 0 ;x<isChecked.length;x++){
                            if(isChecked[x]){
                                temp.append(labels.get(x).getLabel()+",");
                            }
                        }
                        temp.deleteCharAt(temp.length()-1);
                        label.setText(temp.toString());
                    }
                }).show();
            }
        });
        //图书图片编辑
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditBookActivity.this);
                //获取AlertDialog对象
                dialog.setTitle("更换封面");//设置标题
                dialog.setMessage("请选择从本地相册读取或者拍照");//设置信息具体内容
                dialog.setCancelable(true);//设置是否可取消
                dialog.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override//设置拍照事件的事件
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String filePath = Environment.getExternalStorageDirectory() + change_path;
                        File localFile = new File(filePath);
                        if (!localFile.exists()) {
                            localFile.mkdir();
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + change_path
                                , "temp.jpg")));
                        startActivityForResult(intent, PHOTO_GRAPH);

                    }
                });
                dialog.setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override//设置从本地读取图片事件
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent1.setType("image/*");
                        startActivityForResult(intent1, PHOTO_ZOOM);
                    }
                }).show();
            }
        });

        //设置书架的Listener
        bookshelf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String shelf_selected = (String)bookshelf.getItemAtPosition(position);
                if(shelf_selected.equals("添加新书架")){
                    final EditText inputServer = new EditText(EditBookActivity.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditBookActivity.this);
                    builder.setTitle("书架名称").setView(inputServer).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            bookshelf.setSelection(nowIndex,true);
                        }
                    })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Button btn_confrim = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                                    String text = inputServer.getText().toString();
                                    if(text.length()==0){
                                        btn_confrim.setEnabled(false);
                                        bookshelf.setSelection(nowIndex,true);
                                        Toast.makeText(EditBookActivity.this,"Null String",Toast.LENGTH_SHORT).show();
                                    }
                                    else if(text.length()>10){
                                        btn_confrim.setEnabled(false);
                                        bookshelf.setSelection(nowIndex,true);
                                        Toast.makeText(EditBookActivity.this,"Input String too long",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        boolean tag = true;
                                        for(int index=0;index<shelfs.size();index++){
                                            if(text.equals(shelfs.get(index).getShelf())){
                                                tag = false;
                                                break;
                                            }
                                        }
                                        if(tag){
                                            spinner_list.add(spinner_list.size()-1,text);
                                            spinner_adapter.notifyDataSetChanged();
                                            bookshelf.setSelection(spinner_list.size()-2,true);
                                            Shelf newShelf = new Shelf();
                                            newShelf.setShelf(text);
                                            shelfs.add(newShelf);
                                            collection_Shelf.save(EditBookActivity.this,shelfs);
                                            nowIndex = bookshelf.getSelectedItemPosition();
                                        }
                                        else
                                            bookshelf.setSelection(nowIndex,true);
                                    }

                                }
                            });
                    builder.show();
                }
                else
                    nowIndex = bookshelf.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //注册保存按钮的点击事件
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookname.getText().toString().length() == 0) {
                    Toast.makeText(EditBookActivity.this, "标题不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    switch (sign) {
                        //从编辑窗口传过来
                        case "detail":
                        case "main":
                            //以下是修改保存
                            MainActivity.itemViews.get(index).setName(bookname.getText().toString());
                            MainActivity.itemViews.get(index).setAuthor(author.getText().toString());
                            MainActivity.itemViews.get(index).setPublishing_house(publish.getText().toString());
                            MainActivity.itemViews.get(index).setPublishing_time(year.getText().toString() + "-" + moon.getText().toString());
                            MainActivity.itemViews.get(index).setIsbn(isbn.getText().toString());
                            MainActivity.itemViews.get(index).setReading_status(readstatue.getSelectedItemPosition());
                            //保存书架未保存
                            MainActivity.itemViews.get(index).setBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap());
                            MainActivity.itemViews.get(index).setItem_notes(notes.getText().toString());
                            MainActivity.itemViews.get(index).setItem_labels(label.getText().toString());
                            MainActivity.itemViews.get(index).setItem_website(weburl.getText().toString());
                            Intent intent1 = new Intent();
                            intent1.setClass(EditBookActivity.this, MainActivity.class);
                            startActivity(intent1);
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            String filePath = Environment.getExternalStorageDirectory() + change_path;
            File localFile = new File(filePath);
            if (!localFile.exists()) {
                localFile.mkdir();
            }
            File picture = new File(Environment.getExternalStorageDirectory() + change_path
                    + "/temp.jpg");
            imageUri = Uri.fromFile(picture);
            startPhotoZoom(imageUri);
        }
        if (data == null)
            return;
        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        // 处理结果
        if (requestCode == PHOTO_RESOULT) {
            /*Bundle extras = data.getExtras();
            if (extras != null) {
                Toast.makeText(EditBookActivity.this,"1",Toast.LENGTH_SHORT).show();
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
                //此处可以把Bitmap保存到sd卡中
                imageView.setImageBitmap(photo); //把图片显示在ImageView控件上

            }*/
            if (data != null) { //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //返回有缩略图
                if (data.hasExtra("data")) {
                    Toast.makeText(EditBookActivity.this, "不为空", Toast.LENGTH_SHORT).show();
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    //得到bitmap后的操作
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
                    //此处可以把Bitmap保存到sd卡中
                    imageView.setImageBitmap(thumbnail); //把图片显示在ImageView控件上
                } else {
                    Toast.makeText(EditBookActivity.this, "为空", Toast.LENGTH_SHORT).show();
                    //由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    // 通过目标uri，找到图片
                    // 对图片的缩放处理
                    // 操作
                    imageView.setImageURI(imageUri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESOULT);
    }

    /**
     * toolbar绑定menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_tosave) {
            Toast.makeText(EditBookActivity.this, "123", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EditBookActivity.this);
            //获取AlertDialog对象
            dialog.setTitle("舍弃书籍");//设置标题
            dialog.setMessage("我们不会保存此书，书籍信息会被丢弃！");//设置信息具体内容
            dialog.setCancelable(true);//设置是否可取消
            dialog.setPositiveButton("舍弃", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }
        return false;
    }
}
