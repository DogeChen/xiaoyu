package com.ainemo.pad.Contact;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.ainemo.pad.Datas.ContactListData;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import org.litepal.crud.DataSupport;

/**
 * Created by 小武哥 on 2017/5/18.
 */

public class ChangeContactActivity extends AppCompatActivity implements OnClickListener,OnTouchListener
    {

  private CircleImageView headImage;
  private Button save;
  private Button cancle;
  private EditText name;
  private EditText address;
  private EditText xiaoyuNum;
  private EditText phoneNum;
  private EditText remark;
      private View layout;
  private int id;

      private static final String TAG = "ChangeContactActivity";
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact_detail);
    id = getIntent().getIntExtra("id", -1);
    Log.d(TAG, "onCreate: id "+id);
    initView();
    initData();
  }

  private void initView() {
    headImage = (CircleImageView) findViewById(R.id.doctor_head);
    save = (Button) findViewById(R.id.finish);
    save.setOnClickListener(this);
    cancle = (Button) findViewById(R.id.cancel);
    cancle.setOnClickListener(this);
    name = (EditText) findViewById(R.id.doctor_name);
    address = (EditText) findViewById(R.id.doctor_address);
    xiaoyuNum = (EditText) findViewById(R.id.doctor_xiaoyuid);
    phoneNum = (EditText) findViewById(R.id.doctor_telephone);
    remark = (EditText) findViewById(R.id.doctor_note);
    layout=(View)findViewById(R.id.contact_detail);
    layout.setOnTouchListener(this);
  }
  private void initData() {
    if (id>0) {
      ContactListData data = DataSupport.find(ContactListData.class, id);
      name.setText(data.getName());
      name.setSelection(name.getText().length());
      Log.d(TAG, "initData: name.length= "+name.length()+" name.getText().length="+name.getText().length());
      address.setText(data.getAddress());
      address.setSelection(address.length());
      remark.setText(data.getRemark());
      remark.setSelection(remark.length());
      if (data.getImage_url() != null && !data.getImage_url().equals("")) {
        headImage.setImageURI(Uri.parse(data.getImage_url()));
      }else{
        headImage.setImageResource(R.drawable.contact_image);
      }
//      String num=data.getNumber();
//      String NemoNum=null;
//      String telephone=null;
//      if(num.contains(" ")){
//         NemoNum=num.substring(0,num.indexOf(' '));
//         telephone=num.substring(num.lastIndexOf(' '));
//      }else if(num.length()>6){
//        telephone=num;
//        NemoNum="";
//      }else if(num.length()<=6){
//        NemoNum=num;
//        telephone=null;
//      }
      xiaoyuNum.setText(data.getXiaoyuNumber());
      xiaoyuNum.setSelection(xiaoyuNum.length());
      phoneNum.setText(data.getPhoneNumber());
      phoneNum.setSelection(phoneNum.length());
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.finish:
        if(id>0) {
          ContentValues values=new ContentValues();
          values.put("address",address.getText().toString());
          values.put("remark",remark.getText().toString());
          values.put("name",name.getText().toString());
          values.put("xiaoyuNumber",xiaoyuNum.getText().toString());
          values.put("phoneNumber", phoneNum.getText().toString());
          DataSupport.update(ContactListData.class,values,id);
        }else{
          ContactListData contactSave=new ContactListData();
          contactSave.setAddress(address.getText().toString());
          contactSave.setName(name.getText().toString());
          contactSave.setRemark(remark.getText().toString());
          contactSave.setXiaoyuNumber(xiaoyuNum.getText().toString());
          contactSave.setPhoneNumber(phoneNum.getText().toString());
          if(contactSave.save()==true){
            Utils.showShortToast(getBaseContext(),"保存成功");
            Log.d(TAG, "onClick: save ,succeed,id= "+contactSave.getId());
          }else{
            Utils.showShortToast(getBaseContext(),"保存失败");
            Log.d(TAG, "onClick: save ,failed");
          }
        }
        break;
      case R.id.cancel:
        break;
    }
    finish();
  }

      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId()==R.id.contact_detail){
          view.setFocusable(true);
          view.setFocusableInTouchMode(true);
          view.requestFocus();

        InputMethodManager imm = (InputMethodManager) this.getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
        }
        return super.onTouchEvent(motionEvent);
      }
    }


