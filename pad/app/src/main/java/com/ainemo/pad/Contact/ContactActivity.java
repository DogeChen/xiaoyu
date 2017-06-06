package com.ainemo.pad.Contact;

import ainemo.api.openapi.NemoCallback;
import ainemo.api.openapi.NemoOpenAPI;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.ainemo.pad.R;

/**
 * Created by victor on 2017/4/24.
 */


public class ContactActivity extends FragmentActivity implements NemoCallback {

    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private TabLayout tabLayout;
    private TabLayout.Tab one,two;
    private Button back;
    Handler handler;
    private FragmentCall fragmentCall;
    private static final String TAG = "ContactActivity";
    private long lastTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        back=(Button)findViewById(R.id.return_btn);
        NemoOpenAPI.getInstance().registerCallback(this);

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
        fragmentCall=(FragmentCall) viewPageAdapter.instantiateItem(viewPager,0);
        handler=fragmentCall.handler;
        one = tabLayout.getTabAt(0);
        two = tabLayout.getTabAt(1);
        one.setText("呼叫");
        viewPager.setCurrentItem(0);
        two.setText("通讯录");
    }

    @Override
    public void onNemoCallback(Message message) {
        Log.d(TAG, "onNemoCallback: Callback");
//        if(System.currentTimeMillis()-lastTime>=500) {
            handler.sendMessage(Message.obtain(message));
//            lastTime=System.currentTimeMillis();
//        }
    }
}
