package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.EquipmentMaterialEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;

import java.util.List;



public class EquipMgrMaintenMaterialDataListAdapter extends BaseAdapter {
    private List<EquipmentMaterialEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public EquipMgrMaintenMaterialDataListAdapter(Context context, List<EquipmentMaterialEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_equipmgr_maintenance_material_activity, null);

            mViewHolder.mTv_ID = convertView.findViewById(R.id.mTv_ID);
            mViewHolder.mCkb_IsCheck = convertView.findViewById(R.id.mCkb_IsCheck);

            mViewHolder.mTv_SpareName = convertView.findViewById(R.id.mTv_SpareName);
            mViewHolder.mTv_SpareCode = convertView.findViewById(R.id.mTv_SpareCode);
            mViewHolder.mTv_Stander = convertView.findViewById(R.id.mTv_Stander);

            mViewHolder.mTv_Unit = convertView.findViewById(R.id.mTv_Unit);
            mViewHolder.mTv_Price = convertView.findViewById(R.id.mTv_Price);


            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        EquipmentMaterialEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_ID.setText(String.valueOf(dataModel.getID()));
            mViewHolder.mCkb_IsCheck.setChecked(dataModel.getIsCheck());

            mViewHolder.mTv_SpareName.setText(dataModel.getMaterialName());
            mViewHolder.mTv_SpareCode.setText(dataModel.getMaterialCode());

            String stander = dataModel.getStandard();
            if(stander == null || stander.trim().length() == 0)
            {
                stander = "无";
            }
            mViewHolder.mTv_Stander.setText(stander);

            mViewHolder.mTv_Unit.setText(dataModel.getUnit());
            mViewHolder.mTv_Price.setText(dataModel.getPrice());

        }
        return convertView;
    }

    public static class ViewHolder {

        private TextView mTv_ID;
        private CheckBox mCkb_IsCheck;

        private TextView mTv_SpareName;
        private TextView mTv_SpareCode;
        private TextView mTv_Stander;

        private TextView mTv_Unit;
        private TextView mTv_Price;


    }

}
