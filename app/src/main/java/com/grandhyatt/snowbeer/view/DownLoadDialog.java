package com.grandhyatt.snowbeer.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.snowbeer.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * 
 *
 * @author 
 * @email 
 * @mobile 
 * @create 2018/7/20 22:40
 */
public class DownLoadDialog extends android.app.Dialog {

    /**
     *  
     *
     * * @param null
     * @return 
     */
    public DownLoadDialog(Context context) {
        super(context);
    }

    /**
     *  
     *
     * * @param null
     * @return 
     */
    public DownLoadDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 安装apk 
     *
     * * @param null
     * @return 
     */
    private static void installApk(Context c, String apkPath) {
       try {
           File file = new File(apkPath);
           if (!file.exists()) {
               return;
           }

           Intent intent = new Intent(Intent.ACTION_VIEW);
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

           Uri uriFile;
           // 判断版本大于等于7.0
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
               uriFile = FileProvider.getUriForFile(c, "com.grandhyatt.snowbeer.fileprovider", file);
               // 给目标应用一个临时授权
               intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
           } else {
               uriFile = Uri.fromFile(file);
           }

           intent.setDataAndType(uriFile, "application/vnd.android.package-archive");
           c.startActivity(intent);
       }catch(Exception ex)
       {
            ToastUtils.showToast(c,ex.getMessage());
       }
    }

    /**
     *  
     *
     * * @param null
     * @return 
     */
    public static class Builder implements OnKeyListener {
        private Context mContext;
        private String mStr_Title;
        private String mStr_Message;
        private ProgressBar mProgressBar;
        private String mStr_NegativeButtonText;
        private int mInt_PositiveButtonTextColor = -1;
        private View mContentView;
        private TextView mTv_Title;
        private TextView mTv_Percent;
        private Button mBtn_NegativeButton;
        private OnClickListener mNegativeButtonClickListener;
        private DownLoadDialog mDialog;
        private Boolean mBol_IsCancel = false;
        private String mStr_ApkPath;
        private String mStr_Url;

        /**
         *  
         *
         * * @param null
         * @return 
         */
        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * 设置Dialog的标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.mStr_Title = (String) mContext.getText(title);
            return this;
        }

        /**
         * 设置Dialog的标题
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.mStr_Title = title;
            return this;
        }

        /**
         *  
         *
         * * @param null
         * @return 
         */
        public Builder setContentView(View view) {
            this.mContentView = view;
            return this;
        }


        /**
         * 设置确认按钮的文本和单击监听事件
         *
         * @param buttonText
         * @return
         */
        public Builder setNegativeButton(int buttonText) {
            this.mStr_NegativeButtonText = (String) mContext.getText(buttonText);
            return this;
        }

        /**
         * 设置确认按钮的文本和单击监听事件
         *
         * @param buttonText
         * @return
         */
        public Builder setNegativeButton(String buttonText) {
            this.mStr_NegativeButtonText = buttonText;
            return this;
        }

        /**
         * 设置确认按钮的文本和单击监听事件
         *
         * @param buttonText
         * @return
         */
        public Builder setNegativeButton(int buttonText, OnClickListener listener) {
            this.mStr_NegativeButtonText = (String) mContext.getText(buttonText);
            this.mNegativeButtonClickListener = listener;
            return this;
        }

        /**
         * 设置确认按钮的文本和单击监听事件
         *
         * @param buttonText
         * @return
         */
        public Builder setNegativeButton(String buttonText, OnClickListener listener) {
            this.mStr_NegativeButtonText = buttonText;
            this.mNegativeButtonClickListener = listener;
            return this;
        }

        /**
         * 设置PositiveButton的文本颜色
         *
         * @param color
         * @return
         */
        public Builder setPositiveButtonTextColor(int color) {
            this.mInt_PositiveButtonTextColor = color;
            return this;
        }

        /**
         *  
         *
         * * @param null
         * @return 
         */
        public DownLoadDialog create(final String url, String apkPath) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDialog = new DownLoadDialog(mContext, R.style.Dialog);
            View layout = inflater.inflate(R.layout.view_download_dialog, null);
            mDialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            mTv_Title = (TextView) layout.findViewById(R.id.mTv_Title);
            mTv_Percent = (TextView) layout.findViewById(R.id.mTv_Percent);
            mProgressBar = (ProgressBar) layout.findViewById(R.id.mProgressBar);
            mBtn_NegativeButton = (Button) layout.findViewById(R.id.mBtn_NegativeButton);
            mStr_Url = url;
            mStr_ApkPath = apkPath;
            if (mInt_PositiveButtonTextColor != -1) {
                mBtn_NegativeButton.setTextColor(mInt_PositiveButtonTextColor);
            }

            if (!TextUtils.isEmpty(mStr_Title)) {
                mTv_Title.setText(mStr_Title);
                mTv_Title.setVisibility(View.VISIBLE);
            } else {
                mTv_Title.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(mStr_NegativeButtonText)) {
                mBtn_NegativeButton.setText(mStr_NegativeButtonText);

                if (mNegativeButtonClickListener != null) {
                    mBtn_NegativeButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            OkHttpUtils.getInstance().cancelTag(mContext);

                            mDialog.dismiss();
                            mBol_IsCancel = true;
                            mNegativeButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }

            } else {
                mBtn_NegativeButton.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mStr_Message)) {
                mTv_Percent.setText(mStr_Message);
            } else if (mContentView != null) {
                ((LinearLayout) layout.findViewById(R.id.mLL_Content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.mLL_Content)).addView(mContentView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            }
            mTv_Percent.setText(mContext.getResources().getString(R.string.download_progress_percent) + "0%");
            mProgressBar.setProgress(0);

            String strUrl = mStr_Url;
            File file = new File(mStr_ApkPath);
            String strPath = file.getParent();
            String strName = file.getName();
            String strTemo = strPath + strName;
            OkHttpUtils.get()
                    .url(strUrl)
                    .tag(mContext)
                    .build()
                    .execute(new FileCallBack(file.getParent(), file.getName()) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if(mContext instanceof Activity){
                                ToastUtils.showToast(mContext,e.getMessage());
                                mDialog.dismiss();
                            }
                        }

                        @Override
                        public void onResponse(File response, int id) {
                            installApk(mContext, mStr_ApkPath);
                        }

                        @Override
                        public void inProgress(float progress, long total, int id) {
                            mTv_Percent.setText(mContext.getResources().getString(R.string.download_progress_percent) + String.valueOf((int) (100 * progress)) + "%");
                            mProgressBar.setProgress((int) (100 * progress));
//                            Log.e(TAG, "inProgress" + (int) (100 * progress));
                        }

                    });


            mDialog.setContentView(layout);
            return mDialog;
        }

        /**
         *  
         *
         * * @param null
         * @return 
         */
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                return true;
            } else {
                return false; // 默认返回 false
            }
        }
    }


//	DialogLite.Builder builder = new DialogLite.Builder(this);
//    builder.setMessage("你确定真的要删除吗?");
//    builder.setTitle("删除");
//    builder.setPositiveButtonTextColor(getResources().getColor(R.color.blue2));
//    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//        public void onClick(DialogInterface dialog, int which) {
//            dialog.dismiss();
//            //设置你的操作事项
//        }
//    });
//    builder.create().show();

}
