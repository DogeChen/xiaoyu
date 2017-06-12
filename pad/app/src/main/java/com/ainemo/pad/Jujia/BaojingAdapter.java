package com.ainemo.pad.Jujia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ainemo.pad.Datas.OneKeyWarning;
import com.ainemo.pad.R;
import java.util.List;

/**
 * Created by victor on 17-5-4.
 */

public class BaojingAdapter extends RecyclerView.Adapter<BaojingAdapter.ViewHolder> {
    private Context context;
    private List<OneKeyWarning> oneKeyWarnings;
  private static final String TAG = "BaojingAdapter";

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView number;
        private TextView timeYY;
        private TextView timeHH;
        public ViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.baojing_she_bei_number);
            timeYY = (TextView) itemView.findViewById(R.id.baojing_yyyyMMdd);
            timeHH=(TextView)itemView.findViewById(R.id.baojing_hhmmss);
        }
    }

    public BaojingAdapter(Context context, List<OneKeyWarning> oneKeyWarnings) {
        this.context = context;
        this.oneKeyWarnings = oneKeyWarnings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.baojing_adpter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       OneKeyWarning oneKeyWarning = oneKeyWarnings.get(position);
        String time=oneKeyWarning.getAdd_date();
        String time1 ;
        String time2;
        try {
            time1=time.substring(0,4)+"年"+time.substring(5,7)+"月"+time.substring(8,10)+"日";
             time2 = time.substring(time.indexOf(' ') + 1, time.length());
        }catch (Exception e){
            time1=new String("");
            time2=new String("");
            e.printStackTrace();
        }
        holder.timeYY.setText(time1);
        holder.timeHH.setText(time2);
        holder.number.setText("设备编号: " + oneKeyWarning.getSid());
//      Log.d(TAG, "onBindViewHolder: "+oneKeyWarning.getSid());
    }

    @Override
    public int getItemCount() {
        return oneKeyWarnings.size();
    }
}
