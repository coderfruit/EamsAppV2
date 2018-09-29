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
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;

/**
 * 设备维修提醒适配器
 * Created by ycm on 2018/9/28.
 */

public class Equip_Repair_EntityDataListAdapter extends BaseAdapter {
    private List<RepairmentPlanEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public Equip_Repair_EntityDataListAdapter(Context context, List<RepairmentPlanEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_equip_repair_activity, null);

            mViewHolder.mTv_Status = convertView.findViewById(R.id.mTv_Status);
            mViewHolder.mTv_ID = convertView.findViewById(R.id.mTv_ID);
            mViewHolder.mTv_RepairmentLevel = convertView.findViewById(R.id.mTv_RepairmentLevel);
            mViewHolder.mTv_Interval = convertView.findViewById(R.id.mTv_Interval);
            mViewHolder.mTv_EquipName = convertView.findViewById(R.id.mTv_EquipName);
            mViewHolder.mTv_EquipCode = convertView.findViewById(R.id.mTv_EquipCode);
            mViewHolder.mTv_LastRunningDate = convertView.findViewById(R.id.mTv_LastRunningDate);
            mViewHolder.mTv_NextRunningDate = convertView.findViewById(R.id.mTv_NextRunningDate);
            mViewHolder.mTv_Desc = convertView.findViewById(R.id.mTv_Desc);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        RepairmentPlanEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_Status.setText(dataModel.getStatus());
            mViewHolder.mTv_ID.setText(dataModel.getID());
            mViewHolder.mTv_RepairmentLevel.setText(dataModel.getRepairmentLevel());
            mViewHolder.mTv_Interval.setText("每" + dataModel.getInterval() + dataModel.getIntervalUnit() + "执行一次");
            mViewHolder.mTv_EquipName.setText(dataModel.getEquipmentName());
            mViewHolder.mTv_EquipCode.setText(dataModel.getEquipmentCode());
            mViewHolder.mTv_LastRunningDate.setText(dataModel.getLastRunningDate());
            mViewHolder.mTv_NextRunningDate.setText(dataModel.getNextRunningDate());
            mViewHolder.mTv_Desc.setText(dataModel.getDescription());

        }
        return convertView;
    }

    public static class ViewHolder {

        private TextView mTv_Status;
        private TextView mTv_ID;
        private TextView mTv_RepairmentLevel;
        private TextView mTv_Interval;
        private TextView mTv_EquipName;
        private TextView mTv_EquipCode;
        private TextView mTv_LastRunningDate;
        private TextView mTv_NextRunningDate;
        private TextView mTv_Desc;

    }

    public void loadMore(List<RepairmentPlanEntity> data) {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }
}
