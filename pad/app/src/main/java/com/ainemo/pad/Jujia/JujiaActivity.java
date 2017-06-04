package com.ainemo.pad.Jujia;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends AppCompatActivity implements ChartListener {

  private android.support.v4.view.ViewPager jiujiaviewpager;
  private JiuJiaViewPageAdapter viewPageAdapter;

  private AppCompatActivity activity;
  private BarChart barChart;
  private Button back;
  private TextView minHumidity;
  private TextView humidity;
  private List<HomeInfor> homeInfors;
  private HomeInfor homeInfor1;
  private HomeInfor homeInfor2;
  //  private TemperatureFragment fragment;
  private List<Fragment> fragments = new ArrayList<>();
  private static final String TAG = "JujiaActivity";
  private DoorInfor doorInfor;
  private boolean net_work;
  private TextView door_status;
  Handler fragmentHandler1;

  Handler fragmentHandler2;
  private TextView day;

  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == 0x1234) {
        fragments = getSupportFragmentManager().getFragments();
        Log.d(TAG,
            "handleMessage: fragment0 =" + fragments.get(0) + "\nfragment1 " + fragments.get(1));
        fragmentHandler1 = ((TemperatureFragment) fragments.get(1)).handler;
        fragmentHandler2 = ((TemperatureFragment) fragments.get(0)).handler;
        Log.d(TAG, "handleMessage: handler1" + fragmentHandler1);
        try {
          Message msgDay = new Message();
          Bundle bundle1 = new Bundle();
          bundle1.putString("day", "昨天");
          msgDay.setData(bundle1);
          msgDay.what = 0x1234;
          fragmentHandler1.sendMessage(msgDay);

          Bundle bundle2 = new Bundle();
          Log.d(TAG, "handleMessage: handler2" + fragmentHandler2);
          bundle2.putString("day", "今天");
          Message msgDay2 = new Message();
          msgDay2.setData(bundle2);
          msgDay2.what = 0x1234;

          fragmentHandler2.sendMessage(msgDay2);
        } catch (Exception e) {
          e.printStackTrace();
        }
        initData();
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_jujia2);
    initView();
    initEvent();
    net_work = Utils.isNetWorkAvailabe(this);
    activity= this;
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private void initData() {
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
    jiujiaviewpager.setCurrentItem(1);
    door_status = (TextView) findViewById(R.id.room_status_text);
    new GetHomeInforTask().execute();
    new GetDoorInfor().execute();
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
      if (doorInfor.getStatus() == 1) {
        door_status.setText("开启");
      } else {
        door_status.setText("关闭");
      }
      super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {
      if (net_work) {
        String door_infor = Utils.sendRequest(
            GlobalData.GET_ROOM_STATUS + Utils.getValue(JujiaActivity.this, GlobalData.PATIENT_ID));
        doorInfor = gson.fromJson(door_infor, DoorInfor.class);
        try {
          Utils.putIntValue(JujiaActivity.this, GlobalData.DOOR_STATUS, doorInfor.getStatus());
        } catch (NullPointerException e) {
          e.printStackTrace();
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

  class GetHomeInforTask extends AsyncTask<Void, Void, Void> {

    private Gson gson = new Gson();

    @Override
    protected Void doInBackground(Void... params) {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyymmdd");
        String dateString = format.format(date);
        date.setDate(date.getDay() - 1);
        String dateString2 = format.format(date);
        try {
          String infor = Utils.sendRequest(
              GlobalData.GET_HOME_INFOR + Utils.getValue(JujiaActivity.this, GlobalData.PATIENT_ID)
                  + "&date=" + dateString);
          homeInfor1 = gson.fromJson(infor, HomeInfor.class);
          String infor2 = Utils.sendRequest(
              GlobalData.GET_HOME_INFOR + Utils.getValue(JujiaActivity.this, GlobalData.PATIENT_ID)
                  + "&date=" + dateString2);
          homeInfor2 = gson.fromJson(infor2, HomeInfor.class);
        } catch (Exception e) {
          e.printStackTrace();
        }

//      Log.d(TAG, "doInBackground: homeInfo.temperatures " + homeInfor1.getTemperatures());
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      handler.sendEmptyMessage(0x1234);
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

  public HomeInfor getHomeInfo(String date) {
    if (date.equals("今天")) {
      return homeInfor1 == null ? null : homeInfor1;
    } else if (date.equals("昨天")) {
      return homeInfor2 == null ? null : homeInfor2;
    } else {
      return null;
    }
  }
}

