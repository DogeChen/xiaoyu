package com.ainemo.pad.Contact.Record;

import android.provider.CallLog;

import org.litepal.crud.DataSupport;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class CallRecord  extends DataSupport{
  public static final int CALL_IN= CallLog.Calls.INCOMING_TYPE;
  public static final int CALL_OUT=CallLog.Calls.OUTGOING_TYPE;
  public static final int CALL_REJECT=CallLog.Calls.REJECTED_TYPE;

  private int id;
  private String name;
  private String telephoneNum;
  private String xiaoyuId;
  private int type;
  private Long date;
  private Integer duration;
  private String imageUrl;

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public CallRecord() {
  }

  public CallRecord(String name, String telephoneNum, String xiaoyuId, int type, Long date,
                    Integer duration, String imageUrl) {
    this.name = name;
    this.telephoneNum = telephoneNum;
    this.xiaoyuId = xiaoyuId;
    this.type = type;
    this.date = date;
    this.duration = duration;
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

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public Long getDate() {
    return date;
  }

  public void setDate(Long date) {
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
