package com.grandhyatt.snowbeer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ycm on 2018/8/18.
 */

public class ImageUtils {

    public static Bitmap Bytes2Bimap(byte[] b) {
        Bitmap btp = null;
        if (b.length != 0) {

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inDither = false; /* 不进行图片抖动处理 */
            opt.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
            /* 下面两个字段需要组合使用 */
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            opt.inSampleSize = 4;
            btp = BitmapFactory.decodeByteArray(b, 0, b.length);

        }
        return btp;
    }

    // 将InputStream转换成Bitmap
    public static Bitmap InputStream2Bitmap(InputStream is) {
        // BitmapFactory.Options opts = new BitmapFactory.Options();
        // opts.inSampleSize = 2;
        return BitmapFactory.decodeStream(is);
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * 将图片转换成Base64编码的字符串
     *
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            //is = new FileInputStream(path);

            Bitmap bmp = compressImg(path);//压缩图片
            is = Bitmap2InputStream(bmp);//图片转为输入流

            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * list转byte数组
     * @param list
     * @return
     */
    public static byte[] ListToByteArray(java.util.List<Object> list) {
            if (list == null || list.size() < 0)
                return null;
            byte[] bytes = new byte[list.size()];
            int i = 0;
            Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                bytes[i] = (byte) iterator.next();
                i++;
            }
            return bytes;
    }

    /**
     * 压缩图片
     * @param itemUrl
     * @return
     */
    public static Bitmap compressImg(String itemUrl)
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;    //这个的值压缩的倍数（2的整数倍），数值越小，压缩率越小，图片越清晰
        Bitmap bitmap = BitmapFactory.decodeFile(itemUrl,opts);//返回原图解码之后的bitmap对象
        return bitmap;
    }




}
