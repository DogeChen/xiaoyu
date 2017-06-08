package com.ainemo.pad.Contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ainemo.pad.Contact.sortlist.SortModel;
import com.ainemo.pad.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 2017/4/24.
 */

public class Contact_Adapter extends RecyclerView.Adapter<Contact_Adapter.MyViewHoler> {
    private Context context;
    private List<SortModel> list = new ArrayList<>();
//    private ContactDBhelper contactDBhelper;
    private static final String TAG = "Contact_Adapter";

    private MyClickLister myClickLister;
    public  interface MyClickLister{
         void onItemClick(View view);
    }
    public void updateListView(List<SortModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

//    @Override
//    public void onClick(View view) {
//        myClickLister.onClick(view);
//    }

    public static class MyViewHoler extends RecyclerView.ViewHolder {
        private TextView tvLetter;
        private TextView tvTitle;
        private CircleImageView icon;
        private TextView xiaoyuNum;
        private TextView phoneNum;
        private View itemView;

        public MyViewHoler(View itemView) {
            super(itemView);
            this.tvLetter =(TextView) itemView.findViewById(R.id.catalog);
            this.tvTitle = (TextView) itemView.findViewById(R.id.title);
            this.icon = (CircleImageView) itemView.findViewById(R.id.icon);
            this.xiaoyuNum = (TextView) itemView.findViewById(R.id.number_xiaoyu);
            this.phoneNum=(TextView) itemView.findViewById(R.id.number_phone);
            this.itemView=itemView;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public Contact_Adapter(Context context, List<SortModel> list,MyClickLister myClickLister) {
        this.context = context;
        this.list = list;
        this.myClickLister=myClickLister;

    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_phone_constacts, parent, false);
        final MyViewHoler viewHolder = new MyViewHoler(view);
//        viewHoler.itemView.setOnClickListener(this);
//      try {
//        viewHoler.itemView.setTag(list.get((int) viewHoler.getAdapterPosition()).getId());
//      }catch (Exception e){
//        e.printStackTrace();
//      }
        viewHolder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int position =viewHolder.getAdapterPosition();
                SortModel item=list.get(position);
                viewHolder.itemView.setTag(item.getId());
//                Log.d(TAG, "onClick: id="+position);
//                Utils.showShortToast(context,"id="+position);
                myClickLister.onItemClick(view);
//                //创建弹出菜单
//                PopupMenu popupMenu=new PopupMenu(context,view);
//                MenuInflater inflater=popupMenu.getMenuInflater();
//                inflater.inflate(R.menu.contact,popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(this);
//                popupMenu.show();
            }
        });
//        viewHoler.itemView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {
        int section = getSectionForPosition(position);
        final SortModel mContent = list.get(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
           holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(mContent.getSortLetters());
        }else{
            holder.tvLetter.setVisibility(View.GONE);
        }

        holder.tvTitle.setText(this.list.get(position).getName());
//        holder.icon.setText(this.list.get(position).getName());
//        holder.icon.setIconText(context,this.list.get(position).getName());
        holder.xiaoyuNum.setText(this.list.get(position).getNumber());
        holder.phoneNum.setText("");
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }



//    public void onClickItem(View view) {
//
////        int position =viewHoler.getAdapterPosition();
////        SortModel item=list.get(position);
////        touchedId=item.getId();
////        Log.d(TAG, "onClick: id="+position);
////        Utils.showShortToast(context,"id="+position);
//        //创建弹出菜单
//        PopupMenu popupMenu=new PopupMenu(context,view);
//        MenuInflater inflater=popupMenu.getMenuInflater();
//        inflater.inflate(R.menu.contact,popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.show();
//    }
}
