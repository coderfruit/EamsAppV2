package com.grandhyatt.commonlib;

/**
 * API接口调用返回结果
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 10:50
 */
public class Result {
    public static final int RESULT_CODE_SUCCSED = 0;

    public int code;

    public String msg;

    public Result(){
        code = -1;
        msg = "unkonw";
    }

}
