package com.ainemo.pad;

import ainemo.api.openapi.NemoOpenAPI;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import org.litepal.LitePal;

/**
 * Created by 小武哥 on 2017/5/16.
 */

public class MyApplication extends Application {
  private static Context context;
  private static final String TAG = "MyApplication";
  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    LitePal.initialize(context);

    NemoOpenAPI.init(this);
    Log.d(TAG, "onCreate: NemoOpenAPI");
  }


  public static Context getContext() {
    return context;

  }
}
