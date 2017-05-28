package com.ainemo.pad.Case;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.ainemo.pad.Datas.DoctorInfor;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.MyBitmapUtils;
import com.ainemo.pad.SomeUtils.Utils;
import com.google.gson.Gson;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;
import org.litepal.crud.DataSupport;

/**
 * Created by 小武哥 on 2017/5/4.
 */

public class DoctorActivity extends AppCompatActivity {

  private String doctorId;
  private Button returnBtn;
  private de.hdodenhof.circleimageview.CircleImageView persondetailimagedoctor;
  private TextView persondetailnamedoctor;
  //  private TextView persondetailinfodoctor;
  private TextView gender;
  private TextView age;
  private TextView persondetailjobdoctor;
  private TextView doctorpartnamedoctor;
  private TextView doctorparttimedoctor;
  private TextView doctorpartgoodat;
  private TextView doctorinfointrodetail;
  private ProgressDialog dialog;
  private DoctorInfor doctorInfor;
  private boolean net_work_available, has_data;
  private String name;
  private static final String TAG = "DoctorActivity";
  private MyBitmapUtils bitmapUtils = new MyBitmapUtils();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_doctor);
    doctorId = getIntent().getStringExtra("doctorId");
    initView();
    initEvent();
    try {
      new GetDoctorInfor().execute();
    }catch (Exception e){
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
    this.doctorinfointrodetail = (TextView) findViewById(R.id.introduction);
    this.doctorpartgoodat = (TextView) findViewById(R.id.talent);
    this.doctorparttimedoctor = (TextView) findViewById(R.id.email);
    this.doctorpartnamedoctor = (TextView) findViewById(R.id.hospital);
    this.persondetailjobdoctor = (TextView) findViewById(R.id.job_title);
//    this.persondetailinfodoctor = (TextView) findViewById(R.id.);
    this.age = (TextView) findViewById(R.id.doctor_age);
    this.gender = (TextView) findViewById(R.id.doctor_gender);
    this.persondetailnamedoctor = (TextView) findViewById(R.id.doctor_name);
    this.persondetailimagedoctor = (CircleImageView) findViewById(R.id.head);

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
  }

  class GetDoctorInfor extends AsyncTask<Void, Void, Void> {

    private Gson gson = new Gson();

    @Override
    protected Void doInBackground(Void... params) {

      if (net_work_available) {
        doctorInfor = gson
            .fromJson(Utils.sendRequest(GlobalData.GET_DOCTOR_INFOR + doctorId), DoctorInfor.class);
        DataSupport.deleteAll(DoctorInfor.class);
        if (!doctorInfor.isSaved()) {
          doctorInfor.save();
        }
        name = doctorInfor.getName();
        Utils.putValue(DoctorActivity.this, GlobalData.DoctorName, name);
        has_data = true;
      } else {
        if (DataSupport.isExist(DoctorInfor.class)) {
          List<DoctorInfor> doctorInfors = DataSupport.findAll(DoctorInfor.class);
          name = Utils.getValue(DoctorActivity.this, GlobalData.DoctorName);
          for (DoctorInfor doctorInfora : doctorInfors) {
            if (doctorInfora.getName().equals(name)) {
              doctorInfor = doctorInfora;
              break;
            }
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
        persondetailnamedoctor.setText(doctorInfor.getName());
//        persondetailinfodoctor.setText(doctorInfor.getSex() + "   " + doctorInfor.getAge());
        age.setText(doctorInfor.getAge());
        gender.setText(doctorInfor.getSex());
        persondetailjobdoctor.setText(doctorInfor.getJob_title());
        doctorpartnamedoctor.setText(doctorInfor.getHospital());
        doctorparttimedoctor.setText(doctorInfor.getMail());
        doctorpartgoodat.setText(doctorInfor.getGood_at());
        doctorinfointrodetail
            .setText("职称: " + doctorInfor.getJob_title() + "\n其他: " + doctorInfor.getCases());
        Log.e(TAG, "http://139.196.40.97/upload/doctorimage/" + doctorInfor.getImage());
        bitmapUtils.disPlay(persondetailimagedoctor,
            "http://139.196.40.97/upload/doctorimage/" + doctorInfor.getImage());
      }

      dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      dialog.show();
    }
  }

}
