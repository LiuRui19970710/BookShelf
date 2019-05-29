package com.liurui.bookshelf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

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
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ListViewAdapter.OnShowItemClickListener {
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

    public static boolean isShowCheckBox = false;
    private List<Book> selectedBooks;
    private RelativeLayout toolbarDeleteLayout;
    private Toolbar toolbarDelete;
    private Toolbar toolbar;

    // private FloatingActionButton addone;
   // private FloatingActionButton addmany;
    int REQUEST_CODE_SCAN=10;
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
                Intent intent=new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent,REQUEST_CODE_SCAN);

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
                    bundle.putSerializable("book",itemViews.get(i));
                    intent.putExtra("index",i);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });



        spinner_list.add("所有");
        spinner_list.add("默认书架");
        for(int index=0;index<shelfs.size();index++){
            spinner_list.add(shelfs.get(index).getShelf());
        }
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_list);  //创建一个数组适配器
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        spinner = super.findViewById(R.id.spinner);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(new MySpinnerItemSelectedListener());

        //设置左侧列表
        Set_Left_Menu();

        setDelete();
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
        });

        toolbarDelete.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.delete) {
                    if (selectedBooks != null && selectedBooks.size() > 0) {
                        itemViews.removeAll(selectedBooks);
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
                        AddBook addBook = new AddBook(getBaseContext());
                        itemViews.add(addBook.add_book(content));
                        listViewAdapter.notifyDataSetChanged();
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

        MenuItem searchItem = menu.findItem(R.id.action_search);
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
        int id = item.getItemId();

        if (id == 0) {
            // Handle the camera action
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
            Toast.makeText(MainActivity.this,"此处准备写个跳转函数",Toast.LENGTH_LONG).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
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
            navigationView.getMenu().add(1,label_id+6,0,labels.get(label_id).getLabel());
        }
        navigationView.getMenu().add(1,2,0,"添加新标签").setIcon(R.drawable.add);
        navigationView.getMenu().add(2,3,0,"捐赠").setIcon(R.drawable.donate);
        navigationView.getMenu().add(2,4,0,"设置").setIcon(R.drawable.setting);
        navigationView.getMenu().add(2,5,0,"关于").setIcon(R.drawable.about);


    }

    public void Initialize(){
        /*Book book = new Book();
        book.setName("3");
        book.setAuthor("testAuthor");
        book.setPublishing_house("testpublisher");
        book.setPublishing_time("testtime");
        itemViews.add(book);

        book = new Book();
        book.setName("13");
        book.setAuthor("testAuthor");
        book.setPublishing_house("testpublisher");
        book.setPublishing_time("testtime");
        itemViews.add(book);

        book = new Book();
        book.setName("4");
        book.setAuthor("testAuthor");
        book.setPublishing_house("testpublisher");
        book.setPublishing_time("testtime");
        itemViews.add(book);*/

        collection_book.save(MainActivity.this.getBaseContext(),itemViews);

        //添加书本
        ArrayList<Book> tmpList = collection_book.read(getBaseContext());
        itemViews.addAll(tmpList);

        //添加标签
        ArrayList<Label> tmpLabels = collection_Label.read(getBaseContext());
        labels.addAll(tmpLabels);

        //添加书架
        ArrayList<Shelf> tmpShelf = collection_Shelf.read(getBaseContext());
        shelfs.addAll(tmpShelf);
    }

    //返回书本数组的引用
    public ArrayList<Book> getItemViews(){
        return itemViews;
    }
}
