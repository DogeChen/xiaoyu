package com.ainemo.pad.Datas;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 17-5-4.
 */

public class ContactListData extends DataSupport {
    private int id;
    private String name;
    private String xiaoyuNumber;
    private String phoneNumber;

    public String getXiaoyuNumber() {
        return xiaoyuNumber;
    }

    public void setXiaoyuNumber(String xiaoyuNumber) {
        this.xiaoyuNumber = xiaoyuNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String image_url;
    private String remark;
    private String address;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }


    public String getImage_url() {
        return image_url;
    }
}
