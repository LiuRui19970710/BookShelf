package com.liurui.bookshelf;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;



import java.util.ArrayList;
import java.util.List;

public class BatchAddActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION = 1;
    public static TabLayout tabLayout;
    private ViewPager viewPager;
    FragmentPagerAdapter adapter;

    public static List<Book> mBooks;// books added

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_add);
        mBooks = new ArrayList<>();

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
//        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new MyPagerAdapter_keepbooking(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));//将viewpager的滑动事件与tablayout适配器绑定
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.bringToFront();//将tablayout置于最顶层
        tabLayout.setupWithViewPager(viewPager);
    }


    class MyPagerAdapter_keepbooking extends FragmentPagerAdapter {
        private String[] titles={"扫描","已添加"};
        public MyPagerAdapter_keepbooking (android.support.v4.app.FragmentManager fm){
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        //返回两个个页面的fragment
        @Override
        public Fragment getItem(int position) {
            //摄像头
            if (position==0) {
                return new BatchScanFragment();
            }
            //书籍列表
            else  {
                return new BatchListFragment();
            }
        }
        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
