package com.ainemo.pad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.ainemo.pad.vPage.CaseInfor;

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

  private CaseInfor caseInfor;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_case_detail);
    Intent intent = getIntent();
    initView();
    caseInfor = (CaseInfor) intent.getSerializableExtra("caseInfor");
    name.setText(caseInfor.getName());
    gender.setText("");
    age.setText("");
    doctorName.setText(caseInfor.getDoctorId());
    illnessDescription.setText(caseInfor.getIllproblem());
    diagnosisResult.setText(caseInfor.getIllresult());
    detailsOfIllness.setText(caseInfor.getIllproblem());
    back=(Button)findViewById(R.id.return_btn);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  public void initView(){
    name=(TextView)findViewById(R.id.patient_name);
    gender=(TextView)findViewById(R.id.patient_gender);
    age=(TextView)findViewById(R.id.patient_age);
    doctorName=(TextView)findViewById(R.id.doctor_name);
    illnessDescription=(TextView)findViewById(R.id.illness_description);
    diagnosisResult=(TextView)findViewById(R.id.diagnosis_result);
    detailsOfIllness=(TextView)findViewById(R.id.detail_of_illness);

  }
}
