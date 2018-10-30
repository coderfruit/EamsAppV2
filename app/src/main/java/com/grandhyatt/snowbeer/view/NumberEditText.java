package com.grandhyatt.snowbeer.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.grandhyatt.commonlib.utils.ToastUtils;
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

    double NumberLimit;

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
        mEdt_Data.setEnabled(true);
    }

    private void bindEvent(){
        mIBtn_Add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(TextUtils.isEmpty(mEdt_Data.getText().toString()) || mEdt_Data.getText().length() <= 0){
                        return;
                    }

                    double intData = Double.parseDouble(mEdt_Data.getText().toString());
                    intData++;
                    if(NumberLimit > 0){
                        if(intData > NumberLimit){
                            ToastUtils.showToast(getContext(),"数量不可超过" + NumberLimit );
                            intData = NumberLimit;
                        }
                    }
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

                    double intData = Double.parseDouble(mEdt_Data.getText().toString());
                    intData--;
                    if(intData <= 0) intData = 1;
                    mEdt_Data.setText(String.valueOf(intData));
                    mEdt_Data.requestFocus();

                }catch (Exception ex){

                }
            }
        });

        mEdt_Data.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String preText = mEdt_Data.getText().toString();
                final EditText inputContrl = new EditText(getContext());
                inputContrl.setFocusable(true);
                inputContrl.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                inputContrl.setText(preText);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("请输入数字")
                        .setIcon(R.drawable.logo32)
                        .setView(inputContrl)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String inputText = inputContrl.getText().toString();

                                if(inputText != null && inputText.trim().length() != 0) {
                                    try {
                                        double intData = Double.parseDouble(inputText);

                                        if(NumberLimit > 0){
                                            if(intData > NumberLimit){
                                                ToastUtils.showToast(getContext(),"数量不可超过" + NumberLimit );
                                                intData = NumberLimit;
                                            }
                                        }

                                        mEdt_Data.setText(String.valueOf(intData));
                                    } catch (Exception ex) {

                                    }
                                }
                            }
                        });
                builder.show();
            }
        });
    }

    private void refreshUI(){

    }

    public double getData(){
        try {
            if(TextUtils.isEmpty(mEdt_Data.getText().toString()) || mEdt_Data.getText().length() <= 0){
                return 0;
            }

            double intData = Double.parseDouble(mEdt_Data.getText().toString());
            return intData;
        }catch (Exception ex){
            return 0;
        }
    }

    public void setData(double data){
        try {
            mEdt_Data.setText(String.valueOf(data));
        }catch (Exception ex){
        }
    }

    public void SetNumberLimit(double data)
    {
        NumberLimit = data;
    }

    public EditText getEditText(){
        return mEdt_Data;
    }
}
