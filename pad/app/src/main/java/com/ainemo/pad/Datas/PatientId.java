package com.ainemo.pad.Datas;

import org.litepal.crud.DataSupport;

/**
 * Created by 小武哥 on 2017/6/7.
 */

public class PatientId extends DataSupport {
  private String patientID;

  public String getPatientID() {
    return patientID;
  }
  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }
}
