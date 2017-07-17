package com.ainemo.pad.Case;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ainemo.pad.Datas.DoctorInfor;
import com.ainemo.pad.Datas.DoctorXiaoyu;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.MyBitmapUtils;
import com.ainemo.pad.SomeUtils.Utils;
import com.google.gson.Gson;

import ainemo.api.openapi.NemoCallback;
import ainemo.api.openapi.NemoOpenAPI;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

import org.litepal.crud.DataSupport;

/**
 * Created by 小武哥 on 2017/5/4.
 */

public class DoctorActivity extends AppCompatActivity implements NemoCallback {


    private String doctorId;
    private Button returnBtn;
    private de.hdodenhof.circleimageview.CircleImageView ImageView;
    private TextView doctorName;
    //  private TextView persondetailinfodoctor;
    private TextView gender;
    private TextView age;
    private TextView jobTitle;
    private TextView hospital;
    private TextView email;
    private TextView talent;
    private TextView introduction;
    private ProgressDialog dialog;
    private DoctorInfor doctorInfor;
    private boolean net_work_available, has_data;
    private String name;
    private static final String TAG = "DoctorActivity";
    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    private DoctorXiaoyu doctorXiaoyu;
    private boolean callable = false;
    private boolean isPatient;
    private TextView doctorCallText;
    private android.widget.ImageView callImage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        doctorId = getIntent().getStringExtra("doctorId");
        Log.d(TAG, "onCreate: doctorId=" + doctorId);
        isPatient = getIntent().getBooleanExtra(GlobalData.IS_PATIENT, true);
        NemoOpenAPI.getInstance().registerCallback(this);
        initView();
        initEvent();
        try {
            new GetDoctorInfor().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();
        new GetDoctorInfor().execute();
    }

    private void initView() {
        returnBtn = (Button) findViewById(R.id.return_btn);
        dialog = new ProgressDialog(this);
        dialog.show();
        this.introduction = (TextView) findViewById(R.id.introduction);
        this.talent = (TextView) findViewById(R.id.talent);
        this.email = (TextView) findViewById(R.id.email);
        this.hospital = (TextView) findViewById(R.id.hospital);
        this.jobTitle = (TextView) findViewById(R.id.job_title);
//    this.persondetailinfodoctor = (TextView) findViewById(R.id.);
        this.age = (TextView) findViewById(R.id.doctor_age);
        this.gender = (TextView) findViewById(R.id.doctor_gender);
        this.doctorName = (TextView) findViewById(R.id.doctor_name);
        this.ImageView = (CircleImageView) findViewById(R.id.head);

        callImage = (ImageView) findViewById(R.id.doctor_call_image);
        doctorCallText = (TextView) findViewById(R.id.doctor_call_text);
//    make_call = (Button) findViewById(R.id.doctor_part_button_doctor);
        net_work_available = Utils.isNetWorkAvailabe(DoctorActivity.this);
    }

    private void initEvent() {
        returnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        doctorCallText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callable) {
                    NemoOpenAPI.getInstance().makeCall(doctorXiaoyu.getXiaoyuNum(), null, null);
                }
            }
        });
    }

    @Override
    public void onNemoCallback(Message message) {

    }

    class GetDoctorInfor extends AsyncTask<Void, Void, Void> {

        private Gson gson = new Gson();

        @Override
        protected Void doInBackground(Void... params) {

            if (net_work_available) {
                doctorInfor = gson
                        .fromJson(Utils.sendRequest(GlobalData.GET_DOCTOR_INFOR + doctorId), DoctorInfor.class);
                String s = Utils.sendRequest(GlobalData.GET_DOCTOR_XIAOYU + doctorId);
                if (s == null || s.contains("device_not_exist") || s.contains("param_error")) {
                } else {
                    doctorXiaoyu = gson.fromJson(s, DoctorXiaoyu.class);
                    Log.d(TAG, "doInBackground: doctorXiaoyu" + doctorXiaoyu.getXiaoyuNum());
                    DataSupport.deleteAll(DoctorXiaoyu.class, "uid = ?", doctorId);
                    Log.d(TAG, "doInBackground: save?");
                    doctorXiaoyu.save();
                    name = doctorInfor.getName();
                    Utils.putValue(DoctorActivity.this, GlobalData.DoctorName, name);
                }
                if (doctorInfor != null) {
                    has_data = true;
                }
            } else {
                if (DataSupport.isExist(DoctorInfor.class)) {
                    List<DoctorInfor> doctorInfors = DataSupport.where("uid = ?", doctorId).find(DoctorInfor.class);
//                    name = Utils.getValue(DoctorActivity.this, GlobalData.DoctorName);
//                    for (DoctorInfor doctorInfora : doctorInfors) {
//                        if (doctorInfora.getName().equals(name)) {
//                            doctorInfor = doctorInfora;
//                            break;
//                        }
//                    }
                    if (doctorInfors != null && doctorInfors.size() > 0) {
                        doctorInfor = doctorInfors.get(0);
                    }
                    if (doctorInfor != null) {
                        has_data = true;
                    } else {
                        has_data = false;
                    }
                } else {
                    has_data = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (has_data) {
                doctorName.setText(doctorInfor.getName());
//        persondetailinfodoctor.setText(doctorInfor.getSex() + "   " + doctorInfor.getAge());
                age.setText(doctorInfor.getAge());
                gender.setText(doctorInfor.getSex());
                jobTitle.setText(doctorInfor.getJob_title());
                hospital.setText(doctorInfor.getHospital());
                email.setText(doctorInfor.getMail());
                talent.setText(doctorInfor.getGood_at());
                introduction
                        .setText("职称: " + doctorInfor.getJob_title() + "\n\n其他: " + doctorInfor.getCases());
                Log.e(TAG, GlobalData.GET_DOCTOR_IMAGE + doctorInfor.getImage());
                bitmapUtils.disPlay(ImageView,
                        GlobalData.GET_DOCTOR_IMAGE + doctorInfor.getImage());
            }
            if (doctorXiaoyu != null) {
                if (isPatient && doctorXiaoyu.getXiaoyuNum() != "device_not_exist" && doctorXiaoyu.getXiaoyuNum() != "param_error") {
                    callable = true;
                    callImage.setVisibility(View.VISIBLE);
                    doctorCallText.setVisibility(View.VISIBLE);
                }else if(doctorXiaoyu.getXiaoyuNum() .contains("device_not_exist")|| doctorXiaoyu.getXiaoyuNum() .contains( "param_error")){
                    Utils.showShortToast(DoctorActivity.this,"未设置账号");
                }
            }
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }
    }

}
