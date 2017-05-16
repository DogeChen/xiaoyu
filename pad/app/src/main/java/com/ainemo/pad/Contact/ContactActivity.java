package com.ainemo.pad.Contact;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.ainemo.pad.R;

/**
 * Created by victor on 2017/4/24.
 */


public class ContactActivity extends FragmentActivity {

    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private TabLayout tabLayout;
    private TabLayout.Tab one,two;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        back=(Button)findViewById(R.id.return_btn);
        initTab();
        initEvent();
    }

    private void  initEvent(){
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
//                    one.setIcon(getResources().getDrawable(R.drawable.contact_bottom_first_selected));
                    tabLayout.setBackgroundResource(R.drawable.btn_nav_nor);
                    viewPager.setCurrentItem(0);
                } else if (tab == tabLayout.getTabAt(1)) {
//                    two.setIcon(getResources().getDrawable(R.drawable.contact_bottom_second_selected));
                    viewPager.setCurrentItem(1);
                    tabLayout.setBackgroundResource(R.drawable.btn_nav_nor_reversal);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                if (tab == tabLayout.getTabAt(0)) {
//                    one.setIcon(getResources().getDrawable(R.drawable.contact_bottom_first_nor));
//                } else if (tab == tabLayout.getTabAt(1)) {
//                    two.setIcon(getResources().getDrawable(R.drawable.contact_bottom_second_nor));
//                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void  initTab(){
        viewPager = (ViewPager) findViewById(R.id.contact_view_pager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPageAdapter);
        tabLayout = (TabLayout) findViewById(R.id.frag);
        tabLayout.setupWithViewPager(viewPager);
        one = tabLayout.getTabAt(0);
        two = tabLayout.getTabAt(1);
        one.setText("呼叫");
        viewPager.setCurrentItem(0);
        two.setText("通讯录");

    }



}
