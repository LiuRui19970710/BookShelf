package com.liurui.bookshelf;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Book> itemViews = new ArrayList<>();
    BookCollection bookCollection = new BookCollection();
    ListView listView;
    ListViewAdapter listViewAdapter;
    LeftListItem leftlistitem=new LeftListItem();
    SearchView searchView;
    ArrayList<String> spinner_list = new ArrayList<>();

    // private FloatingActionButton addone;
   // private FloatingActionButton addmany;
    int REQUEST_CODE_SCAN=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        listView = (ListView)findViewById(R.id.BookList);
        listViewAdapter = new ListViewAdapter(MainActivity.this,itemViews);
        listView.setAdapter(listViewAdapter);
        Initialize();

        spinner_list.add("所有");
        spinner_list.add("默认书架");
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_list);  //创建一个数组适配器
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式

        Spinner spinner = super.findViewById(R.id.spinner);
        spinner.setAdapter(spinner_adapter);

        registerForContextMenu(listView);  //important!注册上下文菜单
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 10:
                if (resultCode==RESULT_OK){
                    if (data!=null){
                        String content=data.getStringExtra(Constant.CODED_CONTENT);
                        AddBook addBook = new AddBook();
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
        ArrayList sendList = new ArrayList();
        sendList.addAll(itemViews);
        ArrayList tmpList;
        if (id == R.id.action_settings)
        {
            tmpList = bs.sort_by_title(sendList);
        }
        else if(id==R.id.sort_author)
        {
            tmpList = bs.sort_by_author(sendList);
        }
        else if(id==R.id.sort_publish)
        {
            tmpList = bs.sort_by_publish(sendList);
        }
        else if(id==R.id.sort_time)
        {
            tmpList = bs.sort_by_time(sendList);
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        listViewAdapter.delAll();
        itemViews.addAll(tmpList);
        listViewAdapter.notifyDataSetChanged();
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.booklist) {
            // Handle the camera action
        } else if (id == R.id.search) {
            searchView.setIconified(false);
            searchView.setOnQueryTextListener(new MyQueryTextListener());

        } else if (id == R.id.add_labal) {

        } else if (id == R.id.donate) {
            leftlistitem.donate(MainActivity.this);
        } else if (id == R.id.setting) {
            Intent intent=new Intent(MainActivity.this,Set.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent=new Intent(MainActivity.this,About.class);
            startActivity(intent);
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

    public void Initialize(){
        Book book = new Book();
        book.setName("3");
        book.setAuthor("testAuthor");
        book.setPublishing_house("testpublisher");
        book.setPublishing_time("testtime");
        itemViews.add(book);

        book = new Book();
        book.setName("2");
        book.setAuthor("testAuthor");
        book.setPublishing_house("testpublisher");
        book.setPublishing_time("testtime");
        itemViews.add(book);

        bookCollection.save(MainActivity.this.getBaseContext(),itemViews);

        ArrayList<Book> tmpList = bookCollection.read(getBaseContext());
        itemViews.addAll(tmpList);
        listViewAdapter.notifyDataSetChanged();     //不用这一句也能正常运行，可删
    }

    /** Guo: 长按书本删除 **/
    //创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, Menu.FIRST, 0, "删除");  //创建上下文菜单里的选项
    }

    //定义每个选项所要运行的操作
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                //删除指定列表条目
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int position = menuInfo.position;
                itemViews.remove(position);
                break;
        }
        listViewAdapter.notifyDataSetChanged();
        return true;
    }
    /** Guo: 长按书本删除 **/
}
