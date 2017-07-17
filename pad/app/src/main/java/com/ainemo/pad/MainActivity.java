package com.ainemo.pad;

import ainemo.api.openapi.NemoOpenAPI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ainemo.pad.Case.CaseListActivity;
import com.ainemo.pad.Case.DoctorListActivity;
import com.ainemo.pad.Contact.ContactActivity;
import com.ainemo.pad.Datas.PatientId;
import com.ainemo.pad.Datas.UserInfor;
import com.ainemo.pad.Jujia.JujiaActivity;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.Utils;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "MainActivity";
    private MainActivity activity;
    private TextView name;
    private TextView number;
    private Button returnBtn;
    private ImageView call;
    private ImageView patient;
    private ImageView para;
    private long exitTime = 0;
    private String NemoSn;
    private String patientId;
    private boolean isPatient=true;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (patientId == null || patientId.equals("")) {
                new GetPatientIdTask().execute();
                handler.sendEmptyMessageDelayed(0, 10000);
            }
        }
    };
    private UserInfor userInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        initView();
        NemoSn = getIntent().getStringExtra("nemoNumber");

        Log.i(TAG,
                "onCreate: NemoNum=" + NemoSn + " getNemoSn()=" + NemoOpenAPI.getInstance().getNemoSn());
        //获取小鱼序列号的代码
//        NemoSn="813580";
        if (NemoSn == null || NemoSn.equals("")) {
            NemoSn = Utils.getValue(this, GlobalData.NemoNum);
            if (NemoSn == null && NemoSn.equals("")) {
            }
        } else {
            Utils.putValue(this, GlobalData.NemoNum, NemoSn);
        }
//        patientId = "28";//待注释
        Utils.putValue(this, GlobalData.PATIENT_ID, patientId);
        patientId = Utils.getValue(this, GlobalData.PATIENT_ID);
        if (patientId == null || patientId.equals("")) {
            handler.sendEmptyMessage(0);
        }

        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initEvent();
    }

    private void initView() {
        name = (TextView) findViewById(R.id.home_name);
        number = (TextView) findViewById(R.id.home_number);
        returnBtn = (Button) findViewById(R.id.return_btn);
        call = (ImageView) findViewById(R.id.home_call_btn);
        patient = (ImageView) findViewById(R.id.home_par_btn);
        para = (ImageView) findViewById(R.id.home_record_btn);
        call.setOnClickListener(this);
        patient.setOnClickListener(this);
        para.setOnClickListener(this);
        returnBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_call_btn:
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
                break;
            case R.id.home_par_btn:
//        startActivity(new Intent(MainActivity.this,CaseListActivity.class).putExtra("id",patientId));
                if (patientId != null && !patientId.equals("")&&isPatient) {
                    startActivity(new Intent(MainActivity.this, JujiaActivity.class));
                } else if(isPatient) {
                    Utils.showShortToast(this, "未绑定ID");
                }else {
                    Utils.showShortToast(this, "医生账号未绑定家居设备");
                }
                break;
            case R.id.home_record_btn:
                if(patientId==null||patientId.equals("")) {
                    Utils.showShortToast(this, "该小鱼未绑定ID");
                }else
                if (isPatient) {
                    Intent intent = new Intent(MainActivity.this, CaseListActivity.class);
                    intent.putExtra("id", patientId);
                    intent.putExtra(GlobalData.IS_PATIENT, isPatient);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(MainActivity.this, DoctorListActivity.class);
                    intent.putExtra("id", patientId);
                    intent.putExtra(GlobalData.IS_PATIENT, isPatient);
                    startActivity(intent);
                }
                break;
            case R.id.return_btn:
                onBackPressed();
                break;
        }
    }

    private void initEvent() {
//    name.setText("刘云, 欢迎回来");
//    number.setText("小鱼号："+NemoNum);
        try {
            name.setText(Utils.getValue(this, GlobalData.user_name) + " 欢迎回来");
            number.setText("小鱼号：" + Utils.getValue(this, GlobalData.NemoNum));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Utils.showShortToast(this, "再次点击退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


    class GetPatientIdTask extends AsyncTask<Void, Void, Void> {

        private Gson gson = new Gson();

        @Override
        protected Void doInBackground(Void... params) {

            if (NemoSn != null && !NemoSn.equals("")) {
                String infor = Utils.sendRequest(
                        GlobalData.GET_PATIENT_ID + NemoSn);
                if (infor.contains("not_exist")) {
                    patientId = "";
                    Utils.showShortToast(activity, "请检查该小鱼号绑定了用户");
                } else if (infor.contains("param_error")) {
                    patientId = "";
                    Utils.showShortToast(activity, "参数错误");
                } else {
                    try {
                        PatientId patientId1 = gson.fromJson(infor, PatientId.class);
                        if (patientId1 != null) {
                            patientId = patientId1.getUid();
                            isPatient = patientId1.getIdentity().equals("patient");
                            Utils.putValue(MainActivity.this, GlobalData.PATIENT_ID, patientId);
                            Utils.putBooleanValue(MainActivity.this,GlobalData.IS_PATIENT,isPatient);
                        }else{
                            patientId="";
                            isPatient=false;
                            Utils.showShortToast(activity, "参数错误");
                        }
                    } catch (Exception e) {
                        Utils.showShortToast(MainActivity.this, "访问数据错误");
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
