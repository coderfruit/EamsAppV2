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
import com.grandhyatt.snowbeer.view.NumberEditText;

import java.util.List;


/**
 * 显示设备维修时可用备件名称及可用数量，用户可修改数量
 */
public class EquipRepairSpareViewDataListAdapter extends BaseAdapter {
    private List<EquipmentUseSpareEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public EquipRepairSpareViewDataListAdapter(Context context, List<EquipmentUseSpareEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_repairment_spare_check_activity, null);

            mViewHolder.mTv_SpareName = convertView.findViewById(R.id.mTv_SpareName);

            mViewHolder.mTv_Unit = convertView.findViewById(R.id.mTv_Unit);
            mViewHolder.mNEdt_Check = convertView.findViewById(R.id.mNEdt_Check);
            mViewHolder.mTv_SumCount= convertView.findViewById(R.id.mTv_SumCount);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        EquipmentUseSpareEntity dataModel = mDataList.get(position);
        if (dataModel != null) {

            mViewHolder.mTv_SpareName.setText(dataModel.getSpareName() + "(" + dataModel.getSpareStander() + ")");
            mViewHolder.mTv_Unit.setText(dataModel.getSpareUnit());

            int inputCnt = Integer.parseInt(dataModel.getCount());
            mViewHolder.mNEdt_Check.setData(inputCnt);
            mViewHolder.mTv_SumCount.setText(dataModel.getCount());
        }
        return convertView;
    }

    public static class ViewHolder {

        private TextView mTv_SpareName;
        private NumberEditText mNEdt_Check;
        private TextView mTv_Unit;
        private TextView mTv_SumCount;


    }

}
