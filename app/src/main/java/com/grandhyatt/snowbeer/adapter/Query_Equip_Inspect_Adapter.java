package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.InspectBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;

import java.util.List;

/**
 * 设备检验记录检索适配器
 * Created by ycm on 2018/10/22.
 */

public class Query_Equip_Inspect_Adapter extends BaseAdapter {

    private List<InspectBillEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;
    public Query_Equip_Inspect_Adapter(Context context, List<InspectBillEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_query_equip_inspect_info, null);

            mViewHolder.mTv_ID = convertView.findViewById(R.id.mTv_ID);
            mViewHolder.mTv_EquipName = convertView.findViewById(R.id.mTv_EquipName);
            mViewHolder.mTv_EquipCode = convertView.findViewById(R.id.mTv_EquipCode);
            mViewHolder.mTv_InspectionItem = convertView.findViewById(R.id.mTv_InspectionItem);
            mViewHolder.mTv_BillNo = convertView.findViewById(R.id.mTv_BillNo);
            mViewHolder.mTv_InspectionMode = convertView.findViewById(R.id.mTv_InspectionMode);
            mViewHolder.mTv_Inspection_Corp = convertView.findViewById(R.id.mTv_Inspection_Corp);
            mViewHolder.mTv_InspectionResult = convertView.findViewById(R.id.mTv_InspectionResult);
            mViewHolder.mTv_makeUser = convertView.findViewById(R.id.mTv_makeUser);
            mViewHolder.mTv_makeDate = convertView.findViewById(R.id.mTv_makeDate);
            mViewHolder.mTv_Begin_Date = convertView.findViewById(R.id.mTv_Begin_Date);
            mViewHolder.mTv_End_Date = convertView.findViewById(R.id.mTv_End_Date);
            mViewHolder.mTv_Inspection_User = convertView.findViewById(R.id.mTv_Inspection_User);
            mViewHolder.mTv_Money = convertView.findViewById(R.id.mTv_Money);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        InspectBillEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_ID.setText(dataModel.getID());
            mViewHolder.mTv_EquipName.setText(dataModel.getEquipmentName());
            mViewHolder.mTv_EquipCode.setText(dataModel.getEquipmentCode());
            mViewHolder.mTv_InspectionItem.setText(dataModel.getInspectionItem());
            mViewHolder.mTv_BillNo.setText(dataModel.getInspectionBillNO());
            mViewHolder.mTv_InspectionMode.setText(dataModel.getInspectionMode());
            mViewHolder.mTv_Inspection_Corp.setText(dataModel.getInspectionCorp());
            mViewHolder.mTv_InspectionResult.setText(dataModel.getInspectionResult());
            mViewHolder.mTv_makeUser.setText(dataModel.getMakeUser());
            mViewHolder.mTv_makeDate.setText(dataModel.getMakeDate());
            mViewHolder.mTv_Begin_Date.setText(dataModel.getInspectionDate());
            mViewHolder.mTv_End_Date.setText(dataModel.getNextInspectionDate());
            mViewHolder.mTv_Inspection_User.setText(dataModel.getInspectionUser());
            mViewHolder.mTv_Money.setText(dataModel.getTotalMoney());

        }
        return convertView;
    }

    public static class ViewHolder {
        private TextView mTv_ID;
        private TextView mTv_EquipName;
        private TextView mTv_EquipCode;
        private TextView mTv_InspectionItem;
        private TextView mTv_BillNo;
        private TextView mTv_InspectionMode;
        private TextView mTv_Inspection_Corp;
        private TextView mTv_InspectionResult;
        private TextView mTv_makeUser;
        private TextView mTv_makeDate;
        private TextView mTv_Begin_Date;
        private TextView mTv_End_Date;
        private TextView mTv_Inspection_User;
        private TextView mTv_Money;
    }

    public void loadMore(List<InspectBillEntity> data)
    {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }
}
