package com.ainemo.pad.Jujia;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.ainemo.pad.Datas.DoorInfor;
import com.ainemo.pad.Datas.HomeInfor;
import com.ainemo.pad.Jujia.drawSmoothLine.BesselChart.ChartListener;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends AppCompatActivity implements ChartListener {

  private android.support.v4.view.ViewPager jiujiaviewpager;
  private JiuJiaViewPageAdapter viewPageAdapter;
  private static int total = 7;

  private AppCompatActivity activity;
  private BarChart barChart;
  private Button back;
  private TextView minHumidity;
  private TextView humidity;
  private HomeInfor[] homeInfors = new HomeInfor[7];
  private HomeInfor homeInfor1;
  //  private TemperatureFragment fragment;
  private List<Fragment> fragments = new ArrayList<>();
  private static final String TAG = "JujiaActivity";
  private DoorInfor doorInfor;
  private boolean net_work;
  private TextView door_status;
  private GetHomeInforTask[] getHomeInforTask;
  private GetDoorInfor getDoorInfor;
  Handler[] fragmentHandlers = new Handler[7];

  private TextView day;

  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what >= 0x1234 && msg.what < 0x1234 + total) {
        int offset = msg.what - 0x1234;
        fragments = getSupportFragmentManager().getFragments();
        fragmentHandlers[offset] = ((TemperatureFragment) fragments
            .get(total - 1 - offset)).handler;

        try {
          Message msgDay = new Message();
          Bundle bundle1 = new Bundle();
          bundle1.putInt("day", offset);
          msgDay.setData(bundle1);
          msgDay.what = 0x1234;
          fragmentHandlers[offset].sendMessage(msgDay);

        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (msg.what == 0x12) {
        initData();
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_jujia2);
    net_work = Utils.isNetWorkAvailabe(this);
    activity = this;
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
    for (int i = 0; i < total; i++) {
      if (getHomeInforTask[i] != null) {
        getHomeInforTask[i].cancel(true);
      }
    }
  }

  private void initData() {
    homeInfor1 = homeInfors[0];
    IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
      private String[] labels = new String[]{"03", "06", "09", "12", "15", "18", "21", "24"};

      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        float percent = value / axis.mAxisRange;
        return labels[(int) ((labels.length) * percent)];
      }
    };
    List<BarEntry> barEntries = new ArrayList<>();
    if (homeInfor1 != null && homeInfor1.getHumidityies() != null) {
      for (int i = 0; i < 8; i++) {
        barEntries.add(new BarEntry((float) i, homeInfor1.getHumidityies().get(i)));
      }
    } else {
      for (int i = 0; i < 8; i++) {
        barEntries.add(new BarEntry((float) i, (float) 0.5));
      }
    }
    BarDataSet barDataSet = new BarDataSet(barEntries, "");
    barDataSet.setBarShadowColor(0x00ffffff);
    barDataSet.setDrawValues(false);
    barDataSet.setColor(Color.parseColor("#ff77d5dc"));
    XAxis xAxis1 = barChart.getXAxis();
    xAxis1.setValueFormatter(iAxisValueFormatter);
    xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis1.setTextColor(Color.parseColor("#ff77d5dc"));
    xAxis1.setTextSize(10);
    barChart.getAxisRight().setEnabled(false);
    barChart.getAxisLeft().setEnabled(false);
    barChart.getXAxis().setDrawAxisLine(false);
    barChart.getXAxis().setDrawGridLines(false);
    BarData barData = new BarData(barDataSet);
    barChart.setData(barData);
    Description description1 = new Description();
    description1.setText("");
    description1.setPosition(0, 0);
    barChart.setDescription(description1);
    barChart.setDrawBarShadow(true);
    barChart.getLegend().setEnabled(false);
    barChart.invalidate();
    if (homeInfor1 != null) {
      humidity.setText((int) homeInfor1.getHumidity() + "%");
      try {
        List<Float> sort = homeInfor1.getHumidityies();
        Collections.sort(sort, new Comparator<Float>() {
          @Override
          public int compare(Float aFloat, Float t1) {
            if (aFloat < t1) {
              return -1;
            } else if (aFloat > t1) {
              return 1;
            } else {
              return 0;
            }
          }
        });
        minHumidity.setText(sort.get(0).intValue() + "%");
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
    }
  }

  private void initView() {

    humidity = (TextView) findViewById(R.id.humidity);
    minHumidity = (TextView) findViewById(R.id.minHumidity);
    barChart = (BarChart) findViewById(R.id.bar_shidu_chart);
    back = (Button) findViewById(R.id.return_btn);
    this.jiujiaviewpager = (ViewPager) findViewById(R.id.jujia_view_pager);
    viewPageAdapter = new JiuJiaViewPageAdapter(getSupportFragmentManager());
    jiujiaviewpager.setAdapter(viewPageAdapter);
    jiujiaviewpager.setCurrentItem(total - 1);
    door_status = (TextView) findViewById(R.id.room_status_text);
    getHomeInforTask = new GetHomeInforTask[total];
    for (int i = 0; i < total; i++) {
      getHomeInforTask[i] = new GetHomeInforTask();
      getHomeInforTask[i].execute(i);
    }
    getDoorInfor = new GetDoorInfor();
    getDoorInfor.execute();
    initData();
  }

  private void initEvent() {
    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  class GetDoorInfor extends AsyncTask<Void, Void, Void> {

    private Gson gson = new Gson();

    @Override
    protected void onPostExecute(Void aVoid) {
      try {
        if (doorInfor.getStatus() == 1) {
          door_status.setText("开启");
        } else {
          door_status.setText("关闭");
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
        if (door_infor.equals("device_not_exist") || door_infor.equals("not_exist") || door_infor
            .equals("param_error")) {
          try {
            doorInfor.setStatus(Utils.getIntValue(JujiaActivity.this, GlobalData.DOOR_STATUS));
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

  class GetHomeInforTask extends AsyncTask<Integer, Void, Void> {

    private Gson gson = new Gson();
    private int offset;

    @Override
    protected Void doInBackground(Integer... params) {
      try {
        offset = params[0];
        String day = Utils.formatDay(-params[0].intValue(), "yyyyMMdd", false);
        String infor = Utils.sendRequest(
            GlobalData.GET_HOME_INFOR + Utils.getValue(JujiaActivity.this, GlobalData.PATIENT_ID)
                + "&date=" + day);
        homeInfors[params[0]] = gson.fromJson(infor, HomeInfor.class);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

      handler.sendEmptyMessage(0x1234 + offset);
      if (offset == 0) {
        handler.sendEmptyMessage(0x12);
      }
      super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }

  @Override
  public void onMove() {

  }

  public HomeInfor getHomeInfo(int offset) {

    return homeInfors[offset] == null ? null : homeInfors[offset];
  }
}

