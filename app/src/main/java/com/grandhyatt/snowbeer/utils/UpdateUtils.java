package com.grandhyatt.snowbeer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.FileUtils;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.snowbeer.MainActivity;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.CheckUpdateInfoEntity;
import com.grandhyatt.snowbeer.entity.LoginUserInfoEntity;
import com.grandhyatt.snowbeer.network.NetWorkRequestUtils;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.callback.CheckUpdateCallback;
import com.grandhyatt.snowbeer.network.request.CheckUpdateRequest;
import com.grandhyatt.snowbeer.network.result.CheckUpdateResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.view.DownLoadDialog;
import com.grandhyatt.snowbeer.view.activity.LoginActivity;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 版本更新
 *
 * @author
 * @email
 * @mobile
 * @create 2018/5/29 14:25
 */
public class UpdateUtils {

    /**
     * 检查更新
     * @param activity
     * @param showGeneralUpdate
     */
    public static void checkUpdate_SelectDialog(final Activity activity, final Boolean showGeneralUpdate) {
        CheckUpdateRequest request = new CheckUpdateRequest();

        request.setVersionCode(getVersionCode(activity));
        final String versionName = getVersionName(activity);
        request.setVersionName(versionName);
        request.setPlatform("android");

        SoapUtils.checkUpdateAsync(activity,request, new SoapListener(){
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                //获取数据异常
                if (object == null) {
                    ToastUtils.showLongToast(activity, activity.getString(R.string.update_error));
                    return;
                }
                //接口连接失败
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(activity, activity.getString(R.string.update_error));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                //解析json
                CheckUpdateResult result = new Gson().fromJson(strData, CheckUpdateResult.class);

                //校验接口返回代码
                if(result == null) {
                    ToastUtils.showLongToast(activity, activity.getString(R.string.update_error));
                    return;
                }
                else if(result.code != Result.RESULT_CODE_SUCCSED){
                    ToastUtils.showLongToast(activity, activity.getString(R.string.update_error_msg, result.msg));
                    return;
                }
                final CheckUpdateInfoEntity checkUpdateInfoEntity = result.data;

                //不使用该功能
                //String isIgnoreVersion = (String) SPUtils.get(activity,checkUpdateInfoEntity.getVersion(),"false");

                if((checkUpdateInfoEntity.getType() > 0 || showGeneralUpdate) && !checkUpdateInfoEntity.getVersion().equals(versionName) ){

                    List<String> dataList = new ArrayList<String>();
                    dataList.add(activity.getResources().getString(R.string.update_update));
                    //dataList.add(activity.getResources().getString(R.string.update_ignore));

                    String dialogTitle = "";
                    if(checkUpdateInfoEntity.getType() == 0) {
                        dialogTitle = activity.getResources().getString(R.string.update_level_normal, checkUpdateInfoEntity.getVersion());
                    }else if (checkUpdateInfoEntity.getType() == 1){
                        dialogTitle = activity.getResources().getString(R.string.update_level_recommend, checkUpdateInfoEntity.getVersion());
                    }else if(checkUpdateInfoEntity.getType() == 2){
                        dialogTitle = activity.getResources().getString(R.string.update_level_force, checkUpdateInfoEntity.getVersion());
                    }

                    SelectDialog dialog = new SelectDialog(activity, R.style.transparentFrameWindowStyle, new SelectDialog.SelectDialogListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    File tempFile = FileUtils.createTempFile(activity, ".apk");

                                    DownLoadDialog.Builder builder = new DownLoadDialog.Builder(activity);
                                    builder.setTitle(activity.getResources().getString(R.string.download_text));
                                    builder.setNegativeButton(activity.getResources().getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            //如果是强制更新,不可取消,否则退出应用
                                            if(checkUpdateInfoEntity.getType() == 2){
                                                activity.finish();
                                            }
                                        }
                                    });

                                    builder.create(checkUpdateInfoEntity.getUrl(), tempFile.getPath()).show();
                                    break;
//                                case 1:
//                                    SPUtils.put(activity,checkUpdateInfoEntity.getVersion(),"true");
//                                    break;
                                default:
                                    break;
                            }
                        }
                    }, new SelectDialog.SelectDialogCancelListener() {
                        @Override
                        public void onCancelClick(View v) {
                            //如果是强制更新,不可取消,否则退出应用
                            if(checkUpdateInfoEntity.getType() == 2){
                                activity.finish();
                            }
                        }
                    }, dataList, dialogTitle);

                    dialog.setCancelable(false);
                    dialog.show();
                }else{
                    //已经是最新版本
//                    if (!(activity instanceof MainActivity)) {
                    if(showGeneralUpdate) {
                        ToastUtils.showToast(activity, activity.getResources().getString(R.string.update_toast));
                    }
//                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                ToastUtils.showToast(activity, content);
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                String msg = "";
                if(fault != null) {
                    msg = statusCode + fault.toString();
                }
                ToastUtils.showToast(activity,statusCode + msg);
            }
        });

    }



    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVersionName(Context context) {
        String strVersionName = "";
        try {
            strVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return strVersionName;
    }
}
