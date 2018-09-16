package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.FirstCheckBillEntity;

import java.util.List;

/**
 * Created by Wu on 2018-07-08.
 */

public class SelectFirstCheckBillDataListAdapter extends BaseAdapter {

    private List<FirstCheckBillEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public SelectFirstCheckBillDataListAdapter(Context context, List<FirstCheckBillEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_select_first_check_bill_activity, null);

            mViewHolder.mTv_PackageCode = convertView.findViewById(R.id.mTv_PackageCode);
            mViewHolder.mTv_PackageName = convertView.findViewById(R.id.mTv_PackageName);
            mViewHolder.mTv_Type = convertView.findViewById(R.id.mTv_Type);
            mViewHolder.mTv_TrunkNo = convertView.findViewById(R.id.mTv_TrunkNo);
            mViewHolder.mTv_User = convertView.findViewById(R.id.mTv_User);
            mViewHolder.mTv_Date = convertView.findViewById(R.id.mTv_Date);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        FirstCheckBillEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_PackageCode.setText(dataModel.getPackingCode());
            mViewHolder.mTv_PackageName.setText(dataModel.getPackingName());
            if (dataModel.getReceivingBillType().equals("0")) {
                mViewHolder.mTv_Type.setText("周转");
            } else {
                mViewHolder.mTv_Type.setText("回收");
            }
            mViewHolder.mTv_TrunkNo.setText(dataModel.getTruckNo());
            mViewHolder.mTv_User.setText(dataModel.getFirstCheckUser());
            mViewHolder.mTv_Date.setText(dataModel.getFirstCheckDate());
        }
        return convertView;
    }

    public static class ViewHolder {

        private RelativeLayout mItem;
        private TextView mTv_PackageCode;
        private TextView mTv_PackageName;
        private TextView mTv_Type;
        private TextView mTv_TrunkNo;
        private TextView mTv_User;
        private TextView mTv_Date;

    }
}
