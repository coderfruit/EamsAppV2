package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.FailureReportingAttachmentEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;

import java.util.List;

/**
 * Created by ycm on 2018/8/28.
 */

public class RepairmentBillEntityDataListAdapter extends BaseAdapter {
    private List<FailureReportingEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public RepairmentBillEntityDataListAdapter(Context context, List<FailureReportingEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_my_fault_report_activity, null);

            mViewHolder.mTv_ReportID = convertView.findViewById(R.id.mTv_ReportID);
            mViewHolder.mTv_EquipID = convertView.findViewById(R.id.mTv_EquipID);
            mViewHolder.mTv_EquipInfo = convertView.findViewById(R.id.mTv_EquipInfo);
            mViewHolder.mTv_FaultLevel = convertView.findViewById(R.id.mTv_FaultLevel);
            mViewHolder.mTv_Status = convertView.findViewById(R.id.mTv_Status);
            mViewHolder.mTv_ReportDate = convertView.findViewById(R.id.mTv_ReportDate);
            mViewHolder.mTv_ReportUser = convertView.findViewById(R.id.mTv_ReportUser);
            mViewHolder.mTv_ReportNO = convertView.findViewById(R.id.mTv_ReportNO);
            mViewHolder.mTv_FaultDesc = convertView.findViewById(R.id.mTv_FaultDesc);
            mViewHolder.mTv_FileCnt = convertView.findViewById(R.id.mTv_FileCnt);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        FailureReportingEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_ReportID.setText(String.valueOf(dataModel.getID()));
            mViewHolder.mTv_EquipID.setText(String.valueOf(dataModel.getEquipmentID()));
            mViewHolder.mTv_EquipInfo.setText(dataModel.getEquipmentName() + "(" + dataModel.getEquipmentCode() + ")" );
            mViewHolder.mTv_FaultLevel.setText(dataModel.getFailureLevel());
            mViewHolder.mTv_Status.setText(dataModel.getStatus());
            mViewHolder.mTv_ReportDate.setText(dataModel.getReportDate());
            mViewHolder.mTv_ReportUser.setText(dataModel.getMakeUser());
            mViewHolder.mTv_ReportNO.setText(dataModel.getReportNO());
            mViewHolder.mTv_FaultDesc.setText("故障描述:" + dataModel.getFailureDesc());
            List<FailureReportingAttachmentEntity> attachList = dataModel.getFailureReportingAttachmentModelList();
            if(attachList != null) {
                mViewHolder.mTv_FileCnt.setText(attachList.size() + "");
            }
        }
        return convertView;
    }

    public static class ViewHolder {

        private RelativeLayout mItem;
        private TextView mTv_ReportID;
        private TextView mTv_EquipID;
        private TextView mTv_EquipInfo;
        private TextView mTv_FaultLevel;
        private TextView mTv_Status;
        private TextView mTv_ReportDate;
        private TextView mTv_ReportUser;
        private TextView mTv_ReportNO;
        private TextView mTv_FaultDesc;
        private TextView mTv_FileCnt;

    }

    public void loadMore(List<FailureReportingEntity> data)
    {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }
}
