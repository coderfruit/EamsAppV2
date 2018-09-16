package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.SecondChkListEntity;

import java.util.List;

/**
 * 
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/23 19:45
 */
public class ReCheckBillDataListAdapter extends BaseAdapter {

    private List<SecondChkListEntity> mDataList;
    private Context mContext;

    private ViewHolder mViewHolder;

    public ReCheckBillDataListAdapter(Context context, List<SecondChkListEntity> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_re_check_bill_activity, null);

            mViewHolder.mTv_PackageName = convertView.findViewById(R.id.mTv_PackageName);
            mViewHolder.mTv_Amount1 = convertView.findViewById(R.id.mTv_Amount1);
            mViewHolder.mTv_Amount2 = convertView.findViewById(R.id.mTv_Amount2);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        SecondChkListEntity dataModel = mDataList.get(position);
        if (dataModel != null) {
            mViewHolder.mTv_PackageName.setText(TextUtils.isEmpty(dataModel.getPackingName())?"":dataModel.getPackingName());
            mViewHolder.mTv_Amount1.setText(String.valueOf(dataModel.getBadBoxCount()));
            mViewHolder.mTv_Amount2.setText(String.valueOf(dataModel.getBrokenBoxCount()));
        }
        return convertView;
    }

    public static class ViewHolder {

        private RelativeLayout mItem;
        private TextView mTv_PackageName;
        private TextView mTv_Amount1;
        private TextView mTv_Amount2;

    }
}
