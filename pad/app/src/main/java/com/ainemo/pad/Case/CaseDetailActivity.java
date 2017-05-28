package com.ainemo.pad.Case;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.ainemo.pad.Datas.CaseInfor;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.MyBitmapUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import org.litepal.crud.DataSupport;

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

  private MyBitmapUtils bitmapUtils=new MyBitmapUtils();
  private CircleImageView imageView;
  private CaseInfor caseInfor;
  private int id;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_case_detail);
    Intent intent = getIntent();

    initView();
    id = intent.getIntExtra("caseInforId",1);
    caseInfor= DataSupport.find(CaseInfor.class,id);

    initEvent();
  }

  public void initView(){
    name=(TextView)findViewById(R.id.patient_name);
    gender=(TextView)findViewById(R.id.patient_gender);
    age=(TextView)findViewById(R.id.patient_age);
    doctorName=(TextView)findViewById(R.id.doctor_name);
    illnessDescription=(TextView)findViewById(R.id.illness_description);
    diagnosisResult=(TextView)findViewById(R.id.diagnosis_result);
    detailsOfIllness=(TextView)findViewById(R.id.detail_of_illness);
    imageView=(CircleImageView)findViewById(R.id.head);
    back=(Button)findViewById(R.id.return_btn);
  }

  public void initEvent(){
    name.setText(caseInfor.getName());
    gender.setText(caseInfor.getSex());
    age.setText(caseInfor.getAge());
    doctorName.setText(caseInfor.getDoctorName());
    doctorName.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent=new Intent(CaseDetailActivity.this, DoctorActivity.class);
        intent.putExtra("doctorId",caseInfor.getDoctorId());
        startActivity(intent);
      }
    });
    illnessDescription.setText(caseInfor.getIllproblem());
    diagnosisResult.setText(caseInfor.getIllresult());
    detailsOfIllness.setText(caseInfor.getIllproblem());
    try {
      bitmapUtils.disPlay(imageView, GlobalData.GET_PATIENT_IMAGE + caseInfor.getImage());
    }catch(Exception e){
      e.printStackTrace();
    }
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }
}
