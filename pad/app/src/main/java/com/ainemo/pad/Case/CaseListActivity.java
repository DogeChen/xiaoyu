package com.ainemo.pad.Case;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.ainemo.pad.Datas.CaseInfor;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.Utils;
import com.ainemo.pad.Case.vPage.CardPagerAdapter;
import com.ainemo.pad.Case.vPage.CustomViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import org.litepal.crud.DataSupport;

/**
 * Created by 小武哥 on 2017/4/27.
 */

public class CaseListActivity extends AppCompatActivity implements CardPagerAdapter.OnCardItemClickListener,View.OnClickListener{
  private List<CaseInfor> caseList;
  private Button back;
  private static final String TAG = "CaseListActivity";
  private CardPagerAdapter adapter =null;
  private boolean net_work_available, has_data;
  private String patientId;
  private ProgressDialog progressDialog;
  private CustomViewPager viewPager;

  private Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == 0x123) {
        Utils.showShortToast(CaseListActivity.this, "没有数据");
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_case_list);
    patientId = getIntent().getStringExtra("id");
    if (patientId == null || patientId.equals("")) {
      Utils.showShortToast(CaseListActivity.this, "此用户没有绑定患者");
      Utils.finishActivity(CaseListActivity.this);
    } else {
      Log.e("patientid", patientId);
//            DisplayUtils.init(this);//获取屏幕宽度高度信息
      initView();
      initEvent();
      new CaseListTask().execute(patientId);
    }

  }

  public void initView(){
    int mWidth = getWindowManager().getDefaultDisplay().getWidth();
    int mHeight=getWindowManager().getDefaultDisplay().getHeight();
    float heightRatio = (float)mWidth/(float)mHeight;  //根据图片比例


    back=(Button)findViewById(R.id.return_btn);


    //设置阴影大小，即vPage  左右两个图片相距边框  maxFactor + 0.3*CornerRadius   *2
    //设置阴影大小，即vPage 上下图片相距边框  maxFactor*1.5f + 0.3*CornerRadius
    int maxFactor = mWidth / 25;
    adapter.setMaxElevationFactor(maxFactor);

    int mWidthPading =(int )( mWidth / 3);

    //因为我们adapter里的cardView CornerRadius已经写死为10dp，所以0.3*CornerRadius=3
    //设置Elevation之后，控件宽度要减去 (maxFactor + dp2px(3)) * heightRatio
    //heightMore 设置Elevation之后，控件高度 比  控件宽度* heightRatio  多出的部分
    float heightMore = (1.5f * maxFactor + dp2px(3)) - (maxFactor + dp2px(3)) * heightRatio;
//    int mHeightPadding = (int) (mHeight - heightMore);

    int mHeightPadding=mHeight/10;
    viewPager = (CustomViewPager) findViewById(R.id.view_page);

    viewPager.setLayoutParams(
        new RelativeLayout.LayoutParams((int) (mWidth), mHeight));
    viewPager.setPadding(mWidthPading,mHeightPadding, mWidthPading,0);
//    viewPager.setPageMargin(mWidth/10);
    viewPager.setClipToPadding(false);

    viewPager.showTransformer(0.1f);
    net_work_available = Utils.isNetWorkAvailabe(CaseListActivity.this);
    progressDialog = new ProgressDialog(this);
    progressDialog.show();
  }

  public void initEvent(){
    back.setOnClickListener(this);

    adapter.setOnCardItemClickListener(this);
  }
  public void initCaseList() {
    caseList = new ArrayList<>();

  }

  /**
   * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
   */
  public int dp2px(float dpValue) {
    final float scale = getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }


  @Override
  public void onClick(int position) {
    Log.d(TAG, "onClick: viewPage");
    if(position>=0&&position<=caseList.size()){
      Intent intent=new Intent(CaseListActivity.this,CaseDetailActivity.class);
      CaseInfor caseInfor=caseList.get(position);
      intent.putExtra("caseInforId",caseInfor.getId());
      startActivity(intent);
    }
  }

  @Override
  public void onClick(View view) {
    if(view.getId()==R.id.return_btn){
      finish();
    }
  }
  class CaseListTask extends AsyncTask<String, Void, String> {
    private Gson gson = new Gson();

    @Override

    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      if (s != null && has_data) {
        initCaseList();
        adapter = new CardPagerAdapter(getApplicationContext());
        adapter.addCaseList(caseList);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        for (CaseInfor caseInfor : caseList) {
          Log.e("id ", "hello " + caseInfor.getId());
        }

      } else {
        handler.sendEmptyMessage(0x123);
      }
      progressDialog.dismiss();
    }

    @Override
    protected String doInBackground(String... params) {
      if (net_work_available) {
        caseList = gson.fromJson(Utils.sendRequest(GlobalData.GET_PATIENT_CASE + patientId), new TypeToken<List<CaseInfor>>() {
        }.getType());
//        CaseInfor caseInfor = new CaseInfor();
//        caseInfor.setName("流云");
//        caseInfor.setCreationDate("20170518");
//        caseInfor.setDoctorName("孙里");
//        caseInfor.setIllproblem("咳嗽");
//        caseList.add(caseInfor);
        DataSupport.deleteAll(CaseInfor.class);
        for (CaseInfor caseInfor : caseList) {
          if (!caseInfor.isSaved()) {
            caseInfor.save();
          }
        }
        has_data = true;

      } else {
        if (DataSupport.isExist(CaseInfor.class)) {
          caseList = DataSupport.findAll(CaseInfor.class);
          has_data = true;
        } else {
          has_data = false;
        }
      }

      return "ok";
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog.show();
    }
  }
}
