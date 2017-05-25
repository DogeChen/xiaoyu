package com.ainemo.pad.Contact.Record;

import java.util.Date;
import org.litepal.crud.DataSupport;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class CallRecord  extends DataSupport{
  public static final int CALL_IN=0;
  public static final int CALL_OUT=1;
  public static final int CALL_REJECT=2;



  private int id;
  private String name;
  private String telephoneNum;
  private String xiaoyuId;
  private int state;
  private Date date;
  private Date hangUpTime;
  private String imageUrl;

  public Date getHangUpTime() {
    return hangUpTime;
  }

  public void setHangUpTime(Date hangUpTime) {
    this.hangUpTime = hangUpTime;
  }

  public CallRecord() {
  }

  public CallRecord(String name, String telephoneNum, String xiaoyuId, int state, Date date,
                    Date hangUpTime, String imageUrl) {
    this.name = name;
    this.telephoneNum = telephoneNum;
    this.xiaoyuId = xiaoyuId;
    this.state = state;
    this.date = date;
    this.hangUpTime = hangUpTime;
    this.imageUrl=imageUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTelephoneNum() {
    return telephoneNum;
  }

  public void setTelephoneNum(String telephoneNum) {
    this.telephoneNum = telephoneNum;
  }

  public String getXiaoyuId() {
    return xiaoyuId;
  }

  public void setXiaoyuId(String xiaoyuId) {
    this.xiaoyuId = xiaoyuId;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
