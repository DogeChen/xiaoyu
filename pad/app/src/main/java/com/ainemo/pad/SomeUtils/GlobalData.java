package com.ainemo.pad.SomeUtils;

/**
 * Created by victor on 2017/4/28.
 */

public interface GlobalData {
    String Login_status = "login_status";
    String Phone = "phone";
    String Img_URl = "tou_xiang";
    String UserInfo = "UserInfo";
    String NAME = "NAME";
    String PWD = "PWD";
    String User_ID = "User_ID";
    // JSON status
    String Info = "info";
    String Value = "data";
    String Result = "status";
    String NET_ERROR = "网络错误，请稍后再试！";
    String BaiduPullKey = "Uvw5AMP15i9v1cUoS5aY7GR1";
    // 主机地址
    String MAIN_ENGINE = "http://139.196.40.97/OSAdmin-master/uploads/interface/regloginpost.php?";
    String GET_USR_INFOR = "http://139.196.40.97/OSAdmin-master/uploads/interface/getPatientFamily.php?";
    String GET_PATIENT_CASE = "http://139.196.40.97/OSAdmin-master/uploads/interface/patientcases.php?patientId=";

    // 发送验证码 codeType 1注册 2修改密码
    String SendCodeURL = "";
    // 用户注册
    String RegistURL = MAIN_ENGINE + "user/regigter";
    // 用户登录
    String Login_URL = MAIN_ENGINE + "user/login";
    // 更新用户信息
    String UpdateInfoURL = MAIN_ENGINE + "user/update_userinfo";
    // 更新用户信息
    String updateUserInfoURL = MAIN_ENGINE + "004";



}
