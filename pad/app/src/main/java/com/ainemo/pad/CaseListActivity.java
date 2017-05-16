package com.ainemo.pad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.ainemo.pad.vPage.CardPagerAdapter;
import com.ainemo.pad.vPage.CaseInfor;
import com.ainemo.pad.vPage.CustomViewPager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小武哥 on 2017/4/27.
 */

public class CaseListActivity extends AppCompatActivity implements CardPagerAdapter.OnCardItemClickListener,View.OnClickListener{
  private List<CaseInfor> caseList;
  private Button back;
  private static final String TAG = "CaseListActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_case_list);

    int mWidth = getWindowManager().getDefaultDisplay().getWidth();
    int mHeight=getWindowManager().getDefaultDisplay().getHeight();
    float heightRatio = (float)mWidth/(float)mHeight;  //根据图片比例

    initCaseList();

    CardPagerAdapter cardAdapter = new CardPagerAdapter(getApplicationContext());
    cardAdapter.addCaseList(caseList);
    back=(Button)findViewById(R.id.return_btn);
    back.setOnClickListener(this);

    //设置阴影大小，即vPage  左右两个图片相距边框  maxFactor + 0.3*CornerRadius   *2
    //设置阴影大小，即vPage 上下图片相距边框  maxFactor*1.5f + 0.3*CornerRadius
    int maxFactor = mWidth / 25;
    cardAdapter.setMaxElevationFactor(maxFactor);

    int mWidthPading =(int )( mWidth / 3);

    //因为我们adapter里的cardView CornerRadius已经写死为10dp，所以0.3*CornerRadius=3
    //设置Elevation之后，控件宽度要减去 (maxFactor + dp2px(3)) * heightRatio
    //heightMore 设置Elevation之后，控件高度 比  控件宽度* heightRatio  多出的部分
    float heightMore = (1.5f * maxFactor + dp2px(3)) - (maxFactor + dp2px(3)) * heightRatio;
//    int mHeightPadding = (int) (mHeight - heightMore);

    int mHeightPadding=mHeight/10;
    CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.view_page);

    viewPager.setLayoutParams(
        new RelativeLayout.LayoutParams((int) (mWidth), mHeight));
    viewPager.setPadding(mWidthPading,mHeightPadding, mWidthPading,0);
//    viewPager.setPageMargin(mWidth/10);
    viewPager.setClipToPadding(false);
    viewPager.setAdapter(cardAdapter);
    viewPager.showTransformer(0.1f);
    cardAdapter.setOnCardItemClickListener(this);
  }


  public void initCaseList() {
    caseList = new ArrayList<>();
    caseList.add(new CaseInfor("1","张小花","","","","","头痛","无大碍",""));

    caseList.add(new CaseInfor("2","王大白","","","","","头痛","无大碍",""));

    caseList.add(new CaseInfor("3","王小明","","","","","头痛","无大碍",""));
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
      intent.putExtra("caseInfor",caseInfor);
      startActivity(intent);
    }
  }

  @Override
  public void onClick(View view) {
    if(view.getId()==R.id.return_btn){
      finish();
    }
  }
}
