package com.ainemo.pad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.ainemo.pad.vPage.CardPagerAdapter;
import com.ainemo.pad.vPage.CustomViewPager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小武哥 on 2017/4/27.
 */

public class CaseListActivity extends AppCompatActivity {

  private List<String> imgList;
  private Button back;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_case_list);

    int mWidth = getWindowManager().getDefaultDisplay().getWidth();
    int mHeight=getWindowManager().getDefaultDisplay().getHeight();
    float heightRatio = (float)mWidth/(float)mHeight;  //高是宽的 1.630,根据图片比例

    initImgList();

    CardPagerAdapter cardAdapter = new CardPagerAdapter(getApplicationContext());
    cardAdapter.addImgUrlList(imgList);
    back=(Button)findViewById(R.id.return_btn);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

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

    int mHeightPadding=mHeight/5;
    CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.view_page);

    viewPager.setLayoutParams(
        new RelativeLayout.LayoutParams((int) (mWidth), mHeight));
    viewPager.setPadding(mWidthPading, mHeightPadding, mWidthPading, mHeightPadding);
    viewPager.setPageMargin(mWidth/10);
    viewPager.setClipToPadding(false);
    viewPager.setAdapter(cardAdapter);
    viewPager.showTransformer(0.1f);

  }


  public void initImgList() {
    imgList = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      imgList.add("str1");
      imgList.add("str2");
      imgList.add("str3");
      imgList.add("str4");
    }
  }

  /**
   * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
   */
  public int dp2px(float dpValue) {
    final float scale = getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

}
