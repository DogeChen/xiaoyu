package com.ainemo.pad.Jujia;

import android.app.Activity;
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
import com.ainemo.pad.Datas.HomeInfor;
import com.ainemo.pad.R;
import com.ainemo.pad.drawSmoothLine.BesselChart;
import com.ainemo.pad.drawSmoothLine.ChartData;
import com.ainemo.pad.drawSmoothLine.Point;
import com.ainemo.pad.drawSmoothLine.Series;
import com.github.mikephil.charting.charts.BarChart;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小武哥 on 2017/5/19.
 */

public class TemperatureFragment extends Fragment implements BesselChart.ChartListener{
  BesselChart chart;

//    private Chart mChart;

  private BarChart barChart;
  private Button back;
  private List<Point> points=new ArrayList<>();

  private View layout;
  private Activity activity;
  private HomeInfor homeInfor;
  Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == 0x1234) {
        List<Series> seriess = new ArrayList<Series>();
        for(int i=0;i<8;i++){
          points.add(new Point((float)i, homeInfor.getTemperatures().get(i),true));
        }
        seriess.add(new Series("温度", Color.WHITE,points));
        chart.getData().setLabelTransform(new ChartData.LabelTransform() {
          @Override
          public String verticalTransform(int valueY) {
            return String.format("%d", valueY);
          }

          @Override
          public String horizontalTransform(int valueX) {
            return String.format("%s", valueX );
          }
          @Override
          public boolean labelDrawing(int valueX) {
            return true;
          }
        });
        initData();

        chart.getData().setSeriesList(seriess);
        chart.refresh(true);
      }
    }
  };
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    if (layout == null) {
      layout = activity.getLayoutInflater().inflate(R.layout.activity_jujia2, null);
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
    activity = getActivity();
  }

  private void initData(){
  }

  private void initView(){
    chart = (BesselChart) layout.findViewById(R.id.shidu_line_chart);

    barChart = (BarChart) layout.findViewById(R.id.bar_shidu_chart);
    back = (Button) layout.findViewById(R.id.return_btn);
    chart.setSmoothness(0.4f);
    chart.setChartListener(this);
    chart.setChartListener(this);

    chart.setSmoothness(0.33f);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new GetHomeInforTask().execute();
//            }
//        }, 500);
  }

  private void initEvent() {

  }

  /**
   * Point 点的值（x,y）  x为横轴的值，y为纵轴的值，willingDrawing 为使用此点
   *
   * @param willDrawing true(draw) false(don't draw)
   */

  private void getSeriesList(boolean willDrawing) {
    if (willDrawing) {
    }
  }



  @Override
  public void onMove() {
    Log.d("zqt", "onMove");
  }
}
