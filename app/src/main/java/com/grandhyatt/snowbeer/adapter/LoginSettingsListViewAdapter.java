package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.APIHostInfoEntity;

import java.util.List;

/**
 * 登录设置页面列表的适配器
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/6 14:40
 */
public class LoginSettingsListViewAdapter extends BaseAdapter {

    private List<APIHostInfoEntity> mDataList;
    private Context mContext;
    private ViewHolder mViewHolder;

    private APIHostInfoEntity selected;

    /**
     * 获取 selected
     *
     * @return 返回 selected
     */
    public APIHostInfoEntity getSelected() {
        return selected;
    }

    /**
     * 设置 selected
     *
     * @return 返回 selected
     */
    public void setSelected(APIHostInfoEntity selected) {
        this.selected = selected;
    }

    public LoginSettingsListViewAdapter(Context context, List<APIHostInfoEntity> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mDataList == null) {
            return null;
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_login_settings_activity_api_host, null);

            mViewHolder.mTv_Host = convertView.findViewById(R.id.mTv_Host);
            mViewHolder.mTv_Port = convertView.findViewById(R.id.mTv_Port);
            mViewHolder.mIv_Checked = convertView.findViewById(R.id.mIv_Checked);

            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();

        }

        if (position < mDataList.size()) {

            APIHostInfoEntity dataModel = mDataList.get(position);
            if (dataModel != null) {
                mViewHolder.mTv_Host.setText(dataModel.getHost_url());
                mViewHolder.mTv_Port.setText(dataModel.getPort());
            }

            if (null != this.selected && this.selected.getId() == mDataList.get(position).getId()) {
                mViewHolder.mIv_Checked.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.mIv_Checked.setVisibility(View.INVISIBLE);
            }

        }

        return convertView;
    }


    public static class ViewHolder {
        private RelativeLayout mItem;
        private TextView mTv_Host;
        private TextView mTv_Port;
        private ImageView mIv_Checked;
    }
}
