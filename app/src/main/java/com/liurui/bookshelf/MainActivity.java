package com.liurui.bookshelf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ListViewAdapter.OnShowItemClickListener {
    private ArrayList<Book> allBook = new ArrayList<>();
    public static ArrayList<Book> itemViews = new ArrayList<>();
    private ArrayList<Label> labels = new ArrayList<>();
    private ArrayList<Shelf> shelfs = new ArrayList<>();
    int label_id=0;
    NavigationView navigationView;
    Collection collection_book = new Collection("Book");
    Collection collection_Label = new Collection("Label");
    Collection collection_Shelf = new Collection("Shelf");
    ListView listView;
    ListViewAdapter listViewAdapter;
    LeftListItem leftlistitem=new LeftListItem();
    SearchView searchView;
    Spinner spinner;
    ArrayList<String> spinner_list = new ArrayList<>();
    MenuItem searchItem;
    public static boolean isShowCheckBox = false;
    private List<Book> selectedBooks;
    private RelativeLayout toolbarDeleteLayout;
    private Toolbar toolbarDelete;
    private Toolbar toolbar;
    int flag=0;
    private Handler uiHandler = new Handler();
    Scanning scanning= new Scanning();
    final String[] gender = new String[]{"扫描条形码","手动输入ISBN","手动添加书籍"};

    //private FloatingActionButton addone;
    //private FloatingActionButton addmany;
    int REQUEST_CODE_SCAN=10;
    int REQUEST_CODE_MANUALADDITION=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //浮窗绑定
        FloatingActionButton addone=(FloatingActionButton)findViewById(R.id.button_addone);
        FloatingActionButton addmany=(FloatingActionButton)findViewById(R.id.button_addmany);
        //添加书籍的点击事件
        addone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent=new Intent(MainActivity.this, CaptureActivity.class);
                final AlertDialog.Builder builder_isbn = new AlertDialog.Builder(MainActivity.this);
                final EditText et = new EditText(MainActivity.this);
                final Intent intent_Edit = new Intent(MainActivity.this,EditBookActivity.class);
                final Book manual_addition = new Book();
                final Bundle bundle = new Bundle();

                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请选择添加方式");
                builder.setItems(gender, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                            startActivityForResult(intent,REQUEST_CODE_SCAN);
                        if(which==1){
                            builder_isbn.setTitle("手动添加").setMessage("请输入书籍的ISBN码（10或13位数字）").setView(et).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String isbn = et.getText().toString();
                                    if(et.length()==10||et.length()==13){
                                        //Toast.makeText(MainActivity.this,"应调用函数",Toast.LENGTH_SHORT).show();
                                        initData(et.getText().toString());
                                    }
                                    else
                                        Toast.makeText(MainActivity.this,"ISBN长度不对",Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder_isbn.show();
                        }
                        if(which==2){
                            {
                                manual_addition.setId(0);
                                manual_addition.setIsbn("");
                                manual_addition.setPublishing_time("");
                                manual_addition.setPublishing_house("");
                                manual_addition.setName("");
                                manual_addition.setAuthor("");
                                manual_addition.setChecked(false);
                                manual_addition.setItem_bookshelf("");
                                manual_addition.setItem_labels("");
                                manual_addition.setItem_notes("");
                                manual_addition.setItem_website("");
                            }
                            itemViews.add(manual_addition);
                            bundle.putSerializable("manual_add_boook",manual_addition);
                            intent_Edit.putExtras(bundle);
                            intent_Edit.putExtra("index",itemViews.size()-1);
                            intent_Edit.putExtra("method_start","main");
                            startActivityForResult(intent_Edit,REQUEST_CODE_MANUALADDITION);
                        }
                    }
                });
                builder.show();
            }
        });
        //批量添加的点击事件
        addmany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        listView = (ListView)findViewById(R.id.BookList);
        listViewAdapter = new ListViewAdapter(MainActivity.this,itemViews);
        listView.setAdapter(listViewAdapter);

        Initialize();

        //listview的点击事件跳转
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isShowCheckBox) {
                    Book book = itemViews.get(i);
                    boolean isChecked = book.isChecked();
                    if (isChecked) {
                        book.setChecked(false);
                    } else {
                        book.setChecked(true);
                    }
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,Book_Detail_info_Activity.class);
                    Bundle bundle =new Bundle();
                    bundle.putSerializable("label",labels);
                    //bundle.putSerializable("book",itemViews.get(i));
                    intent.putExtra("index",i);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_list);  //创建一个数组适配器
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        spinner = super.findViewById(R.id.spinner);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(new MySpinnerItemSelectedListener());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String shlef_selected = (String)spinner.getItemAtPosition(position);
                if(shlef_selected.equals("所有")){
                    listViewAdapter.delAll();
                    itemViews.addAll(allBook);
                }
                else{
                    listViewAdapter.delAll();
                    for(Book book:allBook){
                        if(book.getItem_bookshelf().equals(shlef_selected))
                            itemViews.add(book);
                    }
                }
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //设置左侧列表
        Set_Left_Menu();
        setDelete();
    }
    public static byte[] getBytes(Bitmap bitmap){
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }
    private void initData(final String ISBN) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 写子线程中的操作
                String information="",name,isbn;
                AddBook addBook = new AddBook(getBaseContext());


                try {
                    information = scanning.main(ISBN);

                    GetString getString = new GetString(information);
                    Log.i("test", "run: " + getString.getName() + " " + getString.getISBN() + " " + getString.getAuthor() + " " + getString.getPublisher()
                            + " " + getString.getPubTime() + " " + getString.getImgPath());
                    if(getString.getName()!="" && getString.getISBN()!="" && getString.getAuthor()!="" && getString.getPublisher()!=""
                            &&getString.getPubTime()!=""&&getString.getImgPath()!="") {
                        DownLoadImg downLoadImg = new DownLoadImg();

                        itemViews.add(addBook.add_book(getString.getName(), getString.getAuthor(), getString.getPublisher(),
                                getString.getPubTime(), downLoadImg.DownloadImg(getString.getImgPath())));
                        collection_book.save(MainActivity.this.getBaseContext(), itemViews);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                listViewAdapter.notifyDataSetChanged();
                            }
                        };
                        uiHandler.post(runnable);
                    }
                    else
                    {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "扫描信息错误", Toast.LENGTH_SHORT).show();
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

    private TextView deleteNum;

    private void setDelete() {
        selectedBooks = new ArrayList<>();

        toolbarDeleteLayout = findViewById(R.id.toolbar_delete_layout);
        toolbarDelete = toolbarDeleteLayout.findViewById(R.id.toolbar_delete);
        toolbarDelete.inflateMenu(R.menu.toolbar_new);
        deleteNum = findViewById(R.id.delete_num);

        listViewAdapter.setOnShowItemClickListener(this);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (isShowCheckBox) {
                    return false;
                } else {
                    isShowCheckBox = true;
                    for (Book book: itemViews) {
                        book.setShow(true);
                    }
                    itemViews.get(position).setChecked(true);
                    listViewAdapter.notifyDataSetChanged();
                    listView.setLongClickable(false);
                    showDeleteToolBar();
                }
                return true;
            }
        });

    }

    @Override
    public void onShowItemClick(Book book) {
        if (book.isChecked() && !selectedBooks.contains(book)) {
            selectedBooks.add(book);
        } else if (!book.isChecked() && selectedBooks.contains(book)) {
            selectedBooks.remove(book);
        }
        deleteNum.setText(selectedBooks.size() + "");
    }

    private void showDeleteToolBar() {
        toolbar.setVisibility(View.GONE);
        toolbarDeleteLayout.setVisibility(View.VISIBLE);

        ImageView returnBack = toolbarDelete.findViewById(R.id.returnback);
        TextView deleteNum = toolbarDelete.findViewById(R.id.delete_num);

        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyDeleteCheckBox();
            }
        });

        toolbarDelete.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.delete) {
                    if (selectedBooks != null && selectedBooks.size() > 0) {
                        itemViews.removeAll(selectedBooks);
                        bulk_delete(selectedBooks);
                        selectedBooks.clear();
                        for (Book book: itemViews) {
                            book.setChecked(false);
                            book.setShow(false);
                        }
                        listViewAdapter.notifyDataSetChanged();
                        isShowCheckBox = false;
                        listView.setLongClickable(true);
                        toolbar.setVisibility(View.VISIBLE);
                        toolbarDeleteLayout.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(MainActivity.this, "Please choose a book to delete", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 10:
                if (resultCode==RESULT_OK){
                    if (data!=null){
                        String content=data.getStringExtra(Constant.CODED_CONTENT);
                        Log.i("test2", "onActivityResult: "+content);
                        initData(content);
                    }
                }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        searchView.setOnQueryTextListener(new MyQueryTextListener());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        BookSort bs=new BookSort();
        if (id == R.id.action_settings)
        {
            bs.sort_by_title();
    }
        else if(id==R.id.sort_author)
        {
            bs.sort_by_author();
        }
        else if(id==R.id.sort_publish)
        {
            bs.sort_by_publish();
        }
        else if(id==R.id.sort_time)
        {
            bs.sort_by_time();
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        //listViewAdapter.delAll();
        collection_book.save(MainActivity.this.getBaseContext(),itemViews);
        listViewAdapter.notifyDataSetChanged();
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == 0) {
            // Handle the camera action
            Return_Main();
        } else if (id == 1) {
            searchView.setIconified(false);
            searchView.setOnQueryTextListener(new MyQueryTextListener());

        } else if (id == 2) {
            final EditText label_input;
            final TextView label_length;
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            final View v = inflater.inflate(R.layout.label_input_window,null, false);
            label_length = v.findViewById(R.id.label_length);
            label_input = v.findViewById(R.id.label_input);
            label_input.addTextChangedListener(new TextWatcher() {

                int current_Length = 0;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (current_Length < 15) {
                        current_Length = label_input.getText().length();
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    label_length.setText(current_Length + "/15");
                }

                @Override
                public void afterTextChanged(Editable s) {
                    label_length.setText(current_Length + "/15");
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("标签名称");
            builder.setView(v);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(MainActivity.this,label_input.getText(),Toast.LENGTH_LONG).show();
                    if(label_input.getText().length()!=0) {
                        Label label = new Label();
                        label.setLabel(label_input.getText().toString());
                        labels.add(label);
                        collection_Label.save(MainActivity.this.getBaseContext(), labels);
                        label_id++;
                        navigationView.getMenu().removeGroup(0);
                        navigationView.getMenu().removeGroup(1);
                        navigationView.getMenu().removeGroup(2);
                        Set_Left_Menu();
                    }
                }

            });

            builder.setNegativeButton("取消", null);
            builder.show();

        } else if (id == 3) {
            leftlistitem.donate(MainActivity.this);
        } else if (id == 4) {
            Intent intent = new Intent(MainActivity.this, Set.class);
            startActivity(intent);
        } else if (id == 5) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        }else if(id>=6&&id<=1000) {
            FloatingActionsMenu addone=( FloatingActionsMenu)findViewById(R.id.fabmenu);
            addone.setVisibility(View.GONE);
            searchItem.setVisible(false);
            toolbar.setBackgroundColor(getResources().getColor(R.color.label_activity));
            if(toolbar.getMenu().size()>5)
            {
                toolbar.getMenu().removeItem(0);
            }
            MenuItem menuItem = toolbar.getMenu().add(0, 0, 0, "删除").setIcon(R.drawable.ico_three);
            menuItem.setShowAsAction(1);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // Toast.makeText(MainActivity.this,"此处准备写个弹窗函数",Toast.LENGTH_LONG).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("标签名称");
                    String[] label_choice = new String[]{"重命名", "删除"};
                    builder.setSingleChoiceItems(label_choice, 0, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                flag = which;
                            } else if (which == 1) {
                                flag = which;
                            }
                            return;
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (flag == 1) {
                                if (labels.size() != 0) {
                                    labels.remove(id - 6);
                                    collection_Label.save(MainActivity.this.getBaseContext(), labels);
                                    navigationView.getMenu().removeGroup(0);
                                    navigationView.getMenu().removeGroup(1);
                                    navigationView.getMenu().removeGroup(2);
                                    Set_Left_Menu();
                                    Return_Main();
                                }
                                flag = 0;
                            } else if (flag == 0) {
                                rename_label(id);
                                Set_Left_Menu();
                                flag = 0;
                            }
                        }
                    });
                    builder.show();

                    return true;
                }
            });
            //选择标签
            String label_selected = item.getTitle().toString();
            listViewAdapter.delAll();
            for(Book book:allBook){
                if(book.getItem_bookshelf().equals(label_selected))
                    itemViews.add(book);
            }
            listViewAdapter.notifyDataSetChanged();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    public void rename_label(int index)
    {
        Add_Label(labels.get(index-6).getLabel(),labels.get(index-6).getId());
        labels.remove(index-6);
        collection_Label.save(MainActivity.this.getBaseContext(), labels);
        navigationView.getMenu().removeGroup(0);
        navigationView.getMenu().removeGroup(1);
        navigationView.getMenu().removeGroup(2);
    }

    public void Return_Main()
    {
        FloatingActionsMenu addone=( FloatingActionsMenu)findViewById(R.id.fabmenu);
        addone.setVisibility(View.VISIBLE);
        searchItem.setVisible(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.getMenu().removeItem(0);
    }

    public void Add_Label(String init, final int index)
    {
        final EditText label_input;
        final TextView label_length;
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(MainActivity.this);
        final View v = inflater.inflate(R.layout.label_input_window,null, false);
        label_length = v.findViewById(R.id.label_length);
        label_input = v.findViewById(R.id.label_input);
        label_input.setText(init);
        label_input.addTextChangedListener(new TextWatcher() {

            int current_Length = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (current_Length < 15) {
                    current_Length = label_input.getText().length();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                label_length.setText(current_Length + "/15");
            }

            @Override
            public void afterTextChanged(Editable s) {
                label_length.setText(current_Length + "/15");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("标签名称");
        builder.setView(v);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this,label_input.getText(),Toast.LENGTH_LONG).show();
                if(label_input.getText().length()!=0) {
                    Label label = new Label();
                    label.setLabel(label_input.getText().toString());
                    label.setId(index);
                    labels.add(label);
                    collection_Label.save(MainActivity.this.getBaseContext(), labels);
                    label_id++;
                    navigationView.getMenu().removeGroup(0);
                    navigationView.getMenu().removeGroup(1);
                    navigationView.getMenu().removeGroup(2);
                    Set_Left_Menu();
                }
            }

        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    class MyQueryTextListener implements SearchView.OnQueryTextListener{
        @Override
        public boolean onQueryTextSubmit(String query) {
            LeftListItem leftListItem = new LeftListItem();
            ArrayList sendList = new ArrayList();
            sendList.addAll(itemViews);
            ArrayList tmpList = leftListItem.LeftListSearch(sendList,query);
            listViewAdapter.delAll();
            itemViews.addAll(tmpList);
            listViewAdapter.notifyDataSetChanged();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

    /**
     * Author:SZEKIN
     * Spinner的点击事件
     */
    class MySpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,int position, long id){
            String bookshelf_selected = (String)spinner.getItemAtPosition(position);      //从spinner中获取被选择的数据

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }

    /**
     * Author：Liurui
     * 设置左侧列表
     */
    public void Set_Left_Menu()
    {
        navigationView.getMenu().add(0,0,0,"书籍").setIcon(R.drawable.book_list);
        navigationView.getMenu().add(0,1,0,"搜索").setIcon(R.drawable.search);


        for(label_id=0;label_id<labels.size();label_id++)
        {
            navigationView.getMenu().add(1,label_id+6,0,labels.get(label_id).getLabel()).setIcon(R.drawable.label);
        }
        navigationView.getMenu().add(1,2,0,"添加新标签").setIcon(R.drawable.add);
        navigationView.getMenu().add(2,3,0,"捐赠").setIcon(R.drawable.donate);
        navigationView.getMenu().add(2,4,0,"设置").setIcon(R.drawable.setting);
        navigationView.getMenu().add(2,5,0,"关于").setIcon(R.drawable.about);


    }

    public void Initialize(){
//        Book book = new Book();
//        book.setName("111");
//        book.setAuthor("testAuthor");
//        book.setPublishing_house("testpublisher");
//        book.setPublishing_time("testtime");
//        itemViews.add(book);
//
//        book = new Book();
//        book.setName("222");
//        book.setAuthor("testAuthor");
//        book.setPublishing_house("testpublisher");
//        book.setPublishing_time("testtime");
//        itemViews.add(book);
//
//        book = new Book();
//        book.setName("333");
//        book.setAuthor("testAuthor");
//        book.setPublishing_house("testpublisher");
//        book.setPublishing_time("testtime");
//        itemViews.add(book);

//        collection_book.save(MainActivity.this.getBaseContext(),itemViews);

        //添加书本
        allBook.addAll(collection_book.read(getBaseContext()));
        itemViews.addAll(allBook);

        //添加标签
        labels.addAll(collection_Label.read(getBaseContext()));

        //添加书架
        /*Shelf s = new Shelf();
        s.setShelf("默认书架");
        shelfs.add(s);
        collection_Shelf.save(MainActivity.this.getBaseContext(),shelfs);*/

        spinner_list.add("所有");
        spinner_list.add("默认书架");
        shelfs.addAll(collection_Shelf.read(getBaseContext()));
        for(int index=0;index<shelfs.size();index++){
            spinner_list.add(shelfs.get(index).getShelf());
        }
    }

    //返回书本数组的引用
    public ArrayList<Book> getItemViews(){
        return itemViews;
    }

    private void deleteBook(int book_id){
        for(int index=0;index<allBook.size();index++){
            if(book_id==allBook.get(index).getId()){
                allBook.remove(index);
                collection_book.save(MainActivity.this.getBaseContext(),allBook);
                break;
            }
        }
    }

    private void bulk_delete(List<Book> selectedBooks){
        /*for(int id_index=0;id_index<book_id.length;id_index++){
            for(int index=0;index<allBook.size();index++){
                if(book_id[id_index]==allBook.get(index).getId()){
                    allBook.remove(index);
                    break;
                }
            }
        }*/
        allBook.removeAll(selectedBooks);
        collection_book.save(MainActivity.this.getBaseContext(),allBook);
    }

    private void destroyDeleteCheckBox() {
        if (isShowCheckBox) {
            selectedBooks.clear();
            for (Book book: itemViews) {
                book.setChecked(false);
                book.setShow(false);
            }
            listViewAdapter.notifyDataSetChanged();
            isShowCheckBox = false;
            listView.setLongClickable(true);
            toolbar.setVisibility(View.VISIBLE);
            toolbarDeleteLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyDeleteCheckBox();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        spinner_list.clear();
        shelfs.clear();
        spinner_list.add("所有");
        spinner_list.add("默认书架");
        shelfs.addAll(collection_Shelf.read(getBaseContext()));
        for(int index=0;index<shelfs.size();index++){
            spinner_list.add(shelfs.get(index).getShelf());
        }

        final Intent intent = getIntent();
        int sid = intent.getIntExtra("sid",-1);
        int book_index = intent.getIntExtra("index",-1);
        Book book = itemViews.get(book_index);
        for(Book book_edit:allBook){
            if(book.getId()==sid){
                book_edit.setName(book.getName());
                book_edit.setAuthor(book.getAuthor());
                book_edit.setPublishing_house(book.getPublishing_house());
                book_edit.setPublishing_time(book.getPublishing_time());
                book_edit.setIsbn(book.getIsbn());
                book_edit.setReading_status(book.getReading_status());
                book_edit.setItem_bookshelf(book.getItem_bookshelf());
                book_edit.setItem_notes(book.getItem_notes());
                book_edit.setItem_labels(book.getItem_labels());
                book_edit.setItem_website(book.getItem_website());
                book_edit.setBitmap(Book.getBytes(book.getBitmap()));
                break;
            }
        }
        collection_book.save(getBaseContext(),allBook);

        listViewAdapter.notifyDataSetChanged();
    }
}
