package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;

import java.util.List;

/**
 * Created by wulifu on 2018/7/24.
 */

public class SpinnerAdapter extends BaseAdapter {

    private List<String> mDataList;
    private Context mContext;

    public SpinnerAdapter(Context pContext, List<String> dataList) {
        this.mContext = pContext;
        this.mDataList = dataList;
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

    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(R.layout.view_item_spinner_text, null);
        if (convertView != null) {
            TextView mTextView = convertView.findViewById(R.id.mTextView);
            mTextView.setText(mDataList.get(position));
        }
        return convertView;
    }
}
