package com.example.myway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(8); //8개의 도움말 프래그먼트를 items 배열에 담을 것이다.

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager()); //프래그먼트 매니저도 연결

        Help_1_Fragment fragment1 = new Help_1_Fragment();
        adapter.addItem(fragment1);

        Help_2_Fragment fragment2 = new Help_2_Fragment();
        adapter.addItem(fragment2);

        Help_3_Fragment fragment3 = new Help_3_Fragment();
        adapter.addItem(fragment3);

        Help_4_Fragment fragment4 = new Help_4_Fragment();
        adapter.addItem(fragment4);

        Help_5_Fragment fragment5 = new Help_5_Fragment();
        adapter.addItem(fragment5);

        Help_6_Fragment fragment6 = new Help_6_Fragment();
        adapter.addItem(fragment6);

        Help_7_Fragment fragment7 = new Help_7_Fragment();
        adapter.addItem(fragment7);

        Help_8_Fragment fragment8 = new Help_8_Fragment();
        adapter.addItem(fragment8);

        pager.setAdapter(adapter); //프래그먼트 추가한 어댑터 연결
    }
}
class MyPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> items = new ArrayList<Fragment>();


    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addItem (Fragment item){
        items.add(item);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
