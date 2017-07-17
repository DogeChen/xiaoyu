package com.ainemo.pad.Datas;

import org.litepal.crud.DataSupport;

/**
 * Created by Silver on 2017/7/17.
 */

public class DoctorCaseList extends DataSupport {


    /**
     * name : 刘云
     * patientid : 2
     * sex : 女
     * age : 67
     * image : 89c20aa1fba1a5dbb02d44b41ba94c96ed42ff6b.jpg
     */

    private int id;
    private String name;
    private String patientid;
    private String sex;
    private String age;
    private String image;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
