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
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;

/**
 * Created by ycm on 2018/8/28.
 */

public class RepairmentPlanCheckDataListAdapter extends BaseAdapter {
    private List<RepairmentPlanEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public RepairmentPlanCheckDataListAdapter(Context context, List<RepairmentPlanEntity> dataList) {
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

            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_repairment_plan_check_activity, null);

            mViewHolder.mCkb_ID = convertView.findViewById(R.id.mCkb_ID);
            mViewHolder.mTv_Status = convertView.findViewById(R.id.mTv_Status);
            if(mViewHolder.mTv_Status.getText().toString().contains("超期")){
                mViewHolder.mTv_Status.setTextColor(Color.RED);
            }else{
                mViewHolder.mTv_Status.setTextColor(Color.GREEN);
            }
            mViewHolder.mTv_RepairmentLevel = convertView.findViewById(R.id.mTv_RepairmentLevel);
            mViewHolder.mTv_PlanInterval = convertView.findViewById(R.id.mTv_PlanInterval);
            mViewHolder.mTv_LastRunningDate = convertView.findViewById(R.id.mTv_Inspection_Date);
            mViewHolder.mTv_NextRunningDate = convertView.findViewById(R.id.mTv_NextDate);
            //mViewHolder.mTv_PlanDescTitle = convertView.findViewById(R.id.mTv_FaultLevel);
            mViewHolder.mTv_PlanDesc = convertView.findViewById(R.id.mTv_PlanDesc);
            mViewHolder.mTv_ID = convertView.findViewById(R.id.mTv_ID);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        RepairmentPlanEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_Status.setText(dataModel.getStatus());
            if(dataModel.getStatus().contains("超期")) {
                mViewHolder.mTv_Status.setTextColor(Color.RED);
            }else{
                mViewHolder.mTv_Status.setTextColor(Color.GREEN);
            }
            mViewHolder.mTv_RepairmentLevel.setText(String.valueOf(dataModel.getRepairmentLevel()));
            mViewHolder.mTv_PlanInterval.setText("每" + dataModel.getInterval() + dataModel.getIntervalUnit() + "执行一次");
            mViewHolder.mTv_LastRunningDate.setText(dataModel.getLastRunningDate());
            mViewHolder.mTv_NextRunningDate.setText(dataModel.getNextRunningDate());

            //mViewHolder.mTv_PlanDescTitle.setText("");
            mViewHolder.mTv_PlanDesc.setText(dataModel.getDescription());
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
    public void loadMore(List<RepairmentPlanEntity> data)
    {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    public  void  removeItem(int position){
//        mDataList.remove(position);
        notifyDataSetChanged();
    }
}
