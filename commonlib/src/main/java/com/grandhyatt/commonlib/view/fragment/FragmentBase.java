package com.grandhyatt.commonlib.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grandhyatt.commonlib.ApplicationBase;
import com.grandhyatt.commonlib.R;
import com.grandhyatt.commonlib.view.LoadingDialog;

/**
 * Fragment基类
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 10:50
 */
public class FragmentBase extends Fragment{

    private Activity mActivity;

    private LoadingDialog mLoadingDialog;

    @Override
    public void onAttach(Activity activity) {
        Log.i(this.getClass().getSimpleName(), "onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(), "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(), "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i(this.getClass().getSimpleName(), "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(this.getClass().getSimpleName(), "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(this.getClass().getSimpleName(), "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(this.getClass().getSimpleName(), "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(this.getClass().getSimpleName(), "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(this.getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i(this.getClass().getSimpleName(), "onDetach");
        super.onDetach();
    }

    /**
     * 显示加载对话框
     */
    private void showDialog() {
        mLoadingDialog = new LoadingDialog(getContext(), R.style.LoadingDialog);
        mLoadingDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    private void cancelDialog() {
        if (mLoadingDialog == null) {
            return;
        }
        mLoadingDialog.dismiss();
    }

    public Context getContext(){
        mActivity = getActivity();
        if(mActivity == null){
            return ApplicationBase.getInstance();
        }

        return mActivity;
    }
}
