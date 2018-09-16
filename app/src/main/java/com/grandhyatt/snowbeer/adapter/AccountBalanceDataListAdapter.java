package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.AccountBalanceInfoEntity;
import com.grandhyatt.snowbeer.entity.SecondChkListEntity;

import java.util.List;

/**
 *
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/23 19:19
 */
public class AccountBalanceDataListAdapter extends BaseExpandableListAdapter {

    private List<AccountBalanceInfoEntity> mDataList;
    private Context mContext;

    private GroupViewHolder mGroupHolder;
    private ChildViewHolder mViewHolder;

    public AccountBalanceDataListAdapter(Context context, List<AccountBalanceInfoEntity> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getGroupCount() {
        return null == mDataList ? 0: mDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (null != mDataList && null != mDataList.get(groupPosition) && null != mDataList.get(groupPosition).getData()) {
            return mDataList.get(groupPosition).getData().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (null != mDataList && mDataList.size() > 0 && null != mDataList.get(groupPosition)) {
            return mDataList.get(groupPosition);
        } else {
            return null;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (null != mDataList && null != mDataList.get(groupPosition) && null != mDataList.get(groupPosition).getData() && null != mDataList.get(groupPosition).getData().get(childPosition)) {
            return mDataList.get(groupPosition).getData().get(childPosition);
        } else {
            return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ext_listview_item_account_balance_activity, null);

            mGroupHolder = new GroupViewHolder();

            mGroupHolder.mRL_Group = (RelativeLayout) convertView.findViewById(R.id.mRL_Group);
            mGroupHolder.mTv_Group = (TextView) convertView.findViewById(R.id.mTv_Group);

            convertView.setTag(mGroupHolder);
        } else {
            mGroupHolder = (GroupViewHolder) convertView.getTag();
        }

        try {
            mGroupHolder.mTv_Group.setText(mDataList.get(groupPosition).getAccountName());
        }catch (Exception ex){

        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ext_listview_item_account_balance_child_activity, null);
            mViewHolder = new ChildViewHolder();
            mViewHolder.mRL_Item = (RelativeLayout) convertView.findViewById(R.id.mRL_Item);
            mViewHolder.mTv_PackageName = (TextView) convertView.findViewById(R.id.mTv_PackageName);
            mViewHolder.mTv_Amount = (TextView) convertView.findViewById(R.id.mTv_Amount);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ChildViewHolder) convertView.getTag();
        }

        try {
            mViewHolder.mTv_PackageName.setText(mDataList.get(groupPosition).getData().get(childPosition).getPackingName());
            mViewHolder.mTv_Amount.setText(String.valueOf(mDataList.get(groupPosition).getData().get(childPosition).getCount()));
        }catch (Exception ex){

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public static class GroupViewHolder{
        private RelativeLayout mRL_Group;
        private TextView mTv_Group;
    }


    public static class ChildViewHolder {
        private RelativeLayout mRL_Item;
        private TextView mTv_PackageName;
        private TextView mTv_Amount;
    }
}
