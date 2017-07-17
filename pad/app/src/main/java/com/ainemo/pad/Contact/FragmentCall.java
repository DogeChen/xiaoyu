package com.ainemo.pad.Contact;

import ainemo.api.openapi.MakeCallResult;
import ainemo.api.openapi.Msg;
import ainemo.api.openapi.NemoConst;
import ainemo.api.openapi.NemoOpenAPI;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ainemo.pad.Contact.Record.CallRecord;
import com.ainemo.pad.Contact.Record.CallRecordAdapter;
import com.ainemo.pad.Contact.Record.CallRecordAdapter.RecordClickLister;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.RecordUtil;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/4/24.
 */

public class FragmentCall extends Fragment implements RecordClickLister, OnClickListener {

    private Activity activity;
    private View layout;
    private Button[] circleTextImageViews = new Button[13];
    private TextView telephoneNum;
    private String call_number="";
    private TextView myNemoNum;
    private CircleImageView backspace_number;
    private RecyclerView recyclerView;
    private CallRecordAdapter adapter;
    private List<CallRecord> list = new ArrayList<>();
    private List<CallRecord> selectList = new ArrayList<>();
    private LinearLayout call;
    private RecordClickLister recordClickLister;
    private WrapContentLinearLayoutManager layoutManager;
    private static final String TAG = "FragmentCall";
    private PopupWindow mPopupWindow;
    private int touchedId;
    private FindRecordsTask findRecordsTask;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Log.d(TAG, "handleMessage: get"+msg.what);
            switch (msg.what) {
                case Msg.OPENAPI_MAKE_CALL_RESULT:

                    MakeCallResult result = msg.getData().getParcelable(NemoConst.KEY_MAKE_CALL_RESULT);
                    Log.w(TAG, "handleMessage: result is" + result);
//          Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG).show();

                    Long date = result.getStartTime();

                    Integer date1 = new Long(result.getDuration()).intValue();

                    int callStatus;
                    if (result.isCallOutSuccess() == true) {
                        callStatus = CallRecord.CALL_OUT;
                    } else {
                        callStatus = CallRecord.CALL_REJECT;
                    }
                    CallRecord callRecord = new CallRecord(result.getCallerName(),
                            result.getCallerNemoNumber(), null, callStatus, date, date1, null);
                    callRecord.save();


                    //RecordUtil.writeCallLog(getActivity().getContentResolver(), callRecord);
                    if (result.getCalleeNemoNumber() != null && !result.getCalleeNemoNumber().equals("")) {
                        Utils.putValue(activity, GlobalData.NemoNum, result.getCalleeNemoNumber());
                        Utils.putValue(activity, GlobalData.user_name, result.getCalleeName());
                    }
                    myNemoNum.setText(result.getCalleeNemoNumber());
                    if (findRecordsTask != null) {
                        findRecordsTask.cancel(true);
                        findRecordsTask = new FindRecordsTask();
                        findRecordsTask.execute(0);
                    } else {
                        findRecordsTask = new FindRecordsTask();
                        findRecordsTask.execute(0);
                    }

                    break;
                case 0x123:
                    adapter = new CallRecordAdapter(activity, selectList, recordClickLister);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    break;
                default:
                    if (msg.what >= 0 && msg.what <= 11) {
                        if (msg.what >= 0 && msg.what <= 9) {
                            telephoneNum.append("" + msg.what);
                            circleTextImageViews[msg.what]
                                    .setBackgroundColor(getResources().getColor(R.color.white));
                            backspace_number.setVisibility(View.VISIBLE);
                        } else if (msg.what == 10) {
                            telephoneNum.append("*");
                            circleTextImageViews[msg.what]
                                    .setBackgroundColor(getResources().getColor(R.color.white));
                            backspace_number.setVisibility(View.VISIBLE);
                        } else if (msg.what == 11) {
                            telephoneNum.append("#");
                            circleTextImageViews[msg.what]
                                    .setBackgroundColor(getResources().getColor(R.color.white));
                            backspace_number.setVisibility(View.VISIBLE);
                        }
                    } else if (msg.what >= 12 && msg.what < 24) {
                        circleTextImageViews[msg.what - 12]
                                .setBackgroundColor(getResources().getColor(R.color.transparent));
                    } else if (msg.what == 24) {
                        call.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (!findRecordsTask.isCancelled()) {
            findRecordsTask.cancel(true);
        }
        findRecordsTask = new FindRecordsTask();
        findRecordsTask.execute(0);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        recordClickLister = this;
        findRecordsTask =
                new FragmentCall.FindRecordsTask();
        findRecordsTask.execute(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (findRecordsTask != null) {
            findRecordsTask.cancel(true);
        }
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
        layoutManager = new WrapContentLinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CallRecordAdapter(getContext(), selectList, this);
        recyclerView.setAdapter(adapter);
        myNemoNum = (TextView) layout.findViewById(R.id.nemo_num);
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
        String num = Utils.getValue(activity, GlobalData.NemoNum);
        if (num != null && !num.equals("")) {
            myNemoNum.setText(num);
        }
        backspace_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_number = telephoneNum.getText().toString();
                if (!call_number.equals("")) {
                    call_number = call_number.substring(0, call_number.length() - 1);
                } else {
                    backspace_number.setVisibility(View.GONE);
                }
                telephoneNum.setText(call_number);
                if (call_number.equals("")) {
                    backspace_number.setVisibility(View.GONE);
                }
            }
        });
        telephoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                call_number = telephoneNum.getText().toString();
                if(findRecordsTask!=null){
                    findRecordsTask.cancel(true);
                }
                findRecordsTask=new FindRecordsTask();
                findRecordsTask.execute(1);
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
//        Date date=new Date(System.currentTimeMillis());

//        CallRecord callRecord=new CallRecord("张三","15912345678","123456",CallRecord.CALL_IN, date,new Date(System.currentTimeMillis()),"");
//        callRecord.save();
//
//        adapter = new CallRecordAdapter(activity, list);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

//        NemoOpenAPI.getInstance().playVoice("开始呼叫");
                NemoOpenAPI.getInstance().makeCall(telephoneNum.getText().toString(), null, null);
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
    public void onItemClick(View view) {
        touchedId = (int) view.getTag();
        Log.d(TAG, "onItemClick: touchedId =" + touchedId);
        //创建弹出菜单
//        PopupMenu popupMenu=new PopupMenu(getContext(),view);
//        MenuInflater inflater=popupMenu.getMenuInflater();
//        inflater.inflate(R.menu.contact,popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.setGravity(center);
//        popupMenu.show();
        try {
            View mPopupWindowView = activity.getLayoutInflater()
                    .inflate(R.layout.menu_record, null);
            mPopupWindow = new PopupWindow(mPopupWindowView, 288, 143, true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.showAsDropDown(view, view.getWidth() / 2, -view.getHeight() / 2);
            TextView textViewCall = (TextView) mPopupWindowView.findViewById(R.id.menu_call);
            TextView textViewDelete = (TextView) mPopupWindowView.findViewById(R.id.menu_delete);
//      TextView textViewChange = (TextView) mPopupWindowView.findViewById(R.id.menu_change);
            textViewCall.setOnClickListener(this);
            textViewDelete.setOnClickListener(this);
//      textViewChange.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_call:
                mPopupWindow.dismiss();
                CallRecord callRecord = DataSupport.find(CallRecord.class, touchedId);
                if (callRecord.getXiaoyuId() != null) {
                    NemoOpenAPI.getInstance().makeCall(callRecord.getXiaoyuId().toString(), null, null);
                    Log.d(TAG, "onClick: call xiaoyu " + callRecord.getXiaoyuId().toString().toString());
                } else if (callRecord.getTelephoneNum() != null) {
                    NemoOpenAPI.getInstance().makeCall(callRecord.getTelephoneNum().toString(), null, null);
                    Log.d(TAG, "onClick: call phone " + callRecord.getTelephoneNum().toString());
                }
                Log.d(TAG, "onMenuItemClick: call");
                break;
            case R.id.menu_delete:
                mPopupWindow.dismiss();
                int deleteCount = DataSupport.delete(CallRecord.class, touchedId);
                Log.d(TAG, "deleteCount = " + deleteCount);
                if (deleteCount == 0) {
                    Utils.showShortToast(activity, "删除失败");
                }
                new FragmentCall.FindRecordsTask().execute(0);
                Log.d(TAG, "onMenuItemClick: delete");
                break;

            default:
                break;
        }
    }

    public class FindRecordsTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {


            if (params[0] == 0) {
//                ContentResolver contentResolver = getActivity().getContentResolver();
//                list = RecordUtil.getCallLog(contentResolver);
                if (DataSupport.isExist(CallRecord.class)) {
                    list.clear();
                    list.addAll(DataSupport.findAll(CallRecord.class));
                } else {
                    list.clear();
                    return 0;
                }
                if (list != null) {
                    Collections.sort(list, new Comparator<CallRecord>() {
                        @Override
                        public int compare(CallRecord callRecord, CallRecord t1) {
                            Long date1 = callRecord.getDate();
                            Long date2 = t1.getDate();
                            if (date1 .compareTo(date2)<0 ) {
                                return 1;
                            } else if (date1 .compareTo(date2)>0) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });
                }

            }


            //添加筛选
            if (!call_number.toString().equals("")) {
                selectList.clear();
                for (CallRecord callRecord : list) {
                    if (callRecord.getXiaoyuId()!=null&&callRecord.getXiaoyuId().contains(call_number)) {
                        selectList.add(callRecord);
                    }else if(callRecord.getTelephoneNum()!=null&&callRecord.getTelephoneNum().contains(call_number)){
                        selectList.add(callRecord);
                    }
                }
            } else {
                selectList.clear();
                for (CallRecord callRecord : list) {
                        selectList.add(callRecord);
                }
            }
//            SimpleDateFormat s=new SimpleDateFormat("M-d");
//            for(CallRecord c:selectList){
//                Log.d(TAG, "doInBackground: date="+s.format(c.getDate()));
//            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                handler.sendEmptyMessage(0x123);
            } else if (integer == 0) {
                handler.sendEmptyMessage(0x123);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}