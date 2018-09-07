package com.grandhyatt.snowbeer.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.CustomerEntity;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;


public class CustomerAdapter extends BaseTurboAdapter<CustomerEntity, BaseViewHolder> {

    private OnItemClickListener mItemClickListener;

    public CustomerAdapter(Context context) {
        super(context);
    }

    public CustomerAdapter(Context context, List<CustomerEntity> data) {
        super(context, data);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mItemClickListener = onItemClickListener;
    }

    @Override
    protected int getDefItemViewType(int position) {
        CustomerEntity customerEntity = getItem(position);
        return customerEntity.getType();
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new CustomerHolder(inflateItemView(R.layout.item_customer_info, parent));
        else
            return new PinnedHolder(inflateItemView(R.layout.item_pinned_header, parent));


    }


    @Override
    protected void convert(BaseViewHolder holder, CustomerEntity item) {
        if (holder instanceof CustomerHolder) {
            ((CustomerHolder) holder).mTv_CustomerName.setText(item.getCustomerName());

            if(mItemClickListener != null) {
                ((CustomerHolder) holder).mRL_Item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(v);
                    }
                });

                ((CustomerHolder) holder).mRL_Item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mItemClickListener.onItemLongClick(v);
                        return true;
                    }
                });
            }
        }else {
            String letter = item.getPys().substring(0, 1);
            ((PinnedHolder) holder).mTv_HeadView.setText(letter);
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(getData().get(i).getType() ==1 && getData().get(i).getPys().equals(letter)){
                return i;
            }
        }
        return -1;
    }

    class CustomerHolder extends BaseViewHolder {

        RelativeLayout mRL_Item;
        TextView mTv_CustomerName;

        public CustomerHolder(View view) {
            super(view);
            mRL_Item = findViewById(R.id.mRL_Item);
            mTv_CustomerName = findViewById(R.id.mTv_CustomName);
        }
    }


    class PinnedHolder extends BaseViewHolder {

        TextView mTv_HeadView;

        public PinnedHolder(View view) {
            super(view);
            mTv_HeadView = findViewById(R.id.mTv_HeadView);
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);
    }
}
