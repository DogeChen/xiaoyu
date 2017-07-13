package com.ainemo.pad.Datas;

import org.litepal.crud.DataSupport;

/**
 * Created by Silver on 2017/7/11.
 */

public class PatientXiaoyu extends DataSupport {


    /**
     * xiaoyuNum : 99971
     * uid : 26
     */

    private String xiaoyuNum;
    private String uid;

    public String getXiaoyuNum() {
        return xiaoyuNum;
    }

    public void setXiaoyuNum(String xiaoyuNum) {
        this.xiaoyuNum = xiaoyuNum;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
