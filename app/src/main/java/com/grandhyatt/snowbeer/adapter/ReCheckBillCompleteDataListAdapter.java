package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.SecondChkListEntity;

import java.util.List;

/**
 * Created by Wu on 2018-07-08.
 */

public class ReCheckBillCompleteDataListAdapter extends BaseAdapter {

    private List<SecondChkListEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public ReCheckBillCompleteDataListAdapter(Context context, List<SecondChkListEntity> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_re_check_bill_complete_activity, null);

            mViewHolder.mTv_PackageName = convertView.findViewById(R.id.mTv_PackageName);
            mViewHolder.mTv_CheckCount = convertView.findViewById(R.id.mTv_CheckCount);
            mViewHolder.mTv_ReturnForegiftCount = convertView.findViewById(R.id.mTv_ReturnForegiftCount);
            mViewHolder.mTv_Money = convertView.findViewById(R.id.mTv_Money);
            mViewHolder.mTv_ReturnLoanCount = convertView.findViewById(R.id.mTv_ReturnLoanCount);
            mViewHolder.mTv_DepositCount = convertView.findViewById(R.id.mTv_DepositCount);


            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        SecondChkListEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_PackageName.setText(TextUtils.isEmpty(dataModel.getPackingName())?"":dataModel.getPackingName());
            mViewHolder.mTv_CheckCount.setText(String.valueOf(dataModel.getSecondCheckCount()));
            mViewHolder.mTv_ReturnForegiftCount.setText(String.valueOf(dataModel.getReturnForegiftCount()));
            mViewHolder.mTv_Money.setText(String.valueOf(dataModel.getMoney()));
            mViewHolder.mTv_ReturnLoanCount.setText(String.valueOf(dataModel.getReturnLoanCount()));
            mViewHolder.mTv_DepositCount.setText(String.valueOf(dataModel.getDepositCount()));
        }
        return convertView;
    }

    public static class ViewHolder {

        private RelativeLayout mItem;
        private TextView mTv_PackageName;
        private TextView mTv_CheckCount;
        private TextView mTv_ReturnForegiftCount;
        private TextView mTv_Money;
        private TextView mTv_ReturnLoanCount;
        private TextView mTv_DepositCount;


    }
}
