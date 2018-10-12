package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.Consts;
import com.grandhyatt.snowbeer.Ctrls.DropDownMenuView;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.EquipmentEntity_DataListAdapter;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.DepartmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.EquipmentRequest;
import com.grandhyatt.snowbeer.network.result.DepartmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentsResult;
import com.grandhyatt.snowbeer.network.result.TextDictoryResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 生产设备查询
 * Created by ycm on 2018/10/11.
 */

public class EquipmentQueryActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    DropDownMenuView dropDownMenu;

    @BindView(R.id.mTv_Corp)
    TextView mTv_Corp;
    @BindView(R.id.mTv_Dept)
    TextView mTv_Dept;
    @BindView(R.id.mTv_UseState)
    TextView mTv_UseState;
    @BindView(R.id.mTv_EquipType)
    TextView mTv_EquipType;
    @BindView(R.id.mBt_Search)
    Button mBt_Search;
    @BindView(R.id.mBt_Clear)
    Button mBt_Clear;
    @BindView(R.id.mBt_Cancle)
    Button mBt_Cancle;

    @BindView(R.id.mEt_EquipInfo)
    EditText mEt_EquipInfo;
    @BindView(R.id.mEt_Location)
    EditText mEt_Location;
    @BindView(R.id.mEt_Keeper)
    EditText mEt_Keeper;
    @BindView(R.id.mEt_Manu)
    EditText mEt_Manu;


    List<DepartmentEntity> _DepartmentList;        //设备对应的部门信息
    List<String> _DeptNamelist = new ArrayList<>();//部门名称列表
    DepartmentEntity _SelectedDept = null;         //选中的部门对象

    List<DepartmentEntity> _EquipmentTypeList;        //设备对应的设备类型
    List<String> _EquipmentTypeNamelist = new ArrayList<>();//设备类型名称列表
    DepartmentEntity _SelectedEquipmentType = null;         //选中的设备类型对象

    List<CorporationEntity> _CorpList;              //组织机构列表
    List<String> _CorpNameList = new ArrayList<>();//组织机构名称列表
    CorporationEntity _SelectedCorp = null;     //选中的组织机构对象

    //当前页码
    private int mPageIndex = 0;
    //页面数据数量
    private int mPageSize = 10;
    //加载类型
    private boolean mIsLoadMore = false;

    private EquipmentEntity_DataListAdapter mAdapter;

    public static final int RESULT_REPORT_COMPLETE_ACTIVITY = 10001;

    /**
     * 使用状态
     */
    String[] _EquipmentStatusArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_query);

        ButterKnife.bind(this);

        dropDownMenu = (DropDownMenuView) findViewById(R.id.dropDownMenu);

        initView();
        bindEvent();
        refreshUI();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBt_Search://检索

                dropDownMenu.close();

                mPageIndex = 0;
                mIsLoadMore = false;

                mLv_DataList.setAdapter(null);
                requestNetworkData();

                break;
            case R.id.mBt_Clear://重置
                _SelectedEquipmentType = null;
                _SelectedDept = null;
                mTv_UseState.setText("使用状况");
                mTv_Dept.setText("部门");
                mTv_EquipType.setText("设备类型");

                mEt_EquipInfo.setText("");
                mEt_Location.setText("");
                mEt_Keeper.setText("");
                mEt_Manu.setText("");

                break;

            case R.id.mBt_Cancle: //取消

                    dropDownMenu.close();

                break;


            case R.id.mTv_Corp://组织机构

                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _SelectedCorp = _CorpList.get(position);

                        getDepartmentInfo(_SelectedCorp.getID());

                        mTv_Corp.setText(_SelectedCorp.getCorporationName());
                    }
                }, _CorpNameList);

                break;
            case R.id.mTv_Dept://部门
                if (_DepartmentList == null || _DepartmentList.size() == 0) {
                    if (_SelectedCorp != null) {
                        getDepartmentInfo(_SelectedCorp.getID());
                    } else {
                        CorporationEntity corpEntity = SPUtils.getLastLoginUserCorporation(this);
                        if (corpEntity != null) {
                            getDepartmentInfo(corpEntity.getID());
                        }
                    }
                }
                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _SelectedDept = _DepartmentList.get(position);

                        mTv_Dept.setText(_SelectedDept.getDepartmentName());
                    }
                }, _DeptNamelist);

                break;
            case R.id.mTv_UseState://使用状态
                final List<String> list = new ArrayList<String>();
                if (_EquipmentStatusArr != null && _EquipmentStatusArr.length > 0) {
                    for (String item : _EquipmentStatusArr) {
                        list.add(item);
                    }
                } else {
                    list.add("未启用");
                    list.add("在用");
                    list.add("出租");
                    list.add("停用");
                    list.add("封存");
                    list.add("报废");
                }

                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mTv_UseState.setText(list.get(position).toString());
                    }
                }, list);
                break;
            case R.id.mTv_EquipType://设备类型

                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _SelectedEquipmentType = _EquipmentTypeList.get(position);

                        mTv_EquipType.setText(_SelectedEquipmentType.getDepartmentName());
                    }
                }, _EquipmentTypeNamelist);

                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        mToolBar.setTitle("生产设备检索");
        mToolBar.showMenuButton();
        mToolBar.setMenuText("筛选");

        //组织机构数据源
        _CorpList = SPUtils.getLastLoginUserCorporations(this);
        CorporationEntity corpEntity = SPUtils.getLastLoginUserCorporation(this);
        if (corpEntity != null) {
            getDepartmentInfo(corpEntity.getID());

            mTv_Corp.setText(corpEntity.getCorporationName());
        }
        bindCorp(_CorpList);
        //设备类型
        getEquipmentType();

        //使用状况
        SoapListener callbackFailureReportingLevel = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, "1获取使用状况" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, "2获取使用状况数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, "3获取使用状况数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, "4获取使用状况数据失败" + statusCode + result.msg);
                    return;
                }
                TextDictionaryEntity data = result.getData();
                if (data != null) {
                    String value = data.getValue();
                    if (value != null && value.length() > 0) {
                        _EquipmentStatusArr = value.split("\\|");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                ToastUtils.showLongToast(EquipmentQueryActivity.this, "0获取使用状况数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(EquipmentQueryActivity.this, "0获取使用状况数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(EquipmentQueryActivity.this, Consts.EnumTextDictonay.EquipmentStatus, callbackFailureReportingLevel);
    }

    @Override
    public void bindEvent() {
        mTv_Corp.setOnClickListener(this);
        mTv_Dept.setOnClickListener(this);
        mTv_UseState.setOnClickListener(this);
        mTv_EquipType.setOnClickListener(this);
        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      if (!dropDownMenu.isOpen()) {
                                                          dropDownMenu.open();
                                                      } else {
                                                          dropDownMenu.close();
                                                      }
                                                  }
                                              }
        );
        mBt_Search.setOnClickListener(this);
        mBt_Clear.setOnClickListener(this);
        mBt_Cancle.setOnClickListener(this);

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                dropDownMenu.close();

                mPageIndex = 0;
                mIsLoadMore = false;
                mRefreshLayout.setNoMoreData(false);
                requestNetworkData();
            }
        });
        /*****************************************************************************************/
        //上拉加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                dropDownMenu.close();

                mPageIndex++;
                mIsLoadMore = true;
                requestNetworkData();
            }
        });

        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView mTv_ID = (TextView) view.findViewById(R.id.mTv_ID);

                dropDownMenu.close();

                //跳转至设备巡检
                Intent intent = new Intent(EquipmentQueryActivity.this, EquipRoutingInspectionActivity.class);
                intent.putExtra("mTv_ID", mTv_ID.getText().toString());
                startActivityForResult(intent, RESULT_REPORT_COMPLETE_ACTIVITY);
            }
        });

    }

    @Override
    public void refreshUI() {

        requestNetworkData();
    }

    @Override
    public void requestNetworkData() {
        showLogingDialog();

        EquipmentRequest request = new EquipmentRequest();
        request.setAssetTypeID(Consts.AssetType_sc);//生产设备
        request.setCurrentLastIdx(String.valueOf(mPageIndex * mPageSize));

        CorporationEntity corpEntity = SPUtils.getLastLoginUserCorporation(this);
        if (_SelectedCorp == null && corpEntity != null) {
            request.setCorpID(corpEntity.getID());
        } else if (_SelectedCorp != null) {
            request.setCorpID(_SelectedCorp.getID());
        }

        if (_SelectedDept != null) {
            request.setDeptID(_SelectedDept.getID());
        }

        if (_SelectedEquipmentType != null) {
            request.setEquipTypeID(_SelectedEquipmentType.getID());
        }

        if (!mTv_UseState.getText().toString().equals("使用状况")) {
            request.setUseState(mTv_UseState.getText().toString());
        }

        if (mEt_EquipInfo.getText().toString().length() > 0) {
            request.setEquipInfo(mEt_EquipInfo.getText().toString());
        }
        if (mEt_Location.getText().toString().length() > 0) {
            request.setLocation(mEt_Location.getText().toString());
        }
        if (mEt_Keeper.getText().toString().length() > 0) {
            request.setKeeper(mEt_Keeper.getText().toString());
        }
        if (mEt_Manu.getText().toString().length() > 0) {
            request.setManu(mEt_Manu.getText().toString());
        }

        SoapUtils.getEquipments(EquipmentQueryActivity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentsResult result = new Gson().fromJson(strData, EquipmentsResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<EquipmentEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null) {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, "没有更多数据了！");
                }
                //判断是否是加载更多
                if (data != null && mIsLoadMore) {
                    //更新数据源
                    mAdapter.loadMore(data);
                    mRefreshLayout.finishLoadMore(true);//设置SmartRefreshLayout加载更多的完成标志
                } else if (data != null) {
                    //设置数据
                    mAdapter = new EquipmentEntity_DataListAdapter(EquipmentQueryActivity.this, data);
                    mLv_DataList.setAdapter(mAdapter);
                    mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
                }


            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    /**
     * 获取组织机构下的部门信息
     *
     * @param corporationID
     */

    private void getDepartmentInfo(String corporationID) {
        SoapUtils.getDepartment(EquipmentQueryActivity.this, corporationID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                DepartmentResult result = new Gson().fromJson(strData, DepartmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<DepartmentEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {
                    ToastUtils.showToast(EquipmentQueryActivity.this, "没有获取到部门信息");
                    return;
                } else {
                    _DepartmentList = data;

                    bindDepart(_DepartmentList);
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });

    }

    /**
     * 部门下拉列表数据源赋值
     *
     * @param dptList
     */
    private void bindDepart(List<DepartmentEntity> dptList) {

        for (DepartmentEntity item : dptList) {
            String value = item.getDepartmentName();
            _DeptNamelist.add(value);
        }
    }

    private void bindEquipmentType(List<DepartmentEntity> equpmentTypeList) {

        for (DepartmentEntity item : equpmentTypeList) {
            String value = item.getDepartmentName();
            _EquipmentTypeNamelist.add(value);
        }
    }

    private void bindCorp(List<CorporationEntity> corpList) {

        for (CorporationEntity item : corpList) {
            String value = item.getCorporationName();
            _CorpNameList.add(value);
        }
    }

    private void getEquipmentType() {
        SoapUtils.getEquipmentType(EquipmentQueryActivity.this, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                DepartmentResult result = new Gson().fromJson(strData, DepartmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<DepartmentEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {
                    ToastUtils.showToast(EquipmentQueryActivity.this, "没有获取到部门信息");
                    return;
                } else {
                    _EquipmentTypeList = data;

                    bindEquipmentType(_EquipmentTypeList);
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipmentQueryActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

}
