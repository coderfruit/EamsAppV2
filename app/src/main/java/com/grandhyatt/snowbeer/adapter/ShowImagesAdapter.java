package com.grandhyatt.snowbeer.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.grandhyatt.snowbeer.R;
import java.util.List;

/**
 * Created by ycm on 2018/8/31.
 */

    public class ShowImagesAdapter extends BaseAdapter {
        private Context context;
        private List<Uri> bpPathlist;
        private ViewHolder mViewHolder;

        public ShowImagesAdapter(Context context, List<Uri> bpPathlist) {

            this.context = context;
            this.bpPathlist = bpPathlist;
        }


        @Override
        public int getCount() {
            return bpPathlist.size();
        }


        @Override
        public Object getItem(int position) {
            return bpPathlist.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_image, null);

                mViewHolder.iv_item = convertView.findViewById(R.id.iv_item);
                convertView.setTag(mViewHolder);

            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            Uri path = bpPathlist.get(position);
            if (path != null) {
                mViewHolder.iv_item.setImageURI(path);
            }
            return convertView;
        }


        private static class ViewHolder {
            private ImageView iv_item;
        }

        public void addImage(Uri path)
        {
            bpPathlist.add(path);
            notifyDataSetChanged();
        }



    }


