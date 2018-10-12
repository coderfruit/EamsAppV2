package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.grandhyatt.commonlib.adapter.IAdapterView;
import com.grandhyatt.snowbeer.adapter.view.HomeFunctionMainActivityGridItemView;
import com.grandhyatt.snowbeer.entity.HomeFunctionEntity;

import java.util.List;

/**
 * 主页面GridView适配器
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018-07-07 20:24
 */
public class HomeFunctionFragmentDataAdapter extends BaseAdapter {

    private Context mContext;

    private List<HomeFunctionEntity> mDataList;

    public HomeFunctionFragmentDataAdapter(Context context, List<HomeFunctionEntity> dataList){
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        IAdapterView<HomeFunctionEntity> dataView = null;
        HomeFunctionEntity dataModel = mDataList.get(position);

        dataView = new HomeFunctionMainActivityGridItemView(mContext);

        dataView.bind(position, dataModel);
        return (View) dataView;
    }

    public View.OnClickListener itemOnClickListener;

    /**
     * 修改预警消息条数
     * @param position
     * @param msgNumber
     */
    public void modifyItem(int position,int msgNumber){
        if (mDataList != null) {
            HomeFunctionEntity entity = mDataList.get(position);
            entity.setMsgNumber(msgNumber);
            notifyDataSetChanged();
        }
    }
}
