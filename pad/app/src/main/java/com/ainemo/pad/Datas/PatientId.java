package com.ainemo.pad.Datas;

import org.litepal.crud.DataSupport;

/**
 * Created by 小武哥 on 2017/6/7.
 */

public class PatientId extends DataSupport {

    /**
     * uid : 4
     * identity : patient
     */

    private String uid;
    private String identity;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
