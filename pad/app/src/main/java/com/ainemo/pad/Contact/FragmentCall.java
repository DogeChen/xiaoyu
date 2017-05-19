package com.ainemo.pad.Contact;

import ainemo.api.openapi.MakeCallResult;
import ainemo.api.openapi.Msg;
import ainemo.api.openapi.NemoCallback;
import ainemo.api.openapi.NemoConst;
import ainemo.api.openapi.NemoOpenAPI;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ainemo.pad.Contact.Record.CallRecord;
import com.ainemo.pad.Contact.Record.CallRecordAdapter;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/4/24.
 */

public class FragmentCall extends Fragment implements NemoCallback {

  private Activity activity;
  private View layout;
  private Button[] circleTextImageViews = new Button[13];
  private TextView telephoneNum;
  private CircleImageView backspace_number;
  private RecyclerView recyclerView;
  private CallRecordAdapter adapter;
  private List<CallRecord> list = new ArrayList<>();
  private LinearLayout call;

  private RecyclerView.LayoutManager layoutManager;
  private static final String TAG = "FragmentCall";


  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == Msg.OPENAPI_MAKE_CALL_RESULT) {
        MakeCallResult result=msg.getData().getParcelable(NemoConst.KEY_MAKE_CALL_RESULT);
        Log.w(TAG, "handleMessage: result is"+result );
        Toast.makeText(getContext(),result.toString(),Toast.LENGTH_LONG).show();
      } else if (msg.what >= 0 && msg.what <= 9) {
        telephoneNum.append("" + msg.what);
        circleTextImageViews[msg.what].setBackgroundColor(getResources().getColor(R.color.white));
        backspace_number.setVisibility(View.VISIBLE);
      } else if (msg.what == 10) {
        telephoneNum.append("*");
        circleTextImageViews[msg.what].setBackgroundColor(getResources().getColor(R.color.white));
        backspace_number.setVisibility(View.VISIBLE);
      } else if (msg.what == 11) {
        telephoneNum.append("#");
        circleTextImageViews[msg.what].setBackgroundColor(getResources().getColor(R.color.white));
        backspace_number.setVisibility(View.VISIBLE);
      }  else if (msg.what >=12 && msg.what < 24) {
        circleTextImageViews[msg.what - 12]
            .setBackgroundColor(getResources().getColor(R.color.transparent));
      }else if(msg.what==24){
        call.setBackgroundColor(getResources().getColor(R.color.transparent));
      }
      else if (msg.what == 0x123) {
        adapter = new CallRecordAdapter(activity, list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
      } else if (msg.what == 0x124) {
        Utils.showShortToast(activity, "没有数据");
      }
    }
  };

  @Override
  public void onResume() {
    super.onResume();
    new FragmentCall.FindRecordsTask().execute(1);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    new FragmentCall.FindRecordsTask().execute(1);

    NemoOpenAPI.getInstance().registerCallback(this);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    if (layout == null) {
      activity = this.getActivity();
      layout = activity.getLayoutInflater().inflate(R.layout.fragment_call, null);
    } else {
      ViewGroup parent = (ViewGroup) layout.getParent();
      if (parent != null) {
        parent.removeView(layout);
      }
    }
    initView();
    return layout;
  }

  private void initView() {

    recyclerView = (RecyclerView) layout.findViewById(R.id.record_list);
    layoutManager = new LinearLayoutManager(activity);
    recyclerView.setLayoutManager(layoutManager);
    adapter=new CallRecordAdapter(getContext(),list);
    recyclerView.setAdapter(adapter);

    circleTextImageViews[0] = (Button) layout.findViewById(R.id.number_call_0);
    circleTextImageViews[1] = (Button) layout.findViewById(R.id.number_call_1);
    circleTextImageViews[2] = (Button) layout.findViewById(R.id.number_call_2);
    circleTextImageViews[3] = (Button) layout.findViewById(R.id.number_call_3);
    circleTextImageViews[4] = (Button) layout.findViewById(R.id.number_call_4);
    circleTextImageViews[5] = (Button) layout.findViewById(R.id.number_call_5);
    circleTextImageViews[6] = (Button) layout.findViewById(R.id.number_call_6);
    circleTextImageViews[7] = (Button) layout.findViewById(R.id.number_call_7);
    circleTextImageViews[8] = (Button) layout.findViewById(R.id.number_call_8);
    circleTextImageViews[9] = (Button) layout.findViewById(R.id.number_call_9);
    circleTextImageViews[10] = (Button) layout.findViewById(R.id.number_call_10);
    circleTextImageViews[11] = (Button) layout.findViewById(R.id.number_call_11);
    call = (LinearLayout) layout.findViewById(R.id.call);
    telephoneNum = (TextView) layout.findViewById(R.id.call_number);
    backspace_number = (CircleImageView) layout.findViewById(R.id.backspace_number_image);
    initEvent();

  }

  private void initEvent() {
    backspace_number.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String text = telephoneNum.getText().toString();
        if (!text.equals("")) {
          text = text.substring(0, text.length() - 1);
        } else {
          backspace_number.setVisibility(View.GONE);
        }
        telephoneNum.setText(text);
        if (text.equals("")) {
          backspace_number.setVisibility(View.GONE);
        }
      }
    });
    for (int i = 0; i < 12; i++) {
      final int j = i;

      circleTextImageViews[i].setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          if (j >= 0 && j <= 11) {

            handler.sendEmptyMessage(j);
            new Timer().schedule(new TimerTask() {
              @Override
              public void run() {
                handler.sendEmptyMessage(j + 12);
              }
            }, 150);
          }
        }
      });
    }
    call.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        handler.sendEmptyMessage(12);
//        new Timer().schedule(new TimerTask() {
//          @Override
//          public void run() {
//            handler.sendEmptyMessage(24);
            //权限检查
            checkPermission();
            //呼叫用户，以及没有密码的会议
//            NemoOpenAPI.getInstance().makeCall(telephoneNum.getText().toString(), "123456");

        NemoOpenAPI.getInstance().playVoice("开始呼叫");
        NemoOpenAPI.getInstance().makeCall("915927698835", "123456");
//          }
//        }, 150);
      }
    });
  }

  private void checkPermission() {
    if (!(ContextCompat
        .checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED) &&
        !(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED)) {
      ActivityCompat
          .requestPermissions(getActivity(),
              new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
    } else if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
        == PackageManager.PERMISSION_GRANTED)) {
      ActivityCompat
          .requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 0);
    } else if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED)) {
      ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
    }
  }

  @Override
  public void onNemoCallback(Message message) {

    Log.d(TAG, "onNemoCallback: Callback");
    handler.sendMessage(Message.obtain(message));
  }

  public class FindRecordsTask extends AsyncTask<Integer, Integer, Integer> {

    @Override
    protected Integer doInBackground(Integer... params) {
      if (DataSupport.isExist(CallRecord.class)) {
        list = DataSupport.findAll(CallRecord.class);
      } else {
        return 0;
      }
      if (list != null) {
        Collections.sort(list, new Comparator<CallRecord>() {
          @Override
          public int compare(CallRecord callRecord, CallRecord t1) {
            Date date1 = callRecord.getDate();
            Date date2 = t1.getDate();
            if (date1.getTime() < date2.getTime()) {
              return 1;
            } else if (date1.getTime() > date2.getTime()) {
              return -1;
            } else {
              return 0;
            }
          }

        });

      }
      return 1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
      super.onPostExecute(integer);
      if (integer == 1) {
        handler.sendEmptyMessage(0x123);

      } else if (integer == 0) {
        handler.sendEmptyMessage(0x124);
      }
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
}
