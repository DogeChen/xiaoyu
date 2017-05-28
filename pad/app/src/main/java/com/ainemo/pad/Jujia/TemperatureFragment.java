package com.ainemo.pad.Jujia;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.ainemo.pad.Datas.HomeInfor;
import com.ainemo.pad.R;
import com.ainemo.pad.Jujia.drawSmoothLine.BesselChart;
import com.ainemo.pad.Jujia.drawSmoothLine.ChartData;
import com.ainemo.pad.Jujia.drawSmoothLine.Point;
import com.ainemo.pad.Jujia.drawSmoothLine.Series;
import com.github.mikephil.charting.charts.BarChart;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by 小武哥 on 2017/5/19.
 */

public class TemperatureFragment extends Fragment implements BesselChart.ChartListener {

  BesselChart chart;

//    private Chart mChart;

  private static final String TAG = "TemperatureFragment";
  private BarChart barChart;
  private Button back;
  private List<Point> points = new ArrayList<>();
  private Point currentTem;
  private View layout;
  private JujiaActivity activity;
  private List<Float> temperatureBefore;
  private List<Float> temperatures;
  private List<Integer> xLabels=new ArrayList<>();
  private TextView maxTem;
  private TextView minTem;
  public TextView day;
  private String dayString;
  private String currentTime;
  Fragment fragment;
  private HomeInfor homeInfor;
  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what){
        case 0x1234:
          dayString=msg.getData().getString("day");
          day.setText(dayString);
          Log.d(TAG, "handleMessage: fragment handler"+handler);
        initData();
          break;
      }
    }
  };

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    if (layout == null) {
      layout = activity.getLayoutInflater().inflate(R.layout.fragment_temperature, null);
    } else {
      ViewGroup parent = (ViewGroup) layout.getParent();
      if (parent != null) {
        parent.removeView(layout);
      }
    }
    initView();
    initEvent();
    return layout;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = (JujiaActivity) getActivity();
    fragment=this;
    Log.d(TAG, "onCreate fragment= "+fragment);

  }

  private void handleTemperatures(){
    temperatureBefore=new ArrayList<>();
    Float tem=0.0f;
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    tem=2.2f;
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    tem=2.8f;
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    tem=3.9f;
    temperatureBefore.add(tem);
    tem=3.9f;
    temperatureBefore.add(tem);
    tem=1.0f;
    temperatureBefore.add(tem);
    tem=5.5f;
    temperatureBefore.add(tem);
    tem=3.9f;
    temperatureBefore.add(tem);
    tem=2.1f;
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    tem=3.9f;
    temperatureBefore.add(tem);
    tem=0.0f;
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);
    temperatureBefore.add(tem);

    temperatures = new ArrayList<>();
    xLabels=new ArrayList<>();
    for(int i=0;i<temperatureBefore.size();i++){

      if(0!=temperatureBefore.get(i).intValue()){
        temperatures.add(temperatureBefore.get(i));

        xLabels.add(i);
      }
    }
  }

  private void initData() {
    List<Series> seriess = new ArrayList<Series>();
    homeInfor=activity.getHomeInfo();
    if(homeInfor==null){
      for(int i=0;i<8;i++){
        temperatureBefore =new ArrayList<>();
        temperatureBefore.add((float)0);
      }
    }else{
      if(dayString!=null&&dayString.equals("今天")){
        temperatureBefore =homeInfor.getTemperatures();
        Log.d(TAG, "initData: Today temperatureBefore"+ temperatureBefore);
        Log.d(TAG, "initData: today fragment= "+fragment);
      }else if(dayString!=null&&dayString.equals("昨天")){
        temperatureBefore =homeInfor.getTemperatures();
        Log.d(TAG, "initData: Yesterday temperatureBefore"+ temperatureBefore);
        Log.d(TAG, "initData: yesterday fragment= "+fragment);
      }
    }
    handleTemperatures();
    try {
      for (int i = 0; i < temperatures.size(); i++) {
        points.add(new Point((float) xLabels.get(i), temperatures.get(i), true));
      }
      Date date=new Date(System.currentTimeMillis());
      java.text.SimpleDateFormat format=new java.text.SimpleDateFormat("H:mm");
      currentTime=format.format(date);
      int hour=date.getHours();
      int minute=date.getMinutes();
//      hour=12;

      try {
//        currentTem = new Point((float) (hour - xLabels.get(0)) + (float) minute / 60.0f,
//            Float.parseFloat(homeInfor.getTemperature()), true);
        currentTem = new Point((float) (hour - xLabels.get(0)) + (float) minute / 60.0f,
            2.1f, true);
        if(hour>=xLabels.get(xLabels.size()-1)||hour<xLabels.get(0)){
          currentTem.willDrawing=false;
        }
      }catch (Exception e){
        currentTem=new Point(0f,0f,false);
        e.printStackTrace();
      }

      chart.getData().setCurrentTemperature(currentTem);
      chart.getData().setCurrentTime(currentTime);
      List<Float> sort=new ArrayList<>();
          sort.addAll(temperatures);
          Collections.sort(sort, new Comparator<Float>() {
        @Override
        public int compare(Float aFloat, Float t1) {
          if (aFloat<t1){
            return -1;
          }else if(aFloat>t1){
            return 1;
          }else {
            return 0;
          }
        }
      });
      maxTem.setText(String.valueOf(sort.get(sort.size()-1).intValue())+"℃");
      minTem.setText(String.valueOf(sort.get(0).intValue())+"℃");

    } catch (Exception e) {
      e.printStackTrace();
    }
    seriess.add(new Series("温度", Color.WHITE, points));
    chart.getData().setLabelTransform(new ChartData.LabelTransform() {
      @Override
      public String verticalTransform(int valueY) {
        return String.format("%d", valueY);
      }

      @Override
      public String horizontalTransform(int valueX) {
        return String.format("%s", valueX);
      }

      @Override
      public boolean labelDrawing(int valueX) {
        return true;
      }
    });

    chart.getData().setSeriesList(seriess);
    chart.refresh(true);
  }

  private void initView() {
    maxTem=(TextView)layout.findViewById(R.id.maxTem);
    minTem=(TextView)layout.findViewById(R.id.minTem);
    chart = (BesselChart) layout.findViewById(R.id.shidu_line_chart);

    barChart = (BarChart) layout.findViewById(R.id.bar_shidu_chart);
    back = (Button) layout.findViewById(R.id.return_btn);
    chart.setSmoothness(0.4f);
    chart.setChartListener(this);

    chart.setSmoothness(0.33f);
    day=(TextView)layout.findViewById(R.id.day);
  }

  private void initEvent() {

  }

  @Override
  public void onMove() {
    Log.d("zqt", "onMove");
  }
}
