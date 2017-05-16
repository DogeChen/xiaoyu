package com.ainemo.pad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.ainemo.pad.Contact.ContactActivity;
import com.ainemo.pad.SomeUtils.Utils;

public class MainActivity extends AppCompatActivity implements OnClickListener {

  private TextView name;
  private TextView number;
  private Button returnBtn;
  private ImageView call;
  private ImageView patient;
  private ImageView para;
  private long exitTime = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
  }

  private void initView() {
    name = (TextView) findViewById(R.id.home_name);
    number = (Button) findViewById(R.id.return_btn);
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
        startActivity(new Intent(MainActivity.this,CaseListActivity.class));
        break;
      case R.id.home_record_btn:
        startActivity(new Intent(MainActivity.this,JujiaActivity.class));
        break;
      case R.id.return_btn:
        onBackPressed();
        break;
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
}
