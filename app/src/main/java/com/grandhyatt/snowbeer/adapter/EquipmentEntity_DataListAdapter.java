package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;

import java.util.List;

/**
 * 设备档案适配器
 * Created by ycm on 2018/9/28.
 */

public class EquipmentEntity_DataListAdapter extends BaseAdapter {
    private List<EquipmentEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public EquipmentEntity_DataListAdapter(Context context, List<EquipmentEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_equipment_activity, null);

            mViewHolder.mTv_ID = convertView.findViewById(R.id.mTv_ID);
            mViewHolder.mTv_Equipment = convertView.findViewById(R.id.mTv_Equipment);
            mViewHolder.mTv_UseState = convertView.findViewById(R.id.mTv_UseState);
            mViewHolder.mTv_Corp = convertView.findViewById(R.id.mTv_Corp);
            mViewHolder.mTv_Department = convertView.findViewById(R.id.mTv_Department);
            mViewHolder.mTv_Location = convertView.findViewById(R.id.mTv_Location);
            mViewHolder.mTv_keeper = convertView.findViewById(R.id.mTv_keeper);
            mViewHolder.mTv_Manu = convertView.findViewById(R.id.mTv_Manu);
            mViewHolder.mTv_Type = convertView.findViewById(R.id.mTv_Type);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        EquipmentEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_ID.setText(dataModel.getID());
            mViewHolder.mTv_Equipment.setText(dataModel.getEquipmentName() + "(" + dataModel.getEquipmentCode() + ")");
            mViewHolder.mTv_UseState.setText(dataModel.getEquipmentStatus());
            mViewHolder.mTv_Corp.setText(dataModel.getCorporationName());
            mViewHolder.mTv_Department.setText(dataModel.getDepartmentName());
            mViewHolder.mTv_Location.setText(dataModel.getInstallLoaction());
            mViewHolder.mTv_keeper.setText(dataModel.getKeeperName());
            mViewHolder.mTv_Manu.setText(dataModel.getManufacturer());
            mViewHolder.mTv_Type.setText(dataModel.getEquipmentTypeName());
        }
        return convertView;
    }

    public static class ViewHolder {

        private TextView mTv_ID;
        private TextView mTv_Equipment;
        private TextView mTv_UseState;
        private TextView mTv_Corp;
        private TextView mTv_Department;
        private TextView mTv_Location;
        private TextView mTv_keeper;
        private TextView mTv_Manu;
        private TextView mTv_Type;
    }

    public void loadMore(List<EquipmentEntity> data)
    {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }
}
