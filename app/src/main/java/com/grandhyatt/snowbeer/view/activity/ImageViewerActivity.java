package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ycm on 2018/8/29.
 */

public class ImageViewerActivity extends com.grandhyatt.snowbeer.view.activity.ActivityBase implements IActivityBase, View.OnClickListener  {

    @BindView(R.id.mIv_Img)
    ImageView mIv_Img;
    @BindView(R.id.mTv_Title)
    TextView mTv_Title;
    @BindView(R.id.mBt_Back)
    Button mBt_Back;

    /**
     * 图片浏览器
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        ButterKnife.bind(this);

        bindEvent();
        Intent intent=getIntent();
        if(intent!=null)
        {
            String filePath = intent.getStringExtra("imgPath");
            Bitmap bitmap=intent.getParcelableExtra("bitmap");
            String title = intent.getStringExtra("title");
            if(filePath == null || filePath.length() == 0)
            {
                mIv_Img.setImageBitmap(bitmap);
            }
            else
            {
                File file = new File(filePath);
                Uri mUri = Uri.parse("file://" + file.getPath());
                 mIv_Img.setImageURI(mUri);
            }

            mTv_Title.setText(title);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBt_Back:
                finish();
                break;

        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindEvent() {
        mBt_Back.setOnClickListener(this);
    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {

    }
}
