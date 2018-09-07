package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;

import java.util.List;

/**
 * 主页面GridView适配器
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018-07-07 20:24
 */
public class CarPlateDataAdapter extends BaseAdapter {

    private Context mContext;

    private List<String> mDataList;

    private ViewHolder mViewHolder;

    public CarPlateDataAdapter(Context context, List<String> dataList){
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (mDataList != null) {
            return mDataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_view_item_input_customer, null);

            mViewHolder.mTv_Name = convertView.findViewById(R.id.mTv_Name);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        String dataModel = mDataList.get(position);
        mViewHolder.mTv_Name.setText(dataModel);
        return convertView;
    }

    public View.OnClickListener itemOnClickListener;

    public static class ViewHolder {
        private TextView mTv_Name;
    }
}
