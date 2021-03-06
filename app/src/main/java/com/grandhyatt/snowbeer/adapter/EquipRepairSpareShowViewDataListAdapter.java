package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.view.NumberEditText;

import java.util.List;


/**
 * 显示设备维修时可用备件名称及可用数量，用户可修改数量
 */
public class EquipRepairSpareShowViewDataListAdapter extends BaseAdapter {
    private List<SpareInEquipmentEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public EquipRepairSpareShowViewDataListAdapter(Context context, List<SpareInEquipmentEntity> dataList) {
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
            mViewHolder.mTv_SumCountxTitle= convertView.findViewById(R.id.mTv_SumCountxTitle);
            mViewHolder.mTv_SumCountx= convertView.findViewById(R.id.mTv_SumCountx);
            mViewHolder.mTv_SpareID=convertView.findViewById(R.id.mTv_SpareID);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        SpareInEquipmentEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            String stander = "";
            if(dataModel.getSpareStander() != null && dataModel.getSpareStander().length() > 0){
                stander += "/" + dataModel.getSpareStander();
            }
            if(dataModel.getSparePartNO() != null && dataModel.getSparePartNO().length() > 0){
                stander += "/" + dataModel.getSparePartNO();
            }
            mViewHolder.mTv_SpareName.setText(dataModel.getSpareName() + stander);
            mViewHolder.mTv_Unit.setText(dataModel.getSpareUnit());
            int inputCnt = Integer.parseInt(dataModel.getReplaceCount());
            mViewHolder.mNEdt_Check.setData(inputCnt);
            if(dataModel.getAllowCount().equals("0")){
                mViewHolder.mTv_SumCountxTitle.setVisibility(View.GONE);
                mViewHolder.mTv_SumCountx.setVisibility(View.GONE);
            }else{
                mViewHolder.mTv_SumCountx.setText(dataModel.getAllowCount());
            }
            mViewHolder.mTv_SpareID.setText(dataModel.getSpareID());
        }
        return convertView;
    }

    public  void  removeItem(int position){
       // mDataList.remove(position);
        notifyDataSetChanged();
    }

    public static class ViewHolder {

        private TextView mTv_SpareName;
        private NumberEditText mNEdt_Check;
        private TextView mTv_Unit;
        private TextView mTv_SumCountxTitle;
        private TextView mTv_SumCountx;
        private  TextView mTv_SpareID;


    }

}
