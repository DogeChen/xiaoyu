package com.ainemo.pad.Datas;

import com.google.gson.annotations.SerializedName;
import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/5/31.
 */

public class OneKeyWarning extends DataSupport{

    /**
     * sid : 158d000155be25
     * 0 : 158d000155be25
     * add_date : 2017-06-11 23:25:10
     * 1 : 2017-06-11 23:25:10
     */

    private String sid;
    @SerializedName("0")
    private String _$0;
    private String add_date;
    @SerializedName("1")
    private String _$1;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String get_$0() {
        return _$0;
    }

    public void set_$0(String _$0) {
        this._$0 = _$0;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String get_$1() {
        return _$1;
    }

    public void set_$1(String _$1) {
        this._$1 = _$1;
    }
}
