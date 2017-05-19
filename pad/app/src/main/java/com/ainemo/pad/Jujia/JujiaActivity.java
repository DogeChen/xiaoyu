package com.ainemo.pad.Jujia;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.ainemo.pad.Datas.HomeInfor;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.Utils;
import com.ainemo.pad.drawSmoothLine.BesselChart.ChartListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends AppCompatActivity implements ChartListener{




    private android.support.v4.view.ViewPager jiujiaviewpager;
    private JiuJiaViewPageAdapter viewPageAdapter;

    Handler handler = new Handler();
    private BarChart barChart;
    private Button back;
    private HomeInfor homeInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jujia2);
        initView();
        initEvent();
        initData();
    }
    private void initData(){
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 10));
        entries.add(new Entry(1f, 20));
        entries.add(new Entry(2f, 23));
        entries.add(new Entry(3f, 18));
        entries.add(new Entry(4f, 45));
        entries.add(new Entry(5f, 30));
        entries.add(new Entry(6f, 35));
        entries.add(new Entry(7f, 16));
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        lineDataSet.setColors(Color.YELLOW, Color.BLUE, Color.DKGRAY);
        lineDataSet.setFillColor(Color.BLUE);
        lineDataSet.setCircleColor(255);
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setLineWidth(6);
        lineDataSet.setFillAlpha(255);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(18);
        lineDataSet.setDrawValues(false);
        LineData lineData = new LineData(lineDataSet);

        IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
            private String[] labels = new String[]{"03","06","09","12","15","18","21","24"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                float percent = value / axis.mAxisRange;
                return labels[(int) ((labels.length - 1) * percent)];
            }
        };
        Description description = new Description();
        description.setText("23");
        description.setTextSize(40);
        description.setPosition(140,225);
        description.setTextColor(Color.WHITE);
//        mChart.setDescription(description);
//
//        mChart.invalidate();
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 45));
        barEntries.add(new BarEntry(1f, 34));
        barEntries.add(new BarEntry(2f, 67));
        barEntries.add(new BarEntry(3f, 20));
        barEntries.add(new BarEntry(4f, 78));
        barEntries.add(new BarEntry(5f, 25));
        barEntries.add(new BarEntry(6f, 10));
        barEntries.add(new BarEntry(7f, 35));
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
        description1.setPosition(0,0);
        barChart.setDescription(description1);
        barChart.setDrawBarShadow(true);

        barChart.getLegend().setEnabled(false);
        barChart.invalidate();
    }

    private void initView(){

        barChart = (BarChart) findViewById(R.id.bar_shidu_chart);
        back = (Button) findViewById(R.id.return_btn);
        this.jiujiaviewpager = (ViewPager) findViewById(R.id.jiujia_view_pager);
        viewPageAdapter = new JiuJiaViewPageAdapter(getSupportFragmentManager());
        jiujiaviewpager.setAdapter(viewPageAdapter);
        jiujiaviewpager.setCurrentItem(1);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class GetHomeInforTask extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();
        @Override
        protected Void doInBackground(Void... params) {
            String infor = Utils.sendRequest(
                GlobalData.GET_HOME_INFOR + Utils.getValue(JujiaActivity.this, GlobalData.PATIENT_ID));
            homeInfor = gson.fromJson(infor, HomeInfor.class);
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
}

