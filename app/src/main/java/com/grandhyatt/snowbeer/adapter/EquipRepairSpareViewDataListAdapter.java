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
            mViewHolder.mTv_SumCountx = convertView.findViewById(R.id.mTv_SumCountx);
            mViewHolder.mTv_SpareID = convertView.findViewById(R.id.mTv_SpareID);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        EquipmentUseSpareEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            String stander = "";
            if(dataModel.getSpareStander() != null && dataModel.getSpareStander().length() > 0){
                stander += "/" + dataModel.getSpareStander();
            }
            mViewHolder.mTv_SpareName.setText(dataModel.getSpareName() + stander);
            mViewHolder.mTv_Unit.setText(dataModel.getSpareUnit());

            if(dataModel.getUserInputCount() != null && dataModel.getUserInputCount().length() > 0){
                double inputCount = Double.parseDouble(dataModel.getUserInputCount());
                mViewHolder.mNEdt_Check.setData(inputCount);
            }else{
                mViewHolder.mNEdt_Check.setData(1);
            }

            if(dataModel.getCount() != null && dataModel.getCount().length() > 0) {
                double limit = Double.parseDouble(dataModel.getCount());
                mViewHolder.mNEdt_Check.SetNumberLimit(limit);
            }
            mViewHolder.mTv_SumCountx.setText(dataModel.getCount());//可用量
            mViewHolder.mTv_SpareID.setText(dataModel.getSpareID());

        }
        return convertView;
    }

    public void removeItem(int position) {
        // mDataList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 修改可用量
     * @param position
     * @param count
     */
    public void modifyCount(int position,String count)
    {
        EquipmentUseSpareEntity item = (EquipmentUseSpareEntity)getItem(0);
        item.setCount(count);
        notifyDataSetChanged();
    }

    public static class ViewHolder {

        private TextView mTv_SpareName;
        private NumberEditText mNEdt_Check;
        private TextView mTv_Unit;
        private TextView mTv_SumCountx;
        private TextView mTv_SpareID;


    }

}
