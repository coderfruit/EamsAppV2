package com.grandhyatt.snowbeer.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.grandhyatt.snowbeer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 带加减号的EditText
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/10 09:08
 */
public class NumberEditText extends RelativeLayout {

    @BindView(R.id.mIBtn_Add)
    ImageButton mIBtn_Add;

    @BindView(R.id.mIBtn_Sub)
    ImageButton mIBtn_Sub;

    @BindView(R.id.mEdt_Data)
    EditText mEdt_Data;


    public NumberEditText(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_number_edit_text, this);

        ButterKnife.bind(this);

        initView();
        bindEvent();
        refreshUI();
    }

    public NumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_number_edit_text, this);

        ButterKnife.bind(this);

        initView();
        bindEvent();
        refreshUI();
    }

    public NumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_number_edit_text, this);

        ButterKnife.bind(this);

        initView();
        bindEvent();
        refreshUI();
    }

    private void initView(){
        mEdt_Data.setEnabled(false);
    }

    private void bindEvent(){
        mIBtn_Add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(TextUtils.isEmpty(mEdt_Data.getText().toString()) || mEdt_Data.getText().length() <= 0){
                        return;
                    }

                    int intData = Integer.parseInt(mEdt_Data.getText().toString());
                    intData++;
                    mEdt_Data.setText(String.valueOf(intData));
                    mEdt_Data.requestFocus();

                }catch (Exception ex){

                }
            }
        });

        mIBtn_Sub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(TextUtils.isEmpty(mEdt_Data.getText().toString()) || mEdt_Data.getText().length() <= 0){
                        return;
                    }

                    int intData = Integer.parseInt(mEdt_Data.getText().toString());
                    intData--;
                    mEdt_Data.setText(String.valueOf(intData));
                    mEdt_Data.requestFocus();

                }catch (Exception ex){

                }
            }
        });
    }

    private void refreshUI(){

    }

    public int getData(){
        try {
            if(TextUtils.isEmpty(mEdt_Data.getText().toString()) || mEdt_Data.getText().length() <= 0){
                return 0;
            }

            int intData = Integer.parseInt(mEdt_Data.getText().toString());
            return intData;
        }catch (Exception ex){
            return 0;
        }
    }

    public void setData(int data){
        try {
            mEdt_Data.setText(String.valueOf(data));
        }catch (Exception ex){
        }
    }

    public EditText getEditText(){
        return mEdt_Data;
    }
}
