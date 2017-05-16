package com.ainemo.pad;

import android.app.Application;
import android.content.Context;
import org.litepal.LitePal;

/**
 * Created by 小武哥 on 2017/5/16.
 */

public class MyApplication extends Application {
  private static Context context;
  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    LitePal.initialize(context);

//        Settings settings = new Settings("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3", false, false);   // production 环境
//          NemoSDK.getInstance().init(getApplicationContext(), settings);
//        String exited=new String("B7XFT0B5M3PFO8TMPDQRQLZECHT0LJZPTK15YTUD/BO=");
//        Settings settings = new Settings(exited); //如果是私有云Settings settings = new Settings(String extid, String privateCloudAddress)
//        NemoSDK nemoSDK=NemoSDK.getInstance();
//        nemoSDK.init(this,settings);
  }


  public static Context getContext() {
    return context;

  }
}
