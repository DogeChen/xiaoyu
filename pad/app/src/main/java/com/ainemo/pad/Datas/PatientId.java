package com.ainemo.pad.Datas;

import org.litepal.crud.DataSupport;

/**
 * Created by 小武哥 on 2017/6/7.
 */

public class PatientId extends DataSupport {
  private String uid;

  public String getUid() {
    return uid;
  }
  public void setUid(String uid) {
    this.uid = uid;
  }
}
