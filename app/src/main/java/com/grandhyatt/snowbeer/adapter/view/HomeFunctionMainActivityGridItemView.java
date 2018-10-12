package com.grandhyatt.snowbeer.adapter.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.grandhyatt.commonlib.adapter.IAdapterView;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.HomeFunctionEntity;
import com.grandhyatt.snowbeer.utils.transform.GlideCircleTransform;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 主页面GridView适配器ItemView
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018-07-07 20:23
 */
public class HomeFunctionMainActivityGridItemView extends RelativeLayout implements IAdapterView<HomeFunctionEntity> {

    private Context mContext;
    private HomeFunctionEntity mDataModel;

    private LinearLayout mRL_Item;
    private ImageView mIv_Icon;
    private TextView mTv_Name;
    private TextView mTv_Amount;

//    Badge qBv_mIv_Icon;
    Badge qBv_mRL_Item;

    public HomeFunctionMainActivityGridItemView(Context context){
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.grid_view_item_home_fragment_function, this);

        mRL_Item = (LinearLayout) findViewById(R.id.mRL_Item);
        mIv_Icon = (ImageView) findViewById(R.id.mIv_Icon);
        mTv_Name = (TextView) findViewById(R.id.mTv_Name);

//        qBv_mIv_Icon = new QBadgeView(this.getContext()).bindTarget(mIv_Icon);
        qBv_mRL_Item = new QBadgeView(this.getContext()).bindTarget(mRL_Item).setBadgeTextSize(18,true);
    }

    @Override
    public void bind(int position, HomeFunctionEntity... t) {
        if (t.length <= 0) {
            return;
        }
        mDataModel = t[0];
        if (mDataModel != null) {
            mTv_Name.setText(mDataModel.getName());

//            qBv_mIv_Icon.setBadgeNumber(mDataModel.getMsgNumber());
            qBv_mRL_Item.setBadgeNumber(mDataModel.getMsgNumber());

            Glide.with(mContext).load(mDataModel.getImage()).centerCrop().transform(new GlideCircleTransform(mContext)).error(R.drawable.logo).placeholder(R.drawable.logo).into(mIv_Icon);
        }
    }
}
