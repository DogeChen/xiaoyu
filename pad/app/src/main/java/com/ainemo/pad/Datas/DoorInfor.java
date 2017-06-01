package com.ainemo.pad.Datas;

/**
 * Created by 小武哥 on 2017/5/31.
 */

public class DoorInfor {
  private String sid;
  private int status;
  private String add_date;

  public void setAdd_date(String add_date) {
    this.add_date = add_date;
  }



  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public String getAdd_date() {
    return add_date;
  }
}
