package com.ainemo.pad.Case.vPage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ainemo.pad.Datas.CaseInfor;
import com.ainemo.pad.Datas.DoctorCaseList;
import com.ainemo.pad.R;
import com.ainemo.pad.SomeUtils.GlobalData;
import com.ainemo.pad.SomeUtils.MyBitmapUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Silver on 2017/7/17.
 */

public class DoctorPagerAdapter  extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<DoctorCaseList> mData;
    private Context mContext;

    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    private static final String TAG = "CardPagerAdapter";

    private int MaxElevationFactor = 9;

    @Override
    public int getMaxElevationFactor() {
        return MaxElevationFactor;
    }

    @Override
    public void setMaxElevationFactor(int MaxElevationFactor) {
        this.MaxElevationFactor = MaxElevationFactor;
    }

    public DoctorPagerAdapter(Context context){
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.mContext = context;
    }


    public void setCardList(List<DoctorCaseList> caseInfors) {
        mData = caseInfors;
        mViews.clear();
        for (int i = 0; i < caseInfors.size(); i++) {
            mViews.add(null);
        }
    }

    public void addCaseList(List<DoctorCaseList> caseList) {
        mData.addAll(caseList);

        for (int i = 0; i < caseList.size(); i++) {
            mViews.add(null);
        }
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_doctor_case, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardItemClickListener != null) {
                    cardItemClickListener.onClick(position);
                }
            }
        });
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.item);

        cardView.setMaxCardElevation(MaxElevationFactor);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        try {
            mViews.set(position, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bind(DoctorCaseList aCase, View view) {
        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.head);
        TextView name = (TextView) view.findViewById(R.id.patient_name);
        TextView gender = (TextView) view.findViewById(R.id.patient_gender);
        TextView age = (TextView) view.findViewById(R.id.patient_age);
        TextView doctorName = (TextView) view.findViewById(R.id.doctor_name);
        TextView illnessDescription = (TextView) view.findViewById(R.id.illness_description);
        try {
            bitmapUtils.disPlay(imageView, GlobalData.GET_PATIENT_IMAGE + aCase.getImage());
            Log.d(TAG, "bind: aCase.getImage = " + aCase.getImage());
            name.setText(aCase.getName());
            gender.setText(aCase.getSex());
            age.setText(aCase.getAge());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CardPagerAdapter.OnCardItemClickListener cardItemClickListener;

    public interface OnCardItemClickListener {
        void onClick(int position);
    }

    public void setOnCardItemClickListener(CardPagerAdapter.OnCardItemClickListener cardItemClickListener) {
        this.cardItemClickListener = cardItemClickListener;
    }

}
