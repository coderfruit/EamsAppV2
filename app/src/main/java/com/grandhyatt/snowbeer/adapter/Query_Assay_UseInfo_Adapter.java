package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.EquipmentAssayUseItemEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;

import java.util.List;

/**
 * 化学仪器使用记录适配器
 * Created by ycm on 2018/10/26.
 */

public class Query_Assay_UseInfo_Adapter extends BaseAdapter {

    private List<EquipmentAssayUseItemEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;
    public Query_Assay_UseInfo_Adapter(Context context, List<EquipmentAssayUseItemEntity> dataList) {
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

    public void removeItem(int position) {
        mDataList.remove(position);
        notifyDataSetChanged();
    }

    public void removeItemByID(String id) {

        for(EquipmentAssayUseItemEntity item : mDataList){
            if(item.getID().equals(id)){
                mDataList.remove(item);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_query_assay_useinfo, null);

            mViewHolder.mTv_ID = convertView.findViewById(R.id.mTv_ID);
            mViewHolder.mTv_EquipID = convertView.findViewById(R.id.mTv_EquipID);
            mViewHolder.mTv_EquipName = convertView.findViewById(R.id.mTv_EquipName);
            mViewHolder.mTv_EquipCode = convertView.findViewById(R.id.mTv_EquipCode);
            mViewHolder.mTv_UseDate = convertView.findViewById(R.id.mTv_UseDate);
            mViewHolder.mTv_UseUser = convertView.findViewById(R.id.mTv_UseUser);
            mViewHolder.mTv_UseReason = convertView.findViewById(R.id.mTv_UseReason);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        EquipmentAssayUseItemEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_ID.setText(dataModel.getID());
            mViewHolder.mTv_EquipID.setText(dataModel.getEquipmentID());
            mViewHolder.mTv_EquipName.setText(dataModel.getEquipmentName());
            mViewHolder.mTv_EquipCode.setText(dataModel.getEquipmentCode());

            mViewHolder.mTv_UseDate.setText(dataModel.getUseDate());
            mViewHolder.mTv_UseUser.setText(dataModel.getUseUser());
            mViewHolder.mTv_UseReason.setText(dataModel.getUseReason());
        }
        return convertView;
    }

    public static class ViewHolder {
        private TextView mTv_ID;
        private TextView mTv_EquipID;
        private TextView mTv_EquipName;
        private TextView mTv_EquipCode;
        private TextView mTv_UseDate;
        private TextView mTv_UseUser;
        private TextView mTv_UseReason;
    }

    public void loadMore(List<EquipmentAssayUseItemEntity> data)
    {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }
}
