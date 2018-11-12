package com.grandhyatt.snowbeer.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by ycm on 2018/8/26.
 */

public class CommonUtils {

    /**
     * 日期转为字符串
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * base64字符串转换为byte数组
     *
     * @param base64Data
     * @return
     */
    public static byte[] base64ToByte(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return bytes;
    }

    /**
     * 将文件转成base64 字符串
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeFileToBase64(String path) throws Exception {

        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);

    }

    /**
     * 将base64字符保存文本文件
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void encodeBase64ToFile(byte[] buffer, String targetPath) throws Exception {
        File directory = new File(targetPath).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            Log.i("encodeBase64ToFile", "Path to file could not be created");
        }
        //byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    /**
     * 获取文件Uri
     *
     * @param filePath
     * @return
     */
    public static Uri getPathUri(String filePath) {
        File file = new File(filePath);
        Uri mUri = Uri.parse("file://" + file.getPath());
        return mUri;
    }

    /**
     * 播放系统提示音
     * @param context
     */
    public static void playMusic(Context context) {
        try {
            //获取系统当前提示音打 Uri:
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            //通过Uri 来获取提示音的实例对象:
            Ringtone mRingtone = RingtoneManager.getRingtone(context, uri);

            //播放
            mRingtone.play();
        } catch (Exception ex) {

        }

    }

    /**
     * 比较两个日期的分钟差
     * @param data1
     * @param data2
     * @return
     */
    public static int compareDateMinutes(String data1,String data2)
    {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        long from = 0;
        try {
            from = simpleFormat.parse(data1).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long to = 0;
        try {
            to = simpleFormat.parse(data2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int minutes = (int) ((to - from)/(1000 * 60));

        return minutes;
    }

    /**
     * 比较两个日期的天数差
     * @param data1
     * @param data2
     * @return
     */
    public static int compareDateDays(String data1,String data2)
    {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        long from = 0;
        try {
            from = simpleFormat.parse(data1).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long to = 0;
        try {
            to = simpleFormat.parse(data2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int days = (int) ((to - from)/(1000 * 60 * 60 * 24));

        return days;
    }

    /**
     * 比较两个日期的小时差
     * @param data1
     * @param data2
     * @return
     */
    public static int compareDateHours(String data1,String data2)
    {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        long from = 0;
        try {
            from = simpleFormat.parse(data1).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long to = 0;
        try {
            to = simpleFormat.parse(data2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int hours = (int) ((to - from)/(1000 * 60 * 60));

        return hours;
    }




}
