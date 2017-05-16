package com.ainemo.pad.vPage;

import java.io.Serializable;

/**
 * Created by 小武哥 on 2017/5/3.
 */

public class CaseInfor  implements Serializable {
  /**
   id:病例id  name：患者名字  creationDate：病例创建时间
   patientId：患者id
   doctorId： 就诊医生id
   illproblem: 病情
   illresult:  诊断结果
   temperaturn: 病人测量体温
   blood_pressure: 病人测量血压值
   */

  private String id;
  private String name;
  private String creationDate;
  private String patientId;
  private String doctorId;
  private String illproblem;
  private String illresult;
  private String temperature;
  private String blood_pressure;

  public CaseInfor(String id, String name, String creationDate, String patientId,
      String doctorId, String illproblem, String illresult, String temperature,
      String blood_pressure) {
    this.id = id;
    this.name = name;
    this.creationDate = creationDate;
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.illproblem = illproblem;
    this.illresult = illresult;
    this.temperature = temperature;
    this.blood_pressure = blood_pressure;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(String doctorId) {
    this.doctorId = doctorId;
  }

  public String getIllproblem() {
    return illproblem;
  }

  public void setIllproblem(String illproblem) {
    this.illproblem = illproblem;
  }

  public String getIllresult() {
    return illresult;
  }

  public void setIllresult(String illresult) {
    this.illresult = illresult;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getBlood_pressure() {
    return blood_pressure;
  }

  public void setBlood_pressure(String blood_pressure) {
    this.blood_pressure = blood_pressure;
  }
}

