package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.view.NumberEditText;

import java.util.List;


/**
 * 显示设备维修时可用备件名称及可用数量，用户可修改数量
 */
public class EquipRepairExSpareViewDataListAdapter extends BaseAdapter {
    private List<SpareInEquipmentEntity> mDataList;
    private Context mContext;

    private EquipRepairExSpareViewDataListAdapter.ViewHolder mViewHolder;

    public EquipRepairExSpareViewDataListAdapter(Context context, List<SpareInEquipmentEntity> dataList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            mViewHolder = new EquipRepairExSpareViewDataListAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_repairmentex_plan_check_activity, null);

            mViewHolder.mCkb_ID = convertView.findViewById(R.id.mCkb_ID);
            mViewHolder.mTv_Status = convertView.findViewById(R.id.mTv_Status);
            mViewHolder.mTv_RepairmentLevel = convertView.findViewById(R.id.mTv_RepairmentLevel);
            mViewHolder.mTv_PlanInterval = convertView.findViewById(R.id.mTv_PlanInterval);
            mViewHolder.mTv_LastRunningDate = convertView.findViewById(R.id.mTv_LastRunningDate);
            mViewHolder.mTv_NextRunningDate = convertView.findViewById(R.id.mTv_NextRunningDate);
            mViewHolder.mTv_PlanDescTitle = convertView.findViewById(R.id.mTv_PlanDescTitle);
            mViewHolder.mTv_PlanDesc = convertView.findViewById(R.id.mTv_PlanDesc);
            mViewHolder.mTv_ID = convertView.findViewById(R.id.mTv_ID);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (EquipRepairExSpareViewDataListAdapter.ViewHolder) convertView.getTag();
        }

        SpareInEquipmentEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_Status.setText(dataModel.getStatus());
            if(dataModel.getStatus().contains("超期"))
            {
                mViewHolder.mTv_Status.setTextColor(Color.RED);
            }
            mViewHolder.mTv_RepairmentLevel.setText(String.valueOf(dataModel.getRepairmentLevel()));
            mViewHolder.mTv_PlanInterval.setText("每" + dataModel.getReplaceCycles() + "天执行一次");
            mViewHolder.mTv_LastRunningDate.setText(dataModel.getLastReplaceDate());
            mViewHolder.mTv_NextRunningDate.setText(dataModel.getNextReplaceDate());
            mViewHolder.mTv_PlanDescTitle.setText("备件信息：");
            mViewHolder.mTv_PlanDesc.setText("(" + dataModel.getSpareCode() + ")" + dataModel.getSpareName());
            mViewHolder.mTv_ID.setText(dataModel.getID());
            mViewHolder.mCkb_ID.setChecked(dataModel.getIsCheck());
        }
        return convertView;
    }

    public static class ViewHolder {
        private CheckBox mCkb_ID;
        private TextView mTv_Status;
        private TextView mTv_RepairmentLevel;
        private TextView mTv_PlanInterval;
        private TextView mTv_LastRunningDate;
        private TextView mTv_NextRunningDate;
        private TextView mTv_PlanDescTitle;
        private TextView mTv_PlanDesc;
        private TextView mTv_ID;
    }

}