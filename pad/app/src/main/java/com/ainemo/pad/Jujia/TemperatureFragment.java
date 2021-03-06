package com.ainemo.pad.Jujia;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ainemo.pad.Datas.HomeInfor;
import com.ainemo.pad.Jujia.drawSmoothLine.BesselChart;
import com.ainemo.pad.Jujia.drawSmoothLine.ChartData;
import com.ainemo.pad.Jujia.drawSmoothLine.Point;
import com.ainemo.pad.Jujia.drawSmoothLine.Series;
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
 * Created by 小武哥 on 2017/5/19.
 */

public class TemperatureFragment extends Fragment implements BesselChart.ChartListener {

  BesselChart chart;

  private TextView minHumidity;

  private TextView humidity;
  private static final String TAG = "TemperatureFragment";
  private BarChart barChart;
  //  private Button back;
  private List<Point> points = new ArrayList<>();
  //  private Point currentTem;
  private View layout;
  private JujiaActivity activity;
  private List<Float> temperatures;

  List<Series> seriess;
  private List<Integer> xbels = new ArrayList<>();
  private TextView maxTem;
  private TextView minTem;
  public TextView day;
  private String dayString;
  private GetHomeInforTask getHomeInforTask;
  public int offset;
  private boolean isGetHomeInfor = false;
  Fragment fragment;
  private static HomeInfor[] homeInfors = new HomeInfor[7];
  private android.support.v4.view.ViewPager viewPager;


  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 0x1234:
          Log.d(TAG, "handleMessage: fragment handler" + handler);
          initData();
          Log.d(TAG, "handleMessage: currIten=" + viewPager.getCurrentItem());
          if (offset == 6 - viewPager.getCurrentItem()) {
            initHumidityData(viewPager.getCurrentItem());
          }
          break;
      }
    }
  };

  @Override
  public void onResume() {
    super.onResume();
  }

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

    if (isGetHomeInfor == false) {
      getHomeInforTask = new GetHomeInforTask();
      getHomeInforTask.execute(offset);
    }
    isGetHomeInfor = true;
    initEvent();

    if (offset == 0) {
      initHumidityData(6);
    }

    return layout;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = (JujiaActivity) getActivity();
    fragment = this;
    Log.d(TAG, "onCreate fragment= " + fragment);
    viewPager = activity.getJiujiaviewpager();
    viewPager.setOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        initHumidityData(position);
        Log.d(TAG, "onPageSelected: true");
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (getHomeInforTask != null) {
      getHomeInforTask.cancel(true);
    }

  }

  private void initHumidityData(int position) {
    minHumidity = activity.getMinHumidity();
    humidity = activity.getHumidity();
    barChart = activity.getBarChart();
    IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
      private String[] labels = new String[]{"03", "06", "09", "12", "15", "18", "21", "24"};

      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        float percent = value / axis.mAxisRange;
        return labels[(int) ((labels.length) * percent)];
      }
    };

    if (position == viewPager.getCurrentItem()) {
      int offset1 = 6 - position;
      List<BarEntry> barEntries = new ArrayList<>();
      if (homeInfors[offset1] != null && homeInfors[offset1].getHumidityies() != null) {
        for (int i = 0; i < 8; i++) {
          barEntries
              .add(new BarEntry((float) i, homeInfors[offset1].getHumidityies().get(i * 3 + 2)));
        }
      } else {
        for (int i = 0; i < 8; i++) {
          barEntries.add(new BarEntry((float) i, (float) 0));
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

      if (homeInfors[offset1] != null) {
        humidity.setText((int) (homeInfors[offset1].getHumidity() + 0.5f) + "%");
        try {
          List<Float> sort = new ArrayList<>();
          sort.addAll(homeInfors[offset1].getHumidityies());
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
          String minHum = "--%";
          for (int i = 0; i < sort.size(); i++) {
            if (!isApproachingZero(sort.get(i))) {
              minHum = sort.get(i).intValue() + "%";
              break;
            }
          }
          minHumidity.setText(minHum);
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void initData() {
    List<Series> seriess = new ArrayList<Series>();
//    homeInfor = activity.getHomeInfo(offset);

    try {
      if (dayString != null) {
        temperatures = homeInfors[offset].getTemperatures();
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    if (temperatures == null) {
      temperatures = new ArrayList<>();
      for (int i = 0; i < 24; i++) {
        temperatures.add(0.0f);
      }
    }

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
      Log.d(TAG, "initData: sorts" + sort);
      if (isApproachingZero(sort.get(sort.size() - 1))) {
        chart.getData().setHasData(false);
      }
      if (offset == 0) {
        chart.getData().setToday(true);
      }
      String curr =
          homeInfors[offset].getTemperature()
              .substring(0, homeInfors[offset].getTemperature().indexOf('.') + 2)
              + "℃";
      chart.getData().setCurrTemperature(curr);
      String max = "--" + "℃";
      String maxWithDot = "--" + "℃";
      chart.getData().setMaxTemperature(max);

      for (int i = sort.size() - 1; i >= 0; i--) {
        if (!isApproachingZero(sort.get(i))) {
          max = String.valueOf(sort.get(i).intValue()) + "℃";
          maxWithDot = String.format("%.1f", sort.get(i).floatValue()) + "℃";
          break;
        }
      }
      maxTem.setText(max);
      chart.getData().setMaxTemperature(maxWithDot);

      String min = "--" + "℃";
      String minWithDot = "--" + "℃";

      for (int i = 0; i < sort.size(); i++) {
        if (!isApproachingZero(sort.get(i))) {
          min = String.valueOf(sort.get(i).intValue()) + "℃";
          minWithDot = String.format("%.1f", sort.get(i).floatValue()) + "℃";
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
    if (i - 0.0f <= 0.1f && i - 0.0f >= -0.1f) {
      return true;
    } else {
      return false;
    }
  }

  private void initView() {
    maxTem = (TextView) layout.findViewById(R.id.maxTem);
    minTem = (TextView) layout.findViewById(R.id.minTem);
    chart = (BesselChart) layout.findViewById(R.id.shidu_line_chart);

    chart.setChartListener(this);
    chart.setSmoothness(0.5f);
    day = (TextView) layout.findViewById(R.id.day);
  }

  private void initEvent() {
    dayString = Utils.formatDay(offset, "M月d日", true);
    day.setText(dayString);
  }

  class GetHomeInforTask extends AsyncTask<Integer, Void, Void> {

    private Gson gson = new Gson();


    @Override
    protected Void doInBackground(Integer... params) {
      try {

        String day = Utils.formatDay(params[0].intValue(), "yyyyMMdd", false);
        Log.d(TAG, "doInBackground: day=" + day);
        String infor = Utils.sendRequest(
            GlobalData.GET_HOME_INFOR + Utils.getValue(activity, GlobalData.PATIENT_ID)
                + "&date=" + day);
        homeInfors[offset] = gson.fromJson(infor, HomeInfor.class);

      } catch (Exception e) {
        e.printStackTrace();
      }
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
    Log.d("zqt", "onMove");
  }
}
