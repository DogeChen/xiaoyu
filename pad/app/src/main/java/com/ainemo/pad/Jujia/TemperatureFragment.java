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
import com.ainemo.pad.Jujia.drawSmoothLine.BesselChart;
import com.ainemo.pad.Jujia.drawSmoothLine.ChartData;
import com.ainemo.pad.Jujia.drawSmoothLine.Point;
import com.ainemo.pad.Jujia.drawSmoothLine.Series;
import com.ainemo.pad.R;
import com.github.mikephil.charting.charts.BarChart;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
  //  private Point currentTem;
  private View layout;
  private JujiaActivity activity;
  private List<Float> temperatures;
//  private List<Float> temperatures;
  List<Series> seriess;
  private List<Integer> xLabels = new ArrayList<>();
  private TextView maxTem;
  private TextView minTem;
  public TextView day;
  private String dayString;
  //  private String currentTime;
  Fragment fragment;
  private HomeInfor homeInfor;
  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 0x1234:
          dayString = msg.getData().getString("day");
          day.setText(dayString);
          Log.d(TAG, "handleMessage: fragment handler" + handler);
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
    fragment = this;
    Log.d(TAG, "onCreate fragment= " + fragment);
  }

  private void initData() {
    List<Series> seriess = new ArrayList<Series>();
    homeInfor = activity.getHomeInfo(dayString);
    try {
      if (dayString != null && dayString.equals("今天")) {
        temperatures = homeInfor.getTemperatures();
        Log.d(TAG, "initData: Today temperatures" + temperatures);
        Log.d(TAG, "initData: today fragment= " + fragment);
      } else if (dayString != null && dayString.equals("昨天")) {
        temperatures = homeInfor.getTemperatures();
        Log.d(TAG, "initData: Yesterday temperatures" + temperatures);
        Log.d(TAG, "initData: yesterday fragment= " + fragment);
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
//    handleTemperatures();
//        temperatures = new ArrayList<>();
//    Float tem = 0.0f;
//    temperatures.add(tem);
//    temperatures.add(tem);
//    tem = 2.2f;
//    temperatures.add(tem);
//    temperatures.add(tem);
//    tem = 2.8f;
//    temperatures.add(tem);
//    temperatures.add(tem);
//    tem = 3.9f;
//    temperatures.add(tem);
//    tem = 3.9f;
//    temperatures.add(tem);
//    tem = 1.0f;
//    temperatures.add(tem);
//    tem = 5.5f;
//    temperatures.add(tem);
//    tem = 3.9f;
//    temperatures.add(tem);
//    tem = 2.1f;
//    temperatures.add(tem);
//    temperatures.add(tem);
//    tem = 3.9f;
//    temperatures.add(tem);
//    tem = 0.0f;
//    temperatures.add(tem);
//    temperatures.add(tem);
//    temperatures.add(tem);
//    temperatures.add(tem);
//    temperatures.add(tem);
//    temperatures.add(tem);
//    temperatures.add(tem);
//    temperatures.add(tem);
//    temperatures.add(tem);
//    temperatures.add(tem);
    try {
      for (int i = 0; i < temperatures.size(); i++) {
        points.add(new Point((float) i, temperatures.get(i),
            !isApproachingZero(temperatures.get(i))));
      }

      List<Float> sort = new ArrayList<>();
      sort.addAll(temperatures);
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
      String max = "--" + "℃";
      String maxWithDot="--" + "℃";
      chart.getData().setMaxTemperature(max);
      for (int i = sort.size() - 1; i >= 0; i--) {
        if (!isApproachingZero(sort.get(i))) {
          max = String.valueOf(sort.get(i).intValue()) + "℃";
          maxWithDot=String.format("%.1f",sort.get(i).floatValue())+"℃";
          break;
        }
      }
      maxTem.setText(max);
      chart.getData().setMaxTemperature(maxWithDot);

      String min = "--" + "℃";
      String minWithDot="--" + "℃";
      for (int i = 0; i < sort.size(); i++) {
        if (!isApproachingZero(sort.get(i))) {
          min = String.valueOf(sort.get(i).intValue()) + "℃";
          minWithDot=String.format("%.1f",sort.get(i).floatValue())+"℃";
          break;
        }
      }
      minTem.setText(min);
      chart.getData().setMinTemperature(minWithDot);

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
        if (valueX % 3 == 0) {
          return String.format("%s", valueX);
        } else {
          return "";
        }
      }

      @Override
      public boolean labelDrawing(int valueX) {
        return true;
      }
    });
    chart.getData().setSeriesList(seriess);
    chart.refresh(false);
  }

  private boolean isApproachingZero(Float i) {
    if (i - 0.0f <= 0.1f&&i - 0.0f >= -0.1f) {
      return true;
    } else {
      return false;
    }
  }

  private void initView() {
    maxTem = (TextView) layout.findViewById(R.id.maxTem);
    minTem = (TextView) layout.findViewById(R.id.minTem);
    chart = (BesselChart) layout.findViewById(R.id.shidu_line_chart);

    barChart = (BarChart) layout.findViewById(R.id.bar_shidu_chart);
    back = (Button) layout.findViewById(R.id.return_btn);

    chart.setChartListener(this);
    chart.setSmoothness(0.5f);
    day = (TextView) layout.findViewById(R.id.day);
  }

  private void initEvent() {

  }

  @Override
  public void onMove() {
    Log.d("zqt", "onMove");
  }
}
