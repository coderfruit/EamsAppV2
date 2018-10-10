package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;

/**
 * Created by ycm on 2018/8/28.
 */

public class MaintenancePlanCheckDataListAdapter extends BaseAdapter {
    private List<MaintenancePlanEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;
    private int tempPosition = -1;  //记录已经点击的CheckBox的位置
    public MaintenancePlanCheckDataListAdapter(Context context, List<MaintenancePlanEntity> dataList) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {

            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_maintenance_plan_check_activity, null);

            mViewHolder.mCkb_ID = convertView.findViewById(R.id.mCkb_ID);
            mViewHolder.mTv_Status = convertView.findViewById(R.id.mTv_Status);
            mViewHolder.mTv_RepairmentLevel = convertView.findViewById(R.id.mTv_RepairmentLevel);
            mViewHolder.mTv_PlanInterval = convertView.findViewById(R.id.mTv_PlanInterval);
            mViewHolder.mTv_LastRunningDate = convertView.findViewById(R.id.mTv_LastRunningDate);
            mViewHolder.mTv_NextRunningDate = convertView.findViewById(R.id.mTv_NextRunningDate);
            //mViewHolder.mTv_PlanDescTitle = convertView.findViewById(R.id.mTv_FaultLevel);
            mViewHolder.mTv_PlanDesc = convertView.findViewById(R.id.mTv_PlanDesc);
            mViewHolder.mTv_ID = convertView.findViewById(R.id.mTv_ID);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        MaintenancePlanEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_RepairmentLevel.setText(String.valueOf(dataModel.getMaintenanceLevel()));
            mViewHolder.mTv_PlanInterval.setText("每" + dataModel.getInterval() + dataModel.getIntervalUnit() + "执行一次");
            mViewHolder.mTv_LastRunningDate.setText(dataModel.getLastRunningDate());
            mViewHolder.mTv_NextRunningDate.setText(dataModel.getNextRunningDate());
            mViewHolder.mTv_Status.setText(dataModel.getStatus());
            //mViewHolder.mTv_PlanDescTitle.setText("");
            mViewHolder.mTv_PlanDesc.setText(dataModel.getDescription());
            mViewHolder.mTv_ID.setText(dataModel.getID());
            mViewHolder.mCkb_ID.setChecked(dataModel.getIsCheck());
            mViewHolder.mCkb_ID.setId(position);    //设置当前position为CheckBox的id
            mViewHolder.mCkb_ID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (tempPosition != -1) {
                            //根据id找到上次点击的CheckBox,将它设置为false.
                            CheckBox tempCheckBox = (CheckBox) parent.findViewById(tempPosition);
                            if (tempCheckBox != null) {
                                tempCheckBox.setChecked(false);
                            }
                        }
                        //保存当前选中CheckBox的id值
                        tempPosition = buttonView.getId();

                    } else {    //当CheckBox被选中,又被取消时,将tempPosition重新初始化.
                        tempPosition = -1;
                    }
                }
            });
            if (position == tempPosition)   //比较位置是否一样,一样就设置为选中,否则就设置为未选中.
                mViewHolder.mCkb_ID.setChecked(true);
            else mViewHolder.mCkb_ID.setChecked(false);
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
    public void loadMore(List<MaintenancePlanEntity> data)
    {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }
}
