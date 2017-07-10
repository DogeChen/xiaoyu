package com.ainemo.pad.Contact.Record;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ainemo.pad.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class CallRecordAdapter extends RecyclerView.Adapter<CallRecordAdapter.ViewHolder> {

  private List<CallRecord> callRecordList;
  private Context context;

  private static final String TAG = "CallRecordAdapter";


  private RecordClickLister myClickLister;
  public  interface RecordClickLister{
    void onItemClick(View view);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    //    private CircleImageView head;
    private TextView name;
    private String telephoneNum;
    private String xiaoyuNum;
    private TextView date;
    private ImageView callType;
    private View itemView;

    public ViewHolder(View view) {
      super(view);
//      head = (CircleImageView) view.findViewById(R.id.record_icon);
      name = (TextView) view.findViewById(R.id.record_name);
      date = (TextView) view.findViewById(R.id.call_time);
      callType = (ImageView) view.findViewById(R.id.call_type);
      this.itemView=view;
    }
  }

  public CallRecordAdapter(Context context, List<CallRecord> callRecordList,RecordClickLister myClickLister) {
    this.callRecordList = callRecordList;
    this.context = context;
    this.myClickLister=myClickLister;
  }

  public void onBindViewHolder(ViewHolder holder, int position) {

       final CallRecord callRecord = callRecordList.get(position);
//    if (callRecord.getImageUrl() != null) {
//      holder.head.setImageURI(Uri.parse(callRecord.getImageUrl()));
//    }
       if (callRecord.getName() != null && !callRecord.getName().equals("")) {
         holder.name.setText(callRecord.getName());
       } else if (callRecord.getXiaoyuId() != null && callRecord.getXiaoyuId() !=null &&!callRecord.getXiaoyuId() .equals("")) {
         holder.name.setText(callRecord.getXiaoyuId());
       } else if (callRecord.getTelephoneNum() != null && !callRecord.getTelephoneNum()
           .equals("")) {
         holder.name.setText(callRecord.getTelephoneNum());
       }
       if (CallRecord.CALL_IN == callRecord.getType()) {
         holder.callType.setImageResource(R.drawable.icon_callin);
       } else if (CallRecord.CALL_OUT == callRecord.getType()) {
         holder.callType.setImageResource(R.drawable.icon_callout);
       } else {
         holder.callType.setImageResource(R.drawable.icon_no_answer);
       }

//    //获取号码
//    holder.telephoneNum = callRecord.getTelephoneNum();
//    holder.xiaoyuNum = callRecord.getXiaoyuId();
//    String num = "";
//    if (null != callRecord.getTelephoneNum() && !"".equals(callRecord.getTelephoneNum())) {
//      num = callRecord.getTelephoneNum();
//    }
//    if (null != callRecord.getXiaoyuId() && !"".equals(callRecord.getXiaoyuId())) {
//      if (!num.equals("")) {
//        num += "       ";
//      }
//      num += callRecord.getXiaoyuId();
//    }
//    holder.number.setText(num);
       //获取名称

       //格式时间
//       if (holder.date.getText() == null || holder.date.getText().equals("")) {
         String dateString = "";
         if (null != callRecord.getDate()) {
           Calendar mCalender = Calendar.getInstance();
           SimpleDateFormat formater = new SimpleDateFormat("yyyy年M月d日");
           SimpleDateFormat outFormater = new SimpleDateFormat("M月d日");
           Date curDate = new Date(System.currentTimeMillis());

           mCalender.setTime(curDate);
           int day = mCalender.get(Calendar.DATE);
           mCalender.set(Calendar.DATE, day - 1);
           String yesterdayString = formater.format(mCalender.getTime());
           mCalender.set(Calendar.DATE, day - 2);
           String theDayBeforeYesterday = formater.format(mCalender.getTime());
           String curString = formater.format(curDate);
           String callString = formater.format(callRecord.getDate());
             Log.d(TAG, "onBindViewHolder:date="+callRecord.getDate()+" callString="+callString);
           if (curString.equals(callString)) {
             dateString = new String("今天");
           } else if (yesterdayString.equals(callString)) {
             dateString = new String("昨天");
           } else if (theDayBeforeYesterday.equals(callString)) {
             dateString = new String("前天");
           } else {
             dateString = outFormater.format(callRecord.getDate())+callString;
           }
         }
         holder.date.setText(dateString);
//       }
  }

  @Override
  public int getItemCount() {
    return callRecordList.size();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_call_record, parent, false);
    final ViewHolder viewHolder = new ViewHolder(view);
//    view.setOnClickListener(this);

    viewHolder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        int position =viewHolder.getAdapterPosition();
        CallRecord item=callRecordList.get(position);
        viewHolder.itemView.setTag(item.getId());
//                Log.d(TAG, "onClick: id="+position);
//        Utils.showShortToast(context,"id="+position);
        myClickLister.onItemClick(view);

      }
    });
    return viewHolder;
  }
}
