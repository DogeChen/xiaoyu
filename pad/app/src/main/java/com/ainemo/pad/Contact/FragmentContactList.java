package com.ainemo.pad.Contact;

import ainemo.api.openapi.NemoOpenAPI;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.ainemo.pad.Contact.Contact_Adapter.MyClickLister;
import com.ainemo.pad.Contact.sortlist.CharacterParser;
import com.ainemo.pad.Contact.sortlist.SideBar;
import com.ainemo.pad.Contact.sortlist.SortModel;
import com.ainemo.pad.Datas.ContactListData;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.Utils;
import com.thinkcool.circletextimageview.CircleTextImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/4/24.
 */

public class FragmentContactList extends Fragment implements MyClickLister, OnClickListener
   {

  private ContactActivity activity;
  private RecyclerView recyclerView;
  private View view;
  private ContactActivity parentactivity;
  private View mBaseView;
  //        private ListView sortListView;
  private RecyclerView sortView;
  private SideBar sideBar;
  private TextView dialog;
  private Contact_Adapter adapter;
  private ClearEditText mClearEditText;

  private CharacterParser characterParser;
  private List<SortModel> SourceDateList;

  private PinyinComparator pinyinComparator;
  private RecyclerView.LayoutManager layoutManager;
  //    private ContactDBhelper contactDBhelper;
  private CircleTextImageView add_new;
  private int touchedId;
//     Handler callHandler;

  private PopupWindow mPopupWindow;
  private static final String TAG = "FragmentContactList";
  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == 0x123) {
        initData();
      } else if (msg.what == 0x124) {
        Utils.showShortToast(activity, "没有数据");
      }
    }
  };

  //    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.menu_call:
//                Log.d(TAG, "onMenuItemClick: call");
//                break;
//            case R.id.menu_delete:
//                Log.d(TAG, "onMenuItemClick: delete");
//                break;
//            case R.id.menu_change:
//                Utils.showShortToast(getContext(),String.valueOf(touchedId));
//                Log.d(TAG, "onMenuItemClick: change");
//                break;
//            default:break;
//        }
//        return true;
//    }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    if (view == null) {
      view = activity.getLayoutInflater().inflate(R.layout.fragment_contact, null);
    } else {
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null) {
        parent.removeView(view);
      }
    }
    initView();
    initEvent();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume: true");
    new ConstactAsyncTask().execute(0);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity =(ContactActivity) getActivity();
    characterParser = CharacterParser.getInstance();
    pinyinComparator = new PinyinComparator();
//    callHandler=activity.handler;
    new ConstactAsyncTask().execute(0);
  }

  private void initView() {
    sideBar = (SideBar) view.findViewById(R.id.sidrbar);
    dialog = (TextView) view.findViewById(R.id.dialog);

    sideBar.setTextView(dialog);
    add_new = (CircleTextImageView) view.findViewById(R.id.add);
    sortView = (RecyclerView) view.findViewById(R.id.sortlist);
    layoutManager = new LinearLayoutManager(activity);
    sortView.setLayoutManager(layoutManager);

    mClearEditText = (ClearEditText) view
        .findViewById(R.id.filter_edit);
    // 实例化汉字转拼音类
    sideBar.setTextView(dialog);
    // 设置右侧触摸监听
    sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

      @SuppressLint("NewApi")
      @Override
      public void onTouchingLetterChanged(String s) {
        // 该字母首次出现的位置
        try {
          int position = adapter.getPositionForSection(s.charAt(0));
          if (position != -1) {
            sortView.scrollToPosition(position);
          }
        }catch (Exception e){
          e.printStackTrace();
        }
      }
    });

    mClearEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

      @Override
      public void onFocusChange(View arg0, boolean arg1) {
        mClearEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

      }
    });
  }

  private void initEvent() {
    add_new.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Utils.startActivity(activity, ChangeContactActivity.class);
      }
    });
  }

  private void initData() {
    adapter = new Contact_Adapter(activity, SourceDateList, this);
    sortView.setAdapter(adapter);
    // 根据输入框输入值的改变来过滤搜索
    mClearEditText.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start,
          int before, int count) {
        // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
        filterData(s.toString());
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start,
          int count, int after) {

      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });

  }

  @Override
  public void onItemClick(View view) {

    touchedId = (int) view.getTag();
    Log.d(TAG, "onItemClick: touchedId ="+touchedId);
    //创建弹出菜单
//        PopupMenu popupMenu=new PopupMenu(getContext(),view);
//        MenuInflater inflater=popupMenu.getMenuInflater();
//        inflater.inflate(R.menu.contact,popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.setGravity(center);
//        popupMenu.show();
    try {
      View mPopupWindowView = activity.getLayoutInflater()
          .inflate(R.layout.menu_contact, null);
      mPopupWindow = new PopupWindow(mPopupWindowView, 288, 215, true);
      mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      mPopupWindow.setOutsideTouchable(true);
      mPopupWindow.showAsDropDown(view, view.getWidth() / 2, -view.getHeight() / 2);
      TextView textViewCall = (TextView) mPopupWindowView.findViewById(R.id.menu_call);
      TextView textViewDelete = (TextView) mPopupWindowView.findViewById(R.id.menu_delete);
      TextView textViewChange = (TextView) mPopupWindowView.findViewById(R.id.menu_change);
      textViewCall.setOnClickListener(this);
      textViewDelete.setOnClickListener(this);
      textViewChange.setOnClickListener(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.menu_call:
        mPopupWindow.dismiss();
        Log.d(TAG, "onMenuItemClick: call id="+touchedId);
        ContactListData contactListData=DataSupport.find(ContactListData.class,touchedId);
        if(contactListData.getXiaoyuNumber()!=null&&!contactListData.getXiaoyuNumber().equals("")){
          NemoOpenAPI.getInstance().makeCall(contactListData.getXiaoyuNumber().toString(),null,null);
          Log.d(TAG, "onClick: call xiaoyu "+contactListData.getXiaoyuNumber().toString());
        }else if(contactListData.getPhoneNumber()!=null&&!contactListData.getPhoneNumber().equals("")){
          NemoOpenAPI.getInstance().makeCall(contactListData.getPhoneNumber().toString(),null,null);
          Log.d(TAG, "onClick: call phone "+contactListData.getPhoneNumber().toString());
        }
        break;
      case R.id.menu_delete:
        mPopupWindow.dismiss();
        int deleteCount=DataSupport.delete(ContactListData.class,touchedId);
        Log.d(TAG, "deleteCount = "+deleteCount);
        if(deleteCount==0){
          Utils.showShortToast(activity,"删除失败");
        }
        new ConstactAsyncTask().execute(0);
        Log.d(TAG, "onMenuItemClick: delete");
        break;
      case R.id.menu_change:
        Utils.showShortToast(getContext(), String.valueOf(touchedId));
        Log.d(TAG, "onMenuItemClick: change is touched,id is " + touchedId);
        mPopupWindow.dismiss();
        Intent intent=new Intent(activity, ChangeContactActivity.class);
        intent.putExtra("id", touchedId);
        Log.d(TAG, " Change id ="+touchedId);
        startActivity(intent);
        break;
      default:
        break;
    }
  }

  private class ConstactAsyncTask extends AsyncTask<Integer, Integer, Integer> {

    @Override
    protected Integer doInBackground(Integer... arg0) {
      if (DataSupport.isExist(ContactListData.class)) {
        List<ContactListData> contactListDatas = DataSupport.findAll(ContactListData.class);
        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        for (ContactListData contactListData : contactListDatas) {
          ids.add(contactListData.getId());
          names.add(contactListData.getName());
          numbers.add(contactListData.getXiaoyuNumber());
        }
        Integer[] ids_list = new Integer[]{};
        String[] names_list = new String[]{};
        String[] numbers_list = new String[]{};
        ids_list = ids.toArray(ids_list);
        numbers_list = numbers.toArray(numbers_list);
        names_list = names.toArray(names_list);
        SourceDateList = filledData(ids_list, names_list, numbers_list);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        handler.sendEmptyMessage(0x123);
      } else {
        SourceDateList=new ArrayList<>();
        handler.sendEmptyMessage(0x124);
        Log.d(TAG, "doInBackground: DataBase don't exsit");
      }
      return 1;
    }

    @Override
    protected void onPostExecute(Integer result) {
      super.onPostExecute(result);

    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

  }

  /**
   * 为ListView填充数据
   */
  private List<SortModel> filledData(Integer[] ids, String[] date, String[] numbers) {
    List<SortModel> mSortList = new ArrayList<SortModel>();



    for (int i = 0; i < date.length; i++) {
      SortModel sortModel = new SortModel();
      sortModel.setId(ids[i]);
      sortModel.setName(date[i]);
      sortModel.setNumber(numbers[i]);
      // 汉字转换成拼音
      String pinyin = characterParser.getSelling(date[i]);
      String sortString = pinyin.substring(0, 1).toUpperCase();

      // 正则表达式，判断首字母是否是英文字母
      if (sortString.matches("[A-Z]")) {
        sortModel.setSortLetters(sortString.toUpperCase());
      } else {
        sortModel.setSortLetters("#");
      }

      mSortList.add(sortModel);
    }
    return mSortList;

  }

  /**
   * 根据输入框中的值来过滤数据并更新recyclerview
   */
  private void filterData(String filterStr) {
    List<SortModel> filterDateList = new ArrayList<SortModel>();

    if (TextUtils.isEmpty(filterStr)) {
      filterDateList = SourceDateList;
    } else {
      filterDateList.clear();
      for (SortModel sortModel : SourceDateList) {
        String name = sortModel.getName();
        if (name.indexOf(filterStr.toString()) != -1
            || characterParser.getSelling(name).startsWith(
            filterStr.toString())) {
          filterDateList.add(sortModel);
        }
      }
    }

    // 根据a-z进行排序
    Collections.sort(filterDateList, pinyinComparator);
    adapter.updateListView(filterDateList);
  }
}
