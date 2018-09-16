package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.CarPlateDataAdapter;
import com.grandhyatt.snowbeer.adapter.HomeFunctionFragmentDataAdapter;
import com.grandhyatt.snowbeer.utils.PackageUtils;
import com.grandhyatt.snowbeer.view.MyGridView;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于页面
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/23 13:27
 */
public class InputCustomerInfoActivity extends ActivityBase implements IActivityBase {

    public static final String RESULT_DATA_CAR_PLATE = "RESULT_DATA_CAR_PLATE";
    public static final int REQUEST_DATA_CAR_PLATE_CODE = 20001;

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

//    @BindView(R.id.mTFL_Province)
//    TagFlowLayout mTFL_Province;

    @BindView(R.id.mRL_Clear)
    RelativeLayout mRL_Clear;

    @BindView(R.id.mGridView)
    MyGridView mGridView;

    @BindView(R.id.mEdt_CarPlate)
    EditText mEdt_CarPlate;

    private CarPlateDataAdapter mAdapter;
    private List<String> mProvinceDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_customer_info);

        ButterKnife.bind(this);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();
    }

    @Override
    public void initView() {
        mProvinceDataList.add("京");
        mProvinceDataList.add("津");
        mProvinceDataList.add("沪");
        mProvinceDataList.add("渝");
        mProvinceDataList.add("冀");
        mProvinceDataList.add("豫");
        mProvinceDataList.add("云");
        mProvinceDataList.add("辽");
        mProvinceDataList.add("吉");
        mProvinceDataList.add("黑");
        mProvinceDataList.add("湘");
        mProvinceDataList.add("皖");
        mProvinceDataList.add("鲁");
        mProvinceDataList.add("新");
        mProvinceDataList.add("苏");
        mProvinceDataList.add("浙");
        mProvinceDataList.add("赣");
        mProvinceDataList.add("鄂");
        mProvinceDataList.add("桂");
        mProvinceDataList.add("甘");
        mProvinceDataList.add("晋");
        mProvinceDataList.add("蒙");
        mProvinceDataList.add("陕");
        mProvinceDataList.add("闽");
        mProvinceDataList.add("贵");
        mProvinceDataList.add("粤");
        mProvinceDataList.add("青");
        mProvinceDataList.add("藏");
        mProvinceDataList.add("川");
        mProvinceDataList.add("宁");
        mProvinceDataList.add("琼");

        mProvinceDataList.add("A");
        mProvinceDataList.add("B");
        mProvinceDataList.add("C");
        mProvinceDataList.add("D");
        mProvinceDataList.add("E");
        mProvinceDataList.add("F");
        mProvinceDataList.add("G");
        mProvinceDataList.add("H");
        mProvinceDataList.add("J");
        mProvinceDataList.add("K");
        mProvinceDataList.add("L");
        mProvinceDataList.add("M");
        mProvinceDataList.add("N");
        mProvinceDataList.add("O");
        mProvinceDataList.add("P");
        mProvinceDataList.add("Q");
        mProvinceDataList.add("R");
        mProvinceDataList.add("S");
        mProvinceDataList.add("T");
        mProvinceDataList.add("U");
        mProvinceDataList.add("V");
        mProvinceDataList.add("W");
        mProvinceDataList.add("X");
        mProvinceDataList.add("Y");
        mProvinceDataList.add("Z");

        mProvinceDataList.add("1");
        mProvinceDataList.add("2");
        mProvinceDataList.add("3");
        mProvinceDataList.add("4");
        mProvinceDataList.add("5");
        mProvinceDataList.add("6");
        mProvinceDataList.add("7");
        mProvinceDataList.add("8");
        mProvinceDataList.add("9");
        mProvinceDataList.add("0");

        mProvinceDataList.add("←");
        mProvinceDataList.add("确定");
    }

    @Override
    public void bindEvent() {

        mRL_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdt_CarPlate.setText("");
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                        mEdt_CarPlate.setText(mProvinceDataList.get(position));
                        break;
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                        mEdt_CarPlate.setText(mEdt_CarPlate.getText().toString() + mProvinceDataList.get(position));
                        break;
                    case 66:
                        if (mEdt_CarPlate.getText().toString().length() > 0) {
                            mEdt_CarPlate.setText(mEdt_CarPlate.getText().toString().substring(0, mEdt_CarPlate.getText().length() - 1));
                        }
                        break;
                    case 67:
                        Intent intentTemp = new Intent();
                        intentTemp.putExtra(RESULT_DATA_CAR_PLATE,mEdt_CarPlate.getText().toString());
                        setResult(REQUEST_DATA_CAR_PLATE_CODE,intentTemp);
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public void refreshUI() {
        mToolBar.setTitle("关于");

        mAdapter = new CarPlateDataAdapter(InputCustomerInfoActivity.this, mProvinceDataList);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

//        final LayoutInflater mInflater = LayoutInflater.from(InputCustomerInfoActivity.this);
//        mTFL_Province.setAdapter(new TagAdapter<String>(mProvinceDataList) {
//
//            @Override
//            public View getView(FlowLayout parent, int position, String t) {
//                TextView tagTextView = (TextView) mInflater.inflate(R.layout.view_item_tag_flow_layout_text, mTFL_Province, false);
//                tagTextView.setText(t);
//                return tagTextView;
//            }
//        });

    }

    @Override
    public void requestNetworkData() {

    }
}
