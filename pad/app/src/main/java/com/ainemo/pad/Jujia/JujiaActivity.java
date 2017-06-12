package com.ainemo.pad.Jujia;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ainemo.pad.Datas.DoorInfor;
import com.ainemo.pad.Jujia.drawSmoothLine.BesselChart.ChartListener;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.google.gson.Gson;

/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends AppCompatActivity implements ChartListener {



  private android.support.v4.view.ViewPager jiujiaviewpager;


  private JiuJiaViewPageAdapter viewPageAdapter;
  private static int total = 7;

  private AppCompatActivity activity;

  public BarChart getBarChart() {
    return barChart;
  }

  private LinearLayout person_status;
  private LinearLayout baojing;
  private BarChart barChart;
  private Button back;
  private TextView minHumidity;
  private TextView humidity;

  public TextView getMinHumidity() {
    return minHumidity;
  }

  public TextView getHumidity() {
    return humidity;
  }

  //  private TemperatureFragment fragment;
//  private List<Fragment> fragments = new ArrayList<>();
  private static final String TAG = "JujiaActivity";
  private DoorInfor doorInfor=new DoorInfor();
  private boolean net_work;
  private TextView door_status;
//  private GetHomeInforTask[] getHomeInforTask;
  private GetDoorInfor getDoorInfor;
//  Handler[] fragmentHandlers = new Handler[7];

  private TextView day;

//  Handler handler = new Handler() {
//    @Override
//    public void handleMessage(Message msg) {
//      if (msg.what >= 0x1234 && msg.what < 0x1234 + total) {
//        int offset = msg.what - 0x1234;
//        fragments = getSupportFragmentManager().getFragments();
//        fragmentHandlers[offset] = ((TemperatureFragment) fragments
//            .get(total - 1 - offset)).handler;
//        try {
//          Message msgDay = new Message();
//          Bundle bundle1 = new Bundle();
//          bundle1.putInt("day", offset);
//          msgDay.setData(bundle1);
//          msgDay.what = 0x1234;
//          fragmentHandlers[offset].sendMessage(msgDay);
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      } else
//        if (msg.what == 0x12) {
//        initData();
//      }
//    }
//  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_jujia);
    net_work = Utils.isNetWorkAvailabe(this);
    activity = this;
   String patientId =Utils.getValue(this,GlobalData.PATIENT_ID);
    if (patientId == null || patientId.equals("")) {
      Utils.showShortToast(this, "此用户没有绑定患者");
      Utils.finishActivity(this);
    }
    initView();
    initEvent();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (getDoorInfor != null) {
      getDoorInfor.cancel(true);
    }
  }



  private void initView() {

    humidity = (TextView) findViewById(R.id.humidity);
    minHumidity = (TextView) findViewById(R.id.minHumidity);
    baojing=(LinearLayout)findViewById(R.id.baojing);
    barChart = (BarChart) findViewById(R.id.bar_shidu_chart);
    back = (Button) findViewById(R.id.return_btn);
    this.jiujiaviewpager = (ViewPager) findViewById(R.id.jujia_view_pager);
    viewPageAdapter = new JiuJiaViewPageAdapter(getSupportFragmentManager());
//    for (int i = 0; i < total; i++) {
//      viewPageAdapter.getItem(i);
//    }

    person_status=(LinearLayout)findViewById(R.id.person_status);
    jiujiaviewpager.setAdapter(viewPageAdapter);
    jiujiaviewpager.setCurrentItem(total - 1);


    door_status = (TextView) findViewById(R.id.room_status_text);
    //getHomeInforTask = new GetHomeInforTask[total];
//    for (int i = 0; i < total; i++) {
//      getHomeInforTask[i] = new GetHomeInforTask();
//      getHomeInforTask[i].execute(i);
//    }
    getDoorInfor = new GetDoorInfor();
    getDoorInfor.execute();
//    initData();
  }

  private void initEvent() {
    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    person_status.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent=new Intent(JujiaActivity.this,PersonStatusActivity.class);
        startActivity(intent);
      }
    });
    baojing.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent=new Intent(JujiaActivity.this,BaojingActivity.class);
        startActivity(intent);
      }
    });
  }
  public ViewPager getJiujiaviewpager() {
    return jiujiaviewpager;
  }

  class GetDoorInfor extends AsyncTask<Void, Void, Void> {

    private Gson gson = new Gson();

    @Override
    protected void onPostExecute(Void aVoid) {
      try {
        if (doorInfor.getStatus() == 1) {
          door_status.setText("开启");
        } else if (doorInfor.getStatus()==0){
          door_status.setText("关闭");
        } else{
          door_status.setText("无设备");
        }
      } catch (Exception e) {
        e.printStackTrace();
        door_status.setText("--");
      }
      super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {
      if (net_work) {
        String door_infor = Utils.sendRequest(
            GlobalData.GET_ROOM_STATUS + Utils.getValue(JujiaActivity.this, GlobalData.PATIENT_ID));
        if (door_infor.contains("device_not_exist") || door_infor.contains("not_exist") || door_infor
            .contains("param_error")) {
          try {

            doorInfor.setStatus(2);
//            doorInfor.setStatus(Utils.getIntValue(JujiaActivity.this, GlobalData.DOOR_STATUS));
          } catch (NullPointerException e) {

            e.printStackTrace();
          }
        }
//        else if(door_infor.equals("not_exist")){
//
//        }else if(door_infor.equals("param_error")){
//
//        }
        else {
          doorInfor = gson.fromJson(door_infor, DoorInfor.class);
          try {
            Utils.putIntValue(JujiaActivity.this, GlobalData.DOOR_STATUS, doorInfor.getStatus());
          } catch (NullPointerException e) {
            e.printStackTrace();
          }
        }

      } else {
        try {
          doorInfor.setStatus(Utils.getIntValue(JujiaActivity.this, GlobalData.DOOR_STATUS));
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
      }
      return null;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }



  @Override
  public void onMove() {

  }

//  public HomeInfor getHomeInfo(int offset) {
//
//    return homeInfors[offset] == null ? null : homeInfors[offset];
//  }
}

