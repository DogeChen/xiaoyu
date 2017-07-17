package com.ainemo.pad.Case;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ainemo.pad.Datas.CaseInfor;
import com.ainemo.pad.Datas.DoctorXiaoyu;
import com.ainemo.pad.Datas.PatientXiaoyu;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.MyBitmapUtils;
import com.ainemo.pad.SomeUtils.Utils;
import com.google.gson.Gson;

import ainemo.api.openapi.NemoOpenAPI;
import de.hdodenhof.circleimageview.CircleImageView;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 小武哥 on 2017/5/4.
 */

public class CaseDetailActivity extends AppCompatActivity {

    private TextView name;
    private TextView gender;
    private TextView age;
    private TextView doctorName;
    private TextView illnessDescription;
    private TextView diagnosisResult;
    private TextView detailsOfIllness;
    private Button back;
    private TextView call;
    private ImageView callImage;
    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    private CircleImageView imageView;
    private CaseInfor caseInfor;
    private int id;
    private boolean isPatient;
    private PatientXiaoyu patientXiaoyu;
    private DoctorXiaoyu doctorXiaoyu;
    private GetXiaoyuTask getXiaoyuTask;
    private boolean net_work_available;
    private ProgressDialog progressDialog;

    private static final String TAG = "CaseDetailActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);
        Intent intent = getIntent();
        isPatient = intent.getBooleanExtra(GlobalData.IS_PATIENT,true);

        Log.d(TAG, "onCreate: ");

        initView();
        id = intent.getIntExtra("caseInforId", 1);
        caseInfor = DataSupport.find(CaseInfor.class, id);
        if (!isPatient) {
            getXiaoyuTask = new GetXiaoyuTask();
            getXiaoyuTask.execute();
        }

        initEvent();
    }

    @Override
    protected void onDestroy() {
        if (getXiaoyuTask != null)
            getXiaoyuTask.cancel(true);
        super.onDestroy();
    }

    public void initView() {
        name = (TextView) findViewById(R.id.patient_name);
        gender = (TextView) findViewById(R.id.patient_gender);
        age = (TextView) findViewById(R.id.patient_age);
        doctorName = (TextView) findViewById(R.id.doctor_name);
        illnessDescription = (TextView) findViewById(R.id.illness_description);
        diagnosisResult = (TextView) findViewById(R.id.diagnosis_result);
        detailsOfIllness = (TextView) findViewById(R.id.detail_of_illness);
        imageView = (CircleImageView) findViewById(R.id.head);
        back = (Button) findViewById(R.id.return_btn);
        net_work_available = Utils.isNetWorkAvailabe(CaseDetailActivity.this);
        progressDialog = new ProgressDialog(this);
        if (!isPatient) {
            call = (TextView) findViewById(R.id.call_text);
            call.setVisibility(View.VISIBLE);
            callImage = (ImageView) findViewById(R.id.call_image);
            callImage.setVisibility(View.VISIBLE);
        }
    }

    public void initEvent() {
        name.setText(caseInfor.getName());
        gender.setText(caseInfor.getSex());
        age.setText(caseInfor.getAge());
        doctorName.setText(caseInfor.getDoctorName());
        doctorName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaseDetailActivity.this, DoctorActivity.class);
                intent.putExtra("doctorId", caseInfor.getDoctorId());
                intent.putExtra(GlobalData.IS_PATIENT,isPatient);
                startActivity(intent);
            }
        });
        illnessDescription.setText(caseInfor.getIllproblem());
        diagnosisResult.setText(caseInfor.getIllresult());
        detailsOfIllness.setText(caseInfor.getIllproblem());
        try {
            bitmapUtils.disPlay(imageView, GlobalData.GET_PATIENT_IMAGE + caseInfor.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class GetXiaoyuTask extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();

        @Override
        protected Void doInBackground(Void... params) {
            if (net_work_available) {
                String s = null;
                s = Utils.sendRequest(GlobalData.GET_PATIENT_XIAOYU + caseInfor.getPatientId());
                Log.d(TAG, "doInBackground: s=" + s + "patientId+" + caseInfor.getPatientId());
                if (s == null || s.equals("") || s.contains("device_not_exist") || s.contains("param_error")) {

                } else {
                    patientXiaoyu = gson.fromJson(s, PatientXiaoyu.class);
                    DataSupport.deleteAll(PatientXiaoyu.class, "uid = ?", patientXiaoyu.getUid());
                    patientXiaoyu.save();
                }

            } else {
                List<PatientXiaoyu> list = DataSupport.where("uid = ?", caseInfor.getPatientId()).find(PatientXiaoyu.class);
                if (list != null && list.size() > 0) {
                    patientXiaoyu = list.get(0);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Log.d(TAG, "onPostExecute: ");
            call.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String num = patientXiaoyu.getXiaoyuNum();
                        if (num != null) {
                            NemoOpenAPI.getInstance().makeCall(num, null, null);
                        } else {
                            Utils.showShortToast(CaseDetailActivity.this, "号码不存在，请稍后再试");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }
    }
}
