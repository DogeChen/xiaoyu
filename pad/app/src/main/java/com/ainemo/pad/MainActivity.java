package com.ainemo.pad;

import ainemo.api.openapi.MakeCallResult;
import ainemo.api.openapi.Msg;
import ainemo.api.openapi.NemoConst;
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
import android.widget.Toast;
import com.ainemo.pad.Case.CaseListActivity;
import com.ainemo.pad.Contact.ContactActivity;
import com.ainemo.pad.Datas.UserInfor;
import com.ainemo.pad.Jujia.JujiaActivity;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.Utils;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements OnClickListener{

  private TextView name;
  private TextView number;
  private Button returnBtn;
  private ImageView call;
  private ImageView patient;
  private ImageView para;
  private long exitTime = 0;
  private String NemoSn;
  private String patientId;

  private UserInfor userInfor;
  private static final String TAG = "MainActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();

    NemoSn = getIntent().getStringExtra("nemoNumber");
    Log.i(TAG, "onCreate: NemoSn="+NemoSn+" getNemoSn()="+ NemoOpenAPI.getInstance().getNemoSn());
    //获取小鱼序列号的代码

    NemoSn="217098";

    patientId=Utils.getValue(this,GlobalData.PATIENT_ID);

    patientId="2";//待注释

    Utils.putValue(MainActivity.this,GlobalData.PATIENT_ID,patientId);
//    if(patientId==null){
//      new GetPatientIdTask().execute();
//    }

    initView();
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
  public void onClick(View view){
    switch(view.getId()){
      case R.id.home_call_btn:
        startActivity(new Intent(MainActivity.this,ContactActivity.class));
        break;
      case R.id.home_par_btn:
//        startActivity(new Intent(MainActivity.this,CaseListActivity.class).putExtra("id",patientId));
        Intent intent=new Intent(MainActivity.this,CaseListActivity.class);
        intent.putExtra("id",patientId);
        startActivity(intent);
        break;
      case R.id.home_record_btn:
        startActivity(new Intent(MainActivity.this,JujiaActivity.class));
        break;
      case R.id.return_btn:
        onBackPressed();
        break;

    }
  }


  private void initEvent(){
//    name.setText("刘云, 欢迎回来");
//    number.setText("小鱼号："+NemoSn);
    try{
      name.setText(Utils.getValue(this,GlobalData.user_name)+" 欢迎回来");
      number.setText("小鱼号："+Utils.getValue(this,GlobalData.xiaoyu));
    }catch (NullPointerException e){
      e.printStackTrace();
    }
  }
  @Override
  public void onBackPressed(){

    if((System.currentTimeMillis()-exitTime) > 2000){
      Utils.showShortToast(this,"再次点击退出程序");
      exitTime = System.currentTimeMillis();
    } else {
      finish();
    }
  }

  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      Log.d(TAG, "handleMessage: onNemoCallback msg"+msg);
      switch (msg.what) {
        case Msg.OPENAPI_MAKE_CALL_RESULT:
          MakeCallResult result = msg.getData().getParcelable(NemoConst.KEY_MAKE_CALL_RESULT);
          Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
          break;
        default:
          break;
      }
    }
  };

  class GetPatientIdTask extends AsyncTask<Void, Void, Void> {

    private Gson gson = new Gson();

    @Override
    protected Void doInBackground(Void... params) {
      String infor = Utils.sendRequest(
          GlobalData.GET_PATIENT_ID +NemoSn);
      try{
        if(infor.equals("not_exist")){
          Utils.showShortToast(getApplicationContext(),"病人ID获取失败，请在手机端下载app注册,检查该小鱼号绑定了用户");
        }else if(infor.equals("param_error")){
          Utils.showShortToast(getApplicationContext(),"参数错误");
        }else{
          Utils.putValue(MainActivity.this,GlobalData.PATIENT_ID,infor);
        }
      }catch (Exception e){
        Utils.showShortToast(getApplicationContext(),"访问数据错误");
        e.printStackTrace();
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
