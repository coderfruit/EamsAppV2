package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;



public class RepairmentPlanViewDataListAdapter extends BaseAdapter {

    private List<RepairmentPlanEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public RepairmentPlanViewDataListAdapter(Context context, List<RepairmentPlanEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_repairment_plan_activity, null);

            mViewHolder.mTFL_Flag = convertView.findViewById(R.id.mTFL_Flag);
            mViewHolder.mTv_wxjh = convertView.findViewById(R.id.mTv_wxjh);
            mViewHolder.mTv_zxzq = convertView.findViewById(R.id.mTv_zxzq);

            mViewHolder.mTv_LastRunningDate = convertView.findViewById(R.id.mTv_Inspection_Date);
            mViewHolder.mTv_NextRunningDate = convertView.findViewById(R.id.mTv_NextDate);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        RepairmentPlanEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_NextRunningDate.setText(dataModel.getNextRunningDate());
            mViewHolder.mTv_LastRunningDate.setText(dataModel.getLastRunningDate());

                mViewHolder.mTFL_Flag.setText(dataModel.getStatus());

            mViewHolder.mTv_zxzq.setText("每" + dataModel.getInterval() + dataModel.getIntervalUnit() + "执行一次");
            mViewHolder.mTv_wxjh.setText(dataModel.getInterval()+"  "+dataModel.getIntervalUnit());

        }
        return convertView;
    }

    public static class ViewHolder {

        private RelativeLayout mItem;
        private TextView mTv_wxjh;
        private TextView mTv_zxzq;
        private TextView mTFL_Flag;

        private TextView mTv_LastRunningDate;
        private TextView mTv_NextRunningDate;

    }
}
