package com.grandhyatt.snowbeer.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.view.ActivityCollector;

import java.util.List;

import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;


public class ActivityBase extends com.grandhyatt.commonlib.view.activity.ActivityBase {

    private static int mInt_Count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        mInt_Count++;
        super.onResume();
    }

    @Override
    protected void onPause() {
        mInt_Count--;
        super.onPause();
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

        super.finish();
        // 关闭窗体动画显示
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        // 关闭窗体动画显示
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    public static boolean isRunInBack() {
        if (mInt_Count <= 0) {
            return true;
        }
        return false;
    }

    protected void setStatusBar(){
        // 第二个参数是状态栏色值:4183d7
        // 第三个参数是兼容5.0到6.0之间的状态栏颜色字体只能是白色，如果沉浸的颜色与状态栏颜色冲突, 设置一层浅色对比能显示出状态栏字体（可以找ui给一个合适颜色值）
        // 如果您的项目是6.0以上机型或者某些界面不适用沉浸, 推荐使用两个参数的setUseStatusBarColor
        StatusUtil.setUseStatusBarColor(this, Color.parseColor("#4183d7"), Color.parseColor("#33000000"));
        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色
        StatusUtil.setSystemStatus(this, false, false);

//        //白色标题栏,黑色状态栏
//        StatusUtil.setUseStatusBarColor(this, Color.parseColor("#000000"));
//        StatusUtil.setSystemStatus(this, false, false);
    }

    /**
     * 显示选择对话框菜单
     */
    protected SelectDialog showSelectDialog(SelectDialog.SelectDialogListener listener, List<String> lists) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, lists);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出输入框，将录入内容写入EditText控件
     * @param context
     * @param title
     * @param etInput
     */
    public void ShowInputDialogForEditText(Context context, String title, final EditText etInput) {

        final EditText inputContrl = new EditText(context);
        inputContrl.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setIcon(R.drawable.logo)
                .setView(inputContrl)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        etInput.setText(inputContrl.getText().toString());
                    }
                });
        builder.show();
    }
    /**
     * 弹出输入框，将录入内容写入EditText控件
     * @param context
     * @param title
     * @param etInput
     */
    public void ShowInputDialogForTextView(Context context, String title, final TextView etInput) {

        final EditText inputContrl = new EditText(context);
        inputContrl.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setIcon(R.drawable.logo32)
                .setView(inputContrl)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        etInput.setText(inputContrl.getText().toString());
                    }
                });
        builder.show();
    }

    /**
     * 弹出选择对话框获取用户选择的是否
     * @param context
     * @param title
     * @param showText
     * @return
     */
    public boolean ShowDialog(Context context, String title,String showText) {

        final boolean[] value = {false};
        final TextView inputContrl = new TextView(context);
        inputContrl.setText(showText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setIcon(R.drawable.logo)
                .setView(inputContrl)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        value[0] = false;
                    }
                });
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        value[0] = true;
                    }
                });
        builder.show();

        return value[0];
    }

    /**
     * 弹出选择对话框
     * @param title
     * @param list
     * @param listener
     */
    public void showSelectDialog(String title,List<String> list,DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.transparentFrameWindowStyle);// Theme_Holo_Light_Dialog);
        builder.setIcon(R.drawable.logo32);
        builder.setTitle(title);

        final String[] cities = (String[]) list.toArray(new String[list.size()]);

        //设置一个下拉的列表选择项
        builder.setItems(cities, listener);
        builder.show();
    }

}
