package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;

/**
 * 设备维修用备件库存信息
 * Created by ycm on 2018/8/28.
 */

public class EquipMgrRepairSpareDataListAdapter extends BaseAdapter {
    private List<EquipmentUseSpareEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public EquipMgrRepairSpareDataListAdapter(Context context, List<EquipmentUseSpareEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_equipmgr_repair_spare_activity, null);

            mViewHolder.mTv_ID = convertView.findViewById(R.id.mTv_ID);
            mViewHolder.mCkb_IsCheck = convertView.findViewById(R.id.mCkb_IsCheck);
            mViewHolder.mTv_Status = convertView.findViewById(R.id.mTv_Status);
            mViewHolder.mTv_SpareName = convertView.findViewById(R.id.mTv_SpareName);
            mViewHolder.mTv_SpareCode = convertView.findViewById(R.id.mTv_SpareCode);
            mViewHolder.mTv_Stander = convertView.findViewById(R.id.mTv_Stander);
            mViewHolder.mTv_Qty = convertView.findViewById(R.id.mTv_Qty);
            mViewHolder.mTv_Unit = convertView.findViewById(R.id.mTv_Unit);
            mViewHolder.mTv_Price = convertView.findViewById(R.id.mTv_Price);
            mViewHolder.mTv_MakeDate = convertView.findViewById(R.id.mTv_MakeDate);
            mViewHolder.mTv_DeptName = convertView.findViewById(R.id.mTv_DeptName);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        EquipmentUseSpareEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_ID.setText(String.valueOf(dataModel.getSpareID()));
            mViewHolder.mCkb_IsCheck.setChecked(dataModel.getIsCheck());
            mViewHolder.mTv_Status.setText(dataModel.getSpareStatus());
            mViewHolder.mTv_SpareName.setText(dataModel.getSpareName());
            mViewHolder.mTv_SpareCode.setText(dataModel.getSpareCode());
            mViewHolder.mTv_Stander.setText(dataModel.getSpareStander());
            mViewHolder.mTv_Qty.setText(dataModel.getCount());
            mViewHolder.mTv_Unit.setText(dataModel.getSpareUnit());
            mViewHolder.mTv_Price.setText(dataModel.getSparePrice());
            mViewHolder.mTv_MakeDate.setText(dataModel.getMakeDate());
            mViewHolder.mTv_DeptName.setText(dataModel.getDeptName());
        }
        return convertView;
    }

    public static class ViewHolder {

        private TextView mTv_ID;
        private CheckBox mCkb_IsCheck;
        private TextView mTv_Status;
        private TextView mTv_SpareName;
        private TextView mTv_SpareCode;
        private TextView mTv_Stander;
        private TextView mTv_Qty;
        private TextView mTv_Unit;
        private TextView mTv_Price;
        private TextView mTv_MakeDate;
        private TextView mTv_DeptName;

    }

}
