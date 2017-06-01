package com.ainemo.pad.Case;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.ainemo.pad.Case.vPage.CardPagerAdapter;
import com.ainemo.pad.Case.vPage.CustomViewPager;
import com.ainemo.pad.Datas.CaseInfor;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
  private CardView item;

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

      initView();
      initEvent();
      new CaseListTask().execute(patientId);
    }

  }

  public void initView(){
    int mWidth = getWindowManager().getDefaultDisplay().getWidth();
    int mHeight=getWindowManager().getDefaultDisplay().getHeight();

    back=(Button)findViewById(R.id.return_btn);


    //设置阴影大小，即vPage  左右两个图片相距边框  maxFactor + 0.3*CornerRadius   *2
    //设置阴影大小，即vPage 上下图片相距边框  maxFactor*1.5f + 0.3*CornerRadius
    int maxFactor = mWidth / 25;


    int mWidthPading =(int )( mWidth / 3);

    viewPager = (CustomViewPager) findViewById(R.id.view_page);

    adapter = new CardPagerAdapter(getApplicationContext());
    adapter.setMaxElevationFactor(maxFactor);

    viewPager.setLayoutParams(
        new LinearLayout.LayoutParams((int) (mWidth), mHeight-200));

//    viewPager.setLayoutParams(new LinearLayout.LayoutParams((int) (mWidth), 595));
    viewPager.setPadding(mWidthPading,0, mWidthPading,0);

    viewPager.setPageMargin(0);
    viewPager.setBottom(100);
    viewPager.setClipToPadding(false);

    net_work_available = Utils.isNetWorkAvailabe(CaseListActivity.this);
    progressDialog = new ProgressDialog(this);
    progressDialog.show();
  }

  public void initEvent(){
    back.setOnClickListener(this);
    adapter.setOnCardItemClickListener(this);
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
//        initCaseList();

        adapter.addCaseList(caseList);
        viewPager.setAdapter(adapter);
        viewPager.showTransformer(0.1f);
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
//        caseList=new ArrayList<>();
//        CaseInfor caseInfor = new CaseInfor();
//        caseInfor.setName("流云");
//        caseInfor.setCreationDate("20170518");
//        caseInfor.setDoctorName("孙里");
//        caseInfor.setIllproblem("咳嗽");
//        caseList.add(caseInfor);
//        caseList.add(caseInfor);
//
//        caseList.add(caseInfor);
//        caseList.add(caseInfor);
//        caseList.add(caseInfor);

        DataSupport.deleteAll(CaseInfor.class);
        for (CaseInfor caseInfor1 : caseList) {
          if (!caseInfor1.isSaved()) {
            caseInfor1.save();
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
